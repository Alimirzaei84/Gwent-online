package view;

import controller.CardController;
import controller.menuConrollers.GameController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import model.Account.Player;
import model.game.Game;
import model.game.Row;
import model.role.Card;
import view.AppMenu;
import view.MainMenu;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
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


    private HBox inHandPlayer1 = new HBox();
    private HBox inHandPlayer2 = new HBox();

    public GameLauncher() {
        gameController = new GameController(Game.getCurrentGame());
    }

    @Override
    public void start(Stage stage) throws Exception {
        URL url = ProfileMenu.class.getResource("/FXML/GameLauncher.fxml");
        assert url != null : "FXML file not found";
        AnchorPane root = FXMLLoader.load(url);
        pane = root;

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/CSS/GameLauncher.css").toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        gameController.initializeGame();
        inHandCurHbox.getChildren().add(inHandPlayer1);

        Platform.runLater(() -> {
            for (Card card : Game.getCurrentGame().getPlayer1().getInHand()) {
                try {
                    String imagePath = CardController.imagePath.getOrDefault(card.getName(), "/assets/sm/monsters_arachas_behemoth.jpg");
                    ImageView imageView = new ImageView(new Image(new File(imagePath).toURI().toURL().toString()));
                    imageView.setOnMouseClicked(event -> selectCard(card , imageView));
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
        String curLeaderPath = CardController.imagePath.get(curPlayer.getLeader().getName());
        String otherLeaderPath = CardController.imagePath.get(otherPlayer.getLeader().getName());
        try {
            setLeadersOnScreen(curLeaderPath, curLeaderVbox);
            setLeadersOnScreen(otherLeaderPath, otherLeaderVbox);
            //TODO : SET MOUSE CLICKED IF NEEDED!
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

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

    private void refreshScores(Player curPlayer, Player otherPlayer) {
        Row[] curRows = curPlayer.getRows();
        Row[] otherRows = otherPlayer.getRows();
        curRow0ScoreText.setText(String.valueOf(curRows[0].getPoint()));
        curRow1ScoreText.setText(String.valueOf(curRows[1].getPoint()));
        curRow2ScoreText.setText(String.valueOf(curRows[2].getPoint()));
        otherRow0ScoreText.setText(String.valueOf(otherRows[0].getPoint()));
        otherRow1ScoreText.setText(String.valueOf(otherRows[0].getPoint()));
        otherRow2ScoreText.setText(String.valueOf(otherRows[0].getPoint()));

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
        ImageView leaderImageView = new ImageView(new Image(new File(leaderPath).toURI().toURL().toString()));
        leaderImageView.setOnDragExited(event -> System.out.println("swipe down"));
        leaderImageView.preserveRatioProperty();
        leaderImageView.setFitWidth(77);
        leaderImageView.setFitHeight(99);
        leaderVbox.getChildren().add(leaderImageView);
    }

    public void selectCard(Card card , ImageView imageView) {
        if (gameController.getSelectedCard() != null){
            ImageView oldImageView = gameController.getSelectedImageView();
            oldImageView.setLayoutY(oldImageView.getLayoutY() - 20);
            gameController.setSelectedImageView(null);
        }

        gameController.setSelectedCard(card);
        imageView.setLayoutY(imageView.getLayoutY() + 20);
        gameController.setSelectedImageView(imageView);
    }

    public void pass() {
        //TODO
    }

    public void showDescription() {
        //TODO
    }

    public void executeAction(){
        //TODO
    }
}
