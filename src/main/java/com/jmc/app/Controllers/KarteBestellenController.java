package com.jmc.app.Controllers;

import com.jmc.app.Models.Account;
import com.jmc.app.Models.DatabaseConnector;
import com.jmc.app.Models.Transaction;
import com.jmc.app.Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Diese Klasse entspricht dem Controller für die Kartenkauf-Seite.
 */
public class KarteBestellenController implements Controller{
    @FXML
    private Button jaButton, neinButton;
    @FXML
    private ComboBox<String> accountComboBox;
    @FXML
    private BorderPane borderPane;

    private User user;

    /**
     * Diese Methode initialisiert die Kartenkauf-Seite.
     * @param user ist eine User-Instanz.
     * @param nulll wird hier nicht benutzt, da eine Account-Instanz hier nicht notwendig ist..
     */
    @FXML
    public void initialize(Object user, Object nulll) {
        this.user = (User) user;
        for (Account account : this.user.getAccounts()) {
            accountComboBox.getItems().add(account.toString());
        }
        SceneChanger.loadLeftFrame(borderPane, this.user);
    }

    /**
     * Diese Methode bringt den User zur Produktseite zurück, wenn er keinen Kartenkauf tätigen möchte.
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
        karteKaufen();
        Stage stage = (Stage) jaButton.getScene().getWindow();
        SceneChanger.changeScene("/com/jmc/app/Dashboard.fxml", stage, this.user, null);
    }

    private void karteKaufen() throws SQLException{
        DatabaseConnector dbConnector = new DatabaseConnector();
        String iban = accountComboBox.getValue();
        Account a = null;
        for (Account account : user.getAccounts()) {
            if (iban.equals(account.getIban())) { a = account; }
        }
        dbConnector.updateBalance(a.getIban(), 10, null, "Ausgang", "Kartenkauf");
        a.addTransaction(new Transaction(10, "Ausgang", null, a.getIban(), "Kartenkauf", 0, 0));
        a.setSaldo(a.getSaldo() - 10);
        dbConnector.karteBestellen(iban, 400, "Debit", a);
    }
}
