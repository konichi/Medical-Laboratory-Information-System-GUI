package com.example.medicallaboratorysystem;

import com.example.medicallaboratorysystem.managers.ManagePatientRecords;
import com.example.medicallaboratorysystem.models.Patient;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class AddNewPatientController {
    ManagePatientRecords mpr = new ManagePatientRecords();

    @FXML
    private TextField addressLabel;

    @FXML
    private TextField birthdayLabel;

    @FXML
    private RadioButton femaleRadioButton;

    @FXML
    private TextField firstNameLabel;

    @FXML
    private TextField idLabel;

    @FXML
    private TextField lastNameLabel;

    @FXML
    private RadioButton maleRadioButton;

    @FXML
    private TextField middleNameLabel;

    @FXML
    private TextField phoneLabel;

    @FXML
    void onSaveRecordClick(ActionEvent event) {
        String firstName = firstNameLabel.getText();
        String lastName = lastNameLabel.getText();
        String middleName = middleNameLabel.getText();
        String birthday = birthdayLabel.getText();
        String address = addressLabel.getText();
        String phoneNumber = phoneLabel.getText();
        long nationalId = Long.parseLong(idLabel.getText());

        String gender = "Male";
        String id = mpr.generateUID();

        int error = mpr.addPatient(
                        new Patient(
                            id,
                            firstName,
                            lastName,
                            middleName,
                            birthday,
                            gender,
                            address,
                            phoneNumber,
                            nationalId
                        )
                    );

        Alert a = new Alert(Alert.AlertType.NONE);

        if(error == 1) {
            a.setAlertType(Alert.AlertType.CONFIRMATION);
            a.show();
        }
        else {
            a.setAlertType(Alert.AlertType.ERROR);
            a.show();
        }
    }

}
