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

    public static String password1; // To hold user's password
    public static String email1;    // To hold user's email

    // Handles the login process
    public void handleLoginButtonAction(ActionEvent event) {
        if (emailField.getText().isEmpty() || passwordField.getText().isEmpty()){
            statusLabel.setText("Bitte E-Mail und Passwort eingeben!");
        } else {
            password1 = passwordField.getText(); // Store password
            email1 = emailField.getText();       // Store email
            if (userAuthenticated(email1, password1)) {
                loadDashboardView();
            } else {
                statusLabel.setText("Login fehlgeschlagen. Überprüfen Sie Ihre Eingaben.");
            }
        }
    }

    // Helper method to check if user exists and password is correct
    private boolean userAuthenticated(String email, String password) {
        final String LOGIN_QUERY = "SELECT password FROM users WHERE email = ?";
        try (Connection con = DatabaseConnector.getConnection(); // Use DatabaseConnector
             PreparedStatement stmt = con.prepareStatement(LOGIN_QUERY)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String retrievedPassword = rs.getString("password");
                    return password.equals(retrievedPassword);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            statusLabel.setText("Datenbankfehler: " + e.getMessage());
        }
        return false;
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
