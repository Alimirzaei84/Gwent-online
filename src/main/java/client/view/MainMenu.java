package client.view;

import client.Out;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;


public class MainMenu extends AppMenu {

    @FXML
    public Button profileMenuButton;

    @Override
    public void start(Stage stage) throws Exception {
        URL url = MainMenu.class.getResource("/FXML/MainMenu.fxml");
        assert url != null;
        AnchorPane root = FXMLLoader.load(url);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/MainMenu.css")).toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        client.User.getInstance().setAppMenu(this);
        System.out.println("USERNAME ---->" + client.User.getInstance().getUsername());
    }

    @FXML
    public void playGame(MouseEvent mouseEvent) throws Exception {
        PreGameMenu preGameMenu = new PreGameMenu<>();
        this.profileMenuButton.getScene().getWindow().hide();
        preGameMenu.start((Stage) this.profileMenuButton.getScene().getWindow());
    }


    @FXML
    public void logout(MouseEvent mouseEvent) {
        LoginMenu loginMenu = new LoginMenu();
        try {
            Out.sendMessage("logout");
            loginMenu.start((Stage) profileMenuButton.getScene().getWindow());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void profileMenu(MouseEvent mouseEvent) {
        ProfileMenu profileMenu = new ProfileMenu();
        try {
            profileMenu.start((Stage) profileMenuButton.getScene().getWindow());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void friendsMenu() {
        FriendsMenu friendsMenu = new FriendsMenu();
        try {
            friendsMenu.start((Stage) profileMenuButton.getScene().getWindow());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleCommand(String command) throws Exception {
        if (command.startsWith("[PLAYGAME]")) {
            try {
                GameLauncher gameLauncher = new GameLauncher();
                gameLauncher.start((Stage) profileMenuButton.getScene().getWindow());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
