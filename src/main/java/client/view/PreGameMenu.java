package client.view;

import client.Out;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import controller.ApplicationController;
import controller.CardController;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;


public class PreGameMenu<T> extends AppMenu {
    private ImageView currentImageView;
    private final int MAX_CARD_IN_LINE = 4;
    private final int BACK_BUTTON_SPACING = 80;
    private final int LAYOUT_BUTTON = 40;

    //    private Label label;
    @FXML
    private AnchorPane readyButton;
    private VBox vBox;
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

        readyButton.setVisible(false);
    }

    public void showAndChangeFaction() throws IOException {
        Out.sendMessage("show factions");
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

    private void handleDifferentColor(ImageView imageView) {
//        controller.setFaction(User.getLoggedInUser(), factionName);
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
            ArrayList<String> restoredList = new ObjectMapper().readValue(reader, ArrayList.class);

            Out.sendMessage("set deck " + new ObjectMapper().writeValueAsString(restoredList));

            Alert alert = getAlert();
            alert.getDialogPane().getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/AlertStyler.css")).toExternalForm());
            alert.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Alert getAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("your deck is now ready");
        return alert;
    }

    public void downloadDeck() throws IOException {
        Out.sendMessage("download deck");
    }

    public void downloadDeck(ArrayList<String> cardNames) throws IOException {

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setTitle("Select an image");
        File selectedFile = fileChooser.showOpenDialog(usernameLabel.getScene().getWindow());
        if (selectedFile != null) {
            try (Writer writer = new FileWriter(selectedFile)) {
                writer.write(new ObjectMapper().writeValueAsString(cardNames));
            }
        }

    }

    public void readyToPlay() {
//        if (currentUser.getUnitCount() >= 22) {
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            currentUser = currentUser.equals(Game.getCurrentGame().getPlayer1().getUser()) ? Game.getCurrentGame().getPlayer2().getUser() : Game.getCurrentGame().getPlayer1().getUser();
//            alert.setContentText("now player2: " + currentUser.getName() + " should pick cards");
//            alert.getDialogPane().getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/AlertStyler.css")).toExternalForm());
//            alert.show();
//            showCurrentUserInfo();
//        } else {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setContentText("You should pick at least 22 unit cards");
//            alert.getDialogPane().getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/AlertStyler.css")).toExternalForm());
//            alert.show();
//        }
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

    private int count(ArrayList<T> arrayList, T element) {
        if (arrayList == null) return 0;
        int count = 0;
        for (T t : arrayList) {
            if (t.equals(element)) count++;
        }
        return count;
    }

    private void showManyCardsInScrollBar(ArrayList<String> cardsNames, Boolean deckOrAll, ArrayList<String> deck) throws IOException {

        AnchorPane pane = new AnchorPane();
        ScrollPane scrollPane = new ScrollPane(pane);
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
            ImageView imageView = getImageView(cardName, imagePath);

            Label label = new Label("count in deck: " + count((ArrayList<T>) deck, (T) cardName));
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
                button.setOnMouseClicked(event -> {
                    try {
                        removeFromDeck(cardBox, cardName, finalHBox, imageView, label, button);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            hBox.getChildren().add(cardBox);
        }

        // Add back button
        Button button = getButton(scrollPane);
        body.getChildren().add(button);

        Image backgroundImage = new Image(Objects.requireNonNull(getClass().getResource("/Images/pregamebackground.jpg")).toExternalForm());
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);

        body.getChildren().add(vBox);
        body.setMinHeight(715);
        body.setMinWidth(1260);
        pane.getChildren().add(body);
        pane.setBackground(new Background(background));


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

    private Button getButton(ScrollPane scrollPane) {
        Button button = new Button("Back");
        button.setMinWidth(100);
        button.setMinHeight(60);
        button.setOnMouseClicked(mouseEvent -> {
            try {
                back(scrollPane);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        button.setLayoutX(LAYOUT_BUTTON);
        button.setLayoutY(LAYOUT_BUTTON);
        return button;
    }

    private void back(ScrollPane scrollPane) throws Exception {
        new PreGameMenu<>().start((Stage) scrollPane.getScene().getWindow());
    }

    private ImageView getImageView(String cardName, String imagePath) throws MalformedURLException {
        ImageView imageView = new ImageView(new Image(new File(imagePath).toURI().toURL().toString()));
        imageView.setOnMouseClicked(event -> {
            try {
                addToDeck(cardName, (VBox) imageView.getParent());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        imageView.setOnDragExited(event -> System.out.println("swipe down"));
        imageView.preserveRatioProperty();
        imageView.setFitWidth(150);
        imageView.setFitHeight(250);
        return imageView;
    }

    private void removeFromDeck(VBox cardBox, String cardName, HBox currentHbox, ImageView imageView, Label label, Button button) throws Exception {
        int count = Integer.parseInt(label.getText().split(" ")[label.getText().split(" ").length - 1]) - 1;
        label.setText("count in deck: " + count);
        if (count == 0) {
            currentHbox.getChildren().remove(label);
            currentHbox.getChildren().remove(imageView);
            currentHbox.getChildren().remove(button);
            currentHbox.getChildren().remove(cardBox);
        }
        Out.sendMessage("removeFromDeck " + cardName);
        System.out.println("[SUCC]: " + cardName + "removed from deck");

//        } catch (Exception e) {
//            Alert alert = new Alert(Alert.AlertType.ERROR);
//            alert.setContentText(e.getMessage());
//            alert.getDialogPane().getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/AlertStyler.css")).toExternalForm());
//            alert.show();
//            System.out.println("[ERR]: " + e.getMessage());
//        }


    }

    private void addToDeck(String cardName, VBox cardBox) throws IOException {

        vBox = cardBox;

        Out.sendMessage("addToDeck " + cardName);
    }

    public void showLeaders() throws IOException {
        Out.sendMessage("show leader");
    }

    public void backToMainMenu() throws Exception {
        MainMenu mainMenu = new MainMenu();
        usernameLabel.getScene().getWindow().hide();
        mainMenu.start((Stage) usernameLabel.getScene().getWindow());
    }


    @Override
    public void handleCommand(String command) throws Exception {

        if (command.startsWith("showManyCardsInScrollBar")) invokeMethod(command);
        else if (command.startsWith("showCurrentUserInfo")) {
            String[] params = command.split(" ");
            showCurrentUserInfo(params[1], params[2], params[3], params[4], params[5], params[6], params[6]);
        } else if (command.startsWith("addToDeckResult")) {
            handleAddToDeckResult(command.split(" ", 2)[1]);
        } else if (Regexes.SHOW_LEADER.matches(command)) {
            showLeaders(new ObjectMapper().readValue(Regexes.SHOW_LEADER.getGroup(command, "factionJson"), Faction.class));
        } else if (Regexes.DOWNLOAD_DECK.matches(command)) {
            downloadDeck(new ObjectMapper().readValue(Regexes.DOWNLOAD_DECK.getGroup(command, "deckJson"), ArrayList.class));
        } else if(Regexes.SHOW_FACTION_RESULT.matches(command)){
            showAndChangeFaction(new ObjectMapper().readValue(Regexes.SHOW_FACTION_RESULT.getGroup(command, "factionJson"), Faction.class));
        }
    }

    private  void showAndChangeFaction(Faction faction) throws MalformedURLException {

        HBox content = new HBox(20); // Add spacing between each VBox
        content.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/PreGamePages.css")).toExternalForm());
        File directory = new File("src/main/resources/assets/lg");

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isFile() && file.getName().contains("faction_")) {
                Image image = new Image(file.toURI().toURL().toString());
                ImageView imageView = new ImageView(image);
                String factionName = buildFactionName(file.getName());

                if (faction.equals(Faction.valueOf(factionName.toUpperCase()))) {
                    ColorAdjust grayscaleEffect = new ColorAdjust();
                    grayscaleEffect.setSaturation(-1.0);
                    imageView.setEffect(grayscaleEffect);
                    currentImageView = imageView;
                }

                imageView.setOnMouseClicked(event -> {
                    handleDifferentColor(imageView);
                    try {
                        Out.sendMessage("set faction " + factionName);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
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
                start((Stage) button.getScene().getWindow());
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

        usernameLabel.getScene().getWindow().hide();
        Stage stage = (Stage) usernameLabel.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("select faction");
        stage.show();
    }

    private void showLeaders(Faction faction) throws MalformedURLException {
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
                start((Stage) button.getScene().getWindow());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        body.getChildren().add(button);
        button.setLayoutX(LAYOUT_BUTTON);
        button.setLayoutY(LAYOUT_BUTTON);

        for (String leaderName : CardController.leaders) {
            if (!CardController.faction.get(leaderName).equals(faction)) continue;
            Card card = CardController.createLeaderCard(leaderName);
            ImageView imageView = new ImageView(new Image(new File(CardController.imagePath.getOrDefault(card.getName(), "src/main/resources/assets/lg/skellige_king_bran.jpg")).toURI().toURL().toString()));
            imageView.setOnMouseClicked(event -> {
                try {
                    Out.sendMessage("change leader " + card.getName());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    start((Stage) imageView.getScene().getWindow());
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

    private void handleAddToDeckResult(String result) {
        if (result.startsWith("[SUCC]")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(result);
            alert.getDialogPane().getScene().getStylesheets().add(getClass().getResource("/CSS/AlertStyler.css").toExternalForm());
            System.out.println(result);

            for (Node node : vBox.getChildren()) {
                if (node instanceof Label label) {
                    label.setText("count in deck: " + (Integer.parseInt(label.getText().split(" ")[3]) + 1));
                    break;
                }
            }

        } else if (result.startsWith("[ERR]")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(result);
            alert.getDialogPane().getScene().getStylesheets().add(getClass().getResource("/CSS/AlertStyler.css").toExternalForm());
            alert.show();
        } else throw new RuntimeException("\"" + result + "\"" + " is not a valid result for adding to deck request");
    }

    private void invokeMethod(String command) throws Exception {

        String json = Regexes.SHOW_MANY_CARDS.getGroup(command, "list");
        Gson gson = new Gson();
        ArrayList<String> list = gson.fromJson(json, ArrayList.class);

        boolean aBoolean = Boolean.parseBoolean(Regexes.SHOW_MANY_CARDS.getGroup(command, "boolean"));

        json = Regexes.SHOW_MANY_CARDS.getGroup(command, "deck");
        ArrayList<String> deck = gson.fromJson(json, ArrayList.class);

        showManyCardsInScrollBar(list, aBoolean, deck);
    }
}