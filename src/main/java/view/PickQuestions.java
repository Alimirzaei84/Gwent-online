package view;

import controller.ApplicationController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Account.User;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class PickQuestions extends AppMenu {
    public TextField color;
    public TextField month;
    public TextField food;

    @Override
    public void start(Stage stage) throws Exception {
        Pane pane = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/FXML/PickQuestion.fxml")));
        Scene scene = new Scene(pane);

        scene.getStylesheets().add(getClass().getResource("/CSS/RegisterMenu.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void setFavoriteColor() throws Exception {
        if (color.getText() != null && color.getText().length() > 2) {
            User.getLoggedInUser().addQuestionAnswer("your favorite color?", color.getText());
            System.out.println("[INFO]: user favorite color is -->>  " + color.getText());
        }
    }

    public void setFavoriteMonth() throws Exception {
        if (month.getText() != null && month.getText().length() > 2) {
            User.getLoggedInUser().addQuestionAnswer("your favorite month?", month.getText());
            System.out.println("[INFO]: user favorite month is -->>  " + month.getText());
        }
    }

    public void setFavoriteFood() throws Exception {
        if (food.getText() != null && food.getText().length() > 1) {
            User.getLoggedInUser().addQuestionAnswer("your favorite food?", food.getText());
            System.out.println("[INFO]: user favorite food is -->>  " + food.getText());
        }
    }

    public void goToLoginMenu() throws Exception {
        setFavoriteColor();
        setFavoriteFood();
        setFavoriteMonth();
        LoginMenu loginMenu = new LoginMenu();
        loginMenu.start(ApplicationController.getStage());
    }

    @Override
    public void initialize() {

    }
}