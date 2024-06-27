package view;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import controller.ApplicationController;
import controller.CardController;
import controller.menuConrollers.PreGameMenuController;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Account.Player;
import model.Account.User;
import model.game.Game;
import model.role.*;

import java.io.*;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class PreGameMenu extends AppMenu {
    private final PreGameMenuController controller;
    private ImageView currentImageView;
    private User currentUser;
    private final int MAX_CARD_IN_LINE = 4;
    private final int BACK_BUTTON_SPACING = 80;
    private final int LAYOUT_BUTTON = 40;

    @FXML
    private Label usernameLabel;
    @FXML
    private Label factionLabel;
    @FXML
    private Label cardsCount;
    @FXML
    private Label unitCount;
    @FXML
    private Label specialCount;
    @FXML
    private Label heroCount;
    @FXML
    private Label totalPower;

    public PreGameMenu() {
        controller = new PreGameMenuController();
        currentImageView = null;
        currentUser = Game.getCurrentGame().getPlayer1().getUser();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL url = ProfileMenu.class.getResource("/FXML/PreGameMenu.fxml");
        assert url != null;
        AnchorPane root = FXMLLoader.load(url);
        Scene scene = new Scene(root);

        scene.getStylesheets().add(getClass().getResource("/CSS/PreGameMenu.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showCurrentUserInfo();
    }

    public void showAndChangeFaction() throws IOException {
        HBox content = new HBox(20); // Add spacing between each VBox
        content.getStylesheets().add(getClass().getResource("/CSS/PreGamePages.css").toExternalForm());
        File directory = new File("src/main/resources/assets/lg");

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isFile() && file.getName().contains("faction_")) {
                Image image = new Image(file.toURI().toURL().toString());
                ImageView imageView = new ImageView(image);
                String factionName = buildFactionName(file.getName());

                if (currentUser.getFaction().equals(Faction.valueOf(factionName.toUpperCase()))) {
                    ColorAdjust grayscaleEffect = new ColorAdjust();
                    grayscaleEffect.setSaturation(-1.0);
                    imageView.setEffect(grayscaleEffect);
                    currentImageView = imageView;
                }

                imageView.setOnMouseClicked(event -> {
                    handleDifferentColor(imageView, factionName);
                    currentUser.setFaction(Faction.valueOf(factionName.toUpperCase()));
                    currentUser.getDeck().clear();
                });

                Label label = new Label(factionName);
                label.setMinSize(30, 30);
                label.setAlignment(Pos.CENTER);

                VBox vbox = new VBox(10, imageView, label);
                vbox.setAlignment(Pos.CENTER);

                content.getChildren().add(vbox);
            }
        }

        Button button = new Button("back");
        button.setMinHeight(100);
        button.setMinWidth(100);
        button.setOnMouseClicked(mouseEvent -> {
            try {
                start(ApplicationController.getStage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        content.getChildren().add(button);

        VBox rootVbox = new VBox(content);
        rootVbox.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(rootVbox);
        Image backgroundImage = new Image(getClass().getResource("/Images/pregamebackground.jpg").toExternalForm());
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        root.setBackground(new Background(background));

        Scene scene = new Scene(root, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/CSS/PreGamePages.css").toExternalForm());

        content.setAlignment(Pos.CENTER);

        ApplicationController.getStage().setScene(scene);
        ApplicationController.getStage().setTitle("select faction");
        ApplicationController.getStage().show();
    }

    private String buildFactionName(String name) {
        return switch (name) {
            case "faction_monsters.jpg" -> "MONSTERS";
            case "faction_nilfgaard.jpg" -> "NILFGAARDIAN_EMPIRE";
            case "faction_realms.jpg" -> "NORTHERN_REALMS";
            case "faction_scoiatael.jpg" -> "SCOIA_TAEL";
            case "faction_skellige.jpg" -> "SKELLIGE";
            default -> "";
        };
    }

    private void handleDifferentColor(ImageView imageView, String factionName) {
        controller.setFaction(User.getLoggedInUser(), factionName);
        ColorAdjust grayscaleEffect = new ColorAdjust();
        grayscaleEffect.setSaturation(-1.0);
        imageView.setEffect(grayscaleEffect);
        if (currentImageView != null) {
            currentImageView.setEffect(null);
        }
        currentImageView = imageView;

    }

    public void showDeck() throws Exception {
        ArrayList<String> cardNames = new ArrayList<>();
        for (Card card : currentUser.getDeck()) {
            cardNames.add(card.getName());
        }
        showManyCardsInScrollBar(cardNames, true);
    }

    public void showCards() throws Exception {
        ArrayList<String> out = new ArrayList<>();
        ArrayList<String> result = new ArrayList<>();
        result.addAll(CardController.units);
        result.addAll(CardController.specials);
        result.addAll(CardController.heroes);
        for (String cardName : result) {
            Faction faction = CardController.faction.getOrDefault(cardName, Faction.ALL);
            if (faction.equals(currentUser.getFaction())
                    || faction.equals(Faction.ALL)) {
                out.add(cardName);
            }

        }
        for (String s : out) {
            Card card = CardController.createCardWithName(s);
            if (card.getType().equals(model.role.Type.WEATHER))
//                System.out.println(STR."\{card.getName()}--->\{card.getAbility()}");
                System.out.println(card.getName() + "--->" + card.getAbility());
        }
        showManyCardsInScrollBar(out, false);
    }

    public void uploadDeck() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setTitle("Select an image");
        File selectedFile = fileChooser.showOpenDialog(ApplicationController.getStage());

        if (selectedFile == null) {
            return;
        }

        try (FileReader reader = new FileReader(selectedFile)) {
            Type listType = new TypeToken<ArrayList<String>>() {
            }.getType();
            List<String> restoredList = new Gson().fromJson(reader, listType);
            for (String string : restoredList) {
                currentUser.getDeck().add(CardController.createCardWithName(string));
            }


            if (currentUser.getSpecialCount() > 10) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setContentText("you have more than 10 special cards please change your deck first\n    " +
                        "we clear your deck");
                alert.getDialogPane().getScene().getStylesheets().add(getClass().getResource("/CSS/AlertStyler.css").toExternalForm());
                alert.show();
                currentUser.getDeck().clear();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("your deck is now ready");
                alert.getDialogPane().getScene().getStylesheets().add(getClass().getResource("/CSS/AlertStyler.css").toExternalForm());
                alert.show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void downloadDeck() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setTitle("Select an image");
        File selectedFile = fileChooser.showOpenDialog(ApplicationController.getStage());

        ArrayList<String> cardNames = new ArrayList<>();
        for (Card card : currentUser.getDeck()) {
            cardNames.add(card.getName());
        }
        if (selectedFile != null) {
            Gson gson = new Gson();
            try (Writer writer = new FileWriter(selectedFile)) {
                gson.toJson(cardNames, writer);
            }
        }
    }

    public void changeTurn() {
        if (currentUser.getUnitCount() >= 22) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            currentUser = Game.getCurrentGame().getPlayer2().getUser();
            alert.setContentText("now player2: " + currentUser.getName() + " should pick cards");
            alert.getDialogPane().getScene().getStylesheets().add(getClass().getResource("/CSS/AlertStyler.css").toExternalForm());
            alert.show();
            showCurrentUserInfo();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You should pick at least 22 unit cards");
            alert.getDialogPane().getScene().getStylesheets().add(getClass().getResource("/CSS/AlertStyler.css").toExternalForm());
            alert.show();
        }
    }

    private int unitCount(User user) {
        int out = 0;
        for (Card card : user.getDeck()) {
            if (card instanceof Unit) out++;
        }
        return out;
    }

    public void startGame() {
        User user1 = Game.getCurrentGame().getPlayer1().getUser();
        User user2 = Game.getCurrentGame().getPlayer2().getUser();
        Game game = new Game(user1, user2);
        game.run();
    }

    public void showCurrentUserInfo() {
        usernameLabel.setText(currentUser.getName());
        factionLabel.setText(currentUser.getFaction().name());
        cardsCount.setText(String.valueOf(currentUser.getDeck().size()));
        unitCount.setText(String.valueOf(currentUser.getUnitCount()));
        specialCount.setText(String.valueOf(currentUser.getSpecialCount()));
        heroCount.setText(String.valueOf(currentUser.getHeroCount()));
        totalPower.setText(String.valueOf(currentUser.getSumOfPower()));
    }

    private void showManyCardsInScrollBar(ArrayList<String> cardsNames, Boolean deckOrAll) throws Exception {

        AnchorPane pane = new AnchorPane();
        HBox body = new HBox();

        VBox vBox = new VBox();
        vBox.setSpacing(10);
        HBox hBox = new HBox();
        body.setSpacing(BACK_BUTTON_SPACING);

        int cardCo = 0;
        vBox.setAlignment(Pos.CENTER);

        //handling duplicate cards in scroll bar
        HashSet<String> cardsInShow = new HashSet<>(cardsNames);
        for (String cardName : cardsInShow) {
            if (cardCo % MAX_CARD_IN_LINE == 0) {
                hBox = new HBox();
                hBox.setSpacing(60);
                hBox.setAlignment(Pos.CENTER);
                vBox.getChildren().add(hBox);
            }

            cardCo++;
            String imagePath = CardController.imagePath.getOrDefault(cardName, "src/main/resources/assets/lg/skellige_king_bran.jpg");
            ImageView imageView = new ImageView(new Image(new File(imagePath).toURI().toURL().toString()));
            imageView.setOnMouseClicked(event -> addToDeck(cardName, (VBox) imageView.getParent()));
            imageView.setOnDragExited(event -> System.out.println("swipe down"));
            imageView.preserveRatioProperty();
            imageView.setFitWidth(150);
            imageView.setFitHeight(250);

            int countInDeck = currentUser.getCardCount(cardName);
            Label label = new Label("count in deck: " + countInDeck);
            label.setAlignment(Pos.CENTER);

            VBox cardBox = new VBox();
            cardBox.setSpacing(5);
            cardBox.setAlignment(Pos.CENTER);
            cardBox.getChildren().addAll(imageView, label);

            if (deckOrAll) {
                Button button = new Button("remove");
                button.setMinWidth(30);
                button.setMinHeight(20);
                cardBox.getChildren().add(button);
                button.setLayoutX(imageView.getLayoutX() + 20);
                button.setLayoutY(imageView.getLayoutY() + 20);
                HBox finalHBox = hBox;
                button.setOnMouseClicked(event -> removeFromDeck(cardBox, cardName, finalHBox, imageView, label, button));
            }

            hBox.getChildren().add(cardBox);
        }

        // Add back button
        Button button = new Button("Back");
        button.setMinWidth(100);
        button.setMinHeight(60);
        button.setOnMouseClicked(mouseEvent -> {
            try {
                start(ApplicationController.getStage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        button.setLayoutX(LAYOUT_BUTTON);
        button.setLayoutY(LAYOUT_BUTTON);
        body.getChildren().add(button);

        Image backgroundImage = new Image(getClass().getResource("/Images/pregamebackground.jpg").toExternalForm());
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);

        body.getChildren().add(vBox);
        body.setMinHeight(715);
        body.setMinWidth(1260);
        pane.getChildren().add(body);
        pane.setBackground(new Background(background));

        ScrollPane scrollPane = new ScrollPane(pane);
        scrollPane.setFitToWidth(true);
        scrollPane.setVvalue(0.0);
        Scene scene = new Scene(scrollPane, 1280, 720);
        scene.getStylesheets().add(getClass().getResource("/CSS/PreGamePages.css").toExternalForm());


        ApplicationController.getStage().setScene(scene);
        ApplicationController.getStage().setTitle("Show Cards");
        ApplicationController.getStage().centerOnScreen();
        ApplicationController.getStage().show();
    }

    private void removeFromDeck(VBox cardBox, String cardName, HBox currentHBox, ImageView imageView, Label label, Button button) {
        try {
            if (currentUser.getCardCount(cardName) - 1 == 0) {
                System.out.println(controller.removeFromDeck(cardBox, cardName, currentUser, currentHBox, imageView, label, button));
                return;
            }

            currentUser.getDeck().remove(currentUser.getCardFromDeckByName(cardName));
            label.setText("count in deck :" + String.valueOf(currentUser.getCardCount(cardName)));
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.getDialogPane().getScene().getStylesheets().add(getClass().getResource("/CSS/AlertStyler.css").toExternalForm());
            alert.show();
            System.out.println("[ERR]: " + e.getMessage());
        }
    }

    private void addToDeck(String cardName, VBox cardBox) {
        try {
            for (Node node : cardBox.getChildren()) {
                if (node instanceof Label) {
                    Label label = (Label) node;
                    label.setText("count in deck :" + String.valueOf(currentUser.getCardCount(cardName) + 1));
                    break;
                }
            }

            String result = controller.addToDeck(cardName, currentUser);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(result);
            alert.getDialogPane().getScene().getStylesheets().add(getClass().getResource("/CSS/AlertStyler.css").toExternalForm());
            System.out.println(result);
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.getDialogPane().getScene().getStylesheets().add(getClass().getResource("/CSS/AlertStyler.css").toExternalForm());
            alert.show();
        }
    }

    public void showLeaders() throws MalformedURLException {
        // TODO: until I find leaders assets

        HBox body = new HBox();
        VBox content = new VBox();
        content.setSpacing(10);
        HBox hBox = new HBox();
        int leaderCo = 0;
        content.setAlignment(Pos.CENTER);
        Button button = new Button("back");
        button.setMinHeight(60);
        button.setMinWidth(100);
        button.setOnMouseClicked(mouseEvent -> {
            try {
                start(ApplicationController.getStage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        body.getChildren().add(button);
        button.setLayoutX(LAYOUT_BUTTON);
        button.setLayoutY(LAYOUT_BUTTON);

        for (String leaderName : CardController.leaders) {
            if (!CardController.faction.get(leaderName).equals(User.getLoggedInUser().getFaction())) continue;

            Card card = CardController.createLeaderCard(leaderName);
            ImageView imageView = new ImageView(new Image(new File(CardController.imagePath.getOrDefault(card.getName(), "src/main/resources/assets/lg/skellige_king_bran.jpg")).toURI().toURL().toString()));
            imageView.setOnMouseClicked(event -> {
                currentUser.setLeader((Leader) card);
                try {
                    start(ApplicationController.getStage());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            imageView.preserveRatioProperty();
            imageView.setFitWidth(150);
            imageView.setFitHeight(250);

            if (leaderCo % MAX_CARD_IN_LINE == 0) {
                hBox = new HBox();
                content.getChildren().add(hBox);
                hBox.setSpacing(60);
                hBox.setAlignment(Pos.CENTER);
            }
            hBox.getChildren().add(imageView);
            leaderCo++;


        }

        Image backgroundImage = new Image(getClass().getResource("/Images/pregamebackground.jpg").toExternalForm());
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);


        body.getChildren().add(content);
        body.setSpacing(120);


        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(body);
        stackPane.setBackground(new Background(background));

        ScrollPane scrollPane = new ScrollPane(stackPane);
        scrollPane.setFitToWidth(true);
        Scene scene = new Scene(scrollPane, 1280, 700);
        scene.getStylesheets().add(getClass().getResource("/CSS/PreGamePages.css").toExternalForm());

        ApplicationController.getStage().setScene(scene);
        ApplicationController.getStage().setTitle("show Leaders");
        ApplicationController.getStage().centerOnScreen();
        ApplicationController.getStage().show();


    }

    public void backToMainMenu() throws Exception {
        MainMenu mainMenu = new MainMenu();
        mainMenu.start(ApplicationController.getStage());
    }
}