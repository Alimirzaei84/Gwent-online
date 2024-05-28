package view;

import controller.*;
import controller.menuConrollers.LoginMenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginMenu extends Application implements Initializable {
    private LoginMenuController controller;

    public LoginMenu() {
        controller = new LoginMenuController();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.setTitle("AntEater");
        ApplicationController.setStage(stage);
        Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/LoginMenu.fxml")));
        stage.setScene(new Scene(pane));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}