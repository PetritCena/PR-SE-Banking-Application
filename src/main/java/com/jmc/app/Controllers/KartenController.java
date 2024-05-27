package com.jmc.app.Controllers;

import com.jmc.app.Models.Account;
import com.jmc.app.Models.Card;
import com.jmc.app.Models.DatabaseConnector;
import javafx.scene.Cursor;
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
    public HBox folgenummerHbox;
    public HBox geheimzahlHbox;
    public Label ibanLabel;
    public Label nameLabel;
    Account account;
    Card card;
    DatabaseConnector db = new DatabaseConnector();
    boolean verschleiert = true;
    @Override
    public void initialize(Object o, Object o2) {
        this.card = (Card) o;
        this.account = (Account) o2;
        SceneChanger.loadLeftFrame(borderpane, account.getUser());
        addCardData();
        kartenlimitFeld.setText(card.getKartenLimit()+"");
        ibanLabel.setText(card.getKartenNummer() + "");
        nameLabel.setText(account.getUser().getFirstName() + " " + account.getUser().getLastName());
    }

    public void addCardData(){
        Label kartennnummer = new Label(card.getKartenNummer() + "");
        kartennummerHbox.getChildren().add(kartennnummer);
        Label iban = new Label(card.getIban() + "");
        ibanHbox.getChildren().add(iban);
        Label inhaber = new Label(account.getUser().getFirstName() + " " + account.getUser().getFirstName());
        inhaberHbox.getChildren().add(inhaber);
        Label folgenummer = new Label(card.getFolgeNummer() + "");
        folgenummerHbox.getChildren().add(folgenummer);
        Label geheimzahl = new Label("****");
        geheimzahlHbox.getChildren().add(geheimzahl);
        geheimzahl.setCursor(Cursor.OPEN_HAND);
        geheimzahl.setOnMouseClicked(mouseEvent -> {
            showGeheimzahl(geheimzahl);
        });
    }

    public void showGeheimzahl(Label geheimzahl){
        if (verschleiert){
            geheimzahl.setText(card.getGeheimZahl() + "");
            verschleiert = false;
        }
        else {
            geheimzahl.setText("****");
            verschleiert = true;
        }
    }
    public void cardLimitButtonOnAction() throws SQLException {
        db.changeCardLimit(card, kartenlimitFeld.getText());
        kartenlimitFeld.setText(card.getKartenLimit()+"");
    }
}
