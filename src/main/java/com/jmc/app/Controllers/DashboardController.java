package com.jmc.app.Controllers;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {
    @FXML
    private FontAwesomeIconView profilButton;
    @FXML
    private Label statusLabel;
    @FXML
    private Button startSeiteButton;
    @FXML
    private Button produktSeiteButton;

    // man drückt Account Icon und wird zur Profilseite weitergeleitet
    public void loadProfilView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jmc/app/profil.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) profilButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(System.err);

        }

    }
}


