package com.jmc.app.Controllers;

import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
/*import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.Image;*/
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

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
      
    /*FileChooser fileChooser = new FileChooser();
    public void choosePhoto(MouseEvent mouseEvent) throws SQLException {
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File imageFile = fileChooser.showOpenDialog(new Stage());
        //profilFoto = new ImageView();
        savePhotoToDatabase(imageFile);
        loadPhoto();
        //image = new Image(imageFile.toURI().toString());
        //profilFoto.setImage(image);
    }
    
    public void savePhotoToDatabase (File file) {
        byte[] imageBytes = null;
        try (FileInputStream fis = new FileInputStream(file)) {
            imageBytes = new byte[(int) file.length()];
            fis.read(imageBytes);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        final String SET_PHOTO = "UPDATE users SET photo = ? WHERE email = ?";
        try (Connection con = DriverManager.getConnection(URL, USER, PWD);
             PreparedStatement stmt = con.prepareStatement(SET_PHOTO)) {
            stmt.setBytes(1, imageBytes);
            stmt.setString(2, email1);
            int affectedRows = stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }
    
    public void loadPhoto() throws SQLException {
        final String GET_PHOTO = "SELECT photo FROM USERS WHERE email = ?";
        try (Connection con = DriverManager.getConnection(URL, USER, PWD);
             PreparedStatement stmt = con.prepareStatement(GET_PHOTO)) {
             stmt.setString(1, email1);
             ResultSet rs = stmt.executeQuery();
             if (rs.next()) {
                byte[] imageData = rs.getBytes("photo");
                if (imageData != null && imageData.length > 0) {
                    ByteArrayInputStream in = new ByteArrayInputStream(imageData);
                    Image image;
                    image = new Image(in);
                    photoCircle.setFill(new ImagePattern(image));
                } else if (imageData==null) {
                    //hier kommt dein Code Fabian
                    //falls imagedata null ist also wenn der Nutzer kein Bild hat
                }
            }
        }
    }

    @Override
    public void initialize(java.net.URL url, ResourceBundle resourceBundle) {
        vornameFeld.setText(getVorname());
        nachnameFeld.setText(getNachname());
        vornameAlt = vornameFeld.getText();
        nachnameAlt = nachnameFeld.getText();
        fileChooser.setInitialDirectory(new File("/Users/oemer.t/Pictures"));
        try {
            loadPhoto();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }*/
}

