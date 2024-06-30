package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class GameHistory extends AppMenu{

    AnchorPane pane;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ScrollPane scrollPane = new ScrollPane();
//        pane.getChildren().add(scrollPane);

    }
}
