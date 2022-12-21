module smith.firstscreen {
    requires javafx.controls;
    requires javafx.fxml;


    opens smith.firstscreen to javafx.fxml;
    exports smith.firstscreen;
    exports controller;
    opens controller to javafx.fxml;
    opens model to javafx.fxml;
    exports model;
}