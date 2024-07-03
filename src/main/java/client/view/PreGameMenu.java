package client.view;

import client.Out;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.game.Game;
import model.role.*;
import server.Enum.Regexes;
import server.User;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;


public class PreGameMenu extends AppMenu {
    private final PreGameMenuController controller;
    private ImageView currentImageView;
    private static User currentUser = User.getLoggedInUser();
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
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL url = ProfileMenu.class.getResource("/FXML/PreGameMenu.fxml");
        assert url != null;
        AnchorPane root = FXMLLoader.load(url);
        Scene scene = new Scene(root);

        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/PreGameMenu.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        client.User.getInstance().setAppMenu(this);

        try {
            showCurrentUserInfo();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void showAndChangeFaction() throws IOException {
        HBox content = new HBox(20); // Add spacing between each VBox
        content.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/PreGamePages.css")).toExternalForm());
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
                    System.out.println(currentUser + "--------" + factionName);
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
        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResource("/Images/pregamebackground.jpg")).toExternalForm());
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        root.setBackground(new Background(background));

        Scene scene = new Scene(root, 1280, 720);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/PreGamePages.css")).toExternalForm());

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
        Out.sendMessage("show deck");
    }

    public void showCards() throws Exception {
        Out.sendMessage("show cards");
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
                alert.getDialogPane().getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/AlertStyler.css")).toExternalForm());
                alert.show();
                currentUser.getDeck().clear();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("your deck is now ready");
                alert.getDialogPane().getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/AlertStyler.css")).toExternalForm());
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
        File selectedFile = fileChooser.showOpenDialog(usernameLabel.getScene().getWindow());

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

    public void changeTurn() throws IOException {
        if (currentUser.getUnitCount() >= 22) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            currentUser = currentUser.equals(Game.getCurrentGame().getPlayer1().getUser()) ? Game.getCurrentGame().getPlayer2().getUser() : Game.getCurrentGame().getPlayer1().getUser();
            alert.setContentText("now player2: " + currentUser.getName() + " should pick cards");
            alert.getDialogPane().getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/AlertStyler.css")).toExternalForm());
            alert.show();
            showCurrentUserInfo();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("You should pick at least 22 unit cards");
            alert.getDialogPane().getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/AlertStyler.css")).toExternalForm());
            alert.show();
        }
    }


    public void startGame() {
        Game game = Game.getCurrentGame();
        System.out.println(game.getPlayer1().getUser().getLeader().getName());
        System.out.println(game.getPlayer2().getUser().getLeader().getName());
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Err");
        alert.getDialogPane().getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/AlertStyler.css")).toExternalForm());
        if (game.getPlayer1().getUser().getDeck().size() < 22 || game.getPlayer1().getUser().getSpecialCount() >= 10) {
            String errMssg = "[ERR]: " + game.getPlayer1().getUser().getName() + "should pick at least 22 cards or has more than 10 special cards!";
            System.out.println(errMssg);
            alert.setContentText(errMssg);
            alert.show();
            return;
        } else if (game.getPlayer2().getUser().getDeck().size() < 22 || game.getPlayer2().getUser().getSpecialCount() >= 10) {
            String errMssg = "[ERR]: " + game.getPlayer2().getUser().getName() + "should pick at least 22 cards or has more than 10 special cards!";
            alert.setContentText(errMssg);
            System.out.println(errMssg);
            alert.show();
            return;
        }

        User user1 = game.getPlayer1().getUser();
        User user2 = game.getPlayer2().getUser();
        try {
            GameLauncher gameLauncher = new GameLauncher();
            gameLauncher.start(ApplicationController.getStage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showCurrentUserInfo() throws IOException {
        Out.sendMessage("showCurrentUserInfo");
    }

    public void showCurrentUserInfo(String name1, String faction1, String deckSize1, String unitCount1, String specialCount1, String heroCount1, String sumOfPower1) {
        usernameLabel.setText(name1);
        factionLabel.setText(faction1);
        cardsCount.setText(String.valueOf(deckSize1));
        unitCount.setText(unitCount1);
        specialCount.setText(String.valueOf(specialCount1));
        heroCount.setText(String.valueOf(heroCount1));
        totalPower.setText(String.valueOf(sumOfPower1));
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

            String imagePath = CardController.imagePath.get(cardName);
            System.out.println(cardName + " -> " + imagePath);
            if (imagePath == null) continue;
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
                start((Stage) usernameLabel.getScene().getWindow());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        button.setLayoutX(LAYOUT_BUTTON);
        button.setLayoutY(LAYOUT_BUTTON);
        body.getChildren().add(button);

        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResource("/Images/pregamebackground.jpg")).toExternalForm());
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
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/PreGamePages.css")).toExternalForm());

        Stage stage = (Stage) usernameLabel.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Show Cards");
        stage.centerOnScreen();
        stage.show();
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
                if (node instanceof Label label) {
                    label.setText("count in deck :" + (currentUser.getCardCount(cardName) + 1));
                    break;
                }
            }


            String result = controller.addToDeck(cardName, currentUser);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(result);
            alert.getDialogPane().getScene().getStylesheets().add(getClass().getResource("/CSS/AlertStyler.css").toExternalForm());
            System.out.println(result);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.getDialogPane().getScene().getStylesheets().add(getClass().getResource("/CSS/AlertStyler.css").toExternalForm());
            alert.show();
        }
    }

    public void showLeaders() throws IOException {
        Out.sendMessage("show leader");


        HBox body = new HBox();
        body.setMinWidth(1260);
        body.setMinHeight(715);
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
                    start((Stage) usernameLabel.getScene().getWindow());
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

        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResource("/Images/pregamebackground.jpg")).toExternalForm());
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);


        body.getChildren().add(content);
        body.setSpacing(120);


        StackPane stackPane = new StackPane();
        stackPane.getChildren().add(body);
        stackPane.setBackground(new Background(background));

        ScrollPane scrollPane = new ScrollPane(stackPane);
        scrollPane.setFitToWidth(true);
        Scene scene = new Scene(scrollPane, 1280, 700);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/PreGamePages.css")).toExternalForm());

        Stage stage = (Stage) usernameLabel.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("show Leaders");
        stage.centerOnScreen();
        stage.show();
    }

    public void backToMainMenu() throws Exception {
        MainMenu mainMenu = new MainMenu();
        usernameLabel.getScene().getWindow().hide();
        mainMenu.start((Stage) usernameLabel.getScene().getWindow());
    }


    @Override
    public void initialize() {

    }

    @Override
    public void handleCommand(String command) throws Exception {
//        if (Regexes.GET_USER.matches(command)) {
//            String userJson = Regexes.GET_USER.getGroup(command, "user");
//            User user = new ObjectMapper().readValue(userJson, User.class);
//            System.out.println("username: " + user.getUsername());
//            System.out.println(user.getStatus().toString());
//        }

        if (command.startsWith("showManyCardsInScrollBar")) invokeMethod(command);
        else if (command.startsWith("showCurrentUserInfo")) {
            String[] params = command.split(" ");
            showCurrentUserInfo(params[1], params[2], params[3], params[4], params[5], params[6], params[6]);
        }
    }

    private void invokeMethod(String command) throws Exception {
        String json = Regexes.SHOW_MANY_CARDS.getGroup(command, "list");
        Gson gson = new Gson();

        ArrayList<String> list = gson.fromJson(json, ArrayList.class);
        boolean aBoolean = Boolean.parseBoolean(Regexes.SHOW_MANY_CARDS.getGroup(command, "boolean"));
        showManyCardsInScrollBar(list, aBoolean);
    }
}