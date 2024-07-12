package com.jmc.app.Controllers;

import com.jmc.app.Models.DatabaseConnector;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;

import java.io.IOException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Diese Klasse entspricht dem Controller für die PasswortVergessen-Seite.
 */
public class ForgotPasswordController {
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField newPasswordField, confirmPasswordField;
    @FXML
    private Button resetPasswordButton, backToLoginButton;
    @FXML
    private Label messageLabel;

    private final DatabaseConnector dbConnector;
    private String resetCode;

    /**
     * Dieser Konstroktur erstellt eine ForgotPasswordController()-Instanz.
     * @throws SQLException wird geworfen, wenn this.dbConnector = new DatabaseConnector() einen Fehler zurückgibt.
     */
    public ForgotPasswordController() throws SQLException {
        this.dbConnector = new DatabaseConnector();
    }

    @FXML
    private void initialize() {
        emailTextField.setOnAction(this::handleEmailSubmit);
        resetPasswordButton.setOnAction(this::handleResetPassword);
        backToLoginButton.setOnAction(this::handleBackToLogin);

        newPasswordField.setVisible(false);
        confirmPasswordField.setVisible(false);
    }

    private void handleEmailSubmit(ActionEvent event) {
        String email = emailTextField.getText().trim();
        if (!isValidEmail(email)) {
            showMessage("Bitte geben Sie eine gültige E-Mail-Adresse ein.", true);
            return;
        }

        if (!emailExists(email)) {
            showMessage("Diese E-Mail-Adresse ist nicht in unserem System registriert.", true);
            return;
        }

        resetCode = generateResetCode();
        sendResetEmail(email, resetCode);
        showResetCodeDialog(email);
    }

    @FXML
    private void handleResetPassword(ActionEvent event) {
        if (newPasswordField.isVisible()) {
            String newPassword = newPasswordField.getText();
            String confirmPassword = confirmPasswordField.getText();

            if (!newPassword.equals(confirmPassword)) {
                showMessage("Die Passwörter stimmen nicht überein. Bitte versuchen Sie es erneut.", true);
                return;
            }

            if (newPassword.length() < 5) {
                showMessage("Das Passwort muss mindestens 5 Zeichen lang sein.", true);
                return;
            }

            try {
                dbConnector.updateField(emailTextField.getText(), newPassword, "password");
                showMessage("Ihr Passwort wurde erfolgreich zurückgesetzt.", false);
                handleBackToLogin(event);
            } catch (SQLException e) {
                e.printStackTrace();
                showMessage("Beim Zurücksetzen Ihres Passworts ist ein Fehler aufgetreten. Bitte versuchen Sie es erneut.", true);
            }
        } else {
            handleEmailSubmit(event);
        }
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) {
        try {
            Stage stage = (Stage) backToLoginButton.getScene().getWindow();
            SceneChanger.changeScene("/com/jmc/app/login.fxml", stage, null, null);
        } catch (IOException e) {
            e.printStackTrace();
            showMessage("Fehler beim Navigieren zum Anmeldebildschirm. Bitte starten Sie die Anwendung neu.", true);
        }
    }

    private boolean isValidEmail(String email) {
        return Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$", email);
    }

    private boolean emailExists(String email) {
        try {
            return dbConnector.userExists(email);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String generateResetCode() {
        SecureRandom random = new SecureRandom();
        return String.format("%06d", random.nextInt(1000000));
    }

    private void sendResetEmail(String email, String code) {
        String subject = "Passwort-Reset-Code";
        String body = "Ihr Passwort-Reset-Code lautet: " + code;
        sendEmail(email, subject, body);
    }

    private void showResetCodeDialog(String email) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Reset-Code eingeben");
        dialog.setHeaderText("Ein 6-stelliger Code wurde an Ihre E-Mail-Adresse gesendet.");
        dialog.setContentText("Bitte geben Sie den Code ein:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(enteredCode -> {
            if (enteredCode.equals(resetCode)) {
                showPasswordResetFields();
            } else {
                showMessage("Der eingegebene Code ist falsch. Bitte versuchen Sie es erneut.", true);
            }
        });
    }

    private void showPasswordResetFields() {
        newPasswordField.setVisible(true);
        confirmPasswordField.setVisible(true);
        emailTextField.setDisable(true);
        resetPasswordButton.setText("Passwort bestätigen");
    }

    private void showMessage(String message, boolean isError) {
        messageLabel.setText(message);
        messageLabel.setTextFill(isError ? javafx.scene.paint.Color.RED : javafx.scene.paint.Color.GREEN);
    }

    private void sendEmail(String to, String subject, String body) {
        // Your SendGrid API Key
        String apiKey = "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";

        Email from = new Email("gptpremiumchat@gmail.com"); // Your SendGrid verified sender email
        Email toEmail = new Email(to);
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(from, subject, toEmail, content);

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost("https://api.sendgrid.com/v3/mail/send");
            httpPost.setHeader("Authorization", "Bearer " + apiKey);
            httpPost.setHeader("Content-Type", "application/json");

            StringEntity entity = new StringEntity(mail.build());
            httpPost.setEntity(entity);

            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                int statusCode = response.getCode();
                String responseBody = EntityUtils.toString(response.getEntity());

                System.out.println("Sent message successfully with status code: " + statusCode);
                System.out.println("Response body: " + responseBody);
            }
        } catch (IOException | ParseException ex) {
            System.err.println("Error sending email: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
