package com.jmc.app.Controllers;

import com.jmc.app.Models.Account;
import com.jmc.app.Models.DatabaseConnector;
import com.jmc.app.Models.Transaction;
import com.jmc.app.Models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class popupÜberweisungController implements Controller{
    @FXML
    private Button überweisenButton;
    @FXML
    private TextField transferBetragFeld, ibanFeld;
    @FXML
    private Label transferLabel, überweisungLabel;

    private User user;
    private Account account;

    @Override
    public void initialize(Object o, Object o2) {
        this.user = (User) o;
        this.account = (Account) o2;
    }

    public void handleÜberweisung(ActionEvent actionEvent) throws SQLException {
        DatabaseConnector db = new DatabaseConnector();
        float betrag = Float.parseFloat(transferBetragFeld.getText());
        db.überweisen(account.getIban(), ibanFeld.getText(), betrag);
        account.setSaldo(account.getSaldo() - betrag);
        /*int transaktionsNummer = 1;
        if(account.getTransactions() != null){
            transaktionsNummer = account.getTransactions().getLast().getTransaktionsnummer() + 1;
        }*/
        account.addTransaction(new Transaction(betrag, "Ausgang", ibanFeld.getText(), account.getIban(), "Überweisung", 0, 0));
        überweisungLabel.setText("Überweisung erfolgreich!");
    }
}


