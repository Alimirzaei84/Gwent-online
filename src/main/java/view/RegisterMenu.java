package view;

import controller.*;
import controller.menuConrollers.RegisterMenuController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Account.User;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.net.URL;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;

public class RegisterMenu extends AppMenu {
    public TextField username;
    public TextField nickname;
    public PasswordField password;
    public PasswordField passwordAgain;
    public TextField email;
    private final RegisterMenuController controller;

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

    public void generateRandomPassword() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 9; // desired length
        String generatedString = ApplicationController.getRandom().ints(leftLimit, rightLimit + 1)
                .filter(Character::isLetterOrDigit)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        password.setText(generatedString);
        passwordAgain.setText(generatedString);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("your random password is -->>  " + generatedString + "  <<--");
        alert.show();
    }

    public void goToLoginMenu() {
    }

    public void register() {
        String result;
        try {
            result = controller.register(username.getText(), password.getText(), passwordAgain.getText(), nickname.getText(), email.getText());
            System.out.println(result);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(result);
            alert.show();
            User newUser = new User(username.getText(), password.getText(), email.getText(), nickname.getText());
            User.setLoggedInUser(newUser);
            PickQuestions pickQuestions = new PickQuestions();
            pickQuestions.start(ApplicationController.getStage());
        } catch (Exception e) {
            result = e.getMessage();
            System.out.println(result);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(result);
            alert.show();
        }
    }
}