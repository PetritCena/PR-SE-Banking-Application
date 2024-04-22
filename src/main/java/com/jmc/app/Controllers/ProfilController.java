package com.jmc.app.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

import static com.jmc.app.Controllers.LoginController.password1;
import static com.jmc.app.Controllers.LoginController.email1;
import static javafx.scene.paint.Color.GREEN;

public class ProfilController {
    // Datenbankverbindungsparameter (an deine Datenbank anpassen)
    public static final String USER = "admin";
    public static final String PWD = "BigBankSoSe2024";
    public static final String CONNECT_STRING = "jdbc:oracle:thin:@e4xxmj5ey9kfqzz5_high?TNS_ADMIN=/Users/petritcena/Desktop/Wallet_E4XXMJ5EY9KFQZZ5";
    public Label statusLabel;

    @FXML
    public TextField vornameFeld;
    public Label statusLabel2;
    @FXML
    private Button speichernButton;
    @FXML
    private TextField nachnameFeld;
    @FXML
    private PasswordField altesPasswortFeld;
    @FXML
    private PasswordField neuesPasswortFeld;
    @FXML
    private PasswordField neuesPasswortBestätigungFeld;
    @FXML
    private Button startSeiteButton;
    @FXML
    private Button produktSeiteButton;
    @FXML
    private Button signoutButton;


    String vornameAlt;
    String nachnameAlt;

    @FXML
    private void initialize(){
        vornameFeld.setText(getVorname());
        nachnameFeld.setText(getNachname());
        vornameAlt = vornameFeld.getText();
        nachnameAlt = nachnameFeld.getText();
    }

    // Hilfsmethode um den Vornamen des Users zu bekommen
    private String getVorname(){
        final String SELECT_NAME = "SELECT vorname FROM users WHERE email = ?";
        try (Connection con = DriverManager.getConnection(CONNECT_STRING, USER, PWD); PreparedStatement stmt = con.prepareStatement(SELECT_NAME)) {
            stmt.setString(1, email1);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                     String retrievedVorname = rs.getString("vorname");
                     return retrievedVorname;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            System.out.println("Datenbankfehler: " + e.getMessage());
        }
        return "Hat nicht funktioniert";
    }

    // Hilfsmethode um den Nachnamen des Users zu bekommen
    private String getNachname(){
        final String SELECT_NAME = "SELECT nachname FROM users WHERE email = ?";
        try (Connection con = DriverManager.getConnection(CONNECT_STRING, USER, PWD); PreparedStatement stmt = con.prepareStatement(SELECT_NAME)) {
            stmt.setString(1, email1);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String retrievedNachname = rs.getString("nachname");
                    return retrievedNachname;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            System.out.println("Datenbankfehler: " + e.getMessage());
        }
        return "Hat nicht funktioniert";
    }

    // wenn man auf Button Speichern drückt, werden die User Daten mit dem neuen Input in der Datenbank geupdatet
    // beim ändern des Passworts, wird gecheckt, ob das alte Passwort mit dem Passwort von der Datenbank übereinstimmt
    // die Felder "neues Passwort" und "neues Passwort bestätigen" müssen gleich sein, um das Passwort zu ändern
    public void datenÄndern(ActionEvent event) {
        if (altesPasswortFeld.getText().equals(password1) && neuesPasswortFeld.getText().equals(neuesPasswortBestätigungFeld.getText())){

            final String UPDATE_PASSWORD = "UPDATE users SET password = ? WHERE email = ?";

            try (Connection con = DriverManager.getConnection(CONNECT_STRING, USER, PWD);
                 PreparedStatement stmt = con.prepareStatement(UPDATE_PASSWORD)) {

                stmt.setString(1, neuesPasswortFeld.getText());
                stmt.setString(2, email1);

                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    statusLabel.setTextFill(GREEN);
                    statusLabel.setText("Passwort erfolgreich geändert");
                }
                else {
                    System.out.println("Datenbank konnte nicht geändert werden");
                }
            } catch (SQLException e) {
                e.printStackTrace(System.err);
                //statusLabel.setText("Datenbankfehler: " + e.getMessage());
                System.out.println("Datenbankfehler: " + e.getMessage());
            }
        } else if (neuesPasswortFeld.getText().equals(neuesPasswortBestätigungFeld.getText())==false){
            statusLabel.setText("die Passwörter stimmen nicht überein");
        }
        else if (!altesPasswortFeld.getText().equals(password1) && altesPasswortFeld.getText().isEmpty() == false){
            statusLabel.setText("falsches Passwort");
        }

        //Vorname ändern
        if (vornameAlt.equals(vornameFeld.getText()) == false){
            final String UPDATE_VORNAME = "UPDATE users SET vorname = ? WHERE email = ?";

            try (Connection con = DriverManager.getConnection(CONNECT_STRING, USER, PWD);
                 PreparedStatement stmt = con.prepareStatement(UPDATE_VORNAME)) {

                stmt.setString(1, vornameFeld.getText());
                stmt.setString(2, email1);

                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    statusLabel2.setText("Name erfolgreich geändert");
                }
                else {
                    System.out.println("Datenbank konnte nicht geändert werden");
                }
            } catch (SQLException e) {
                e.printStackTrace(System.err);
                //statusLabel.setText("Datenbankfehler: " + e.getMessage());
                System.out.println("Datenbankfehler: " + e.getMessage());
            }
        }

        //Nachname ändern
        if (nachnameAlt.equals(nachnameFeld.getText()) == false){
            final String UPDATE_NACHNAME = "UPDATE users SET nachname = ? WHERE email = ?";

            try (Connection con = DriverManager.getConnection(CONNECT_STRING, USER, PWD);
                 PreparedStatement stmt = con.prepareStatement(UPDATE_NACHNAME)) {

                stmt.setString(1, nachnameFeld.getText());
                stmt.setString(2, email1);

                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    statusLabel2.setText("Name erfolgreich geändert");
                }
                else {
                    System.out.println("Datenbank konnte nicht geändert werden");
                }
            } catch (SQLException e) {
                e.printStackTrace(System.err);
                //statusLabel.setText("Datenbankfehler: " + e.getMessage());
                System.out.println("Datenbankfehler: " + e.getMessage());
            }
        }
    }

    //man drück Button Startseite und wird zur Startseite weitergeleitet
    public void startSeiteButtonOnAction(ActionEvent event) throws IOException {
        Parent fxmlLoader = FXMLLoader.load(getClass().getResource("/com/jmc/app/Dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader, 850, 750);
        Stage stage = (Stage) startSeiteButton.getScene().getWindow();
        stage.setTitle("Startseite");
        stage.setScene(scene);
        stage.show();
    }

    //man drück Button Signout und ausgeloggt und zum Login weitergeleitet
    public void signoutButtonOnAction(ActionEvent event) throws IOException {
        Parent fxmlLoader = FXMLLoader.load(getClass().getResource("/com/jmc/app/login.fxml"));
        Scene scene = new Scene(fxmlLoader, 520, 400);
        Stage stage = (Stage) signoutButton.getScene().getWindow();
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }
}