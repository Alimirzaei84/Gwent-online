package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Account.Player;
import view.AppMenu;
import view.MainMenu;

import java.net.URL;
import java.util.ResourceBundle;

public class GameLauncher extends AppMenu {

    private Player player;
    private Stage stage;

    public GameLauncher(Player player) {
        this.player = player;
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

    }
}
