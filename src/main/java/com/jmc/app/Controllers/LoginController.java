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
    public Button loginButton, noAccountButton;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label statusLabel;

    private User user;

    // Handles the login process
    public void handleLoginButtonAction(ActionEvent event) {
        if (emailField.getText().isEmpty() || passwordField.getText().isEmpty()){
            statusLabel.setText("Bitte E-Mail und Passwort eingeben!");
        } else {
            String password = passwordField.getText();
            String email = emailField.getText();
            DatabaseConnector dbConnector = new DatabaseConnector();
            user = dbConnector.authenticateUser(email, password);
            if (user != null){
                loadDashboardView();
            } else {
                statusLabel.setText("Login fehlgeschlagen. Überprüfen Sie Ihre Eingaben.");
            }
        }
    }

    // Helper method to load the Dashboard view
    private void loadDashboardView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jmc/app/Dashboard.fxml"));
            Parent root = loader.load();
            DashboardController controller = loader.getController();
            controller.initialize(user);
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setTitle("Startseite");
            stage.setScene(new Scene(root));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace(System.err);
            statusLabel.setText("Fehler beim Laden des Dashboards.");
        }
    }

    // Redirect to the SignUp page
    public void noAccountButtonAction(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jmc/app/Signup.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) noAccountButton.getScene().getWindow();
        stage.setTitle("Signup");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
