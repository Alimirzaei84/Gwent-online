module testAnt {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;
    requires javafx.swing;
    requires java.base;
    requires java.logging;
//    requires com.google.gson;
    exports view;
//    exports model;
    opens view to javafx.fxml;
}