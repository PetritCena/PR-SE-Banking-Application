package com.jmc.app.Controllers;

import com.jmc.app.Models.*;
import javafx.geometry.Insets;
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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Objects;

public class DashboardController implements Controller{
    @FXML
    public ScrollPane scrollpane;
    @FXML
    public HBox hbox;
    @FXML
    private Text nameHauptkonto, ibanHauptkonto, saldoHauptkonto, typ;
    @FXML
    private Circle photoCircle;
    @FXML
    private Button startSeiteButton;
    private User user;
    private ArrayList<Account> accounts;
    private ArrayList<Card> cards = new ArrayList<>();

    @FXML
    public void initialize(User user) {
        this.user = user;
        accounts = user.getAccounts();
        Account hauptkonto = null;
        for(Account account : accounts) {
            if(Objects.equals(account.getTyp(), "Hauptkonto")){
                hauptkonto = account;
            }
            cards.addAll(account.getCards());
        }
        if (user.getPic() != null && user.getPic().length > 0) {
            Image image = new Image(new ByteArrayInputStream(user.getPic()));
            photoCircle.setFill(new ImagePattern(image));
        }
        else {
            displayInitialsInCircle();
        }
        typ.setText(hauptkonto.getTyp());
        nameHauptkonto.setText(user.getFirstName() + " " + user.getLastName());
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
    // man drückt Account Icon und wird zur Profilseite weitergeleitet
    public void loadProfilView() throws IOException {
        SceneChanger.changeScene("/com/jmc/app/profil.fxml", 850, 750, startSeiteButton, user);
    }
    public void produktButtonOnAction(MouseEvent mouseEvent) throws IOException {
        SceneChanger.changeScene("/com/jmc/app/Produktseite.fxml", 850, 750, startSeiteButton, user);
    }

    public void addBoxFromDatabase() {
        DatabaseConnector db = new DatabaseConnector();
        final String QUERY = "SELECT iban, saldo from accounts where USER_EMAIL = ? AND TYP = 'Spacekonto'";
        try (Connection con = db.getConnection(); PreparedStatement pst = con.prepareStatement(QUERY)){

             pst.setString(1, user.getEmail());
             ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                VBox spaceBox = new VBox();
                hbox.getChildren().add(spaceBox);
                Label spacekonto = new Label("Spacekonto");
                spacekonto.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
                Label name = new Label("Name: " + user.getFirstName() + " " + user.getLastName());
                Label ibannummer = new Label("IBAN: " + rs.getString("iban"));
                Label sal = new Label("Saldo: " + rs.getFloat("saldo") + "€");

                spaceBox.getChildren().add(spacekonto);
                spaceBox.getChildren().add(name);
                spaceBox.getChildren().add(ibannummer);
                spaceBox.getChildren().add(sal);
                //design
                BackgroundFill backgroundFill = new BackgroundFill(Color.rgb(218,	236,	251), new CornerRadii(20), null);
                Background background = new Background(backgroundFill);
                spaceBox.setBackground(background);
                spaceBox.setMaxSize(250, 110);
                spaceBox.setMinSize(250, 110);
                spaceBox.setPadding(new Insets(17));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}