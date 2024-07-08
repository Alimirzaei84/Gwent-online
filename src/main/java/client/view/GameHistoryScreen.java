package client.view;

import client.Out;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import server.game.GameHistory;
import server.game.StateAfterADiamond;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class GameHistoryScreen extends AppMenu {

    public HBox dataHBox;
    private VBox vBox1 = new VBox();
    private VBox vBox2 = new VBox();
    private VBox vBox3 = new VBox();
    AnchorPane pane;
    private int number = 0;
    ArrayList<GameHistory> gameHistories;

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL url = ProfileMenu.class.getResource("/FXML/GameHistory.fxml");
        assert url != null;
        AnchorPane root = FXMLLoader.load(url);
        Scene scene = new Scene(root);
        pane = root;

        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/ProfileMenu.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void back() {
        try {
            ProfileMenu profileMenu = new ProfileMenu();
            client.User.getInstance().setAppMenu(profileMenu);
            profileMenu.start((Stage) dataHBox.getScene().getWindow());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isJsonValid(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.readTree(jsonString);
            return true;
        } catch (JsonProcessingException e) {
            return false;
        }
    }

    @Override
    public void handleCommand(String command) {
        if (isJsonValid(command)) {
            gameHistories = fromJson(command);
            if (!gameHistories.isEmpty()) {
                showAGameHistory(number);
            }
        } else if (command.startsWith("[PLAYGAME]")) {
            try {
                GameLauncher gameLauncher = new GameLauncher();
                gameLauncher.start((Stage) dataHBox.getScene().getWindow());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static ArrayList<GameHistory> fromJson(String jsonString) {
        System.out.println("JSON STRING :------>" + jsonString );
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonString, new TypeReference<ArrayList<GameHistory>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean showAGameHistory(int index) {
        if (gameHistories.size() <= index || index < 0)
            return false;

        vBox1.getChildren().clear();
        vBox2.getChildren().clear();
        vBox3.getChildren().clear();
        GameHistory gameHistory = gameHistories.get(index);
        int roundNum = 1;
        for (StateAfterADiamond state : gameHistory.getRoundsInformations()) {
            Label roundLabel = new Label();
            Label winnerLabel = new Label();
            Label loserLabel = new Label();
            roundLabel.setText("Round " + roundNum);
            winnerLabel.setText(state.winner().getUser().getUsername() + " : " + state.winnerScore());
            winnerLabel.setTextFill(Color.RED);
            winnerLabel.getStylesheets().add(getClass().getResource("/CSS/WinnerStyle.css").toExternalForm());
            loserLabel.setText(state.getLooser().getUser().getUsername() + " : " + state.getLooserScore());

            winnerLabel.getStylesheets().add(getClass().getResource("/CSS/WinnerStyle.css").toExternalForm());
            loserLabel.setText(state.getLooser().getUser().getUsername() + " : " + state.getLooserScore());
            vBox1.getChildren().add(roundLabel);
            vBox2.getChildren().add(winnerLabel);
            vBox3.getChildren().add(loserLabel);
            roundNum++;
        }

        Label winnerTotalScore = new Label();
        Label loserTotalScore = new Label();
        Label winnerTotalScoreData = new Label();
        Label loserTotalScoreData = new Label();
        Label dateLabel = new Label();
        Label dateLabelData = new Label();
        Label winnerInfoLabel = new Label();
        Label loserInfoLabel = new Label();
        winnerInfoLabel.setAlignment(Pos.CENTER);
        winnerInfoLabel.setTextAlignment(TextAlignment.CENTER);
        loserInfoLabel.setTextAlignment(TextAlignment.CENTER);
        loserInfoLabel.setAlignment(Pos.CENTER);
        winnerTotalScore.setAlignment(Pos.CENTER);
        winnerTotalScore.setTextAlignment(TextAlignment.CENTER);
        loserTotalScore.setTextAlignment(TextAlignment.CENTER);
        loserTotalScore.setAlignment(Pos.CENTER);
        winnerTotalScoreData.setAlignment(Pos.CENTER);
        winnerTotalScoreData.setTextAlignment(TextAlignment.CENTER);
        loserTotalScoreData.setAlignment(Pos.CENTER);
        loserTotalScoreData.setTextAlignment(TextAlignment.CENTER);
        dateLabel.setAlignment(Pos.CENTER);
        dateLabel.setTextAlignment(TextAlignment.CENTER);
        dateLabelData.setAlignment(Pos.CENTER);
        dateLabelData.setTextAlignment(TextAlignment.CENTER);

        loserInfoLabel.setText("LOSER");
        winnerInfoLabel.setText("WINNER");
        winnerTotalScore.setText(gameHistory.getWinner().getUsername());
        winnerTotalScore.getStylesheets().add(getClass().getResource("/CSS/WinnerStyle.css").toExternalForm());
        loserTotalScore.setText(gameHistory.getWinner().getUsername());
        winnerTotalScoreData.setText(String.valueOf(gameHistory.getWinnerTotalScore()));
        winnerTotalScoreData.getStylesheets().add(getClass().getResource("/CSS/WinnerStyle.css").toExternalForm());
        loserTotalScoreData.setText(String.valueOf(gameHistory.getLoserTotalScore()));

        dateLabel.setText("Date");
        dateLabelData.setText(gameHistory.getDateFormattedString());

        vBox1.getChildren().addAll(winnerInfoLabel, loserInfoLabel, dateLabel);
        vBox2.getChildren().addAll(winnerTotalScore, loserTotalScore, dateLabelData);
        vBox3.getChildren().addAll(winnerTotalScoreData, loserTotalScoreData);

        return true;
    }

    public void next() {
        int num = number + 1;
        if (showAGameHistory(num)) {
            number++;
        }
    }

    public void previous() {
        int num = number--;
        if (num >= 0) {
            showAGameHistory(num);
            number--;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        client.User.getInstance().setAppMenu(this);
        dataHBox.getChildren().addAll(vBox1, vBox2, vBox3);
        vBox1.setAlignment(Pos.CENTER);
        vBox3.setAlignment(Pos.CENTER);
        vBox2.setAlignment(Pos.CENTER);
        dataHBox.setSpacing(20);
        dataHBox.setAlignment(Pos.CENTER);
        try {
            Out.sendMessage("get game histories");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
