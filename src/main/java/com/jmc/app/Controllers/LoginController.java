package com.jmc.app.Controllers;

import com.jmc.app.Models.DatabaseConnector;
import com.jmc.app.Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class LoginController{
    @FXML
    public Button loginButton, noAccountButton;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label statusLabel;
    private User user;

    // Handles the login process
    public void handleLoginButtonAction(ActionEvent event) throws IOException {
        if (emailField.getText().isEmpty() || passwordField.getText().isEmpty()){
            statusLabel.setText("Bitte E-Mail und Passwort eingeben!");
        } else {
            String password = passwordField.getText(); // Store password
            String email = emailField.getText(); // Store email

            DatabaseConnector dbConnector = new DatabaseConnector();
            user = dbConnector.authenticateUser(email, password);
            if (user != null) {
                loadDashboardView();
            } else {
                statusLabel.setText("Login fehlgeschlagen. Überprüfen Sie Ihre Eingaben.");
            }
        }
    }

    // Helper method to load the Dashboard view
    private void loadDashboardView() throws IOException {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        SceneChanger.changeScene("/com/jmc/app/Dashboard.fxml", stage, user, null);
    }

    // Redirect to the SignUp page
    public void noAccountButtonAction(ActionEvent event) throws IOException {
        Stage stage = (Stage) noAccountButton.getScene().getWindow();
        SceneChanger.changeScene("/com/jmc/app/signup.fxml", stage, null, null);
    }
}
