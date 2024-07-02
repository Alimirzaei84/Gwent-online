package view;

import client.Main;
import client.Out;
import controller.*;
import controller.menuConrollers.RegisterMenuController;
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
    private Wrapper wrapper;
    private int id;
    public TextField nickname;
    public PasswordField password;
    public PasswordField passwordAgain;
    public TextField email;
    private final RegisterMenuController controller;

    public RegisterMenu() throws IOException {
        controller = new RegisterMenuController();
        CardController.load_data();
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
//        int random = ApplicationController.getRandom().nextInt(0, 1000);
//        ApplicationController.addStage(random, stage);
//        for (int i = 0; i < 5; i++) {
//            ApplicationController.addStage(10, new Stage());
//        }
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.setTitle("AntEater");
        Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/RegisterMenu.fxml")));
        Scene scene = new Scene(pane);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/RegisterMenu.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
//        System.out.println(stage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Main.connectSocket();
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
        passwordAgain.setText(generatedString);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("your random password is -->>  " + generatedString + "  <<--");
        Scene scene = alert.getDialogPane().getScene();
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/AlertStyler.css")).toExternalForm());
        alert.showAndWait();
    }

    public void goToLoginMenu() throws Exception {
        LoginMenu loginMenu = new LoginMenu();
        loginMenu.start(ApplicationController.getStage());
    }

    public void register() {
        String result;
        try {
            result = controller.register(username.getText(), password.getText(), passwordAgain.getText(), nickname.getText(), email.getText());

            /*
             *  @param attention to this code
             */

            System.out.println(result);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText(result);

            Scene scene = alert.getDialogPane().getScene();
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/AlertStyler.css")).toExternalForm());
//            User newUser = new User(username.getText(), password.getText(), email.getText(), nickname.getText());


            Out.sendMessage("register " + username.getText() + " " + password.getText() + " " + nickname.getText() + " " + email.getText());
//            User.setLoggedInUser(newUser);
           email.getScene().getWindow().hide();
//            ApplicationController.closeStage(id).close();
            PickQuestions pickQuestions = new PickQuestions();
            pickQuestions.start((Stage) email.getScene().getWindow());
        } catch (Exception e) {
            result = e.getMessage();
            System.out.println(result);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            Scene scene = alert.getDialogPane().getScene();
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/AlertStyler.css")).toExternalForm());
            alert.setContentText(result);
            alert.showAndWait();
        }
    }

    @Override
    public void initialize() {

    }
}