package com.jmc.app.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.sql.*;

public class LoginController {

    // Datenbankverbindungsparameter (an deine Datenbank anpassen)
    public static final String USER = "admin";
    public static final String PWD = "BigBankSoSe2024";
    public static final String CONNECT_STRING = "jdbc:oracle:thin:@e4xxmj5ey9kfqzz5_high?TNS_ADMIN=/Users/petritcena/Desktop/Wallet_E4XXMJ5EY9KFQZZ5";

    @FXML
    public Button loginButton;
    @FXML
    public AnchorPane mainPain;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label statusLabel;
    @FXML
    private Button noAccountButton;

    public static String password1; //Änderung
    public static String email1;   //Änderung

    // User Input wird gecheckt
    // also, ob alle Felder ausgefüllt wurden
    // es wird auch überprüft, ob die User Daten wirklich existieren
    // wenn, alles klappt wird man zur Startseite weitergeleitet
    public void handleLoginButtonAction(ActionEvent event) {
        if (emailField.getText().isEmpty() || passwordField.getText().isEmpty()){
            statusLabel.setText("Bitte E-Mail und Passwort eingeben!");
        }
        else {
            password1 = passwordField.getText(); //Änderung
            email1 = emailField.getText();       //Änderung
            if (userAuthenticated(emailField.getText(), passwordField.getText())) {
                loadDashboardView();
            }
            else {
                statusLabel.setText("Login fehlgeschlagen. Überprüfen Sie Ihre Eingaben.");
            }
        }
    }

    //Hilfsmethode, um zu checken, ob User Input in der Datenbank vorhanden sind
    private boolean userAuthenticated(String email, String password) {
        final String LOGIN_QUERY = "SELECT password FROM users WHERE email = ?";

        try (Connection con = DriverManager.getConnection(CONNECT_STRING, USER, PWD);
             PreparedStatement stmt = con.prepareStatement(LOGIN_QUERY)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String retrievedPassword = rs.getString("password");
                    // Vergleiche das Passwort
                    return password.equals(retrievedPassword);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            statusLabel.setText("Datenbankfehler: " + e.getMessage());
        }
        return false;
    }

    // Hilfsmethode, um Startseite zu laden
    private void loadDashboardView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jmc/app/Dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setTitle("Startseite");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(System.err);
            statusLabel.setText("Fehler beim Laden des Dashboards.");
        }
    }

    // wenn Text "Noch kein Konto? Hier registrieren" gedrückt wird, wird man zur SignUp Seite weitergeleitet
    public void noAccountButtonAction(ActionEvent event) throws IOException {
        Parent fxmlLoader = FXMLLoader.load(getClass().getResource("/com/jmc/app/signup.fxml"));
        Scene scene = new Scene(fxmlLoader, 520, 400);
        Stage stage = (Stage) noAccountButton.getScene().getWindow();
        stage.setTitle("Signup");
        stage.setScene(scene);
        stage.show();
    }
}
