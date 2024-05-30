package com.jmc.app.Controllers;

import com.jmc.app.Models.DatabaseConnector;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import java.util.regex.Pattern;

import java.io.IOException;
import java.sql.*;
public class SignUpController {


    public FontAwesomeIconView VisabiltiyButton1;
    public FontAwesomeIconView VisabiltiyButton2;

    @FXML
    private TextField firstNameTextField, lastNameTextField, emailTextField;
    @FXML
    private PasswordField passwordTextField, passwordAgainTextField;
    @FXML
    private Button registerButton, haveAcccountButton;
    @FXML
    private Label messageLabel;
    @FXML
    private TextField visiblePasswordTextField;
    @FXML
    private TextField visiblePasswordAgainTextField;


    public void registerButtonAction(ActionEvent actionEvent) throws IOException, SQLException {
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String email = emailTextField.getText();
        String password = passwordTextField.getText();

        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank() || passwordAgainTextField.getText().isBlank()) {
            messageLabel.setText("Please fill in all the fields");
            return;
        } else if (!password.equals(passwordAgainTextField.getText())) {
            messageLabel.setText("Passwords do not match");
            return;
        } else if (!Pattern.matches("[A-Za-z-']+", firstName) || !Pattern.matches("[A-Za-z-']+", lastName)) {
            messageLabel.setText("Invalid name format");
            return;
        } else if (!Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", email)) {
            messageLabel.setText("Invalid email format");
            return;
        } else if (!Pattern.matches(".{5,}", password)) {
            messageLabel.setText("Password must be at least 5 characters long");
            return;
        }

        DatabaseConnector db = new DatabaseConnector();
        db.registerUser(firstName, lastName, email, password);

        // If registration is successful, redirect to login view
        Stage stage = (Stage) registerButton.getScene().getWindow();
        loadLoginView(stage);
    }

    private void loadLoginView(Stage stage) throws IOException {
        SceneChanger.changeScene("/com/jmc/app/login.fxml", stage, null, null);
    }

    public void haveAcccountButtonAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) haveAcccountButton.getScene().getWindow();
        loadLoginView(stage);
    }


    @FXML
    public void togglePasswordVisibility(ActionEvent event) {
        Object source = event.getSource();
        Button btn = null;
        FontAwesomeIconView iconView = null;

        if (source instanceof Button) {
            btn = (Button) source;
        } else if (source instanceof FontAwesomeIconView) {
            iconView = (FontAwesomeIconView) source;
            btn = (Button) iconView.getParent(); // Assuming the icon is nested directly within the button
        }

        if (btn == null) {
            System.out.println("Button is null");
            return;
        }

        PasswordField pwdField;
        TextField txtField;

        // Determine which fields to toggle based on the button ID
        if ("togglePasswordVisibilityButton".equals(btn.getId())) {
            pwdField = passwordTextField;
            txtField = visiblePasswordTextField;
        } else if ("toggleConfirmPasswordVisibilityButton".equals(btn.getId())) {
            pwdField = passwordAgainTextField;
            txtField = visiblePasswordAgainTextField;
        } else {
            System.out.println("Unknown button ID");
            return; // Optionally, log or handle this error scenario
        }

        // Toggle visibility and update icon
        if (pwdField.isVisible()) {
            txtField.setText(pwdField.getText());
            txtField.setVisible(true);
            pwdField.setVisible(false);
            if (iconView != null) {
                iconView.setGlyphName("EYE_SLASH");
            }
        } else {
            pwdField.setText(txtField.getText());
            pwdField.setVisible(true);
            txtField.setVisible(false);
            if (iconView != null) {
                iconView.setGlyphName("EYE");
            }
        }
        System.out.println("Toggled visibility");
    }
}
