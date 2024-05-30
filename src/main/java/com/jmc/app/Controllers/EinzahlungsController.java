package com.jmc.app.Controllers;

import com.jmc.app.Models.DatabaseConnector;
import com.jmc.app.Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;


public class EinzahlungsController implements Controller {

    public Text saldoHauptkonto;
    public Label validationMessage;
    @FXML
    public Button Einzahlen;
    @FXML
    private BorderPane borderPane;
    private User user;
    @FXML
    public Button Back;

    @FXML
    private TextField kartennummerField;

    @FXML
    private TextField folgenummerField;

    @FXML
    private PasswordField geheimzahlField;

    @FXML
    private TextField BetragFeld;

    @FXML
    public void initialize(Object user, Object nulll) {
        this.user = (User) user;
        SceneChanger.loadLeftFrame(borderPane, this.user);
    }

    public void Back(MouseEvent mouseEvent) throws IOException {
        Stage stage = (Stage) Back.getScene().getWindow();
        SceneChanger.changeScene("/com/jmc/app/Simulator.fxml", stage, user, user);
    }

    public void Transaction(MouseEvent mouseEvent) throws IOException, SQLException {
        if (validateCard()) {
            Stage stage = (Stage) Einzahlen.getScene().getWindow();
            SceneChanger.changeScene("/com/jmc/app/TransactionErfolgreich.fxml", stage, user, user);
        }
    }

    private boolean validateCard() throws SQLException {
        DatabaseConnector dbConnector = new DatabaseConnector();

        // Retrieve the values from the input fields
        String kartennummerText = kartennummerField.getText();
        String folgenummerText = folgenummerField.getText();
        String geheimzahlText = geheimzahlField.getText();
        String betragText = BetragFeld.getText();

        // Validate input fields
        if (kartennummerText.isEmpty() || folgenummerText.isEmpty() || geheimzahlText.isEmpty() || betragText.isEmpty()) {
            validationMessage.setText("All fields must be filled out.");
            validationMessage.setStyle("-fx-text-fill: red;");
            return false;
        }

        if (!kartennummerText.matches("\\d{16}")) {
            validationMessage.setText("Card number must be 16 digits.");
            validationMessage.setStyle("-fx-text-fill: red;");
            return false;
        }

        if (!folgenummerText.matches("\\d+")) {
            validationMessage.setText("Sequence number must be a number.");
            validationMessage.setStyle("-fx-text-fill: red;");
            return false;
        }

        if (!geheimzahlText.matches("\\d{4}")) {
            validationMessage.setText("PIN must be 4 digits.");
            validationMessage.setStyle("-fx-text-fill: red;");
            return false;
        }

        if (!betragText.matches("\\d+(\\.\\d{1,2})?")) {
            validationMessage.setText("Amount must be a valid number.");
            validationMessage.setStyle("-fx-text-fill: red;");
            return false;
        }

        long kartennummer = Long.parseLong(kartennummerText);
        int folgenummer = Integer.parseInt(folgenummerText);
        int geheimzahl = Integer.parseInt(geheimzahlText);
        float betrag = Float.parseFloat(betragText)*-1;

        // Validate the card data
        boolean isValid = dbConnector.isCardDataValid(kartennummer, folgenummer, geheimzahl);

        if (isValid) {
            dbConnector.updateAccountBalance(kartennummer, folgenummer, geheimzahl, betrag);

        } else {
            validationMessage.setText("Card data is invalid.");
            validationMessage.setStyle("-fx-text-fill: red;");
        }

        return isValid;
    }
}