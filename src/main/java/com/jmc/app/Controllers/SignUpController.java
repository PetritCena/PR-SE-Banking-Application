package com.jmc.app.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;



public class SignUpController {

    @FXML
    private TextField firstnameTextField;

    @FXML
    private TextField lastnameTextField;

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

    public void registerButtonAction(ActionEvent actionEvent) {
        if(firstnameTextField.getText().isBlank() || lastnameTextField.getText().isBlank() || emailTextField.getText().isBlank() || passwordTextField.getText().isBlank() || passwordAgainTextField.getText().isBlank()) {
            messageLabel.setText("Please fill all the fields");
        }
        else messageLabel.setText("You try to register!");
    }
}
