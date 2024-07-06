package client.view;

import javafx.application.Application;
import javafx.fxml.Initializable;
import server.game.Board;

public abstract class AppMenu extends Application implements Initializable {
    public abstract void handleCommand(String command) throws Exception;
}