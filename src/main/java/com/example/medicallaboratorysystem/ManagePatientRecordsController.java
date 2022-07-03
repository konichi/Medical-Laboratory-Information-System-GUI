package com.example.medicallaboratorysystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ManagePatientRecordsController {

    // TODO: Focus on current window only
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    void onAddPatientClick(ActionEvent event) {
        openWindow("add-new-patient.fxml");
    }

    @FXML
    void onDeleteRecordClick(ActionEvent event) {

    }

    @FXML
    void onEditRecordClick(ActionEvent event) {

    }

    @FXML
    void onSearchPatientRecordClick(ActionEvent event) {
        Alert alert = new Alert(
                Alert.AlertType.NONE,
                "Do you know the patient's UID?",
                ButtonType.YES,
                ButtonType.NO
        );
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
//            resetDisplay();
        }
        else {
            alert.setContentText("Do you know the patient's National ID no.?");
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {

            }

            else {
                openWindow("search-patient-record.fxml");
            }
        }


    }

    @FXML
    void initialize() {

    }

    void openWindow(String fileName) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fileName));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
//            stage.initOwner(pane.getScene().getWindow());
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
