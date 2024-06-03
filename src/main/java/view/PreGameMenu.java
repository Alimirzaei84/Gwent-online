package view;

import controller.ApplicationController;
import controller.menuConrollers.PreGameMenuController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Account.User;
import model.role.Faction;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class PreGameMenu extends AppMenu {
    private final PreGameMenuController controller;
    private ImageView currentImageView;
    public PreGameMenu() {
        controller = new PreGameMenuController();
        currentImageView = null;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        URL url = ProfileMenu.class.getResource("/FXML/PreGameMenu.fxml");
        assert url != null;
        AnchorPane root = FXMLLoader.load(url);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    public void showAndChangeFaction() throws IOException {
        VBox content = new VBox();
        content.setAlignment(Pos.CENTER);
        File directory = new File("src/main/resources/assets/lg");
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isFile() && file.getName().contains("faction_")) {
                Image image = new Image(file.toURI().toURL().toString());
                AnchorPane anchorPane = new AnchorPane();
                ImageView imageView = new ImageView(image);
                String factionName = file.getName().substring(8, file.getName().length() - 4);


                if (User.getLoggedInUser().getFaction().equals(Faction.valueOf(factionName.toUpperCase()))) {
                    ColorAdjust grayscaleEffect = new ColorAdjust();
                    grayscaleEffect.setSaturation(-1.0);
                    imageView.setEffect(grayscaleEffect);
                    currentImageView = imageView;
                }

                imageView.setOnMouseClicked(_ -> handleDifferentColor(imageView, factionName));
                anchorPane.getChildren().add(imageView);
                Label label = new Label(factionName);
                label.setMinSize(30, 30);
                anchorPane.getChildren().add(label);
                content.getChildren().add(anchorPane);
            }
        }

        Button button = new Button("back");
        button.setMinHeight(100);
        button.setMinWidth(100);
        button.setOnMouseClicked(_ -> {
            try {
                start(ApplicationController.getStage());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        content.getChildren().add(button);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        Scene scene = new Scene(scrollPane, 800, 800);
        ApplicationController.getStage().setScene(scene);
        ApplicationController.getStage().setTitle("select faction");
        ApplicationController.getStage().show();
    }

    private void handleDifferentColor(ImageView imageView, String factionName) {
        controller.setFaction(User.getLoggedInUser(), factionName);
        ColorAdjust grayscaleEffect = new ColorAdjust();
        grayscaleEffect.setSaturation(-1.0);
        imageView.setEffect(grayscaleEffect);
        if (currentImageView != null) {
            currentImageView.setEffect(null);
        }
        currentImageView = imageView;

    }

    public void showDeck() {
    }

    public void shewCards() {
    }

    public void uploadDeck() {
    }

    public void downloadDeck() {
    }

    public void changeTurn() {
    }

    public void startGame() {
    }

    public void showCurrentUserInfo() {
    }

    public void showLeaders() {
    }

    public void backToMainMenu() throws Exception {
        MainMenu mainMenu = new MainMenu();
        mainMenu.start(ApplicationController.getStage());
    }
}