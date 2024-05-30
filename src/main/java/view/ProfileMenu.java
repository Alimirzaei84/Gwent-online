package view;

import controller.menuConrollers.ProfileMenuController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Account.User;

import java.net.URL;

public class ProfileMenu {

    @FXML
    private Label usernameLabel;
    @FXML
    private Label nicknameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label rankLabel;
    @FXML
    private Label highestScoreLabel;
    @FXML
    private Label gamesPlayedLabel;
    @FXML
    private Label tieLabel;
    @FXML
    private Label winsLabel;
    @FXML
    private Label lossesLabel;

    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField nicknameField;
    @FXML
    private PasswordField newPasswordField;
    @FXML
    private PasswordField oldPasswordField;


    public void start(Stage stage) throws Exception {
        URL url = ProfileMenu.class.getResource("/FXML/ProfileMenu.fxml");
        AnchorPane root = FXMLLoader.load(url);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void initialize() {
        setUsernameLabel(User.getLoggedInUser().getName());
        setEmailLabel(User.getLoggedInUser().getEmail());
        setNicknameLabel(User.getLoggedInUser().getNickname());
        setGamesPlayedLabel(String.valueOf(User.getLoggedInUser().getGamesPlayed()));
        setLossesLabel(String.valueOf(User.getLoggedInUser().getLosses()));
        setWinsLabel(String.valueOf(User.getLoggedInUser().getWins()));
        setTieLabel(String.valueOf(User.getLoggedInUser().getTies()));
        setRankLabel(String.valueOf(User.getLoggedInUser().getRank()));
        setHighestScoreLabel(String.valueOf(User.getLoggedInUser().getHighestScore()));
    }

    public void setRankLabel(String rank) {
        rankLabel.setText(rank);
    }

    public void setHighestScoreLabel(String highestScore) {
        highestScoreLabel.setText(highestScore);
    }

    public void setGamesPlayedLabel(String gamesPlayed) {
        gamesPlayedLabel.setText(gamesPlayed);
    }

    public void setTieLabel(String tie) {
        tieLabel.setText(tie);
    }

    public void setWinsLabel(String wins) {
        winsLabel.setText(wins);
    }

    public void setLossesLabel(String losses) {
        lossesLabel.setText(losses);
    }

    public void setUsernameLabel(String username) {
        usernameLabel.setText(username);
    }

    public void setNicknameLabel(String nickname) {
        nicknameLabel.setText(nickname);
    }

    public void setEmailLabel(String email) {
        emailLabel.setText(email);
    }

    public void changePassword(MouseEvent mouseEvent) {
        String newPassword = newPasswordField.getText();
        String oldPassword = oldPasswordField.getText();
        String res;

        try {
            res = ProfileMenuController.changePassword(newPassword, oldPassword);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText(res);
            System.out.println("[SUCC] : " + res);
            alert.show();
        } catch (Exception e) {
            res = e.getMessage();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText(res);
            System.out.println("[ERR] : " + res);
            alert.show();
        }
    }

    public void changeNickName(MouseEvent mouseEvent) {
        String nickname = nicknameField.getText();

        if (nickname.equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Nickname Error");
            alert.setHeaderText("Problem in nickname");
            alert.setContentText("Nickname field is empty!");
            System.out.println("[ERR] : Nickname field is empty!");
            alert.show();

        } else if (nickname.equals(User.getLoggedInUser().getNickname())) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Nickname Error");
            alert.setHeaderText("Problem in nickname");
            alert.setContentText("Enter a new nickname!");
            System.out.println("[ERR] : Enter a new nickname!");
            alert.show();
        } else {
            ProfileMenuController.changeNickname(nickname);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Nickname changed successfully!");
            alert.show();
            System.out.println("[SUCC] : nickname changed successfully!");
        }
    }

    public void changeUsername(MouseEvent mouseEvent) {
        String username = usernameField.getText();
        String res;

        try {
            res = ProfileMenuController.changeUsername(username);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText(res);
            System.out.println("[SUCC] : " + res);
            alert.show();
        } catch (Exception e) {
            res = e.getMessage();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText(res);
            System.out.println("[ERR] : " + res);
            alert.show();
        }
    }

    public void changeEmail(MouseEvent mouseEvent) {
        String email = emailField.getText();
        String res;

        try {
            res = ProfileMenuController.changeEmail(email);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText(res);
            System.out.println("[SUCC] : " + res);
            alert.show();
        } catch (Exception e) {
            res = e.getMessage();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setContentText(res);
            System.out.println("[ERR] : " + res);
            alert.show();
        }
    }

    public void showGameHistories(MouseEvent mouseEvent){
        //TODO
    }
}
