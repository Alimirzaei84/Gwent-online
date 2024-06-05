package view;

import com.google.gson.Gson;
import controller.ApplicationController;
import controller.CardController;
import controller.menuConrollers.PreGameMenuController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
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
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void showAndChangeFaction() throws IOException {
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER);
        File directory = new File("src/main/resources/assets/lg");
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isFile() && file.getName().contains("faction_")) {
                Image image = new Image(file.toURI().toURL().toString());
                AnchorPane anchorPane = new AnchorPane();
                ImageView imageView = new ImageView(image);
                String factionName = buildFactionName(file.getName());


                if (currentUser.getFaction().equals(Faction.valueOf(factionName.toUpperCase()))) {
                    ColorAdjust grayscaleEffect = new ColorAdjust();
                    grayscaleEffect.setSaturation(-1.0);
                    imageView.setEffect(grayscaleEffect);
                    currentImageView = imageView;
                }

                imageView.setOnMouseClicked(_ -> {
                    handleDifferentColor(imageView, factionName);
                    currentUser.setFaction(Faction.valueOf(factionName.toUpperCase()));
                    currentUser.getDeck().clear();
                });
                anchorPane.getChildren().add(imageView);
                Label label = new Label(factionName);
                label.setMinSize(30, 30);
                anchorPane.getChildren().add(label);
                content.getChildren().add(anchorPane);
            }
        }

        Button button = new Button("back");
        button.setMinHeight(100);
        button.setMinWidth(100);
        button.setOnMouseClicked(_ -> {
            try {
                start(ApplicationController.getStage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        content.getChildren().add(button);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        Scene scene = new Scene(scrollPane, 800, 800);
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
        //TODO just for test
//        out.addAll(CardController.leaders);
        showManyCardsInScrollBar(out, false);
    }

    public void uploadDeck() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setTitle("Select an image");
        File selectedFile = fileChooser.showOpenDialog(ApplicationController.getStage());
        try (FileReader reader = new FileReader(selectedFile)) {
            Type listType = new com.google.gson.reflect.TypeToken<ArrayList<String>>() {
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
            alert.setContentText(STR."now player2: \{currentUser.getName()} should pick cards");
            alert.show();
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
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(STR."Player name: \{currentUser.getName()}\nFaction: \{currentUser.getFaction().name()}\nCards count: \{currentUser.getDeck().size()}\nUnit count: \{currentUser.getUnitCount()}\nSpecial count: \{currentUser.getSpecialCount()}\nHero count: \{currentUser.getHeroCount()}\nSum of power: \{currentUser.getSumOfPower()}");
        alert.show();
    }

    private void showManyCardsInScrollBar(ArrayList<String> cardsNames, Boolean deckOrAll) throws Exception {
        VBox vBox = new VBox();
        HBox hBox = new HBox();
        boolean shouldCreateNew = true;
        vBox.setAlignment(Pos.CENTER);
        for (String cardName : cardsNames) {
            if (shouldCreateNew) {
                hBox = new HBox();
                hBox.setSpacing(4);
                vBox.getChildren().add(hBox);
            }
            String imagePath = CardController.imagePath.getOrDefault(cardName, "");
            if (imagePath.isEmpty()) {
                continue;
            }

            ImageView imageView = new ImageView(new Image(new File(imagePath).toURI().toURL().toString()));
            imageView.setOnMouseClicked(_ -> addToDeck(cardName));
            imageView.setOnDragExited(_ -> System.out.println("swipe down"));
            Button button;
            if (deckOrAll) {
                button = new Button("remove");
                button.setMinWidth(20);
                button.setMinHeight(20);
                hBox.getChildren().add(button);
                button.setLayoutX(imageView.getLayoutX() + 20);
                button.setLayoutY(imageView.getLayoutY() + 20);
            } else {
                button = null;
            }
            hBox.getChildren().add(imageView);
            int countInDeck = currentUser.getCardCount(cardName);
            Text text = new Text(STR."count in deck: \{countInDeck}");
            hBox.getChildren().add(text);
            HBox finalHBox = hBox;
            if (deckOrAll) button.setOnMouseClicked(_ -> {
                removeFromDeck(cardName, finalHBox, imageView, text, button);
            });
            shouldCreateNew = !shouldCreateNew;
        }


        // Add back button
        Button button = new Button("Back");
        button.setMinWidth(100);
        button.setMinHeight(100);
        button.setOnMouseClicked(_ -> {
            try {
                start(ApplicationController.getStage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        vBox.getChildren().add(button);


        ScrollPane scrollPane = new ScrollPane(vBox);
        scrollPane.setFitToWidth(true);
        Scene scene = new Scene(scrollPane, 1200, 1200);
        ApplicationController.getStage().setScene(scene);
        ApplicationController.getStage().setTitle("show cards");
        ApplicationController.getStage().show();
    }

    private void removeFromDeck(String cardName, HBox currentHbox, ImageView imageView, Text text, Button button) {
        try {
            System.out.println(controller.removeFromDeck(cardName, currentUser, currentHbox, imageView, text, button));
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.show();
            System.out.println(STR."[ERR]: \{e.getMessage()}");
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
        content.setAlignment(Pos.CENTER);
        for (String leaderName : CardController.leaders) {
            if (!CardController.faction.get(leaderName).equals(User.getLoggedInUser().getFaction())) continue;
            Card card = CardController.createLeaderCard(leaderName);
            ImageView imageView = new ImageView(new Image(new File("src/main/resources/assets/lg/skellige_king_bran.jpg").toURI().toURL().toString()));
            imageView.setOnMouseClicked(_ -> {
                currentUser.setLeader((Leader) card);
                try {
                    start(ApplicationController.getStage());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            content.getChildren().add(imageView);

//            System.out.println(CardController.imagePath.get(leaderName.toLowerCase()));
//            System.out.println(leaderName);
//            for (Map.Entry<String, String> entry : CardController.imagePath.entrySet()) {
//                System.out.println(STR."\{entry.getKey()}++\{entry.getValue()}");
//            }
//            if (file.isFile() && file.getName().contains("faction_")) {
//                Image image = new Image(file.toURI().toURL().toString());
//                AnchorPane anchorPane = new AnchorPane();
//                ImageView imageView = new ImageView(image);
//                String factionName = file.getName().substring(8, file.getName().length() - 4);
//
//
//                if (User.getLoggedInUser().getFaction().equals(Faction.valueOf(factionName.toUpperCase()))) {
//                    ColorAdjust grayscaleEffect = new ColorAdjust();
//                    grayscaleEffect.setSaturation(-1.0);
//                    imageView.setEffect(grayscaleEffect);
//                    currentImageView = imageView;
//                }

//            imageView.setOnMouseClicked(_ -> handleDifferentColor(imageView, factionName));
//            anchorPane.getChildren().add(imageView);
//            Label label = new Label(factionName);
//            label.setMinSize(30, 30);
//            anchorPane.getChildren().add(label);
//            content.getChildren().add(anchorPane);
//        }
//        }

//    Button button = new Button("back");
//        button.setMinHeight(100);
//        button.setMinWidth(100);
//        button.setOnMouseClicked(_ ->
//
//    {
//        try {
//            start(ApplicationController.getStage());
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    });
//        content.getChildren().
//
//    add(button);
//
//    ScrollPane scrollPane = new ScrollPane(content);
//        scrollPane.setFitToWidth(true);
//    Scene scene = new Scene(scrollPane, 800, 800);
//        ApplicationController.getStage().
//
//    setScene(scene);
//        ApplicationController.getStage().
//
//    setTitle("select faction");
//        ApplicationController.getStage().
//
//    show();
        }
        Pane pane = new Pane();
        pane.getChildren().add(content);
        System.out.println("403");
        Scene scene = new Scene(pane);
        System.out.println("403");
        ApplicationController.getStage().setScene(scene);
        System.out.println("403");
        ApplicationController.getStage().show();
    }

    public void backToMainMenu() throws Exception {
        MainMenu mainMenu = new MainMenu();
        mainMenu.start(ApplicationController.getStage());
    }
}