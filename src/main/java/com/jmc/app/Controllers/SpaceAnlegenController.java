package com.jmc.app.Controllers;

import com.jmc.app.Models.Account;
import com.jmc.app.Models.DatabaseConnector;
import com.jmc.app.Models.Transaction;
import com.jmc.app.Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Diese Klasse entspricht dem Controller für die Spacekauf-Seite.
 */
public class SpaceAnlegenController implements Controller{
    @FXML
    private Button jaButton, neinButton;
    @FXML
    private BorderPane borderPane;
    @FXML
    private TextField nameSpace;

    private User user;

    /**
     * Diese Methode initialisiert die Spacekauf-Seite.
     * @param user ist eine User-Instanz.
     * @param nulll wird hier nicht benutzt, da eine Account-Instanz hier nicht notwendig ist.
     */
    @FXML
    public void initialize(Object user, Object nulll) {
        this.user = (User) user;
        SceneChanger.loadLeftFrame(borderPane, this.user);
    }

    /**
     * Diese Methode bringt den User zur Produktseite zurück, wenn er keinen Spacekauf tätigen möchte.
     * @throws IOException wird geworfen, wenn SceneChanger.changeScene("/com/jmc/app/Produktseite.fxml", stage, this.user, null) einen Fehler zurückgibt.
     */
    public void neinButtonOnAction() throws IOException {
        Stage stage = (Stage) neinButton.getScene().getWindow();
        SceneChanger.changeScene("/com/jmc/app/Produktseite.fxml", stage, this.user, null);
    }

    /**
     * Diese Methode bringt den User zum Dashboard zurück, wenn er einen Spacekauf getätigt hat.
     * @throws IOException wird geworfen, wenn SceneChanger.changeScene("/com/jmc/app/Dashboard.fxml", stage, this.user, null) einen Fehler zurückgibt.
     */
    public void jaButtonOnAction() throws IOException, SQLException {
        spaceKaufen();
        Stage stage = (Stage) jaButton.getScene().getWindow();
        SceneChanger.changeScene("/com/jmc/app/Dashboard.fxml", stage, this.user, null);
    }

    private void spaceKaufen() throws SQLException{
        DatabaseConnector dbConnector = new DatabaseConnector();
        Account hauptAccount = user.getHauptkonto();
        dbConnector.updateBalance(hauptAccount.getIban(), 10, null, "Ausgang", "Spacekauf");
        hauptAccount.addTransaction(new Transaction(10, "Ausgang", null, hauptAccount.getIban(), "Spacekauf", 0, 0));
        dbConnector.createSpace(0, "Spacekonto", user, nameSpace.getText());
    }
}
