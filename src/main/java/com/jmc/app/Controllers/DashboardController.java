package com.jmc.app.Controllers;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {
    @FXML
    private Label welcomeLabel;

    @FXML
    private Label statusLabel; // Achte darauf, dass diese Deklaration korrekt ist

    @FXML
    private void initialize() {
        statusLabel.setText("Dashboard geladen.");
    }
    @FXML
    public void setUserText(String userText) {
        if (welcomeLabel != null) {
            welcomeLabel.setText("Willkommen, " + userText + "!");
        }
    }

    public void loadDashboardView() {
        try {
            // Ersetze "Dashboard.fxml" durch den tatsächlichen Pfad zu deiner Dashboard-FXML-Datei
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jmc/app/Dashboard.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) statusLabel.getScene().getWindow(); // Nutze irgendein Element, das bereits geladen ist, um das Fenster zu erhalten
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Fehler beim Laden des Dashboards.");
        }
    }

}


