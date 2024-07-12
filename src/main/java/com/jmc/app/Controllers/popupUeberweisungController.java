package com.jmc.app.Controllers;

import com.jmc.app.Models.Account;
import com.jmc.app.Models.DatabaseConnector;
import com.jmc.app.Models.Transaction;
import com.jmc.app.Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.SQLException;

/**
 * Diese Klasse entspricht dem Controller für den Überweisungsdialog.
 */
public class popupUeberweisungController implements Controller{
    @FXML
    private TextField transferBetragFeld, ibanFeld;
    @FXML
    private Label überweisungLabel;

    private User user;
    private Account account;
    private SpaceAccountController cntrl;

    /**
     * Diese Methode initialisiert den Überweisungsdialog.
     * @param o ist eine User-Instanz.
     * @param o2 ist eine Account-Instanz.
     */
    @Override
    public void initialize(Object o, Object o2) {
        this.user = (User) o;
        this.account = (Account) o2;
    }

    /**
     * Diese Methode übernimmt den mitgegebenen cntrl für den lokalen cntrl. Der SpaceAccountController wird deswegen mitgegeben,
     * weil das Saldo und die Transaktionen sich gleich ändern sollen, wenn die Überweisung getätigt wird.
     * @param cntrl ist eine SpaceAccountController-Instanz.
     */
    public void setSpaceController(SpaceAccountController cntrl){
        this.cntrl = cntrl;
    }

    /**
     * Diese Methode führt die Überweisung durch.
     * @throws SQLException wird geowrfen, wenn DatabaseConnector db = new DatabaseConnector() oder db.überweisen(account.getIban(), ibanFeld.getText(), betrag) einen Fehler zurückgibt.
     */
    public void handleÜberweisung() throws SQLException {
        DatabaseConnector db = new DatabaseConnector();
        float betrag = Float.parseFloat(transferBetragFeld.getText());
        db.überweisen(account.getIban(), ibanFeld.getText(), betrag);
        account.setSaldo(account.getSaldo() - betrag);
        account.addTransaction(new Transaction(betrag, "Ausgang", ibanFeld.getText(), account.getIban(), "Überweisung", 0, 0));
        Account receiver = null;
        for(Account a : user.getAccounts()) {
            if(a.getIban().equals(ibanFeld.getText())){
                receiver = a;
            }
        }
        receiver.addTransaction(new Transaction(betrag, "Eingang", ibanFeld.getText(), account.getIban(), "Überweisung", 0, 0));
        receiver.setSaldo(receiver.getSaldo() + betrag);
        überweisungLabel.setText("Überweisung erfolgreich!");
        cntrl.fillListView(account.getTransactions());
        cntrl.saldoLabel.setText(account.getSaldo()+ "€");
    }
}


