package client.view;

import client.Out;
import controller.ApplicationController;
import controller.CardController;
import controller.menuConrollers.GameController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import server.Account.Player;
import server.game.Board;
import server.game.Game;
import server.game.Row;
import model.role.Card;
import model.role.Special;
import model.role.Weather;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class GameLauncher extends AppMenu {
    private GameController gameController;

    private AnchorPane pane = new AnchorPane();
    public HBox inHandCurHbox;
    public VBox curLeaderVbox;
    public Label otherUsernameLabel;
    public Label curUsernameLabel;
    public Label curFactionLabel;
    public Label otherFactionLabel;
    public Label curInHandCoLabel;
    public Label otherInHandCoLabel;
    public HBox otherDiamondHBox;
    public HBox curDiamondHBox;
    public VBox otherLeaderVbox;
    public Label curDeckCountLabel;
    public Label otherDeckCountLabel;
    public VBox otherDeckVBox;
    public VBox curDeckVBox;
    public Text curRow0ScoreText;
    public Text curRow1ScoreText;
    public Text curRow2ScoreText;
    public Text otherRow0ScoreText;
    public Text otherRow1ScoreText;
    public Text otherRow2ScoreText;
    public Text curScore;
    public Text otherScore;
    public StackPane curRow0StackPane;
    public StackPane curRow2StackPane;
    public StackPane curRow1StackPane;
    public StackPane otherRow0StackPane;
    public StackPane otherRow1StackPane;
    public StackPane otherRow2StackPane;
    public HBox otherSpecialRow2HBox;
    public HBox otherSpecialRow1HBox;
    public HBox otherSpecialRow0HBox;
    public HBox curSpecialRow2HBox;
    public HBox curSpecialRow1HBox;
    public HBox curSpecialRow0HBox;
    public HBox weatherHBox;
    public VBox otherDiscardPileVBox;
    public VBox curDiscardPileVBox;
    public Button vetoButton;
    public HBox displayCardHBox;
    public HBox curRow2HBox = new HBox();
    public HBox curRow1HBox = new HBox();
    public HBox curRow0HBox = new HBox();
    public HBox otherRow0HBox = new HBox();
    public HBox otherRow1HBox = new HBox();
    public HBox otherRow2HBox = new HBox();


    private final int MAX_CARD_SHOW = 12;
    private final HBox inHandPlayer1 = new HBox();

    public GameLauncher() {
    }

    @Override
    public void start(Stage stage) throws Exception {
        URL url = ProfileMenu.class.getResource("/FXML/GameLauncher.fxml");
        assert url != null : "FXML file not found";
        AnchorPane root = FXMLLoader.load(url);
        pane = root;

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/GameLauncher.css")).toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();

        pane.requestFocus();
        handleKeyEvents();
    }

    private void handleKeyEvents() {
        try {
            pane.setOnKeyPressed(event -> {
                switch (event.getCode()) {
                    case DIGIT0 -> {
//                        recoverCard(1, Game.getCurrentGame().getCurrentPlayer());
                    }
                    case INSERT -> {
//                        recoverCard(2, Game.getCurrentGame().getCurrentPlayer());
                    }

                    case D -> {
//                        getFromDeck(Game.getCurrentGame().getCurrentPlayer());
                    }

                    case PLUS -> {
//                        makeWeathersEmpty(Game.getCurrentGame().getWeathers());
                    }

                    case E -> {
//                        decreaseOpponentDiamonds(Game.getCurrentGame().getOtherPlayer());
                    }

                    case F -> {
//                        increaseDiamond(Game.getCurrentGame().getCurrentPlayer());
                    }

                    case G -> {
//                        destroyOpponentClose(Game.getCurrentGame().getOtherPlayer());
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    private void makeWeathersEmpty(ArrayList<Card> weathers) {
//        weathers.clear();
//    }
//
//    private void destroyOpponentClose(Player otherPlayer) {
//        otherPlayer.getRows()[1].getCards().clear();
//    }
//
//    private void increaseDiamond(Player currentPlayer) {
//        currentPlayer.setDiamond((short) Math.max(currentPlayer.getDiamond() + 1, 1));
//    }
//
//    private void decreaseOpponentDiamonds(Player player) {
//        player.setDiamond((short) Math.max(0, player.getDiamond() - 1));
//    }
//
//    private void getFromDeck(Player currentPlayer) {
//        if (currentPlayer.getUser().getDeck().isEmpty()) return;
//        Card card = currentPlayer.getRandomCard(currentPlayer.getUser().getDeck());
//        currentPlayer.getInHand().add(card);
//        currentPlayer.getUser().getDeck().remove(card);
//    }
//
//    private void recoverCard(int i, Player currentPlayer) {
//        for (int j = 0; j < i; j++) {
//            if (currentPlayer.getDiscardCards().isEmpty()) return;
//            Card card = currentPlayer.getRandomCard(currentPlayer.getDiscardCards());
//            currentPlayer.getInHand().add(card);
//            currentPlayer.getDiscardCards().remove(card);
//        }
//    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        client.User.getInstance().setAppMenu(this);
        gameController = new GameController();
        try {
            Out.sendMessage(client.User.getInstance().getUsername() + " give board");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
//
//        Platform.runLater(() -> {
//            for (Card card : Game.getCurrentGame().getPlayer1().getInHand()) {
//                try {
//                    String imagePath = CardController.imagePath.getOrDefault(card.getName(), "/assets/sm/monsters_arachas_behemoth.jpg");
//                    ImageView imageView = new ImageView(new Image(new File(imagePath).toURI().toURL().toString()));
//                    imageView.setOnMouseClicked(event -> selectCard(card, imageView));
//                    imageView.setOnDragExited(event -> System.out.println("swipe down"));
//                    imageView.preserveRatioProperty();
//                    imageView.setFitWidth(52.5);
//                    imageView.setFitHeight(90);
//                    inHandPlayer1.getChildren().add(imageView);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//
//
//        Player curPlayer = Game.getCurrentGame().getCurrentPlayer();
//        Player otherPlayer = Game.getCurrentGame().getOtherPlayer();
//        refreshLeaderOnScreen(curPlayer, otherPlayer);
//
//        curUsernameLabel.setText(curPlayer.getUser().getUsername());
//        curUsernameLabel.setAlignment(Pos.CENTER);
//        otherUsernameLabel.setText(otherPlayer.getUser().getUsername());
//        otherUsernameLabel.setAlignment(Pos.CENTER);
//        curFactionLabel.setText(curPlayer.getUser().getFaction().name());
//        curFactionLabel.setAlignment(Pos.CENTER);
//        otherFactionLabel.setText(otherPlayer.getUser().getFaction().name());
//        otherFactionLabel.setAlignment(Pos.CENTER);
//        curInHandCoLabel.setText("In Hand : " + curPlayer.getInHand().size());
//        curInHandCoLabel.setAlignment(Pos.CENTER);
//        otherInHandCoLabel.setText("In Hand : " + otherPlayer.getInHand().size());
//        otherInHandCoLabel.setAlignment(Pos.CENTER);
//
//        for (Node node : curDiamondHBox.getChildren()) {
//            if (node instanceof Polygon) {
//                Polygon polygon = (Polygon) node;
//                polygon.setFill(Color.rgb(89, 114, 115));
//            }
//        }
//
//        for (Node node : otherDiamondHBox.getChildren()) {
//            Polygon polygon = (Polygon) node;
//            polygon.setFill(Color.rgb(89, 114, 115));
//        }
//
//        curDeckCountLabel.setText(String.valueOf(curPlayer.getUser().getDeck().size()));
//        otherDeckCountLabel.setText(String.valueOf(otherPlayer.getUser().getDeck().size()));
//        try {
//            setFactionOnDeckView(curPlayer.getUser().getFaction().name(), curDeckVBox);
//            setFactionOnDeckView(otherPlayer.getUser().getFaction().name(), otherDeckVBox);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//
//        refreshScores(curPlayer, otherPlayer);

    }

    private void addRainEffect(Pane rainPane) {
        // Create raindrops
        for (int i = 0; i < 100; i++) {
            Line rainDrop = new Line(0, 0, 0, 10);
            rainDrop.setStroke(Color.LIGHTBLUE);
            rainDrop.setStrokeWidth(2);

            rainDrop.setTranslateX(Math.random() * rainPane.getWidth());
            rainDrop.setTranslateY(Math.random() * rainPane.getHeight());

            TranslateTransition tt = new TranslateTransition(Duration.seconds(1), rainDrop);
            tt.setByY(rainPane.getHeight() / 2);  // Halve the Y distance
            tt.setCycleCount(Timeline.INDEFINITE);
            tt.setDelay(Duration.seconds(Math.random()));
            tt.play();

            rainPane.getChildren().add(rainDrop);
        }
    }


    //TODO : CALL AFTER END OF THE GAME
    private void goToStatsMenu() {
        try {
            EndOfGameScreen endOfGameScreen = new EndOfGameScreen();
            endOfGameScreen.start(ApplicationController.getStage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayCard(Player curPlayer, Player otherPlayer) { // TODO : fix
        displayCardHBox.getChildren().clear();
        for (Card card : otherPlayer.getCardInfo()) {
            try {
                String imagePath = CardController.imagePath.getOrDefault(card.getName(), "/assets/sm/monsters_arachas_behemoth.jpg");
                ImageView imageView = new ImageView(new Image(new File(imagePath).toURI().toURL().toString()));
                imageView.setOnMouseClicked(event -> selectCard(card, imageView));
                imageView.setOnDragExited(event -> System.out.println("swipe down"));
                imageView.preserveRatioProperty();
                imageView.setFitWidth(52.5);
                imageView.setFitHeight(90);
                displayCardHBox.getChildren().add(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void refreshScreen(Board board) throws MalformedURLException {


        //TODO : FIX later
//        displayCard(curPlayer, otherPlayer);
        //remove veto button
        if (board.getNumTurn() == 1) {
            if (vetoButton != null)
                vetoButton.setVisible(false);
        }

        //Deck
        setUpHand(board.getMyHand());
        //Left side of the screen
        curUsernameLabel.setText(board.getMyUsername());
        curFactionLabel.setText(board.getMyFaction().name());
        curInHandCoLabel.setText("In Hand : " + board.getMyHand().size());
        otherUsernameLabel.setText(board.getOpponentUsername());
        otherFactionLabel.setText(board.getOpponentFaction().name());
        otherInHandCoLabel.setText("In Hand : " + board.getOppHand().size());

        if (board.isMyTurn()) {
            curUsernameLabel.setTextFill(Color.RED);
            otherUsernameLabel.setTextFill(Color.WHITE);
        } else {
            otherUsernameLabel.setTextFill(Color.RED);
            curUsernameLabel.setTextFill(Color.WHITE);
        }
        //TODO : UPDATE DIAMONDS
        updateDiamondsForPlayer(board.getMyDiamondCount(), curDiamondHBox);
        updateDiamondsForPlayer(board.getOpponentDiamondCount(), otherDiamondHBox);
        refreshLeaderOnScreen(board.getMyLeader().getName(), board.getOpponentLeader().getName());

        //Right side of the screen
        curDeckCountLabel.setText(String.valueOf(board.getMyDeck().size()));
        otherDeckCountLabel.setText(String.valueOf(board.getOppDeck().size()));
        setFactionOnDeckView(board.getMyFaction().name(), curDeckVBox);
        setFactionOnDeckView(board.getOpponentFaction().name(), otherDeckVBox);

        //TODO : update discard pile
        setUpDiscardPile(board.getMyDiscardPile(), curDiscardPileVBox);
        setUpDiscardPile(board.getOpponentDiscardPile(), otherDiscardPileVBox);

        //curRows
        Row[] curRows = board.getMyRows();
        setRowOnScreen(curRows[0], curRow0StackPane, curRow0HBox, curSpecialRow0HBox);
        setRowOnScreen(curRows[1], curRow1StackPane, curRow1HBox, curSpecialRow1HBox);
        setRowOnScreen(curRows[2], curRow2StackPane, curRow2HBox, curSpecialRow2HBox);

        //otherRows
        Row[] otherRows = board.getOppRows();
        setRowOnScreen(otherRows[0], otherRow0StackPane, otherRow0HBox, otherSpecialRow0HBox);
        setRowOnScreen(otherRows[1], otherRow1StackPane, otherRow1HBox, otherSpecialRow1HBox);
        setRowOnScreen(otherRows[2], otherRow2StackPane, otherRow2HBox, otherSpecialRow2HBox);

        //scores
        refreshScores(curRows, otherRows, board.getMyPoint(), board.getOppPoint());

        //Weather pile
        setWeatherOnScreen(board.getWeatherArrayList()); //TODO : TEST

    }

    private void setUpDiscardPile(ArrayList<Card> discardArr, VBox discardPile) throws MalformedURLException {
        if (discardPile == null) return;
        discardPile.getChildren().clear();
        discardPile.setAlignment(Pos.CENTER);
        discardPile.setSpacing(10);
        if (discardArr.size() == 0) return;

        Card card = discardArr.getLast();
        String imagePath = CardController.imagePath.getOrDefault(card.getName(), "/assets/sm/monsters_arachas_behemoth.jpg");
        ImageView imageView = new ImageView(new Image(new File(imagePath).toURI().toURL().toString()));
        imageView.setOnDragExited(event -> System.out.println("swipe down"));
        imageView.preserveRatioProperty();
        imageView.setFitWidth(52.5);
        imageView.setFitHeight(90);
        discardPile.getChildren().add(imageView);

    }

    private void setWeatherOnScreen(ArrayList<Card> weathers) throws MalformedURLException {
        weatherHBox.getChildren().clear();
        for (Card weather : weathers) {
            String imagePath = CardController.imagePath.getOrDefault(weather.getName(), "/assets/sm/monsters_arachas_behemoth.jpg");
            ImageView imageView = new ImageView(new Image(new File(imagePath).toURI().toURL().toString()));
            imageView.setOnDragExited(event -> System.out.println("swipe down"));
            imageView.preserveRatioProperty();
            imageView.setFitWidth(52.5);
            imageView.setFitHeight(90);
            weatherHBox.getChildren().add(imageView);
        }
    }

    private void setRowOnScreen(Row row, StackPane rowStackPane, HBox rowHBox, HBox specialRowBox) throws MalformedURLException {
        // Clear existing children from rowHBox and specialRowBox
        rowHBox.getChildren().clear();
        specialRowBox.getChildren().clear();
        rowStackPane.getChildren().clear();

        // Create the rainPane and bind its size to rowStackPane's size
        Pane rainPane = new Pane();
        rainPane.prefWidthProperty().bind(rowStackPane.widthProperty());
        rainPane.prefHeightProperty().bind(rowStackPane.heightProperty());

        // Add rowHBox and specialRowBox to rowStackPane
        rowStackPane.getChildren().addAll(rowHBox, specialRowBox);

        // Add rainPane to rowStackPane if it's raining
        if (row.isOnFrost()) {
            addRainEffect(rainPane);
            rowStackPane.getChildren().add(rainPane);
        }

        // Populate the row with cards
        int index = 0;
        for (Card card : row.getCards()) {
            if (index >= 10)
                continue;

            String imagePath = CardController.imagePath.getOrDefault(card.getName(), "/assets/sm/monsters_arachas_behemoth.jpg");
            ImageView imageView = new ImageView(new Image(new File(imagePath).toURI().toURL().toString()));
            imageView.setOnDragExited(event -> System.out.println("swipe down"));
            imageView.preserveRatioProperty();
            imageView.setFitWidth(52.5);
            imageView.setFitHeight(90);
            rowHBox.getChildren().add(imageView);

            index++;
        }

        // Add the special card if present
        if (row.getSpecial() != null) {
            String imagePath = CardController.imagePath.getOrDefault(row.getSpecial().getName(), "/assets/sm/monsters_arachas_behemoth.jpg");
            ImageView imageView = new ImageView(new Image(new File(imagePath).toURI().toURL().toString()));
            imageView.setOnDragExited(event -> System.out.println("swipe down"));
            imageView.preserveRatioProperty();
            imageView.setFitWidth(52.5);
            imageView.setFitHeight(72);
            specialRowBox.getChildren().add(imageView);
        }
    }


    private void refreshLeaderOnScreen(String curLeaderName, String otherLeaderName) {
        String curLeaderPath = CardController.imagePath.get(curLeaderName);
        String otherLeaderPath = CardController.imagePath.get(otherLeaderName);
        try {
            setLeadersOnScreen(curLeaderPath, curLeaderVbox);
            setLeadersOnScreen(otherLeaderPath, otherLeaderVbox);
            //TODO : SET MOUSE CLICKED IF NEEDED!
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void updateDiamondsForPlayer(int diamondCo, HBox diamondHBox) {
        int max = diamondCo;
        int num = 0;
        for (Node node : diamondHBox.getChildren()) {
            if (node instanceof Polygon polygon) {
                num++;
                if (num > max) polygon.setFill(Color.rgb(89, 114, 115));
                else polygon.setFill(Color.rgb(112, 36, 40));
            }
        }
    }

    private void setUpHand(ArrayList<Card> inHand) {
        inHandCurHbox.getChildren().clear();
        int index = 0;
        System.out.println("HAND : ---->" + inHand.size());
        for (Card card : inHand) {
            try {
                if (index >= MAX_CARD_SHOW) continue;
                String imagePath = CardController.imagePath.getOrDefault(card.getName(), "/assets/sm/monsters_arachas_behemoth.jpg");
                ImageView imageView = new ImageView(new Image(new File(imagePath).toURI().toURL().toString()));
                imageView.setOnMouseClicked(event -> selectCard(card, imageView));
                imageView.setOnDragExited(event -> System.out.println("swipe down"));
                imageView.preserveRatioProperty();
                imageView.setFitWidth(52.5);
                imageView.setFitHeight(90);
                inHandCurHbox.getChildren().add(imageView);
                index++;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void refreshScores(Row[] curRows, Row[] otherRows, int curTotalPoint, int otherTotalPoint) {
        curRow0ScoreText.setText(String.valueOf(curRows[0].getPoint()));
        curRow1ScoreText.setText(String.valueOf(curRows[1].getPoint()));
        curRow2ScoreText.setText(String.valueOf(curRows[2].getPoint()));
        otherRow0ScoreText.setText(String.valueOf(otherRows[0].getPoint()));
        otherRow1ScoreText.setText(String.valueOf(otherRows[1].getPoint()));
        otherRow2ScoreText.setText(String.valueOf(otherRows[2].getPoint()));

        curScore.setText(String.valueOf(curTotalPoint));
        otherScore.setText(String.valueOf(otherTotalPoint));

        curRow0ScoreText.setTextAlignment(TextAlignment.CENTER);
        curRow2ScoreText.setTextAlignment(TextAlignment.CENTER);
        curRow1ScoreText.setTextAlignment(TextAlignment.CENTER);

        otherRow0ScoreText.setTextAlignment(TextAlignment.CENTER);
        otherRow1ScoreText.setTextAlignment(TextAlignment.CENTER);
        otherRow2ScoreText.setTextAlignment(TextAlignment.CENTER);

        curScore.setTextAlignment(TextAlignment.CENTER);
        otherScore.setTextAlignment(TextAlignment.CENTER);
    }

    private void setFactionOnDeckView(String factionName, VBox vBox) throws MalformedURLException {
        vBox.getChildren().clear();
        String imagePath = switch (factionName.toUpperCase()) {
            case "NORTHERN_REALMS" -> "src/main/resources/assets/lg/faction_realms.jpg";
            case "NILFGAARDIAN_EMPIRE" -> "src/main/resources/assets/lg/faction_nilfgaard.jpg";
            case "MONSTERS" -> "src/main/resources/assets/lg/faction_monsters.jpg";
            case "SCOIA_TAEL" -> "src/main/resources/assets/lg/faction_scoiatael.jpg";
            default -> "src/main/resources/assets/lg/faction_skellige.jpg";
        };

        ImageView imageView = setImageView(imagePath);
        vBox.getChildren().add(imageView);
    }

    private ImageView setImageView(String imagePath) throws MalformedURLException {
        ImageView imageView = new ImageView(new Image(new File(imagePath).toURI().toURL().toString()));
        imageView.setOnDragExited(event -> System.out.println("swipe down"));
        imageView.preserveRatioProperty();
        imageView.setFitWidth(77);
        imageView.setFitHeight(99);
        return imageView;
    }

    private void setLeadersOnScreen(String leaderPath, VBox leaderVbox) throws MalformedURLException {
        leaderVbox.getChildren().clear();
        ImageView leaderImageView = new ImageView(new Image(new File(leaderPath).toURI().toURL().toString()));
        leaderImageView.setOnDragExited(event -> System.out.println("swipe down"));
        leaderImageView.preserveRatioProperty();
        leaderImageView.setFitWidth(77);
        leaderImageView.setFitHeight(99);
        leaderVbox.getChildren().add(leaderImageView);
    }

    public void selectCard(Card card, ImageView imageView) {
        if (gameController.getSelectedCard() != null) {
            deSelectCard();
        }

        applyBlue(imageView);
        gameController.setSelectedCard(card);
        gameController.setSelectedImageView(imageView);
    }

    public void applyBlue(ImageView imageView) {
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setHue(0.5);
        colorAdjust.setSaturation(0.5);
        Blend blend = new Blend();

        blend.setMode(BlendMode.MULTIPLY);
        blend.setTopInput(new ColorInput(0, 0, 52.5, 90, Color.color(0, 0, 1, 0.5)));
        blend.setBottomInput(colorAdjust);
        imageView.setEffect(blend);
    }

    public void resetTint(ImageView imageView) {
        imageView.setEffect(null);
    }

    public void deSelectCard() {
        resetTint(gameController.getSelectedImageView());
        gameController.setSelectedCard(null);
        gameController.setSelectedImageView(null);
    }

    public void putCardWeather() throws MalformedURLException {
        try {
            Out.sendMessage(client.User.getInstance().getUsername() + " put card " + gameController.getSelectedCard().getName());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            deSelectCard();
        }
    }

    public void putCardSpecial() throws MalformedURLException {
        try {
            Out.sendMessage(client.User.getInstance().getUsername() + " put card " + gameController.getSelectedCard().getName());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            deSelectCard();
        }
    }

    public void putCard() throws MalformedURLException {
        Card selectedCard = gameController.getSelectedCard();

        if (selectedCard == null) return;

        if (selectedCard instanceof Weather) {
            putCardWeather();
            return;
        }

        if (selectedCard instanceof Special) {
            putCardSpecial();
            return;
        }

        gameController.getSelectedImageView().setOnMouseClicked(event -> {
        });

        //TODO : might have to remove this later
        int rowNum = CardController.getRowNumber(gameController.getSelectedCard().getName());
        switch (rowNum) {
            case 1 -> curRow1HBox.getChildren().add(gameController.getSelectedImageView());
            case 2 -> curRow2HBox.getChildren().add(gameController.getSelectedImageView());
            default -> curRow0HBox.getChildren().add(gameController.getSelectedImageView());
        }


        try {
            Out.sendMessage(client.User.getInstance().getUsername() + " put card " + gameController.getSelectedCard().getName());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("HERE");
            deSelectCard();
        }

    }

    public void pass() throws IOException {
        Out.sendMessage(client.User.getInstance().getUsername() + " passed");

    }

    public void showDescription() {
        //TODO
    }

    public void veto() {
        if (gameController.getSelectedCard() == null) return;
        try {
            Out.sendMessage(client.User.getInstance().getUsername() + " veto " + gameController.getSelectedCard().getName());
        } catch (Exception e) {
            System.err.println("[ERR] : " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("You can only veto twice in the firstRound");
            alert.getDialogPane().getScene().getStylesheets().add(getClass().getResource("/CSS/AlertStyler.css").toExternalForm());
            alert.show();
        }
    }


    public void executeAction() throws MalformedURLException {

        try {
            Out.sendMessage(client.User.getInstance().getUsername() + " leader");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleCommand(String command) {
        //TODO : handle winning condition
        if (command.startsWith("[ERR]")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText(command.substring("[ERR]:".length()));
            Scene scene = alert.getDialogPane().getScene();
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/AlertStyler.css")).toExternalForm());
            alert.showAndWait();
        }


    }

    public synchronized void getBoard(Board board) throws MalformedURLException {
        System.out.println("in line 700 game launcher " + board.getMyHand().size());
        System.out.println("in line 700 game launcher " + board.getOppHand().size());
        System.out.println("----I GOT BOARD HERE!-----");
        refreshScreen(board);
    }

}
