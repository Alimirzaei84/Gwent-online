package client.view;

import client.Out;
import controller.ApplicationController;
import controller.menuConrollers.LoginMenuController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import server.User;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginMenu extends AppMenu {
    public TextField username;
    public PasswordField password;
    private final LoginMenuController controller;

    public LoginMenu() {
        controller = new LoginMenuController();
    }

    @Override
    public void start(Stage stage) throws Exception {
        Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/LoginMenu.fxml")));
        Scene scene = new Scene(pane);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/RegisterMenu.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        client.User.getInstance().setAppMenu(this);
    }

    public void goToRegisterMenu() throws IOException, InterruptedException {
        Out.sendMessage("back");
        RegisterMenu registerMenu = new RegisterMenu();
        username.getScene().getWindow().hide();
        registerMenu.start((Stage) username.getScene().getWindow());
    }

    public void login(MouseEvent mouseEvent) throws IOException {
        Out.sendMessage("login " + username.getText() + " " + password.getText());
    }

    public void forgetPassword(MouseEvent mouseEvent) throws Exception {
        Out.sendMessage("forgetPassword " + username.getText());
    }


    @Override
    public void initialize() {

    }

    @Override
    public void handleCommand(String command) throws Exception {
        String[] parts = command.split(" ", 2);
        String commandType = parts[0];
        String result = parts[1];
        System.out.println(command);
        switch (commandType) {
            case "login" -> {
                handleLoginResult(result);
            }
            case "forgetPassword" -> {
                handleForgetPasswordResult(result);
            }
            default -> {
                throw new RuntimeException("Invalid command type");
            }
        }
    }

    private void handleForgetPasswordResult(String result) throws Exception {

        if (result.startsWith("[ERR]")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("no user found with this username " + "\"" + username.getText() + "\"");
            Scene scene = alert.getDialogPane().getScene();
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/AlertStyler.css")).toExternalForm());
            alert.showAndWait();
        } else if (result.startsWith("[SUCC]")) {
            ForgetPassword forgetPassword = new ForgetPassword();
            username.getScene().getWindow().hide();
            forgetPassword.start((Stage) username.getScene().getWindow());
        } else throw new RuntimeException("Invalid forgetPassword result");

    }

    private void handleLoginResult(String result) throws Exception {

        if (result.startsWith("[INFO]")) {
            System.out.println(result);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            Scene scene = alert.getDialogPane().getScene();
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/AlertStyler.css")).toExternalForm());
            alert.setContentText(result);
            alert.showAndWait();

            MainMenu mainMenu = new MainMenu();
            username.getScene().getWindow().hide();
            mainMenu.start((Stage) username.getScene().getWindow());
        } else if (result.startsWith("[ERR]")) {
            System.out.println(result);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login failed!");
            Scene scene = alert.getDialogPane().getScene();
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/AlertStyler.css")).toExternalForm());
            alert.setContentText(result);
            alert.showAndWait();
        } else throw new RuntimeException("Invalid login result");

    }

}
