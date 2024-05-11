package com.jmc.app.Controllers;

import com.jmc.app.Models.DatabaseConnector;
import com.jmc.app.Models.User;
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

    public static String email;    // To hold user's email
    private static String password;

    // Handles the login process
    public void handleLoginButtonAction(ActionEvent event) throws SQLException {
        if (emailField.getText().isEmpty() || passwordField.getText().isEmpty()){
            statusLabel.setText("Bitte E-Mail und Passwort eingeben!");
        } else {
            password = passwordField.getText(); // Store password
            email = emailField.getText();       // Store email
            if (DatabaseConnector.authenticateUser(email, password)) {
                loadDashboardView();
            } else {
                statusLabel.setText("Login fehlgeschlagen. Überprüfen Sie Ihre Eingaben.");
            }
        }
        Object[] userData = DatabaseConnector.getUserData(email);
        User user = new User((String) userData[0], (String) userData[1], email, password, (byte[]) userData[3]);
    }

    // Helper method to load the Dashboard view
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

    // Redirect to the SignUp page
    public void noAccountButtonAction(ActionEvent event) throws IOException {
        Parent fxmlLoader = FXMLLoader.load(getClass().getResource("/com/jmc/app/signup.fxml"));
        Scene scene = new Scene(fxmlLoader, 520, 400);
        Stage stage = (Stage) noAccountButton.getScene().getWindow();
        stage.setTitle("Signup");
        stage.setScene(scene);
        stage.show();
    }
}
