package client.view;

import client.Out;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import server.request.FriendRequest;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class FriendsMenu extends AppMenu {

    public TextField usernameField;
    public HBox friendsContainer;
    public HBox requestsContainer;
    public Timeline refreshTimeLine;
    //TODO : PUT TIMELINE

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        client.User.getInstance().setAppMenu(this);
        try {
            Out.sendMessage("get friends");
            Out.sendMessage("get requests");
        } catch (Exception e) {
            e.printStackTrace();
        }

        refreshTimeLine = new Timeline(new KeyFrame(Duration.seconds(3), event -> refreshData()));
//        refreshTimeLine.setCycleCount(-1);
//        refreshTimeLine.play();
    }

    @Override
    public void start(Stage stage) throws Exception {
        URL url = MainMenu.class.getResource("/FXML/FriendsMenu.fxml");
        assert url != null;
        AnchorPane root = FXMLLoader.load(url);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/FriendsMenu.css")).toExternalForm());
//        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/ScrollBarCss.css")).toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    public void refreshData() {
        try {
            Out.sendMessage("get friends");
            Out.sendMessage("get requests");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handleCommand(String command) throws Exception {
        if (isValidFriendRequestJson(command)) {
            ArrayList<FriendRequest> requests = fromJsonString(command);
            VBox usernames = new VBox();
            VBox denyButtons = new VBox();
            VBox acceptButtons = new VBox();
            requestsContainer.getChildren().clear();
            requestsContainer.getChildren().addAll(usernames, denyButtons, acceptButtons);
            for (FriendRequest request : requests) {
                for (int i = 0; i < 10; i++)
                    System.out.println("HERE --->" + request.getRequester().getUsername());
                Label usernameLabel = new Label();
                usernameLabel.setText(request.getRequester().getUsername());
                Button denyButton = new Button();
                Button acceptButton = new Button();
                denyButton.setOnMouseClicked(event -> reject(usernameLabel.getText()));
                acceptButton.setOnMouseClicked(event -> accept(usernameLabel.getText()));
                denyButton.setText("REJECT");
                acceptButton.setText("ACCEPT");
                usernames.getChildren().add(usernameLabel);
                denyButtons.getChildren().add(denyButton);
                acceptButtons.getChildren().add(acceptButton);
            }

        } else if (command.startsWith("[SUCC]")) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Successful");
            alert.setHeaderText("Successful");
            alert.setContentText(command.substring("[SUCC]:".length()));
            Scene scene = alert.getDialogPane().getScene();
            scene.getStylesheets().add(getClass().getResource("/CSS/AlertStyler.css").toExternalForm());
            alert.showAndWait();
        } else if (command.startsWith("[ERR]")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText(command.substring("[ERR]:".length()));
            Scene scene = alert.getDialogPane().getScene();
            scene.getStylesheets().add(getClass().getResource("/CSS/AlertStyler.css").toExternalForm());
            alert.showAndWait();
        } else if (command.startsWith("[FRIENDS]")) {
            String input = command.substring("[FRIENDS]:".length());
            String[] usernames = input.split("\\|");
            VBox vBox1 = new VBox();
            VBox vBox2 = new VBox();
            friendsContainer.getChildren().clear();
            friendsContainer.getChildren().addAll(vBox1, vBox2);

            for (String username : usernames) {
                if (username.equals(""))
                    continue;
                Label usernameLabel = new Label();
                usernameLabel.setText(username);
                vBox1.getChildren().add(usernameLabel);
                Button playButton = new Button();
                playButton.setOnMouseClicked(event -> playGame());
                vBox2.getChildren().add(playButton);
            }
        }
    }

    public void playGame() {
        //TODO
    }

    public void sendFriendRequest() {
        try {
            Out.sendMessage("let's be friend " + usernameField.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isValidFriendRequestJson(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            mapper.readValue(jsonString, new TypeReference<ArrayList<FriendRequest>>() {
            });
            return true; // Successfully parsed, valid JSON
        } catch (Exception e) {
            return false; // Failed to parse, invalid JSON
        }
    }

    public static ArrayList<FriendRequest> fromJsonString(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            return mapper.readValue(jsonString, new TypeReference<ArrayList<FriendRequest>>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void accept(String username) {
        try {
            Out.sendMessage("accept friend request from " + username);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reject(String username) {
        try {
            Out.sendMessage("deny friend request from " + username);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void back() {
        try {
            refreshTimeLine.stop();
            MainMenu mainMenu = new MainMenu();
            mainMenu.start((Stage) friendsContainer.getScene().getWindow());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

