package com.jmc.app.Controllers;

import com.jmc.app.Models.*;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.SnapshotParameters;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Diese Klasse entspricht dem Controller für die Dashboard-Seite.
 */
public class DashboardController implements Controller{
    @FXML
    private HBox hbox;
    @FXML
    private VBox hauptKontoBox;
    @FXML
    private Text nameHauptkonto, ibanHauptkonto, saldoHauptkonto, typ;
    @FXML
    private Circle photoCircle;
    @FXML
    private BorderPane borderPane;

    private User user;
    private ArrayList<Account> accounts;
    private ArrayList<Card> cards = new ArrayList<>();
    private Account hauptkonto = null;

    /**
     * Diese Methode initialisiert die Dashboard-Seite.
     * @param user ist eine User-Instanz.
     * @param nulll wird hier nicht benutzt, da eine Account-Instanz hier nicht notwendig ist.
     */
    @Override
    public void initialize(Object user, Object nulll) {
        this.user = (User) user;
        SceneChanger.loadLeftFrame(borderPane, this.user);
        accounts = this.user.getAccounts();
        for (Account account : accounts) {
            if (Objects.equals(account.getTyp(), "Hauptkonto")) {
                hauptkonto = account;
            }
            cards.addAll(account.getCards());
        }
        if (this.user.getPic() != null && this.user.getPic().length > 0) {
            Image image = new Image(new ByteArrayInputStream(this.user.getPic()));
            photoCircle.setFill(new ImagePattern(image));
        } else {
            displayInitialsInCircle();
        }
        typ.setText(hauptkonto.getTyp());
        nameHauptkonto.setText(this.user.getFirstName() + " " + this.user.getLastName());
        ibanHauptkonto.setText(hauptkonto.getIban());
        saldoHauptkonto.setText(hauptkonto.getSaldo() + "€");

        addBoxFromDatabase();
    }

    private String getInitials(String firstName, String lastName) {
        String initials = "";
        if (firstName != null && !firstName.isEmpty()) {
            initials += firstName.substring(0, 1).toUpperCase();
        }
        if (lastName != null && !lastName.isEmpty()) {
            initials += " " + lastName.substring(0, 1).toUpperCase();
        }
        return initials;
    }

    private void displayInitialsInCircle() {
        Text text = new Text(getInitials(user.getFirstName(), user.getLastName()));
        text.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        text.setFill(Color.rgb(53, 73, 90));

        Circle background = new Circle(photoCircle.getRadius(), Color.rgb(218, 236, 251));
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(background, text);
        stackPane.setMinSize(photoCircle.getRadius() * 2, photoCircle.getRadius() * 2);
        stackPane.setMaxSize(photoCircle.getRadius() * 2, photoCircle.getRadius() * 2);

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        Image image = stackPane.snapshot(parameters, null);

        photoCircle.setFill(new ImagePattern(image));
    }

    private void addBoxFromDatabase() {
        for (Account account : accounts) {
            if (Objects.equals(account.getTyp(), "Spacekonto")) {
                VBox spaceBox = new VBox();
                hbox.getChildren().add(spaceBox);
                Label spacekonto = new Label(account.getName());
                spacekonto.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

                String iban = account.getIban();
                float saldo = account.getSaldo();

                Label name = new Label("Name: " + user.getFirstName() + " " + user.getLastName());
                Label ibannummer = new Label("IBAN: " + iban);
                Label sal = new Label("Saldo: " + saldo + "€");

                spaceBox.getChildren().add(spacekonto);
                spaceBox.getChildren().add(name);
                spaceBox.getChildren().add(ibannummer);
                spaceBox.getChildren().add(sal);
                //design
                BackgroundFill backgroundFill = new BackgroundFill(Color.rgb(218, 236, 251), new CornerRadii(20), null);
                Background background = new Background(backgroundFill);
                spaceBox.setBackground(background);
                spaceBox.setMaxSize(250, 110);
                spaceBox.setMinSize(250, 110);
                spaceBox.setPadding(new Insets(17));
                spaceBox.setCursor(Cursor.OPEN_HAND);

                spaceBox.setOnMouseClicked(mouseEvent -> {
                    try {
                        accountsButtonOnAction(spaceBox, account, user);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
    }

    /**
     * Diese Methode führt den User zum Hauptkonto.
     * @throws IOException wird geworfen, wenn accountsButtonOnAction(hauptKontoBox, hauptkonto, user) einen Fehler zurückgibt.
     */
    public void hauptKontoBoxOnAction() throws IOException {
        accountsButtonOnAction(hauptKontoBox, hauptkonto, user);
    }

    /**
     * Diese Methode führt den User zum ausgewählten Konto.
     * @param box ist VBox, die das Konto repräsentiert.
     * @param account ist der ausgewählte Account mit seinen ganzen Informationen.
     * @param user ist der aktuelle User.
     * @throws IOException wird geworfen, wenn SceneChanger.changeScene("/com/jmc/app/spaceAccount.fxml", stage, account, user) einen Fehler zurückgibt.
     */
    public void accountsButtonOnAction(VBox box, Account account, User user) throws IOException {
        Stage stage = (Stage) box.getScene().getWindow();
        SceneChanger.changeScene("/com/jmc/app/spaceAccount.fxml", stage, account, user);
    }
}