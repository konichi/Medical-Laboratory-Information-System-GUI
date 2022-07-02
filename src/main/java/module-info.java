module com.example.medicallaboratorysystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.medicallaboratorysystem to javafx.fxml;
    exports com.example.medicallaboratorysystem;
    opens com.example.medicallaboratorysystem.controllers to javafx.fxml;
    exports com.example.medicallaboratorysystem.managers;
    opens com.example.medicallaboratorysystem.managers to javafx.fxml;
}