package com.jmc.app.Controllers;

import com.jmc.app.Models.Account;
import com.jmc.app.Models.Card;
import com.jmc.app.Models.DatabaseConnector;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.sql.SQLException;

public class KartenController implements Controller{

    public BorderPane borderpane;
    public HBox kartennummerHbox;
    public HBox ibanHbox;
    public HBox inhaberHbox;
    public HBox kartenlimitHbox;
    public TextField kartenlimitFeld;
    public Button kartenlimitButton;
    Account account;
    Card card;
    DatabaseConnector db = new DatabaseConnector();
    @Override
    public void initialize(Object o, Object o2) {
        this.card = (Card) o;
        this.account = (Account) o2;
        SceneChanger.loadLeftFrame(borderpane, account.getUser());
        addCardData();
        kartenlimitFeld.setText(card.getKartenLimit()+"");
    }

    public void addCardData(){
        Label kartennnummer = new Label(card.getKartenNummer() + "");
        kartennummerHbox.getChildren().add(kartennnummer);
        Label iban = new Label(card.getIban() + "");
        ibanHbox.getChildren().add(iban);
        Label inhaber = new Label(account.getUser().getFirstName() + " " + account.getUser().getFirstName());
        inhaberHbox.getChildren().add(inhaber);
    }

    public void cardLimitButtonOnAction() throws SQLException {
        db.changeCardLimit(card, kartenlimitFeld.getText());
        kartenlimitFeld.setText(card.getKartenLimit()+"");
    }
}
