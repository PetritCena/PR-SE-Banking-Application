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

public class popupTransferController implements Controller{
    @FXML private Button zumHauptkontoButton, vomHauptkontoButton;
    @FXML
    private TextField transferBetragFeld;
    @FXML
    private Label transferLabel;

    private User user;
    private Account account;

    //private int transaktionsNummerSpace = 1;
    //private int transaktionsNummerHauptkonto = 1;

    @Override
    public void initialize(Object o, Object o2) {
        this.user = (User) o;
        this.account = (Account) o2;
    }

    public void handleZumHauptkonto(ActionEvent actionEvent) throws SQLException {
        DatabaseConnector db = new DatabaseConnector();
        float betrag = Float.parseFloat(transferBetragFeld.getText());
        db.transferHauptkonnto(user.getHauptkonto().getIban(), account.getIban(), betrag, "zum Hauptkonto");
        account.setSaldo(account.getSaldo() - betrag);
        user.getHauptkonto().setSaldo(user.getHauptkonto().getSaldo() + betrag);
        /*if(account.getTransactions() != null && user.getHauptkonto().getTransactions() != null){
            transaktionsNummerSpace = account.getTransactions().getLast().getTransaktionsnummer() + 1;
            transaktionsNummerHauptkonto = user.getHauptkonto().getTransactions().getLast().getTransaktionsnummer() + 1;
        }*/
        account.addTransaction(new Transaction(betrag, "Ausgang", user.getHauptkonto().getIban(), account.getIban(), "Transfer", 0, 0));
        user.getHauptkonto().addTransaction(new Transaction(betrag, "Eingang", user.getHauptkonto().getIban(), account.getIban(), "Transfer", 0, 0));
        transferLabel.setText("Transfer erfolgreich!");
    }

    public void handleVomHauptkonto(ActionEvent actionEvent) throws SQLException {
        DatabaseConnector db = new DatabaseConnector();
        float betrag = Float.parseFloat(transferBetragFeld.getText());
        db.transferHauptkonnto(user.getHauptkonto().getIban(), account.getIban(), betrag, "vom Hauptkonto");
        account.setSaldo(account.getSaldo() + betrag);
        user.getHauptkonto().setSaldo(user.getHauptkonto().getSaldo() - betrag);
        /*if(account.getTransactions() != null && user.getHauptkonto().getTransactions() != null){
            transaktionsNummerSpace = account.getTransactions().getLast().getTransaktionsnummer() + 1;
            transaktionsNummerHauptkonto = user.getHauptkonto().getTransactions().getLast().getTransaktionsnummer() + 1;
        }*/
        account.addTransaction(new Transaction(betrag, "Eingang", account.getIban(), user.getHauptkonto().getIban(), "Transfer", 0, 0));
        user.getHauptkonto().addTransaction(new Transaction(betrag, "Ausgang", account.getIban() , user.getHauptkonto().getIban(), "Transfer", 0, 0));
        transferLabel.setText("Transfer erfolgreich!");
    }
}
