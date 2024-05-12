package com.jmc.app.Controllers;

import com.jmc.app.Models.DatabaseConnector;
import com.jmc.app.Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.sql.*;

public class LoginController {
    @FXML
    public Button loginButton;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label statusLabel;
    public static String email;    // To hold user's email
    private static String password;

    // Handles the login process
    public void handleLoginButtonAction(ActionEvent event) throws SQLException, IOException {
        if (emailField.getText().isEmpty() || passwordField.getText().isEmpty()){
            statusLabel.setText("Bitte E-Mail und Passwort eingeben!");
        } else {
            password = passwordField.getText(); // Store password
            email = emailField.getText();       // Store email
            if (DatabaseConnector.authenticateUser(email, password)) {
                startSeiteButtonOnAction(event);
            } else {
                statusLabel.setText("Login fehlgeschlagen. Überprüfen Sie Ihre Eingaben.");
            }
        }
        Object[] userData = DatabaseConnector.getUserData(email);
        User user = new User((String) userData[0], (String) userData[1], email, password, (byte[]) userData[3]);
    }

    public void startSeiteButtonOnAction(ActionEvent event) throws IOException {
        SceneChanger.changeScene("/com/jmc/app/Dashboard.fxml", 850, 750, "Startseite", loginButton);
    }
    public void noAccountButtonAction(ActionEvent event) throws IOException {
        SceneChanger.changeScene("/com/jmc/app/signup.fxml", 850, 750, "Startseite", loginButton);
    }
}
