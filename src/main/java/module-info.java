module com.example.dotsandboxes {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.dotsandboxes to javafx.fxml;
    opens com.example.dotsandboxes.view to javafx.fxml;
    exports com.example.dotsandboxes.view to javafx.graphics;
    exports com.example.dotsandboxes;
}