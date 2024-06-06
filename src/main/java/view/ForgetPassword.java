package view;

import controller.ApplicationController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Account.User;

import java.net.URL;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;

public class ForgetPassword extends AppMenu {

    public TextField color;
    public TextField month;
    public TextField food;
    public PasswordField newPassword;
    public PasswordField newPasswordAgain;

    @Override
    public void start(Stage stage) throws Exception {
        Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/ForgotPassword.fxml")));
        Scene scene = new Scene(pane);
        scene.getStylesheets().add(getClass().getResource("/CSS/RegisterMenu.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void confirm() throws Exception {
        User user = ApplicationController.getForgetPasswordUser();
        if (user == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("no user found");

            Scene scene = alert.getDialogPane().getScene();
            scene.getStylesheets().add(getClass().getResource("/CSS/AlertStyler.css").toExternalForm());
            alert.showAndWait();
        }
        assert user != null;
        HashMap<String, String> answers = user.getAnswers();
        Alert error = new Alert(Alert.AlertType.ERROR);
        Alert succ = new Alert(Alert.AlertType.INFORMATION);

        if (!answers.getOrDefault("your favorite color?", "DASH!").equals(color.getText())
                && !answers.getOrDefault("your favorite food?", "^DASH^").equals(food.getText())
                && !answers.getOrDefault("your favorite month?", "DASH").equals(month.getText())
        ) {
            error.setContentText("your answers does not correct!");
            System.out.println("[ERR]: your answers does not correct!");

            Scene scene = error.getDialogPane().getScene();
            scene.getStylesheets().add(getClass().getResource("/CSS/AlertStyler.css").toExternalForm());
            error.showAndWait();
            return;
        }

        if (newPassword.getText().length() < 8) {
            error.setContentText("password must be at least 8 characters");
            System.out.println("[ERR]: password must be at least 8 characters");

            Scene scene = error.getDialogPane().getScene();
            scene.getStylesheets().add(getClass().getResource("/CSS/AlertStyler.css").toExternalForm());
            error.showAndWait();
            return;
        }

        if (!newPassword.getText().equals(newPasswordAgain.getText())) {
            error.setContentText("passwords does not mathe");
            System.out.println("[ERR]: passwords does not mathe");

            Scene scene = error.getDialogPane().getScene();
            scene.getStylesheets().add(getClass().getResource("/CSS/AlertStyler.css").toExternalForm());
            error.showAndWait();
            return;
        }

        user.setPassword(newPassword.getText());
        System.out.println("[SUCC]: password changed successfully");
        succ.setContentText("password changed successfully");

        Scene scene = succ.getDialogPane().getScene();
        scene.getStylesheets().add(getClass().getResource("/CSS/AlertStyler.css").toExternalForm());
        succ.showAndWait();
        ApplicationController.setForgetPasswordUser(null);
        LoginMenu loginMenu = new LoginMenu();
        loginMenu.start(ApplicationController.getStage());
    }

    public void generateRandomPassword(MouseEvent mouseEvent) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 9; // desired length
        String generatedString = ApplicationController.getRandom().ints(leftLimit, rightLimit + 1)
                .filter(Character::isLetterOrDigit)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        newPassword.setText(generatedString);
        newPasswordAgain.setText(generatedString);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("your random password is -->>  " + generatedString + "  <<--");

        Scene scene = alert.getDialogPane().getScene();
        scene.getStylesheets().add(getClass().getResource("/CSS/AlertStyler.css").toExternalForm());
        alert.showAndWait();
    }
}
