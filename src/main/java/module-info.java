module testAnt {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;
    requires javafx.swing;
    requires java.base;
    requires java.logging;
    requires com.google.gson;
    requires java.compiler;
    opens model.role to com.google.gson;
    exports view;
    exports model.game;
    exports model.Account;
    opens view to javafx.fxml;
}