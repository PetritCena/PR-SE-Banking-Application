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
    private TextField vornameFeld;
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

    LoginController l = new LoginController();
    public void passwortÄndern(ActionEvent event) {

        if (altesPasswortFeld.getText().equals(password1) && neuesPasswortFeld.getText().equals(neuesPasswortBestätigungFeld.getText())){

            final String UPDATE_STATEMENT = "UPDATE users SET password = ? WHERE email = ?";

            try (Connection con = DriverManager.getConnection(CONNECT_STRING, USER, PWD);
                 PreparedStatement stmt = con.prepareStatement(UPDATE_STATEMENT)) {

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
        } else if (altesPasswortFeld.getText().isEmpty() || neuesPasswortFeld.getText().isEmpty() || neuesPasswortBestätigungFeld.getText().isEmpty()) {
            statusLabel.setText("Feld ist Leer!");
        } else if (neuesPasswortFeld.getText().equals(neuesPasswortBestätigungFeld.getText())==false){
            statusLabel.setText("die Passwörter stimmen nicht überein");
        }
        else {
            statusLabel.setText("falsches Passwort");
        }
    }



}



