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
 * Diese Klasse entspricht dem Controller für den Transfersdialog.
 */
public class popupTransferController implements Controller{
    @FXML
    private TextField transferBetragFeld;
    @FXML
    private Label transferLabel;

    private User user;
    private Account account;
    private SpaceAccountController cntrl;

    /**
     * Diese Methode initialisiert den Transfersdialog.
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
     * weil das Saldo und die Transaktionen sich gleich ändern sollen, wenn der Transfer getätigt wird.
     * @param cntrl ist eine SpaceAccountController-Instanz.
     */
    public void setSpaceController(SpaceAccountController cntrl){
        this.cntrl = cntrl;
    }

    /**
     * Diese Methode führt einen Transfer, der vom Hauptkonto ausgehend zum betroffenen Spacekonto geht, aus.
     * @throws SQLException wird geworfen, wenn DatabaseConnector db = new DatabaseConnector() oder
     * db.transferHauptkonto(user.getHauptkonto().getIban(), account.getIban(), betrag, "zum Hauptkonto") einen Fehler zurückgibt.
     */
    public void handleZumHauptkonto() throws SQLException {
        DatabaseConnector db = new DatabaseConnector();
        float betrag = Float.parseFloat(transferBetragFeld.getText());
        db.transferHauptkonto(user.getHauptkonto().getIban(), account.getIban(), betrag, "zum Hauptkonto");
        account.setSaldo(account.getSaldo() - betrag);
        user.getHauptkonto().setSaldo(user.getHauptkonto().getSaldo() + betrag);
        account.addTransaction(new Transaction(betrag, "Ausgang", user.getHauptkonto().getIban(), account.getIban(), "Transfer", 0, 0));
        user.getHauptkonto().addTransaction(new Transaction(betrag, "Eingang", user.getHauptkonto().getIban(), account.getIban(), "Transfer", 0, 0));
        transferLabel.setText("Transfer erfolgreich!");
        cntrl.fillListView(account.getTransactions());
        cntrl.saldoLabel.setText(account.getSaldo()+ "€");
    }

    /**
     * Diese Methode führt einen Transfer, der vom betroffenen Spacekonto ausgehend zum Hauptkonto geht, aus.
     * @throws SQLException wird geworfen, wenn DatabaseConnector db = new DatabaseConnector() oder
     * db.transferHauptkonto(user.getHauptkonto().getIban(), account.getIban(), betrag, "vom Hauptkonto") einen Fehler zurückgibt.
     */
    public void handleVomHauptkonto() throws SQLException {
        DatabaseConnector db = new DatabaseConnector();
        float betrag = Float.parseFloat(transferBetragFeld.getText());
        db.transferHauptkonto(user.getHauptkonto().getIban(), account.getIban(), betrag, "vom Hauptkonto");
        account.setSaldo(account.getSaldo() + betrag);
        user.getHauptkonto().setSaldo(user.getHauptkonto().getSaldo() - betrag);
        account.addTransaction(new Transaction(betrag, "Eingang", account.getIban(), user.getHauptkonto().getIban(), "Transfer", 0, 0));
        user.getHauptkonto().addTransaction(new Transaction(betrag, "Ausgang", account.getIban() , user.getHauptkonto().getIban(), "Transfer", 0, 0));
        transferLabel.setText("Transfer erfolgreich!");
        cntrl.fillListView(account.getTransactions());
        cntrl.saldoLabel.setText(account.getSaldo()+ "€");
    }
}
