package com.jmc.app.Controllers;

import com.jmc.app.Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Diese Klasse entspricht dem Controller für die Produktseite.
 */
public class ProduktseiteController implements Controller{
    @FXML
    private Button spaceButton, karteButton;
    @FXML
    private BorderPane borderPane;

    private User user;

    /**
     * Diese Seite initialisiert die Produktseite.
     * @param user ist eine User-Instanz.
     * @param nulll wird hier nicht benutzt, da eine Account-Instanz hier nicht notwendig ist.
     */
    @FXML
    public void initialize(Object user, Object nulll) {
        this.user = (User) user;
        SceneChanger.loadLeftFrame(borderPane, this.user);
    }

    /**
     * Diese Methode führt den User zur Spacekauf-Seite.
     * @throws IOException wird geworfen, wenn SceneChanger.changeScene("/com/jmc/app/spaceAnlegen.fxml", stage, user, null) einen Fehler zurückgibt.
     */
    public void spaceOnAction() throws IOException{
        Stage stage = (Stage) spaceButton.getScene().getWindow();
        SceneChanger.changeScene("/com/jmc/app/spaceAnlegen.fxml", stage, user, null);
    }

    /**
     * Diese Methode führt den User zur Kartenkauf-Seite.
     * @throws IOException wird geworfen, wenn SceneChanger.changeScene("/com/jmc/app/karteBestellen.fxml", stage, user, null).
     */
    public void karteOnAction() throws IOException{
        Stage stage = (Stage) karteButton.getScene().getWindow();
        SceneChanger.changeScene("/com/jmc/app/karteBestellen.fxml", stage, user, null);
    }
}
