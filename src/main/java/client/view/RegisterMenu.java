package client.view;

import client.Main;
import client.Out;
import controller.*;
import client.menuConrollers.RegisterMenuController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class RegisterMenu extends AppMenu {
    public TextField username;
    public TextField nickname;
    public PasswordField password;
    public PasswordField passwordAgain;
    public TextField email;
    private final RegisterMenuController controller;

    public RegisterMenu() throws IOException {
        controller = new RegisterMenuController();
        removeDuplicate();
    }

    public void removeDuplicate() {
        CardController.heroes = CardController.removeDuplicates(CardController.heroes);
        CardController.units = CardController.removeDuplicates(CardController.units);
        CardController.specials = CardController.removeDuplicates(CardController.specials);
        CardController.leaders = CardController.removeDuplicates(CardController.leaders);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException, InterruptedException {
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.setTitle("AntEater");
        Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/RegisterMenu.fxml")));
        Scene scene = new Scene(pane);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/RegisterMenu.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
        CardController.load_data();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Main.connectSocket();
        client.User.getInstance().setAppMenu(this);
    }

    public void exitFromGame() {
        System.exit(0);
    }

    public void generateRandomPassword() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 9; // desired length
        String generatedString = ApplicationController.getRandom().ints(leftLimit, rightLimit + 1).filter(Character::isLetterOrDigit).limit(targetStringLength).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
        password.setText(generatedString);
        passwordAgain.setText(generatedString + ApplicationController.getRandom().nextInt(0, 10));
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("your random password is -->>  " + generatedString + "  <<--");
        Scene scene = alert.getDialogPane().getScene();
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/AlertStyler.css")).toExternalForm());
        alert.showAndWait();
    }

    public void goToLoginMenu() throws Exception {
        LoginMenu loginMenu = new LoginMenu();
        loginMenu.start((Stage) username.getScene().getWindow());
    }

    public void register() throws Exception {
        Out.sendMessage("register " + username.getText() + " " + password.getText() + " " + passwordAgain.getText() + " " + nickname.getText() + " " + email.getText());
    }

    public void handleCommand(String result) throws Exception {

        if (result.startsWith("[SUCC]")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(result);
            Scene scene = alert.getDialogPane().getScene();
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/AlertStyler.css")).toExternalForm());

            client.User.getInstance().setUsername(username.getText());
            email.getScene().getWindow().hide();
            PickQuestions pickQuestions = new PickQuestions();
            pickQuestions.start((Stage) email.getScene().getWindow());

        } else if (result.startsWith("[ERR]")) {
            System.out.println(result);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            Scene scene = alert.getDialogPane().getScene();
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/AlertStyler.css")).toExternalForm());
            alert.setContentText(result);
            alert.showAndWait();
        } else {
            throw new RuntimeException("Invalid result");
        }
    }

}