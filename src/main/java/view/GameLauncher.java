package view;

import controller.CardController;
import controller.menuConrollers.GameController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Account.Player;
import model.game.Game;
import model.game.Row;
import model.role.Card;
import model.role.Special;
import model.role.Weather;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class GameLauncher extends AppMenu {
    private final GameController gameController;
    private Timeline sideSwapTimeLine;


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
    public HBox curRow2HBox;
    public HBox curRow1HBox;
    public HBox curRow0HBox;
    public HBox otherRow0HBox;
    public HBox otherRow1HBox;
    public HBox otherRow2HBox;
    public HBox otherSpecialRow2HBox;
    public HBox otherSpecialRow1HBox;
    public HBox otherSpecialRow0HBox;
    public HBox curSpecialRow2HBox;
    public HBox curSpecialRow1HBox;
    public HBox curSpecialRow0HBox;
    public HBox weatherHBox;
    public VBox otherDiscardPileVBox;
    public VBox curDiscardPileVBox;


    private boolean isScreenLocked;
    private final int MAX_CARD_SHOW = 12;
    private HBox inHandPlayer1 = new HBox();

    public GameLauncher() {
        gameController = new GameController(Game.getCurrentGame());
        isScreenLocked = false;
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
        pane.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case DIGIT0 -> recoverCard(1, Game.getCurrentGame().getCurrentPlayer());
                case INSERT -> recoverCard(2, Game.getCurrentGame().getCurrentPlayer());
                case D -> getFromDeck(Game.getCurrentGame().getCurrentPlayer());
                case PLUS -> makeWeathersEmpty(Game.getCurrentGame().getWeathers());
                case E -> decreaseOpponentDiamonds(Game.getCurrentGame().getOtherPlayer());
                case F -> increaseDiamond(Game.getCurrentGame().getCurrentPlayer());
                case G -> destroyOpponentClose(Game.getCurrentGame().getOtherPlayer());
            }

        });
    }

    private void makeWeathersEmpty(ArrayList<Card> weathers) {
        weathers.clear();
    }

    private void destroyOpponentClose(Player otherPlayer) {
        otherPlayer.getRows()[1].getCards().clear();
    }

    private void increaseDiamond(Player currentPlayer) {
        currentPlayer.setDiamond((short) Math.max(currentPlayer.getDiamond() + 1, 1));
    }

    private void decreaseOpponentDiamonds(Player player) {
        player.setDiamond((short) Math.max(0, player.getDiamond() - 1));
    }


    private void getFromDeck(Player currentPlayer) {
        if (currentPlayer.getUser().getDeck().isEmpty()) return;
        Card card = currentPlayer.getRandomCard(currentPlayer.getUser().getDeck());
        currentPlayer.getInHand().add(card);
        currentPlayer.getUser().getDeck().remove(card);
    }


    private void recoverCard(int i, Player currentPlayer) {
        for (int j = 0; j < i; j++) {
            if (currentPlayer.getDiscardCards().isEmpty()) return;
            Card card = currentPlayer.getRandomCard(currentPlayer.getDiscardCards());
            currentPlayer.getInHand().add(card);
            currentPlayer.getDiscardCards().remove(card);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gameController.initializeGame();
        inHandCurHbox.getChildren().add(inHandPlayer1);
        setUpTimeLine();

        Platform.runLater(() -> {
            for (Card card : Game.getCurrentGame().getPlayer1().getInHand()) {
                try {
                    String imagePath = CardController.imagePath.getOrDefault(card.getName(), "/assets/sm/monsters_arachas_behemoth.jpg");
                    ImageView imageView = new ImageView(new Image(new File(imagePath).toURI().toURL().toString()));
                    imageView.setOnMouseClicked(event -> selectCard(card, imageView));
                    imageView.setOnDragExited(event -> System.out.println("swipe down"));
                    imageView.preserveRatioProperty();
                    imageView.setFitWidth(52.5);
                    imageView.setFitHeight(90);
                    inHandPlayer1.getChildren().add(imageView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Player curPlayer = Game.getCurrentGame().getCurrentPlayer();
        Player otherPlayer = Game.getCurrentGame().getOtherPlayer();
        refreshLeaderOnScreen(curPlayer, otherPlayer);

        curUsernameLabel.setText(curPlayer.getUser().getUsername());
        curUsernameLabel.setAlignment(Pos.CENTER);
        otherUsernameLabel.setText(otherPlayer.getUser().getUsername());
        otherUsernameLabel.setAlignment(Pos.CENTER);
        curFactionLabel.setText(curPlayer.getUser().getFaction().name());
        curFactionLabel.setAlignment(Pos.CENTER);
        otherFactionLabel.setText(otherPlayer.getUser().getFaction().name());
        otherFactionLabel.setAlignment(Pos.CENTER);
        curInHandCoLabel.setText("In Hand : " + curPlayer.getInHand().size());
        curInHandCoLabel.setAlignment(Pos.CENTER);
        otherInHandCoLabel.setText("In Hand : " + otherPlayer.getInHand().size());
        otherInHandCoLabel.setAlignment(Pos.CENTER);

        for (Node node : curDiamondHBox.getChildren()) {
            if (node instanceof Polygon) {
                Polygon polygon = (Polygon) node;
                polygon.setFill(Color.rgb(89, 114, 115));
            }
        }

        for (Node node : otherDiamondHBox.getChildren()) {
            Polygon polygon = (Polygon) node;
            polygon.setFill(Color.rgb(89, 114, 115));
        }

        curDeckCountLabel.setText(String.valueOf(curPlayer.getUser().getDeck().size()));
        otherDeckCountLabel.setText(String.valueOf(otherPlayer.getUser().getDeck().size()));
        try {
            setFactionOnDeckView(curPlayer.getUser().getFaction().name(), curDeckVBox);
            setFactionOnDeckView(otherPlayer.getUser().getFaction().name(), otherDeckVBox);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        refreshScores(curPlayer, otherPlayer);

    }

    //TODO : EMPTY SELECTED CARD FOR THE CHANGE TURN
    private void swapSides(Player curPlayer, Player otherPlayer) throws MalformedURLException {
        isScreenLocked = false;
        if (gameController.getSelectedCard() != null) {
            deSelectCard();
        }

        refreshScreen(curPlayer, otherPlayer);
    }

    private void setUpTimeLine() {
        sideSwapTimeLine = new Timeline(new KeyFrame(Duration.seconds(5), event -> {
            try {
                swapSides(Game.getCurrentGame().getCurrentPlayer(), Game.getCurrentGame().getOtherPlayer());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }));
        sideSwapTimeLine.setCycleCount(1);
    }

    private void refreshScreen(Player curPlayer, Player otherPlayer) throws MalformedURLException {
        //Deck
        setUpHand(curPlayer);
        System.out.println(Game.getCurrentGame().getCurrentPlayer().getDiamond());
        System.out.println(Game.getCurrentGame().getOtherPlayer().getDiamond());
        //Left side of the screen
        curUsernameLabel.setText(curPlayer.getUser().getUsername());
        curFactionLabel.setText(curPlayer.getLeader().getFaction().name());
        curInHandCoLabel.setText("In Hand : " + curPlayer.getInHand().size());
        otherUsernameLabel.setText(otherPlayer.getUser().getUsername());
        otherFactionLabel.setText(otherPlayer.getLeader().getFaction().name());
        otherInHandCoLabel.setText("In Hand : " + otherPlayer.getInHand().size());

        updateDiamondsForPlayer(curPlayer, curDiamondHBox);
        updateDiamondsForPlayer(otherPlayer, otherDiamondHBox);
        refreshLeaderOnScreen(curPlayer, otherPlayer);

        //Right side of the screen
        curDeckCountLabel.setText(String.valueOf(curPlayer.getUser().getDeck().size()));
        otherDeckCountLabel.setText(String.valueOf(otherPlayer.getUser().getDeck().size()));
        setFactionOnDeckView(curPlayer.getLeader().getFaction().name(), curDeckVBox);
        setFactionOnDeckView(otherPlayer.getLeader().getFaction().name(), otherDeckVBox);

        //TODO : test discard pile
        setUpDiscardPile(curPlayer, curDiscardPileVBox);
        setUpDiscardPile(otherPlayer, otherDiscardPileVBox);

        //curRows
        Row[] curRows = curPlayer.getRows();
        setRowOnScreen(curPlayer, curRows[0], curRow0HBox, curSpecialRow0HBox);
        setRowOnScreen(curPlayer, curRows[1], curRow1HBox, curSpecialRow1HBox);
        setRowOnScreen(curPlayer, curRows[2], curRow2HBox, curSpecialRow2HBox);

        //otherRows
        Row[] otherRows = otherPlayer.getRows();
        setRowOnScreen(otherPlayer, otherRows[0], otherRow0HBox, otherSpecialRow0HBox);
        setRowOnScreen(otherPlayer, otherRows[1], otherRow1HBox, otherSpecialRow1HBox);
        setRowOnScreen(otherPlayer, otherRows[2], otherRow2HBox, otherSpecialRow2HBox);

        //scores
        refreshScores(curPlayer, otherPlayer);

        //Weather pile
        setWeatherOnScreen(); //TODO : TEST


    }

    private void setUpDiscardPile(Player player, VBox discardPile) throws MalformedURLException {
        if (discardPile == null) return;
        discardPile.getChildren().clear();
        discardPile.setAlignment(Pos.CENTER);
        discardPile.setSpacing(10);
        if (player.getDiscardCards().size() == 0) return;

        Card card = player.getDiscardCards().getLast();
        String imagePath = CardController.imagePath.getOrDefault(card.getName(), "/assets/sm/monsters_arachas_behemoth.jpg");
        ImageView imageView = new ImageView(new Image(new File(imagePath).toURI().toURL().toString()));
        imageView.setOnDragExited(event -> System.out.println("swipe down"));
        imageView.preserveRatioProperty();
        imageView.setFitWidth(52.5);
        imageView.setFitHeight(90);
        discardPile.getChildren().add(imageView);

    }

    private void showNextPlayersTurn() {

    }

    private void setWeatherOnScreen() throws MalformedURLException {
        weatherHBox.getChildren().clear();
        for (Card weather : Game.getCurrentGame().getWeathers()) {
            String imagePath = CardController.imagePath.getOrDefault(weather.getName(), "/assets/sm/monsters_arachas_behemoth.jpg");
            ImageView imageView = new ImageView(new Image(new File(imagePath).toURI().toURL().toString()));
            imageView.setOnDragExited(event -> System.out.println("swipe down"));
            imageView.preserveRatioProperty();
            imageView.setFitWidth(52.5);
            imageView.setFitHeight(90);
            weatherHBox.getChildren().add(imageView);
        }
    }

    private void setRowOnScreen(Player player, Row row, HBox rowHBox, HBox specialRowBox) throws MalformedURLException {

        rowHBox.getChildren().clear();
        specialRowBox.getChildren().clear();
        int index = 0;
        for (Card card : row.getCards()) {

            if (index >= MAX_CARD_SHOW) continue;

            System.out.println("Row " + row.getName() + " card : " + card.getName());
            String imagePath = CardController.imagePath.getOrDefault(card.getName(), "/assets/sm/monsters_arachas_behemoth.jpg");
            ImageView imageView = new ImageView(new Image(new File(imagePath).toURI().toURL().toString()));
            imageView.setOnDragExited(event -> System.out.println("swipe down"));
            imageView.preserveRatioProperty();
            imageView.setFitWidth(52.5);
            imageView.setFitHeight(90);
            rowHBox.getChildren().add(imageView);
            index++;
        }

        if (row.getSpecial() == null) {
            System.err.println("NULLAM KHAR KOSE");
        }

        if (row.getSpecial() != null && specialRowBox != null) {
            String imagePath = CardController.imagePath.getOrDefault(row.getSpecial().getName(), "/assets/sm/monsters_arachas_behemoth.jpg");
            ImageView imageView = new ImageView(new Image(new File(imagePath).toURI().toURL().toString()));
            imageView.setOnDragExited(event -> System.out.println("swipe down"));
            imageView.preserveRatioProperty();
            imageView.setFitWidth(77);
            imageView.setFitHeight(72);
            specialRowBox.getChildren().add(imageView);
        }
    }

    private void refreshLeaderOnScreen(Player curPlayer, Player otherPlayer) {
        String curLeaderPath = CardController.imagePath.get(curPlayer.getLeader().getName());
        String otherLeaderPath = CardController.imagePath.get(otherPlayer.getLeader().getName());
        try {
            setLeadersOnScreen(curLeaderPath, curLeaderVbox);
            setLeadersOnScreen(otherLeaderPath, otherLeaderVbox);
            //TODO : SET MOUSE CLICKED IF NEEDED!
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void updateDiamondsForPlayer(Player player, HBox diamondHBox) {
        int max = player.getDiamond();
        int num = 0;
        for (Node node : diamondHBox.getChildren()) {
            if (node instanceof Polygon polygon) {
                num++;
                if (num > max) polygon.setFill(Color.rgb(89, 114, 115));
                else polygon.setFill(Color.rgb(112, 36, 40));
            }
        }
    }

    private void setUpHand(Player curPlayer) {
        inHandCurHbox.getChildren().clear();
        int index = 0;
        for (Card card : curPlayer.getInHand()) {
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

    private void refreshScores(Player curPlayer, Player otherPlayer) {
        Row[] curRows = curPlayer.getRows();
        Row[] otherRows = otherPlayer.getRows();
        curRow0ScoreText.setText(String.valueOf(curRows[0].getPoint()));
        curRow1ScoreText.setText(String.valueOf(curRows[1].getPoint()));
        curRow2ScoreText.setText(String.valueOf(curRows[2].getPoint()));
        otherRow0ScoreText.setText(String.valueOf(otherRows[0].getPoint()));
        otherRow1ScoreText.setText(String.valueOf(otherRows[1].getPoint()));
        otherRow2ScoreText.setText(String.valueOf(otherRows[2].getPoint()));

        curPlayer.updateTotalPoint();
        otherPlayer.updateTotalPoint();
        curScore.setText(String.valueOf(curPlayer.getTotalPoint()));
        otherScore.setText(String.valueOf(otherPlayer.getTotalPoint()));

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

    public void putCardWeather(Player curPlayer) throws MalformedURLException {
        if (Game.getCurrentGame().getWeathers().size() == 3) return;

        try {
            Game.getCurrentGame().getCurrentPlayer().putCard(gameController.getSelectedCard());
        } catch (Exception e) {
            e.printStackTrace();
            Game.getCurrentGame().changeTurn();
        } finally {
            deSelectCard();
            endOfTurn(curPlayer);
        }
    }

    public void putCardSpecial(Player curPlayer) throws MalformedURLException {
        try {
            Game.getCurrentGame().getCurrentPlayer().putCard(gameController.getSelectedCard());
        } catch (Exception e) {
            e.printStackTrace();
            Game.getCurrentGame().changeTurn();
        } finally {
            deSelectCard();
            endOfTurn(curPlayer);
        }
    }

    public void putCard() throws MalformedURLException {
        if (isScreenLocked) return;
        Card selectedCard = gameController.getSelectedCard();
        Player curPlayer = Game.getCurrentGame().getCurrentPlayer();

        if (selectedCard == null) return;

        if (selectedCard instanceof Weather) {
            putCardWeather(curPlayer);
            return;
        }

        if (selectedCard instanceof Special) {
            putCardSpecial(curPlayer);
            return;
        }

        gameController.getSelectedImageView().setOnMouseClicked(event -> {
        });


        int rowNum = CardController.getRowNumber(gameController.getSelectedCard().getName());
        switch (rowNum) {
            case 1 -> curRow1HBox.getChildren().add(gameController.getSelectedImageView());
            case 2 -> curRow2HBox.getChildren().add(gameController.getSelectedImageView());
            default -> curRow0HBox.getChildren().add(gameController.getSelectedImageView());
        }


        try {
            Game.getCurrentGame().getCurrentPlayer().putCard(selectedCard);
        } catch (Exception e) {
//            System.out.println("[ERR] : " + e.getMessage());
            Game.getCurrentGame().changeTurn();
            e.printStackTrace();
        } finally {
            System.out.println("HERE");
            deSelectCard();
            endOfTurn(curPlayer);
        }

    }

    private void endOfTurn(Player curPlayer) throws MalformedURLException {
        if (!curPlayer.equals(Game.getCurrentGame().getCurrentPlayer())) //The usual method
            refreshScreen(Game.getCurrentGame().getOtherPlayer(), Game.getCurrentGame().getCurrentPlayer());
        else //in case an error occurred
            refreshScreen(Game.getCurrentGame().getCurrentPlayer(), Game.getCurrentGame().getOtherPlayer());


        isScreenLocked = true;
        sideSwapTimeLine.play();
    }

    public void pass() throws MalformedURLException {
        if (isScreenLocked) return;
        Player curPlayer = Game.getCurrentGame().getCurrentPlayer();
        Game.getCurrentGame().getCurrentPlayer().passRound();
        endOfTurn(curPlayer);
    }


    public void showDescription() {
        //TODO
    }

    public void executeAction() throws MalformedURLException {
        if (isScreenLocked) return;
        if (Game.getCurrentGame().getCurrentPlayer().isActionLeaderDone()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("You have already used your leader's ability!");
            alert.getDialogPane().getScene().getStylesheets().add(getClass().getResource("/CSS/AlertStyler.css").toExternalForm());
            alert.show();
            return;
        }


        Player curPlayer = Game.getCurrentGame().getCurrentPlayer();
        Game.getCurrentGame().getCurrentPlayer().playLeader();
        endOfTurn(curPlayer);
    }
}