package com.example.medicallaboratorysystem;

import com.example.medicallaboratorysystem.managers.ManagePatientRecords;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.time.format.DateTimeFormatter;

public class SearchRecordController {

    //region Declarations
    ManagePatientRecords mpr = new ManagePatientRecords();

    @FXML
    private AnchorPane searchRecordPane;

    @FXML
    private DatePicker birthdayPicker;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private Button searchButton;
    //endregion

    @FXML
    void onSearchRecordClick(ActionEvent event) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String birthday = birthdayPicker.getValue().format(formatter);


    }

    @FXML
    void initialize() {
        searchButton.setDisable(false);
    }
}
