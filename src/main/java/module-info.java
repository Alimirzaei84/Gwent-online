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
    requires java.mail;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires mysql.connector.j;
    requires jcommander;
    requires org.jetbrains.annotations;
    exports server to com.fasterxml.jackson.databind;
    opens server to com.fasterxml.jackson.databind;
//    opens model.game to  com.fasterxml.jackson.databind;
//    opens model.Account to  com.fasterxml.jackson.databind;
    opens controller to com.fasterxml.jackson.databind;
    exports client.view;
    exports model.role;
    opens client.view to javafx.fxml;
    opens server.request to com.fasterxml.jackson.databind;
    exports server.game to com.fasterxml.jackson.databind;
}
