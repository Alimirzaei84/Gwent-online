package view;

import controller.menuConrollers.GameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Account.Player;
import model.game.Game;
import view.AppMenu;
import view.MainMenu;

import java.net.URL;
import java.util.ResourceBundle;

public class GameLauncher extends AppMenu {
    private GameController gameController;
    private AnchorPane pane;
    private HBox inHandPlayer1 = new HBox();
    private HBox inHandPlayer2 = new HBox();
    private VBox body = new VBox();

    public GameLauncher() {
        gameController = new GameController(Game.getCurrentGame());
    }

    @Override
    public void start(Stage stage) throws Exception {
        URL url = MainMenu.class.getResource("/FXML/GameLauncher.fxml");
        assert url != null;
        AnchorPane root = FXMLLoader.load(url);

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

        pane.getChildren().add(body);
        body.getChildren().add(inHandPlayer1);
//        inHandPlayer1.setLayoutY();
    }
}
