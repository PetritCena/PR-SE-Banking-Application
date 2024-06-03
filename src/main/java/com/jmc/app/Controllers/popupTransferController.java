package com.jmc.app.Controllers;

import com.jmc.app.Models.Account;
import com.jmc.app.Models.DatabaseConnector;
import com.jmc.app.Models.User;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class popupTransferController implements Controller{
    public Button zumHauptkontoButton;
    public Button vomHauptkontoButton;
    public TextField transferBetragFeld;
    public Label transferLabel;

    private User user;
    private Account account;




    @Override
    public void initialize(Object o, Object o2) {
        this.user = (User) o;
        this.account = (Account) o2;
    }

    public void handleZumHauptkonto(ActionEvent actionEvent) throws SQLException {
        DatabaseConnector db = new DatabaseConnector();
        float betrag = Float.parseFloat(transferBetragFeld.getText());
        db.transferHauptkonnto(user.getHauptkonto().getIban(), account.getIban(), betrag, "zum Hauptkonto");
        transferLabel.setText("Transfer erfolgreich!");
    }

    public void handleVomHauptkonto(ActionEvent actionEvent) throws SQLException {
        DatabaseConnector db = new DatabaseConnector();
        float betrag = Float.parseFloat(transferBetragFeld.getText());
        db.transferHauptkonnto(user.getHauptkonto().getIban(), account.getIban(), betrag, "vom Hauptkonto");
        transferLabel.setText("Transfer erfolgreich!");
    }
}
