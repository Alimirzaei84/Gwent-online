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
    requires java.sql;
    requires mysql.connector.j;
    opens model.role to com.google.gson;
    exports client.view;
    exports model.game;
    exports model.Account;
    exports model.role;
    opens client.view to javafx.fxml;
}