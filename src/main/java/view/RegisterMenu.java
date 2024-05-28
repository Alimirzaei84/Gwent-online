package view;

import controller.*;
import controller.menuConrollers.RegisterMenuController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class RegisterMenu extends AppMenu {
    public TextField username;
    public TextField nickname;
    public PasswordField password;
    public PasswordField passwordAgain;
    public TextField email;
    private RegisterMenuController controller;

    public RegisterMenu() {
        controller = new RegisterMenuController();
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
        Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/RegisterMenu.fxml")));
        stage.setScene(new Scene(pane));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void exitFromGame() {
        System.exit(0);
    }

    public void generateRandomPassword(MouseEvent mouseEvent) {
    }

    public void goToLoginMenu(MouseEvent mouseEvent) {
    }

    public void register(MouseEvent mouseEvent){
        String name = username.getText();
        String pass = password.getText();
        String passAgain = passwordAgain.getText();
        String nick = nickname.getText();
        String emailOfUser = email.getText();
    }
}