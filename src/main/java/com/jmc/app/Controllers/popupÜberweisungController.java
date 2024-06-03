package com.jmc.app.Controllers;

import com.jmc.app.Models.Account;
import com.jmc.app.Models.DatabaseConnector;
import com.jmc.app.Models.User;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.SQLException;




public class popupÜberweisungController implements Controller{
    public Button überweisenButton;
    public TextField transferBetragFeld;
    public Label transferLabel;
    public TextField ibanFeld;
    public Label überweisungLabel;
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
        überweisungLabel.setText("Überweisung erfolgreich!");
    }
}


