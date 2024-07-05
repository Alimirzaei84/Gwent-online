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
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    exports server to com.fasterxml.jackson.databind;
    exports view to com.fasterxml.jackson.databind;
    opens server to com.fasterxml.jackson.databind;
    opens model.role to  com.fasterxml.jackson.databind;
    opens model.game to  com.fasterxml.jackson.databind;
    opens server.Account to  com.fasterxml.jackson.databind;
    opens controller to  com.fasterxml.jackson.databind;
    exports client.view;
    exports model.game;
    exports server.Account;
    exports model.role;
    opens client.view to javafx.fxml;
    opens server.request to com.fasterxml.jackson.databind;
    exports server.game;
    opens server.game to com.fasterxml.jackson.databind;
}