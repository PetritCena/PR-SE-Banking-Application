package com.jmc.app.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.sql.*;

import static com.jmc.app.Controllers.LoginController.email1;
import static com.jmc.app.Controllers.LoginController.password1;

public class ProfilController {
    @FXML public Label statusLabel;
    @FXML public TextField vornameFeld;
    @FXML public Label statusLabel2;
    @FXML private Button speichernButton;
    @FXML private TextField nachnameFeld;
    @FXML private PasswordField altesPasswortFeld;
    @FXML private PasswordField neuesPasswortFeld;
    @FXML private PasswordField neuesPasswortBestätigungFeld;
    @FXML private Button startSeiteButton;
    @FXML private Button produktSeiteButton;
    @FXML private Button signoutButton;

    String vornameAlt;
    String nachnameAlt;

    @FXML
    private void initialize() {
        loadData();
    }

    private void loadData() {
        try {
            vornameFeld.setText(getVorname());
            nachnameFeld.setText(getNachname());
            vornameAlt = vornameFeld.getText();
            nachnameAlt = nachnameFeld.getText();
        } catch (SQLException e) {
            statusLabel.setText("Datenbankfehler beim Laden der Daten.");
            statusLabel.setTextFill(Color.RED);
            e.printStackTrace(System.err);
        }
    }

    private String getVorname() throws SQLException {
        final String SELECT_NAME = "SELECT vorname FROM users WHERE email = ?";
        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(SELECT_NAME)) {
            stmt.setString(1, email1);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("vorname");
                }
            }
        }
        return "Vorname nicht gefunden";
    }

    private String getNachname() throws SQLException {
        final String SELECT_NAME = "SELECT nachname FROM users WHERE email = ?";
        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement stmt = con.prepareStatement(SELECT_NAME)) {
            stmt.setString(1, email1);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nachname");
                }
            }
        }
        return "Nachname nicht gefunden";
    }

    public void datenÄndern(ActionEvent event) {
        Connection con = null;
        try {
            con = DatabaseConnector.getConnection();
            con.setAutoCommit(false); // Set auto-commit to false to manage transaction manually

            updatePassword(con);
            updateField(con, vornameAlt, vornameFeld.getText(), "vorname");
            updateField(con, nachnameAlt, nachnameFeld.getText(), "nachname");

            con.commit(); // Commit the transaction if all updates are successful
        } catch (SQLException e) {
            try {
                if (con != null) {
                    con.rollback(); // Attempt to rollback changes if there's an error
                }
                statusLabel.setText("Änderungen zurückgerollt: " + e.getMessage());
                statusLabel.setTextFill(Color.RED);
            } catch (SQLException ex) {
                statusLabel.setText("Fehler beim Zurückrollen: " + ex.getMessage());
                statusLabel.setTextFill(Color.RED);
            }
            e.printStackTrace(System.err);
        } finally {
            try {
                if (con != null) con.close(); // Ensure connection is closed after operation
            } catch (SQLException ex) {
                ex.printStackTrace(System.err);
            }
        }
    }


    private void updatePassword(Connection con) throws SQLException {
        if (!neuesPasswortFeld.getText().equals(neuesPasswortBestätigungFeld.getText())) {
            statusLabel.setText("Die Passwörter stimmen nicht überein.");
            statusLabel.setTextFill(Color.RED);
            return;
        }
        if (!altesPasswortFeld.getText().equals(password1)) {
            statusLabel.setText("Falsches altes Passwort.");
            statusLabel.setTextFill(Color.RED);
            return;
        }
        if (altesPasswortFeld.getText().equals(neuesPasswortFeld.getText())) {
            statusLabel.setText("Neues passwort ist gleich wie altes Passwort");
            statusLabel.setTextFill(Color.RED);
            return;
        }

        final String UPDATE_PASSWORD = "UPDATE users SET password = ? WHERE email = ?";
        try (PreparedStatement stmt = con.prepareStatement(UPDATE_PASSWORD)) {
            statusLabel.setText("Änderungen erfolgreich gespeichert.");
            statusLabel.setTextFill(Color.GREEN);
            stmt.setString(1, neuesPasswortFeld.getText());
            stmt.setString(2, email1);
            stmt.executeUpdate();
        }
    }

    private void updateField(Connection con, String oldVal, String newVal, String field) throws SQLException {
        if (!oldVal.equals(newVal)) {
            final String UPDATE_NAME = "UPDATE users SET " + field + " = ? WHERE email = ?";
            try (PreparedStatement stmt = con.prepareStatement(UPDATE_NAME)) {
                stmt.setString(1, newVal);
                stmt.setString(2, email1);
                stmt.executeUpdate();
            }
        }
    }

    public void startSeiteButtonOnAction(ActionEvent event) throws IOException {
        changeScene("/com/jmc/app/Dashboard.fxml", 850, 750, "Startseite");
    }

    public void signoutButtonOnAction(ActionEvent event) throws IOException {
        changeScene("/com/jmc/app/login.fxml", 520, 400, "Login");
    }

    private void changeScene(String fxmlPath, int width, int height, String title) throws IOException {
        Parent fxmlLoader = FXMLLoader.load(getClass().getResource(fxmlPath));
        Scene scene = new Scene(fxmlLoader, width, height);
        Stage stage = (Stage) startSeiteButton.getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
}
