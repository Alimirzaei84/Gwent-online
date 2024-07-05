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
    public HBox inviteContainer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        client.User.getInstance().setAppMenu(this);
        try {
            Out.sendMessage("get friends");
            Out.sendMessage("get requests");
            Out.sendMessage("get invites");
        } catch (Exception e) {
            e.printStackTrace();
        }

        refreshTimeLine = new Timeline(new KeyFrame(Duration.seconds(1), event -> refreshData()));
        refreshTimeLine.setCycleCount(-1);
        refreshTimeLine.play();
    }

    @Override
    public void start(Stage stage) throws Exception {
        URL url = MainMenu.class.getResource("/FXML/FriendsMenu.fxml");
        assert url != null;
        AnchorPane root = FXMLLoader.load(url);

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/FriendsMenu.css")).toExternalForm());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/CSS/ScrollBarCss.css")).toExternalForm());
        stage.setScene(scene);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();
    }

    public void refreshData() {
        try {
            Out.sendMessage("get friends");
            Out.sendMessage("get requests");
            Out.sendMessage("get invites");
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


            for (int j = 0; j < requests.size(); j++) {
                FriendRequest request = requests.get(j);
                Label usernameLabel = new Label();
                usernameLabel.setText(request.getRequester().getUsername());
                Button denyButton = new Button();
                Button acceptButton = new Button();
                denyButton.setOnMouseClicked(event -> rejectRequest(usernameLabel.getText()));
                acceptButton.setOnMouseClicked(event -> acceptRequest(usernameLabel.getText()));
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
            friendsContainer.getChildren().clear();
            friendsContainer.getChildren().addAll(vBox1);

            for (String username : usernames) {
                if (username.equals(""))
                    continue;
                Label usernameLabel = new Label();
                usernameLabel.setText(username);
                usernameLabel.setPrefHeight(46);
                vBox1.getChildren().add(usernameLabel);
                usernameLabel.setPrefWidth(330);
            }

        } else if (command.startsWith("[REQUESTS]:")) {
            String input = command.substring("[REQUESTS]:".length());
            String[] usernames = input.split("\\|");
            VBox usernamesVBox = new VBox();
            VBox denyButtons = new VBox();
            VBox acceptButtons = new VBox();
            requestsContainer.getChildren().clear();
            requestsContainer.getChildren().addAll(usernamesVBox, denyButtons, acceptButtons);

            for (String username : usernames) {
                if (username.equals(""))
                    continue;
                Label usernameLabel = new Label();
                usernameLabel.setText(username);
                Button denyButton = new Button();
                Button acceptButton = new Button();
                denyButton.setOnMouseClicked(event -> rejectRequest(username));
                acceptButton.setOnMouseClicked(event -> acceptRequest(username));
                denyButton.setStyle("-fx-background-color: red;");
                acceptButton.setStyle("-fx-background-color: green;");
                usernameLabel.setMinWidth(250);
                usernameLabel.setMaxWidth(250);
                usernameLabel.setPrefHeight(46);
                denyButton.setPrefHeight(46);
                acceptButton.setPrefHeight(46);
                acceptButton.setPrefWidth(1);
                denyButton.setPrefWidth(1);
//                denyButton.setText("REJECT");
//                acceptButton.setText("ACCEPT");
                usernamesVBox.getChildren().add(usernameLabel);
                denyButtons.getChildren().add(denyButton);
                acceptButtons.getChildren().add(acceptButton);
            }
        } else if (command.startsWith("[INVITES]")) {
            String input = command.substring("[INVITES]:".length());
            String[] usernames = input.split("\\|");
            VBox usernamesVBox = new VBox();
            VBox denyButtons = new VBox();
            VBox acceptButtons = new VBox();
            inviteContainer.getChildren().clear();
            inviteContainer.getChildren().addAll(usernamesVBox, denyButtons, acceptButtons);

            for (String username : usernames) {
                if (username.equals(""))
                    continue;
                Label usernameLabel = new Label();
                usernameLabel.setText(username);
                Button denyButton = new Button();
                Button acceptButton = new Button();
                denyButton.setOnMouseClicked(event -> rejectInvite(username));
                acceptButton.setOnMouseClicked(event -> acceptInvite(username));
                denyButton.setStyle("-fx-background-color: red;");
                acceptButton.setStyle("-fx-background-color: green;");
                usernameLabel.setMinWidth(250);
                usernameLabel.setMaxWidth(250);
                usernameLabel.setPrefHeight(46);
                denyButton.setPrefHeight(46);
                acceptButton.setPrefHeight(46);
                acceptButton.setPrefWidth(1);
                denyButton.setPrefWidth(1);
                usernamesVBox.getChildren().add(usernameLabel);
                denyButtons.getChildren().add(denyButton);
                acceptButtons.getChildren().add(acceptButton);
            }}
        }

        public void playGame () {
            //TODO
        }

        public void sendFriendRequest () {
            try {
                Out.sendMessage("let's be friend " + usernameField.getText());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static boolean isValidFriendRequestJson (String jsonString){
            ObjectMapper mapper = new ObjectMapper();

            try {
                mapper.readValue(jsonString, new TypeReference<ArrayList<FriendRequest>>() {
                });
                return true; // Successfully parsed, valid JSON
            } catch (Exception e) {
                return false; // Failed to parse, invalid JSON
            }
        }

        public static ArrayList<FriendRequest> fromJsonString (String jsonString){
            ObjectMapper mapper = new ObjectMapper();

            try {
                return mapper.readValue(jsonString, new TypeReference<ArrayList<FriendRequest>>() {
                });
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        public void acceptRequest (String username){
            try {
                Out.sendMessage("accept friend request from " + username);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void rejectRequest (String username){
            try {
                Out.sendMessage("deny friend request from " + username);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void rejectInvite(String username){
            try {
                Out.sendMessage("deny invitation from " + username);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void acceptInvite(String username){
            try {
                Out.sendMessage("accept game with " + username);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void back () {
            try {
                refreshTimeLine.stop();
                MainMenu mainMenu = new MainMenu();
                mainMenu.start((Stage) friendsContainer.getScene().getWindow());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

