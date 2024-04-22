package com.jmc.app.Controllers;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {
    @FXML
    private FontAwesomeIconView profilButton;
    @FXML
    private Label welcomeLabel;

    @FXML
    private Label statusLabel; // Achte darauf, dass diese Deklaration korrekt ist

    public ProfilController p = new ProfilController();
    /*@FXML
    private void initialize() {
        statusLabel.setText("Dashboard geladen.");
    }*/
    @FXML
    public void setUserText(String userText) {
        if (welcomeLabel != null) {
            welcomeLabel.setText("Willkommen, " + userText + "!");
        }
    }

    public void loadProfilView() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jmc/app/profil.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) profilButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            statusLabel.setText("Fehler beim Laden der Profilseite.");
        }

    }

}


