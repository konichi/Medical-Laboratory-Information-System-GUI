package com.example.medicallaboratorysystem;

import com.example.medicallaboratorysystem.managers.ManagePatientRecords;
import com.example.medicallaboratorysystem.models.Patient;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddNewPatientController {

    //region Declarations
    ManagePatientRecords mpr = new ManagePatientRecords();

    @FXML
    private AnchorPane addPatientPane;

    @FXML
    private TextField addressField;

    @FXML
    private DatePicker birthdayPicker;

    @FXML
    private RadioButton femaleRadioButton;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField idField;

    @FXML
    private TextField lastNameField;

    @FXML
    private RadioButton maleRadioButton;

    @FXML
    private TextField middleNameField;

    @FXML
    private TextField phoneField;

    @FXML
    private Button saveButton;
    //endregion

    // TODO: INPUT VALIDATION

    @FXML
    void onSaveRecordClick(ActionEvent event) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String middleName = middleNameField.getText();
        String birthday = birthdayPicker.getValue().format(formatter);
        String gender = maleRadioButton.isSelected() ? maleRadioButton.getText() : femaleRadioButton.getText();
        String address = addressField.getText();
        String phoneNumber = phoneField.getText();
        long nationalId = Long.parseLong(idField.getText());

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

        Alert alert = new Alert(
                Alert.AlertType.NONE,
                "Would you like to add another patient?",
                ButtonType.YES,
                ButtonType.NO
                );
        alert.setHeaderText("Patient record successfully added.");
        alert.showAndWait();

        if(error == 1) {
            alert.setHeaderText("Patient record not added.");
            alert.showAndWait();
        }
        else {
            alert.setHeaderText("Patient record successfully added.");
            alert.showAndWait();
        }

        if (alert.getResult() == ButtonType.YES) {
            resetDisplay();
        }
        else {
            closeWindow();
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) addPatientPane.getScene().getWindow();
        stage.close();
    }

    private void resetDisplay() {
        firstNameField.setText("");
        lastNameField.setText("");
        middleNameField.setText("");
        birthdayPicker.setValue(LocalDate.now());
        maleRadioButton.setSelected(true);
        addressField.setText("");
        phoneField.setText("");
        idField.setText("");
    }

    @FXML
    void initialize() {
        ToggleGroup group = new ToggleGroup();
        maleRadioButton.setToggleGroup(group);
        femaleRadioButton.setToggleGroup(group);
        maleRadioButton.setSelected(true);

        saveButton.setDisable(false);

//        if(!firstNameField.getText().isEmpty() &&
//            !lastNameField.getText().isEmpty() &&
//            !middleNameField.getText().isEmpty() &&
//            !birthdayField.getText().isEmpty() &&
//            !addressField.getText().isEmpty() &&
//            !phoneField.getText().isEmpty() &&
//            !idField.getText().isEmpty()) {
//            System.out.println("hello");
//            saveButton.setDisable(false);
//        }
    }

}
