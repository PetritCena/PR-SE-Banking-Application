package com.jmc.app.Controllers;

import com.jmc.app.Models.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

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
    Account hauptkonto = null;

    @FXML
    public void initialize(Object user) {
        this.user = (User) user;
        SceneChanger.loadLeftFrame(borderPane, this.user);
        accounts = this.user.getAccounts();
        for(Account account : accounts) {
            if(Objects.equals(account.getTyp(), "Hauptkonto")){
                hauptkonto = account;
            }
            cards.addAll(account.getCards());
        }
        if (this.user.getPic() != null && this.user.getPic().length > 0) {
            Image image = new Image(new ByteArrayInputStream(this.user.getPic()));
            photoCircle.setFill(new ImagePattern(image));
        }
        else {
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
                Label spacekonto = new Label("Spacekonto");
                spacekonto.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

                String iban = account.getIban();
                float saldo = account.getSaldo();

                Label name = new Label("Name: " + user.getFirstName() + " " + user.getLastName());
                Label ibannummer = new Label("IBAN: " + iban);
                Label sal = new Label("Saldo: " + saldo + "€");

                //Account account = new Account(iban, saldo, "Space", user, null);

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
                        accountsButtonOnAction(spaceBox, account);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
        /*DatabaseConnector db = new DatabaseConnector();
        final String QUERY = "SELECT iban, saldo from accounts where USER_EMAIL = ? AND TYP = 'Spacekonto'";
        try (Connection con = db.getConnection(); PreparedStatement pst = con.prepareStatement(QUERY)){

             pst.setString(1, user.getEmail());
             ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                VBox spaceBox = new VBox();
                hbox.getChildren().add(spaceBox);
                Label spacekonto = new Label("Spacekonto");
                spacekonto.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

                String iban = rs.getString("iban");;
                float saldo = rs.getFloat("saldo");

                Label name = new Label("Name: " + user.getFirstName() + " " + user.getLastName());
                Label ibannummer = new Label("IBAN: " + iban);
                Label sal = new Label("Saldo: " + saldo + "€");

                Account account = new Account(iban, saldo, "Space", user, null);

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
                        accountsButtonOnAction(spaceBox, account);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
        catch (SQLException e) {
            e.printStackTrace(System.err);
        }*/
    }

    public void hauptKontoBoxOnAction(MouseEvent mouseEvent) throws IOException {
        accountsButtonOnAction(hauptKontoBox, hauptkonto);
    }

    public void accountsButtonOnAction(VBox box, Account account) throws IOException {
        Stage stage = (Stage) box.getScene().getWindow();
        SceneChanger.changeScene("/com/jmc/app/spaceAccount.fxml", stage, account);
    }
}