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

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
