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
    requires org.testng;
    opens model.role to com.google.gson;
    exports view;
    exports model.game;
    exports model.Account;
    exports model.role;
    opens view to javafx.fxml;
}