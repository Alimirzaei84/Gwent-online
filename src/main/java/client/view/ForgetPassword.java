package client.view;

import client.Out;
import controller.ApplicationController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
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
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/RegisterMenu.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        client.User.getInstance().setAppMenu(this);
    }

    public void confirm() throws Exception {
        Alert error = new Alert(Alert.AlertType.ERROR);

        if (newPassword.getText().length() < 8) {
            error.setContentText("password must be at least 8 characters");
            System.out.println("[ERR]: password must be at least 8 characters");

            Scene scene = error.getDialogPane().getScene();
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/AlertStyler.css")).toExternalForm());
            error.showAndWait();
            return;
        }

        if (!newPassword.getText().equals(newPasswordAgain.getText())) {
            error.setContentText("passwords does not mathe");
            System.out.println("[ERR]: passwords does not mathe");

            Scene scene = error.getDialogPane().getScene();
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/AlertStyler.css")).toExternalForm());
            error.showAndWait();
            return;
        }

        Out.sendMessage("changePassword " + food.getText() + " " + color.getText() + " " + month.getText() + " " + newPassword.getText());
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
        generatedString = generatedString + ApplicationController.getRandom().nextInt(0, 10);
        newPassword.setText(generatedString);
        newPasswordAgain.setText(generatedString);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("your random password is -->>  " + generatedString + "  <<--");

        Scene scene = alert.getDialogPane().getScene();
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/AlertStyler.css")).toExternalForm());
        alert.showAndWait();
    }


    @Override
    public void handleCommand(String command) throws Exception {
        Alert error = new Alert(Alert.AlertType.ERROR);
        Alert succ = new Alert(Alert.AlertType.INFORMATION);

          if(command.startsWith("[ERR]")){
              error.setContentText("your answers does not correct!");
              System.out.println(command);

              Scene scene = error.getDialogPane().getScene();
              scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/AlertStyler.css")).toExternalForm());
              error.showAndWait();

          } else if (command.startsWith("[SUCC]")) {
              System.out.println(command);
              succ.setContentText("password changed successfully");
              Scene scene = succ.getDialogPane().getScene();
              scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/AlertStyler.css")).toExternalForm());
              succ.showAndWait();

              LoginMenu loginMenu = new LoginMenu();
              food.getScene().getWindow().hide();
              loginMenu.start((Stage) food.getScene().getWindow());
          }


    }
}
