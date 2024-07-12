package com.jmc.app.Controllers;

import com.jmc.app.Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Diese Klasse entspricht dem Controller für die TransaktionErfoglreich-Seite.
 */
public class TransactionOKcontroller implements Controller {
    @FXML
    private Button BackToProduct;
    @FXML
    private BorderPane borderPane;

    private  User user;

    /**
     * Diese Methode initialisiert die TransaktionErfoglreich-Seite.
     * @param user ist eine User-Instanz.
     * @param nulll wird hier nicht, benutzt, da keine Account-Instanz notwendig ist.
     */
    @Override
    public void initialize(Object user, Object nulll) {
        this.user = (User) user;
        SceneChanger.loadLeftFrame(borderPane, this.user);
    }

    /**
     * Diese Methode bringt den User zum Dashboard zurück.
     * @throws IOException wird geworfen, wenn SceneChanger.changeScene("/com/jmc/app/Dashboard.fxml", stage, user, null) einen Fehler zurückgibt.
     */
    public void BackToProduct() throws IOException {
        Stage stage = (Stage) BackToProduct.getScene().getWindow();
        SceneChanger.changeScene("/com/jmc/app/Dashboard.fxml", stage, user, null);
    }
}