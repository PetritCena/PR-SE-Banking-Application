package com.jmc.app.Controllers;

import com.jmc.app.Models.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Diese Klasse entspricht dem Controller für die Abhebungsseite.
 */
public class AbhebungContoller implements Controller {
    @FXML
    private Button Abhebung, Back;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Label validationMessage;
    @FXML
    private TextField folgenummerField, BetragFeld;
    @FXML
    private PasswordField geheimzahlField;
    @FXML
    private ComboBox<String> kartenNummerAuswahl;

    private User user;
    private ArrayList<Account> accounts = new ArrayList<>();
    private ArrayList<Card> cards = new ArrayList<>();

    /**
     * Diese Methode initialisiert die Abhebungsseite.
     * @param user ist eine User-Instanz.
     * @param nulll wird hier nicht, benutzt, da keine Account-Instanz notwendig ist.
     */
    @Override
    public void initialize(Object user, Object nulll) {
        this.user = (User) user;
        accounts = this.user.getAccounts();
        for(Account account : accounts) {
            cards.addAll(account.getCards());
        }
        for (Card card : cards) {
            kartenNummerAuswahl.getItems().add(card.toString());
        }
        SceneChanger.loadLeftFrame(borderPane, this.user);
    }

    /**
     * Diese Methode führt die Transaktion durch und bringt den User zur TransaktionErfolgreich-Seite.
     * @throws IOException wird geworfen, wenn SceneChanger.changeScene("/com/jmc/app/TransactionErfolgreich.fxml", stage, user, null) einen Fehler zurückgibt.
     * @throws SQLException wird geworfen, wenn validateCard() einen Fehler zurückgibt.
     */
    public void Transaction() throws IOException, SQLException {
        if(validateCard()) {
            Stage stage = (Stage) Abhebung.getScene().getWindow();
            SceneChanger.changeScene("/com/jmc/app/TransactionErfolgreich.fxml", stage, user, null);
        }
    }

    /**
     * Diese Methode führt den User zurück zur Simulator-Seite.
     * @throws IOException wird geworfen, wenn SceneChanger.changeScene("/com/jmc/app/Simulator.fxml", stage, user, user) einen Fehler zurückgibt.
     */
    public void Back() throws IOException {
        Stage stage = (Stage) Back.getScene().getWindow();
        SceneChanger.changeScene("/com/jmc/app/Simulator.fxml", stage, user, null);
    }

    private boolean validateCard() throws SQLException {
        DatabaseConnector db = new DatabaseConnector();

        // Retrieve the values from the input fields
        String kartennummerText = kartenNummerAuswahl.getValue();
        String folgenummerText = folgenummerField.getText();
        String geheimzahlText = geheimzahlField.getText();
        String betragText = BetragFeld.getText();

        // Validate input fields
        if (kartennummerText.isEmpty() || folgenummerText.isEmpty() || geheimzahlText.isEmpty() || betragText.isEmpty()) {
            validationMessage.setText("All fields must be filled out.");
            validationMessage.setStyle("-fx-text-fill: red;");
            return false;
        }

        if (!kartennummerText.matches("\\d{16}")) {
            validationMessage.setText("Card number must be 16 digits.");
            validationMessage.setStyle("-fx-text-fill: red;");
            return false;
        }

        if (!folgenummerText.matches("\\d+")) {
            validationMessage.setText("Sequence number must be a number.");
            validationMessage.setStyle("-fx-text-fill: red;");
            return false;
        }

        if (!geheimzahlText.matches("\\d{4}")) {
            validationMessage.setText("PIN must be 4 digits.");
            validationMessage.setStyle("-fx-text-fill: red;");
            return false;
        }

        if (!betragText.matches("\\d+(\\.\\d{1,2})?")) {
            validationMessage.setText("Amount must be a valid number.");
            validationMessage.setStyle("-fx-text-fill: red;");
            return false;
        }

        long kartennummer = Long.parseLong(kartennummerText);
        int folgenummer = Integer.parseInt(folgenummerText);
        int geheimzahl = Integer.parseInt(geheimzahlText);
        float betrag = Float.parseFloat(betragText);
        String iban = "";
        for (Card card : cards) {
            if(card.getKartenNummer() == kartennummer) {
                iban = card.getIban();
            }
        }

        // Validate the card data
        boolean isValid = db.isCardDataValid(kartennummer, folgenummer, geheimzahl);

        if (isValid) {
            db.updateBalance(iban, betrag, kartennummer, "Ausgang", "Abhebung");
            for(Account account : accounts) {
                if(iban.equals(account.getIban())) {
                    account.setSaldo(account.getSaldo() - betrag);
                    account.addTransaction(new Transaction(betrag, "Ausgang", null, account.getIban(), "Abhebung", 0, kartennummer));
                }
            }

        } else {
            validationMessage.setText("Card data is invalid.");
            validationMessage.setStyle("-fx-text-fill: red;");
        }

        return isValid;
    }
}