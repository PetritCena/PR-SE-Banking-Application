package com.jmc.app.Controllers;

import com.jmc.app.Models.User;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Diese Klasse entspricht dem Controller für die Simulator-Seite.
 */
public class SimulatorController implements Controller {
    @FXML
    private VBox EinzahlungButton, KartenZahlungButton, AbhebungButton;
    @FXML
    private BorderPane borderPane;

    private User user;

    /**
     * Diese Methode initialisiert die Simulator-Seite.
     * @param user ist eine User-Instanz.
     * @param nulll wird hier nicht, benutzt, da keine Account-Instanz notwendig ist.
     */
    @Override
    public void initialize(Object user, Object nulll) {
        this.user = (User) user;
        SceneChanger.loadLeftFrame(borderPane, this.user);
    }

    /**
     * Diese Methode führt den User zur Einzahlungsseite.
     * @throws IOException wird geworfen, wenn SceneChanger.changeScene("/com/jmc/app/Einzahlung.fxml", stage, user, null) einen Fehler zurückgibt.
     */
    public void handleEinzahlung() throws IOException {
        Stage stage = (Stage) EinzahlungButton.getScene().getWindow();
        SceneChanger.changeScene("/com/jmc/app/Einzahlung.fxml", stage, user, null);
    }

    /**
     * Diese Methode führt den User zur Abhebungsseite.
     * @throws IOException wird geworfen, wenn SceneChanger.changeScene("/com/jmc/app/Abhebung.fxml", stage, user, null) einen Fehler zurückgibt.
     */
    public void HandleAbhebung() throws IOException {
        Stage stage = (Stage) AbhebungButton.getScene().getWindow();
        SceneChanger.changeScene("/com/jmc/app/Abhebung.fxml", stage, user, null);
    }

    /**
     * Diese Methode führt den User zur Kartenzahlungsseite.
     * @throws IOException wird geworfen, wenn SceneChanger.changeScene("/com/jmc/app/KartenZahlung.fxml", stage, user, null) einen Fehler zurückgibt.
     */
    public void HandleKartenZahlung() throws IOException {
        Stage stage = (Stage) KartenZahlungButton.getScene().getWindow();
        SceneChanger.changeScene("/com/jmc/app/KartenZahlung.fxml", stage, user, null);
    }
}

