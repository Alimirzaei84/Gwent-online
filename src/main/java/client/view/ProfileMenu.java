package client.view;

import client.Out;
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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ProfileMenu extends AppMenu {

    @FXML
    public TextField verificationText;
    @FXML
    public Label usernameLabel;
    @FXML
    public Label nicknameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label rankLabel;
    @FXML
    public Label highestScoreLabel;
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
        assert url != null;
        AnchorPane root = FXMLLoader.load(url);
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/CSS/ProfileMenu.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
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
        try {
            Out.sendMessage("change password " + newPassword + " " + oldPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void verify(MouseEvent mouseEvent) throws IOException {
        String verificationCode = verificationText.getText();
        Out.sendMessage("verify " + verificationCode);
    }

    public void changeNickName(MouseEvent mouseEvent) throws IOException {
        String nickname = nicknameField.getText();
        Out.sendMessage("change nickname " + nickname);
        Out.sendMessage("give nickname");
    }

    public void changeUsername(MouseEvent mouseEvent) throws IOException {
        String username = usernameField.getText();
        Out.sendMessage("change username " + username);
        Out.sendMessage("give username");
    }

    public void changeEmail(MouseEvent mouseEvent) throws IOException {
        String email = emailField.getText();
        Out.sendMessage("change email " + email);
        Out.sendMessage("give email");
    }

    public void showGameHistories(MouseEvent mouseEvent) {
        try {
            GameHistoryScreen gameHistory = new GameHistoryScreen();
            gameHistory.start((Stage) usernameLabel.getScene().getWindow());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void back(MouseEvent mouseEvent) {
        MainMenu mainMenu = new MainMenu();
        try {
            client.User.getInstance().setAppMenu(mainMenu);
            mainMenu.start((Stage) usernameLabel.getScene().getWindow());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        client.User.getInstance().setAppMenu(this);
        try {
            Out.sendMessage("give username");
            Out.sendMessage("give email");
            Out.sendMessage("give nickname");
            Out.sendMessage("give gamesplayed");
            Out.sendMessage("give losses");
            Out.sendMessage("give wins");
            Out.sendMessage("give tie");
            Out.sendMessage("give rank");
            Out.sendMessage("give maxscore");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void handleCommand(String command) throws Exception {
        System.out.println("command:" + command);
        if (command.startsWith("[USERNAME]:")) {
            String username = command.substring("[USERNAME]:".length());
            setUsernameLabel(username);
        } else if (command.startsWith("[EMAIL]:")) {
            String email = command.substring("[EMAIL]:".length());
            setEmailLabel(email);
        } else if (command.startsWith("[NICKNAME]:")) {
            String nickname = command.substring("[NICKNAME]:".length());
            setNicknameLabel(nickname);
        } else if (command.startsWith("[GAMESPLAYED]:")) {
            String gamesPlayed = command.substring("[GAMESPLAYED]:".length());
            setGamesPlayedLabel(gamesPlayed);
        } else if (command.startsWith("[LOSSES]:")) {
            String losses = command.substring("[LOSSES]:".length());
            setLossesLabel(losses);
        } else if (command.startsWith("[WINS]:")) {
            String wins = command.substring("[WINS]:".length());
            setWinsLabel(wins);
        } else if (command.startsWith("[TIE]:")) {
            String tie = command.substring("[TIE]:".length());
            setTieLabel(tie);
        } else if (command.startsWith("[RANK]:")) {
            String rank = command.substring("[RANK]:".length());
            setRankLabel(rank);
        } else if (command.startsWith("[MAXSCORE]:")) {
            String maxScore = command.substring("[MAXSCORE]:".length());
            setHighestScoreLabel(maxScore);
        } else if (command.startsWith("[SUCC]")) {
            System.out.println(command);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful");
            alert.setHeaderText("Successful");
            alert.setContentText(command.substring("[SUCC]:".length()));
            Scene scene = alert.getDialogPane().getScene();
            scene.getStylesheets().add(getClass().getResource("/CSS/AlertStyler.css").toExternalForm());
            alert.showAndWait();
        } else if (command.startsWith("[ERR]")) {
            System.out.println(command);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText(command.substring("[ERR]:".length()));
            Scene scene = alert.getDialogPane().getScene();
            scene.getStylesheets().add(getClass().getResource("/CSS/AlertStyler.css").toExternalForm());
            alert.showAndWait();
        } else if (command.startsWith("[PLAYGAME]")) {
            try {
                GameLauncher gameLauncher = new GameLauncher();
                gameLauncher.start((Stage) usernameLabel.getScene().getWindow());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
