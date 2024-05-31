package com.jmc.app.Controllers;

import com.jmc.app.Models.Account;
import com.jmc.app.Models.Card;
import com.jmc.app.Models.DatabaseConnector;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.sql.SQLException;

public class KartenController implements Controller{
    @FXML
    private BorderPane borderpane;
    @FXML
    private HBox kartennummerHbox, ibanHbox, inhaberHbox, kartenlimitHbox, folgenummerHbox, geheimzahlHbox;
    @FXML
    private TextField kartenlimitFeld;
    @FXML
    private Button kartenlimitButton;
    @FXML
    private Label ibanLabel, nameLabel;

    private Account account;
    private Card card;
    private boolean verschleiert = true;


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
        DatabaseConnector db = new DatabaseConnector();
        db.changeCardLimit(card, kartenlimitFeld.getText());
        kartenlimitFeld.setText(card.getKartenLimit()+"");
    }
}
