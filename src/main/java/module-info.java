module com.example.dotsandboxes {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.dotsandboxes to javafx.fxml;
    exports com.example.dotsandboxes;
}