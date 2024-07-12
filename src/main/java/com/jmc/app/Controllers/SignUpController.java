package com.jmc.app.Controllers;

import com.jmc.app.Models.DatabaseConnector;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
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
 * Diese Klasse ist der Controller für das Signup.
 */
public class SignUpController {
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

    private String verificationCode;

    public void registerButtonAction() {
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
        verificationCode = generateVerificationCode();
        sendEmail(email, "Welcome to Our Application", "Dear " + firstName + " " + lastName + ",\n\nThank you for registering at Big Bank!\nYour verification code is: " + verificationCode);
        showVerificationCodeDialog(firstName, lastName, email, password);
    }

    private void loadLoginView(Stage stage) throws IOException {
        SceneChanger.changeScene("/com/jmc/app/login.fxml", stage, null, null);
    }

    /**
     * Diese Methode führt den User zur Login-Seite.
     * @throws IOException wird geworfen, wenn loadLoginView(stage) einen Fehler zurückgibt.
     */
    public void haveAcccountButtonAction() throws IOException {
        Stage stage = (Stage) haveAcccountButton.getScene().getWindow();
        loadLoginView(stage);
    }

    /**
     * Diese Methode ist dazu da, dass der Inhalt der Passwordfelder angezeigt oder versteckt wird.
     * @param event ist das Event (Mausdruck auf Button), das diese Methode auslöst.
     */
    public void togglePasswordVisibility(ActionEvent event) {
        Object source = event.getSource();
        Button btn = null;
        FontAwesomeIconView iconView = null;

        if (source instanceof Button) {
            btn = (Button) source;
        } else if (source instanceof FontAwesomeIconView) {
            iconView = (FontAwesomeIconView) source;
            btn = (Button) iconView.getParent();
        }

        if (btn == null) {
            System.out.println("Button is null");
            return;
        }

        PasswordField pwdField;
        TextField txtField;

        if ("togglePasswordVisibilityButton".equals(btn.getId())) {
            pwdField = passwordTextField;
            txtField = visiblePasswordTextField;
        } else if ("toggleConfirmPasswordVisibilityButton".equals(btn.getId())) {
            pwdField = passwordAgainTextField;
            txtField = visiblePasswordAgainTextField;
        } else {
            System.out.println("Unknown button ID");
            return;
        }

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
    }

    private String generateVerificationCode() {
        SecureRandom random = new SecureRandom();
        StringBuilder code = new StringBuilder(12);
        for (int i = 0; i < 12; i++) {
            int digit = random.nextInt(10);
            code.append(digit);
        }
        return code.toString();
    }

    private void showVerificationCodeDialog(String firstName, String lastName, String email, String password) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Verification Code");
        dialog.setHeaderText("Enter Verification Code");
        dialog.setContentText("Please enter the 12-digit verification code sent to your email:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(code -> {
            if (code.equals(verificationCode)) {
                try {
                    DatabaseConnector db = new DatabaseConnector();
                    db.registerUser(firstName, lastName, email, password);
                    showAlert(Alert.AlertType.INFORMATION, "Verification Successful", "Your account has been successfully verified.");

                    // If registration is successful, redirect to login view
                    Stage stage = (Stage) registerButton.getScene().getWindow();
                    loadLoginView(stage);
                } catch (SQLException | IOException e) {
                    showAlert(Alert.AlertType.ERROR, "Registration Failed", "An error occurred while registering your account. Please try again.");
                    e.printStackTrace();
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Verification Failed", "The verification code you entered is incorrect. Please try again.");
            }
        });
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void sendEmail(String to, String subject, String body) {
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