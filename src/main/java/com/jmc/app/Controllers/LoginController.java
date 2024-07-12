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

/**
 * Diese Klasse ist der Controller für das Login.
 */
public class LoginController{
    @FXML
    private Button loginButton, noAccountButton, forgotPasswordButton;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label statusLabel;

    private User user;

    /**
     * Diese Methode übernimmt den Login-Vorgang.
     * @throws SQLException wird geworfen, wenn dbConnector.authenticateUser(email, password) einen Fehler zurückgibt.
     * @throws IOException wird geworfen, wenn loadDashboardView() einen Fehler zurückgibt.
     */
    public void handleLoginButtonAction() throws SQLException, IOException {
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

    private void loadDashboardView() throws IOException {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        SceneChanger.changeScene("/com/jmc/app/Dashboard.fxml", stage, user, null);
    }

    /**
     * Diese Methode führt den User zur Signup-Seite.
     * @throws IOException wird geworfen, wenn SceneChanger.changeScene("/com/jmc/app/signup.fxml", stage, null, null) einen Fehler zurückgibt.
     */
    public void noAccountButtonAction() throws IOException {
        Stage stage = (Stage) noAccountButton.getScene().getWindow();
        SceneChanger.changeScene("/com/jmc/app/signup.fxml", stage, null, null);
    }

    /**
     * Diese Methode führt den User zur Signup-Seite.
     * @throws IOException wird geworfen, SceneChanger.changeScene("/com/jmc/app/forgotPassword.fxml", stage, null, null);
     */
    public void handleForgotPasswordAction() throws IOException {
        Stage stage = (Stage) forgotPasswordButton.getScene().getWindow();
        SceneChanger.changeScene("/com/jmc/app/forgotPassword.fxml", stage, null, null);
    }
}
