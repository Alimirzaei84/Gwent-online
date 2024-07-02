package view;

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
        scene.getStylesheets().add(getClass().getResource("/CSS/RegisterMenu.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void goToRegisterMenu(MouseEvent mouseEvent) throws IOException {
        RegisterMenu registerMenu = new RegisterMenu();
        registerMenu.start(ApplicationController.getStage());
    }

    public void login(MouseEvent mouseEvent) {
        String result;
        try {
            result = controller.login(username.getText(), password.getText());
            System.out.println(result);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            Scene scene = alert.getDialogPane().getScene();
            scene.getStylesheets().add(getClass().getResource("/CSS/AlertStyler.css").toExternalForm());
            alert.setContentText(result);
            alert.showAndWait();
            MainMenu mainMenu = new MainMenu();
            mainMenu.start(ApplicationController.getStage());

        } catch (Exception e) {
            result = e.getMessage();
            System.out.println(result);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login failed!");

            Scene scene = alert.getDialogPane().getScene();
            scene.getStylesheets().add(getClass().getResource("/CSS/AlertStyler.css").toExternalForm());
            alert.setContentText(result);
            alert.showAndWait();
        }
    }

    public void forgetPassword(MouseEvent mouseEvent) throws Exception {
        User user = User.getUserByUsername(username.getText());
        if (user == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("no user found with this username " + "\"" + username.getText() + "\"");

            Scene scene = alert.getDialogPane().getScene();
            scene.getStylesheets().add(getClass().getResource("/CSS/AlertStyler.css").toExternalForm());
            alert.showAndWait();
            return;
        }

        ApplicationController.setForgetPasswordUser(user);
        ForgetPassword forgetPassword = new ForgetPassword();
        forgetPassword.start(ApplicationController.getStage());
    }

    @Override
    public void initialize() {

    }
}
