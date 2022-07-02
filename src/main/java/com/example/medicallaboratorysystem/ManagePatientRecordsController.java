package com.example.medicallaboratorysystem;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ManagePatientRecordsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    void onAddPatientClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add-new-patient.fxml"));
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

    @FXML
    void onDeleteRecordClick(ActionEvent event) {

    }

    @FXML
    void onEditRecordClick(ActionEvent event) {

    }

    @FXML
    void onSearchPatientRecordClick(ActionEvent event) {

    }

    @FXML
    void initialize() {

    }

}
