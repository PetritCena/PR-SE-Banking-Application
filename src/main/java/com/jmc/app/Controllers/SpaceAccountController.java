package com.jmc.app.Controllers;

import com.jmc.app.Models.Account;
import com.jmc.app.Models.Card;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.util.ArrayList;

public class SpaceAccountController implements Controller {
    @FXML
    private Label ibanLabel, saldoLabel, typLabel;
    @FXML
    private BorderPane borderPane;
    @FXML
    private HBox hbox;

    private Account account;
    private ArrayList<Card> cards = null;

    @Override
    public void initialize(Object o) {
        this.account = (Account) o;
        cards = account.getCards();
        SceneChanger.loadLeftFrame(borderPane, account.getUser());
        ibanLabel.setText(account.getIban());
        saldoLabel.setText(account.getSaldo() + "€");
        typLabel.setText(account.getTyp());
        loadCard();
    }

    private void loadCard(){
        if(cards == null) return;
        VBox vbox = new VBox();
        vbox.setStyle("-fx-background-radius: 20");
        vbox.setPrefSize(250, 110);
        vbox.setPadding(new Insets(17));
        vbox.setBackground(new Background(new BackgroundImage(
                new Image(getClass().getResource("/com/jmc/app/MasterCard_blue.jpeg").toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, true))));
        vbox.setCursor(Cursor.OPEN_HAND);
        hbox.getChildren().add(vbox);
    }
}
