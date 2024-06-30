package view;

import controller.ApplicationController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.game.Game;
import model.game.stateAfterADiamond;
import org.w3c.dom.Text;

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

        this.winnerLabel.setText(Game.getCurrentGame().getWinner().getUser().getUsername() + " : " + Game.getCurrentGame().getWinner().getTotalPoint());
        VBox vBox1 = new VBox();
        VBox vBox2 = new VBox();
        VBox vBox3 = new VBox();
        vBox1.setAlignment(Pos.CENTER);
        vBox3.setAlignment(Pos.CENTER);
        vBox2.setAlignment(Pos.CENTER);
        
        dataHBox.getChildren().addAll(vBox1, vBox2, vBox3);
        dataHBox.setSpacing(20);

        for (stateAfterADiamond state : Game.getCurrentGame().getStates()) {
            Label roundLabel = new Label();
            Label winnerLabel = new Label();
            Label loserLabel = new Label();
            roundLabel.setText("Round " + state.getRound());
            winnerLabel.setText(state.getWinner().getUser().getUsername() + " : " + state.getWinnerScore());
            loserLabel.setText(state.getLooser().getUser().getUsername() + " : " + state.getLooserScoer());
            vBox1.getChildren().add(roundLabel);
            vBox2.getChildren().add(winnerLabel);
            vBox3.getChildren().add(loserLabel);
        }
    }

    public void setSizes(){


    }

    public void backToMainMenu() {
        try{
            MainMenu mainMenu = new MainMenu();
            mainMenu.start(ApplicationController.getStage());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
