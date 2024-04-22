package com.jmc.app.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
//import java.awt.event.ActionEvent;
import java.sql.*;

import static com.jmc.app.Controllers.LoginController.password1;
import static com.jmc.app.Controllers.LoginController.email1;
import static javafx.scene.paint.Color.GREEN;

public class ProfilController {
    // Datenbankverbindungsparameter (an deine Datenbank anpassen)
    public static final String USER = "admin";
    public static final String PWD = "BigBankSoSe2024";
    public static final String CONNECT_STRING = "jdbc:oracle:thin:@e4xxmj5ey9kfqzz5_high?TNS_ADMIN=/Users/oemer.t/Downloads/Wallet_E4XXMJ5EY9KFQZZ5";
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

    String vornameAlt;
    String nachnameAlt;

    @FXML
    private void initialize(){
        vornameFeld.setText(getVorname());
        nachnameFeld.setText(getNachname());
        vornameAlt = vornameFeld.getText();
        nachnameAlt = nachnameFeld.getText();
    }

    public String getVorname(){
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
            e.printStackTrace();
            System.out.println("Datenbankfehler: " + e.getMessage());
        }
        return "Hat nicht funktioniert";
    }

    public String getNachname(){
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
            e.printStackTrace();
            System.out.println("Datenbankfehler: " + e.getMessage());
        }
        return "Hat nicht funktioniert";
    }



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
                e.printStackTrace();
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
                e.printStackTrace();
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
                e.printStackTrace();
                //statusLabel.setText("Datenbankfehler: " + e.getMessage());
                System.out.println("Datenbankfehler: " + e.getMessage());
            }
        }


    }



}



//else if (altesPasswortFeld.getText().isEmpty() || neuesPasswortFeld.getText().isEmpty() || neuesPasswortBestätigungFeld.getText().isEmpty()) {
//            statusLabel.setText("Feld ist Leer!");