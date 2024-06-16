package view;

import controller.ApplicationController;
import controller.menuConrollers.MainMenuController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Account.User;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class MainMenu extends AppMenu {

    private TextField usernameField;

    @Override
    public void start(Stage stage) throws Exception {
        URL url = MainMenu.class.getResource("/FXML/MainMenu.fxml");
        assert url != null;
        AnchorPane root = FXMLLoader.load(url);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/CSS/MainMenu.css").toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    @FXML
    public void playGame(MouseEvent mouseEvent) {
        String username = null;
        boolean validUsername = false;
        Alert alert = new Alert(Alert.AlertType.WARNING);
        boolean alertShown = false; // Flag to track if the alert has been shown

        Scene scene = alert.getDialogPane().getScene();
        scene.getStylesheets().add(getClass().getResource("/CSS/AlertStyler.css").toExternalForm());

        while (!validUsername) {
            if (alertShown) { // Check if the alert has been shown
                if (!alert.isShowing()) { // Check if the alert is closed
                    alertShown = false; // Reset the flag
                }
                continue; // Skip showing the dialog while the alert is still showing
            }

            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Enter Username");
            dialog.setHeaderText("Please enter the username of the target player:");
            dialog.getDialogPane().getScene().getStylesheets().add(getClass().getResource("/CSS/AlertStyler.css").toExternalForm());

            scene.getStylesheets().add(getClass().getResource("/CSS/AlertStyler.css").toExternalForm());

            ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

            TextField usernameField = new TextField();
            usernameField.setPromptText("Username");

            VBox dialogVBox = new VBox(10);
            dialogVBox.setPadding(new Insets(20));
            Label label = new Label("Username:");
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
                try {
                    message = MainMenuController.opponentValidation(username);
                    System.out.println("[SUCC]:" + message);
                    validUsername = true;
                    PreGameMenu preGameMenu = new PreGameMenu();
                    preGameMenu.start(ApplicationController.getStage());
                } catch (Exception e) {
                    message = e.getMessage();
                    System.out.println("[ERR]:" + message);
                    alert.setTitle("Error");

                    scene.getStylesheets().add(getClass().getResource("/CSS/AlertStyler.css").toExternalForm());
                    alert.setContentText(message);
                    alert.showAndWait();
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


    @FXML
    public void logout(MouseEvent mouseEvent) {
        User.setLoggedInUser(null);
        LoginMenu loginMenu = new LoginMenu();
        try {
            loginMenu.start(ApplicationController.getStage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void profileMenu(MouseEvent mouseEvent) {
        ProfileMenu profileMenu = new ProfileMenu();
        try {
            profileMenu.start(ApplicationController.getStage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
