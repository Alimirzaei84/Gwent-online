package client.view;

import client.Out;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import server.Enum.Regexes;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class EndOfGameScreen extends AppMenu {

    public HBox dataHBox;
    public Label winnerLabel;
    public AnchorPane pane;

    @Override
    public void start(Stage stage) throws Exception {
        URL url = ProfileMenu.class.getResource("/FXML/EndOfGameScreen.fxml");
        assert url != null : "FXML file not found";
        AnchorPane root = FXMLLoader.load(url);
        pane = root;

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/EndOfGame.css")).toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        client.User.getInstance().setAppMenu(this);
        try {
            Out.sendMessage("get end of game data");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setSizes() {


    }

    public void backToMainMenu() {
        try {
            MainMenu mainMenu = new MainMenu();
            mainMenu.start((Stage) dataHBox.getScene().getWindow());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleCommand(String command) {
        if (Regexes.SEND_DATA.matches(command)) {
            String winnerUsername = Regexes.SEND_DATA.getGroup(command, "winnerUsername");
            String loserUsername = Regexes.SEND_DATA.getGroup(command, "loserUsername");
            int winnerTotalScore = Integer.parseInt(Regexes.SEND_DATA.getGroup(command, "winnerTotal"));
            String rawWinnerScores = Regexes.SEND_DATA.getGroup(command, "winnerScores");
            String[] rawWinnerScoresArr = rawWinnerScores.split(" ");
            int[] winnerScores = new int[rawWinnerScoresArr.length];

            for (int i = 0; i < rawWinnerScoresArr.length; i++) {
                winnerScores[i] = Integer.parseInt(rawWinnerScoresArr[i]);
            }

            String rawLoserScores = Regexes.SEND_DATA.getGroup(command, "looserScores");
            String[] rawLoserScoresArr = rawLoserScores.split(" ");
            int[] loserScores = new int[rawWinnerScoresArr.length];

            for (int i = 0; i < rawWinnerScoresArr.length; i++) {
                loserScores[i] = Integer.parseInt(rawLoserScoresArr[i]);
            }


            this.winnerLabel.setText("WINNER " + winnerUsername + " : " + winnerTotalScore);
            this.winnerLabel.setAlignment(Pos.CENTER);
            this.winnerLabel.setTextAlignment(TextAlignment.CENTER);
            VBox vBox1 = new VBox();
            VBox vBox2 = new VBox();
            VBox vBox3 = new VBox();
            vBox1.setAlignment(Pos.CENTER);
            vBox3.setAlignment(Pos.CENTER);
            vBox2.setAlignment(Pos.CENTER);

            dataHBox.getChildren().addAll(vBox1, vBox2, vBox3);
            dataHBox.setSpacing(20);
            dataHBox.setAlignment(Pos.CENTER);

            int roundNum = 1;

            for (int i = 0; i < winnerScores.length; i++) {
                Label roundLabel = new Label();
                Label winnerLabel = new Label();
                Label loserLabel = new Label();
                roundLabel.setText("Round " + roundNum);
                winnerLabel.setText(winnerUsername + " : " + winnerScores[i]);
                winnerLabel.setTextFill(Color.RED);
                winnerLabel.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/WinnerStyle.css")).toExternalForm());
                winnerLabel.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/WinnerStyle.css")).toExternalForm());
                loserLabel.setText(loserUsername + " : " + loserScores[i]);

                winnerLabel.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/WinnerStyle.css")).toExternalForm());
                loserLabel.setText(loserUsername + " : " + loserScores[i]);
                vBox1.getChildren().add(roundLabel);
                vBox2.getChildren().add(winnerLabel);
                vBox3.getChildren().add(loserLabel);
                roundNum++;
            }

        }
    }


}