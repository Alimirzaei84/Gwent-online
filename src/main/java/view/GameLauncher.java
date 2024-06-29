package view;

import controller.CardController;
import controller.menuConrollers.GameController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import javafx.stage.Stage;
import model.Account.Player;
import model.game.Game;
import model.role.Card;
import view.AppMenu;
import view.MainMenu;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class GameLauncher extends AppMenu {
    private GameController gameController;
    private AnchorPane pane = new AnchorPane();
    @FXML
    private HBox inHandCurHbox;
    @FXML
    private VBox curLeaderVbox;
    @FXML
    private Label otherUsernameLabel;
    public Label curUsernameLabel;
    public Label curFactionLabel;
    public Label otherFactionLabel;
    public HBox otherDiamondHBox;
    public HBox curDiamondHBox;
    private VBox otherLeaderVbox;
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
//                    URL imageUrl = getClass().getResource(imagePath);
//                    if (imageUrl == null) {
//                        System.err.println("Image not found: " + imagePath);
//                        continue;
//                    }

                    Image image = new Image(new File(imagePath).toURI().toURL().toString());
                    if (image == null) {
                        System.out.println("HELLO");
                    }
                    ImageView imageView = new ImageView(new Image(new File(imagePath).toURI().toURL().toString()));
                    imageView.setOnMouseClicked(event -> selectCard(card));
                    imageView.setOnDragExited(event -> System.out.println("swipe down"));
                    imageView.preserveRatioProperty();
                    imageView.setFitWidth(62.5);
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
        otherUsernameLabel.setText(otherPlayer.getUser().getUsername());
        curFactionLabel.setText(curPlayer.getUser().getUsername());
        otherFactionLabel.setText(otherPlayer.getUser().getUsername());
        for (Node node : curDiamondHBox.getChildren()) {
            if (node instanceof Polygon) {
                Polygon polygon = (Polygon) node;
                polygon.setFill(Color.rgb(89,114,115));
            }
        }

        for (Node node : otherDiamondHBox.getChildren()){
            Polygon polygon = (Polygon) node;
            polygon.setFill(Color.rgb(89,114,115));
        }

    }

    private void setLeadersOnScreen(String leaderPath, VBox leaderVbox) throws MalformedURLException {
        ImageView leaderImageView = new ImageView(new Image(new File(leaderPath).toURI().toURL().toString()));
        leaderImageView.setOnDragExited(event -> System.out.println("swipe down"));
        leaderImageView.preserveRatioProperty();
        leaderImageView.setFitWidth(77);
        leaderImageView.setFitHeight(99);
        leaderVbox.getChildren().add(leaderImageView);
    }

    public void selectCard(Card card) {
        // Implement card selection logic
    }
}
