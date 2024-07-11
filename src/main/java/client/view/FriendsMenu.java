package client.view;

import client.Out;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import server.Enum.Regexes;
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
    public HBox gamesContainer;
    public Text myUsernameText;
    public VBox searchedUserDataBox;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        client.User.getInstance().setAppMenu(this);
        try {
            Out.sendMessage("get friends");
            Out.sendMessage("get requests");
            Out.sendMessage("get invites");
            Out.sendMessage("get games");
        } catch (Exception e) {
            e.printStackTrace();
        }

        refreshTimeLine = new Timeline(new KeyFrame(Duration.seconds(1), event -> refreshData()));
        refreshTimeLine.setCycleCount(-1);
        refreshTimeLine.play();

        myUsernameText.setText(client.User.getInstance().getUsername());
        myUsernameText.setTextAlignment(TextAlignment.CENTER);
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
            Out.sendMessage("get games");
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
        } else if (Regexes.RUNNING_GAMES_INFO.matches(command)) {
            ArrayList<String[]> gameData = translateInfo(command);
            VBox vBox1 = new VBox();
            VBox vBox2 = new VBox();
            gamesContainer.getChildren().clear();
            gamesContainer.getChildren().addAll(vBox1, vBox2);

            for (String[] strings : gameData) {
                String username = strings[1];
                int gameId = Integer.parseInt(strings[0]);
                System.out.println(username + " " + gameId);

                Label usernameLabel = new Label();
                usernameLabel.setText(username);
                usernameLabel.setPrefHeight(46);
                vBox1.getChildren().add(usernameLabel);
                Button playButton = new Button();
                playButton.setText("view");
                playButton.setOnMouseClicked(event -> view(username, gameId));
                usernameLabel.setPrefWidth(250);
                playButton.setPrefHeight(46);
                vBox2.getChildren().add(playButton);
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

            for (String string : usernames) {
                if (string.equals("")) {
                    continue;
                }
                System.out.println(string);
                String[] data = string.split(":");
                String username = data[0];
                String status = data[1];
                if (username.equals("")) {
                    continue;
                }

                Label usernameLabel = new Label();
                usernameLabel.setText(username);
                usernameLabel.setPrefHeight(46);

                if (status.equals("online")) {
                    usernameLabel.setStyle("-fx-text-fill: green;");
                } else if (status.equals("offline")) {
                    usernameLabel.setStyle("-fx-text-fill: red;");
                }

                vBox1.getChildren().add(usernameLabel);
                Button playButton = new Button();
                playButton.setText("play");
                playButton.setOnMouseClicked(event -> playGame(username));
                usernameLabel.setPrefWidth(250);
                playButton.setPrefHeight(46);
                vBox2.getChildren().add(playButton);
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
            }
        } else if (command.startsWith("[PLAYGAME]")) {
            try {
                refreshTimeLine.stop();
                GameLauncher gameLauncher = new GameLauncher();
                gameLauncher.start((Stage) usernameField.getScene().getWindow());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (command.startsWith("[SEARCHED_USER_PROFILE]")) {
            System.out.println("WE ARE HERE-------?>");
            String[] userData = command.substring("[SEARCHED_USER_PROFILE]:".length()).split("\\|");
            searchedUserDataBox.getChildren().clear();
            for (int i = 0; i < 3; i++) {
                Label label = new Label();
                label.setText(userData[i]);
                label.setTextAlignment(TextAlignment.CENTER);
                label.setPrefWidth(175);
                label.setPrefHeight(46);
                searchedUserDataBox.setAlignment(Pos.CENTER);
                searchedUserDataBox.getChildren().add(label);
                label.setAlignment(Pos.CENTER);
            }
        }
    }

    public void playGame(String username) {
        try {
            Out.sendMessage("let's play " + username);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public void acceptRequest(String username) {
        try {
            Out.sendMessage("accept friend request from " + username);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rejectRequest(String username) {
        try {
            Out.sendMessage("deny friend request from " + username);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void rejectInvite(String username) {
        try {
            Out.sendMessage("deny invitation from " + username);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void acceptInvite(String username) {
        try {
            Out.sendMessage("accept game with " + username);
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

    private ArrayList<String[]> translateInfo(String info) {
        System.out.println(info);
        // String[0] : game id && String[1] = the friend username
        ArrayList<String[]> gamePairedByUsername = new ArrayList<>();
        String[] pairs = Regexes.RUNNING_GAMES_INFO.getGroup(info, "INFO").split(" ");
        for (String pair : pairs) {
            gamePairedByUsername.add(pair.split("_"));
        }
        return gamePairedByUsername;

    }

    public void view(String username, int gameId) {
        try {
            refreshTimeLine.stop();
            GameView view = new GameView();
            view.start((Stage) friendsContainer.getScene().getWindow());
            Out.sendMessage("watch " + gameId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

