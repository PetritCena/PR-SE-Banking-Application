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

public class SignUpController {

    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private PasswordField passwordAgainTextField;
    @FXML
    private Button registerButton;
    @FXML
    private Label messageLabel;
    @FXML
    private Button haveAcccountButton;

    public void registerButtonAction(ActionEvent actionEvent) throws IOException {
        if (firstNameTextField.getText().isBlank() || lastNameTextField.getText().isBlank() || emailTextField.getText().isBlank() || passwordTextField.getText().isBlank() || passwordAgainTextField.getText().isBlank()) {
            messageLabel.setText("Please fill in all the fields");
            return;
        } else if (!passwordTextField.getText().equals(passwordAgainTextField.getText())) {
            messageLabel.setText("Passwords do not match");
            return;
        }

        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String email = emailTextField.getText();
        String password = passwordTextField.getText();

        try (Connection con = DatabaseConnector.getConnection()) {
            String checkEmail = "SELECT 1 FROM users WHERE email = ?";
            try (PreparedStatement checkStmt = con.prepareStatement(checkEmail)) {
                checkStmt.setString(1, email);
                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    messageLabel.setText("Email is already in use");
                    return;
                }
            }

            String query = "INSERT INTO users (vorname, nachname, email, password) VALUES (?, ?, ?, ?)";
            try (PreparedStatement insert = con.prepareStatement(query)) {
                insert.setString(1, firstName);
                insert.setString(2, lastName);
                insert.setString(3, email);
                insert.setString(4, password);
                insert.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("Database error during registration");
            e.printStackTrace(System.err);
            messageLabel.setText("Database error: " + e.getMessage());
        }

        // If registration is successful, redirect to login view
        Stage stage = (Stage) registerButton.getScene().getWindow();
        loadLoginView(stage);
    }

    private void loadLoginView(Stage stage) throws IOException {
        Parent fxmlLoader = FXMLLoader.load(getClass().getResource("/com/jmc/app/login.fxml"));
        Scene scene = new Scene(fxmlLoader, 520, 400);
        stage.setTitle("Signup");
        stage.setScene(scene);
        stage.show();
    }

    public void haveAcccountButtonAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) haveAcccountButton.getScene().getWindow();
        loadLoginView(stage);
    }
}
