package client.view;

import javafx.application.Application;
import javafx.fxml.Initializable;

public abstract class AppMenu extends Application implements Initializable {
    public abstract void initialize();

    public abstract void handleCommand(String command) throws Exception;
}