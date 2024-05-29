package com.jmc.app.Controllers;

import com.jmc.app.Models.Account;
import com.jmc.app.Models.Card;
import com.jmc.app.Models.User;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
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

    private User user;


    @Override
    public void initialize(Object o, Object o2) {
        this.account = (Account) o;
        this.user = (User) o2;
        cards = account.getCards();
        SceneChanger.loadLeftFrame(borderPane, account.getUser());
        ibanLabel.setText(account.getIban());
        saldoLabel.setText(account.getSaldo() + "€");
        typLabel.setText(account.getTyp());
        loadCard();
    }


//C2AA10FF  3F3F3F  404F1BFF    #003366  #C0C0C0  #D3D3D3  #B0E0E6

    private void loadCard(){
        if(cards == null) return;
        for (Card card : cards){
            //Kreditkarte
            VBox vboxKarte = new VBox();
            hbox.getChildren().add(vboxKarte);
            vboxKarte.setMinSize(183, 117);
            vboxKarte.setMaxSize(183, 117);
            BackgroundFill backgroundFill = new BackgroundFill(Color.rgb(63,63,63), new CornerRadii(8), null);
            Background background = new Background(backgroundFill);
            vboxKarte.setBackground(background);
            vboxKarte.setCursor(Cursor.OPEN_HAND);

            //VISA Zeichen
            Text visa = new Text("VISA");
            visa.setFill(Color.WHITE);
            visa.setFont(Font.font("System", FontWeight.BOLD, FontPosture.ITALIC, 16));
            vboxKarte.getChildren().add(visa);
            VBox.setMargin(visa, new Insets(10,0,0,10));

            //imageview vom Chip
            Image chip = new Image(getClass().getResource("/com/jmc/app/iconsChipcard.png").toExternalForm());
            ImageView imageView = new ImageView(chip);
            imageView.setFitHeight(33);
            imageView.setFitWidth(38);
            Image nfc = new Image(getClass().getResource("/com/jmc/app/iconsNfc.png").toExternalForm());
            ImageView imageView2 = new ImageView(nfc);
            imageView2.setFitHeight(21);
            imageView2.setFitWidth(22);
            HBox hBox = new HBox(imageView, imageView2);
            vboxKarte.getChildren().add(hBox);
            HBox.setMargin(imageView, new Insets(3,0,0,7));
            HBox.setMargin(imageView2, new Insets(9,0,0,-4));

            //Kartennummer
            //card.getKartenNummer() = card.getKartenNummer() * 10000000000000000;
            Text kartennummerText = new Text(card.getKartenNummer()+""); //nicht IBAN sonder Kartennummer!!!
            kartennummerText.setFill(Color.WHITE);
            kartennummerText.setFont(Font.font( 14));
            vboxKarte.getChildren().add(kartennummerText);
            VBox.setMargin(kartennummerText, new Insets(0,0,0,30));

            //Name
            Text name = new Text(user.getFirstName().toUpperCase() + " " + user.getLastName().toUpperCase()); //user holen
            name.setFill(Color.WHITE);
            name.setFont(Font.font( 10));
            vboxKarte.getChildren().add(name);
            VBox.setMargin(name, new Insets(7,0,0,30));

            vboxKarte.setOnMouseClicked(event -> {
                Stage stage = (Stage) vboxKarte.getScene().getWindow();
                try {
                    SceneChanger.changeScene("/com/jmc/app/karte.fxml", stage, card, account);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
