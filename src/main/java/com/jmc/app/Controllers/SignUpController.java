package com.jmc.app.Controllers;

import com.jmc.app.Models.DatabaseConnector;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
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

    public void registerButtonAction(ActionEvent event) throws IOException, SQLException {
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

        DatabaseConnector.registerUser(firstName,lastName,email,password);

        // If registration is successful, redirect to login view
        //Stage stage = (Stage) registerButton.getScene().getWindow();
        loadLoginView(event);
    }

    public void loadLoginView(ActionEvent event) throws IOException {
        SceneChanger.changeScene("/com/jmc/app/login.fxml", 520, 400, "Signup", registerButton);
    }
}
