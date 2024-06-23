package view;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import controller.ApplicationController;
import controller.CardController;
import controller.menuConrollers.PreGameMenuController;
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
import model.Account.User;
import model.game.Game;
import model.role.Card;
import model.role.Faction;
import model.role.Leader;
import model.role.Unit;

import java.io.*;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class PreGameMenu extends AppMenu {
    private final PreGameMenuController controller;
    private ImageView currentImageView;
    private User currentUser;

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

                VBox vbox = new VBox(10, imageView, label); // Add spacing between ImageView and Label
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
        Image backgroundImage = new Image(getClass().getResource("/Images/stone.jpg").toExternalForm());
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        root.setBackground(new Background(background));

        Scene scene = new Scene(root, 1600, 900);
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
        ArrayList<String> result = new ArrayList<>(CardController.heroes);
        ArrayList<String> out = new ArrayList<>();
//        result.addAll(CardController.leaders);
        result.addAll(CardController.units);
        result.addAll(CardController.specials);
        for (String cardName : result) {
            if (CardController.faction.getOrDefault(cardName, Faction.ALL).equals(currentUser.getFaction())) {
                out.add(cardName);
            }
        }
        showManyCardsInScrollBar(out, false);
    }

    public void uploadDeck() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setTitle("Select an image");
        File selectedFile = fileChooser.showOpenDialog(ApplicationController.getStage());
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
                alert.show();
                currentUser.getDeck().clear();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("your deck is now ready");
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
            alert.show();
            showCurrentUserInfo();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You should pick at least 22 unit cards");
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
        //TODO: start the game and initialize the game object
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
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        HBox hBox = new HBox();
        boolean shouldCreateNew = true;
        vBox.setAlignment(Pos.CENTER);
        for (String cardName : cardsNames) {
            if (shouldCreateNew) {
                hBox = new HBox();
                hBox.setSpacing(10);
                hBox.setAlignment(Pos.CENTER);
                vBox.getChildren().add(hBox);
            }
            String imagePath = CardController.imagePath.getOrDefault(cardName, "src/main/resources/assets/lg/skellige_king_bran.jpg");
            ImageView imageView = new ImageView(new Image(new File(imagePath).toURI().toURL().toString()));
            imageView.setOnMouseClicked(event -> addToDeck(cardName));
            imageView.setOnDragExited(event -> System.out.println("swipe down"));
            int countInDeck = currentUser.getCardCount(cardName);
            Text text = new Text("count in deck: " + countInDeck);
            if (deckOrAll) {
                Button button = new Button("remove");
                button.setMinWidth(20);
                button.setMinHeight(20);
                hBox.getChildren().add(button);
                button.setLayoutX(imageView.getLayoutX() + 20);
                button.setLayoutY(imageView.getLayoutY() + 20);
                HBox finalHBox = hBox;
                button.setOnMouseClicked(event -> removeFromDeck(cardName, finalHBox, imageView, text, button));
            }
            hBox.getChildren().add(imageView);
            hBox.getChildren().add(text);
            shouldCreateNew = !shouldCreateNew;
        }


        // Add back button
        Button button = new Button("Back");
        button.setMinWidth(100);
        button.setMinHeight(100);
        button.setOnMouseClicked(mouseEvent -> {
            try {
                start(ApplicationController.getStage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        vBox.getChildren().add(button);


        ScrollPane scrollPane = new ScrollPane(vBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setVvalue(0.0);
        Scene scene = new Scene(scrollPane, 1500, 800);
        ApplicationController.getStage().setScene(scene);
        ApplicationController.getStage().setTitle("show cards");
        ApplicationController.getStage().centerOnScreen();
        ApplicationController.getStage().show();
    }

    private void removeFromDeck(String cardName, HBox currentHbox, ImageView imageView, Text text, Button button) {
        try {
            System.out.println(controller.removeFromDeck(cardName, currentUser, currentHbox, imageView, text, button));
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
            System.out.println("[ERR]: " + e.getMessage());
        }
    }

    private void addToDeck(String cardName) {
        try {
            String result = controller.addToDeck(cardName, currentUser);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(result);
            System.out.println(result);
            alert.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
        }
    }

    public void showLeaders() throws MalformedURLException {
        // TODO: until I find leaders assets
        VBox content = new VBox();
        content.setSpacing(10);
        HBox hBox = new HBox();
        boolean shouldCreateNew = true;
        content.setAlignment(Pos.CENTER);
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
            if (shouldCreateNew) {
                hBox = new HBox();
                content.getChildren().add(hBox);
                hBox.setSpacing(10);
                hBox.setAlignment(Pos.CENTER);
            }
            hBox.getChildren().add(imageView);
            shouldCreateNew = !shouldCreateNew;

            ScrollPane scrollPane = new ScrollPane(content);
            scrollPane.setFitToWidth(true);
            Scene scene = new Scene(scrollPane, 800, 800);
            ApplicationController.getStage().setScene(scene);
            ApplicationController.getStage().setTitle("show Leaders");
            ApplicationController.getStage().centerOnScreen();
            ApplicationController.getStage().show();
        }
    }

    public void backToMainMenu() throws Exception {
        MainMenu mainMenu = new MainMenu();
        mainMenu.start(ApplicationController.getStage());
    }
}