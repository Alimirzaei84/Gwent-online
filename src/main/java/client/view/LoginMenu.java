package client.view;

import client.Out;
import client.User;
import client.menuConrollers.LoginMenuController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.*;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class LoginMenu extends AppMenu {
    public TextField username;
    public PasswordField password;
    public String correctCode;
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
        User.getInstance().setAppMenu(this);
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
    public void handleCommand(String command) throws Exception {
        String[] parts = command.split(" ", 2);
        String commandType = parts[0];
        String result = parts[1];
        switch (commandType) {
            case "login" -> {
                handleLoginResult(result);
            }
            case "forgetPassword" -> {
                handleForgetPasswordResult(result);
            }
            default -> {
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
        } else if (result.startsWith("dialog")) {
            verify(result.split(" ")[1]);
        } else throw new RuntimeException("Invalid login result");

    }

    private void verify(String code) throws Exception {
        String username = null;
        boolean validUsername = false;
        Alert alert = new Alert(Alert.AlertType.WARNING);
        boolean alertShown = false; // Flag to track if the alert has been shown

        Scene scene = alert.getDialogPane().getScene();
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/AlertStyler.css")).toExternalForm());

        while (!validUsername) {
            if (alertShown) { // Check if the alert has been shown
                if (!alert.isShowing()) { // Check if the alert is closed
                    alertShown = false; // Reset the flag
                }
                continue; // Skip showing the dialog while the alert is still showing
            }

            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Enter the code  ");
            dialog.setHeaderText("Please enter the username of the target player:");
            dialog.getDialogPane().getScene().getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/AlertStyler.css")).toExternalForm());

            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/AlertStyler.css")).toExternalForm());

            ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

            TextField usernameField = new TextField();
            usernameField.setPromptText("2FA code");

            VBox dialogVBox = new VBox(10);
            dialogVBox.setPadding(new Insets(20));
            Label label = new Label("Code:");
            label.setTextFill(Color.WHITE);
            dialogVBox.getChildren().addAll(label, usernameField);

            dialog.getDialogPane().setContent(dialogVBox);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == okButtonType) {
                    return usernameField.getText();
                }
                return null;
            });


            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                username = result.get();
                String message;

                System.out.println(result.get() + " but " + code);
                if (result.get().equals(code)) {
//                if(true){
                    System.out.println("[SUCC]:");
                    validUsername = true;
                    Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                    Scene scene1 = alert1.getDialogPane().getScene();
                    scene1.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/AlertStyler.css")).toExternalForm());
                    alert1.setContentText("[SUCC]");
                    alert1.showAndWait();

                    MainMenu mainMenu = new MainMenu();
                    password.getScene().getWindow().hide();
                    mainMenu.start((Stage) password.getScene().getWindow());
                } else {
                    System.out.println("[ERR]:");
                    alert.setTitle("Error");


                    scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/AlertStyler.css")).toExternalForm());
                    alert.setContentText("[ERR:]");
                    alert.showAndWait(); // Show the alert and wait for user acknowledgment
                    alertShown = true; // Set the flag indicating that the alert has been shown
                }

            } else {
                break;
            }
        }

        if (validUsername) {
            System.out.println("Valid username entered: " + username);
        }
    }

}
