package com.jmc.app.Controllers;

import com.jmc.app.Models.DatabaseConnector;
import com.jmc.app.Models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class ProfilController {
    @FXML
    private TextField vornameFeld, nachnameFeld;
    @FXML
    private PasswordField altesPasswortFeld, neuesPasswortFeld, neuesPasswortBestätigungFeld;
    @FXML
    private Label statusLabel;
    @FXML
    private Circle photoCircle;
    @FXML
    private Button signoutButton, startSeiteButton, produktSeiteButton;

    private FileChooser fileChooser = new FileChooser();
    private String password;

    @FXML
    public void initialize() {
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        String photoAsString = User.getPic();
        byte[] photo = photoAsString.getBytes();

        Image image = null;
        if (photo != null && photo.length > 0) {
            image = new Image(new ByteArrayInputStream(photo));
        }
        //Image image = DatabaseConnector.loadPhoto(User.getEmail());
        if (image != null) {
            photoCircle.setFill(new ImagePattern(image));
        }
        loadUserData();
    }

    private void loadUserData() {
        vornameFeld.setText(User.getFirstName());
        nachnameFeld.setText(User.getLastName());
        password = User.getPassword();
    }
    public void datenÄndern(ActionEvent actionEvent) {
        updateUserData();
        updatePassword();
    }

    public void choosePhoto(MouseEvent event) throws SQLException {
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File imageFile = fileChooser.showOpenDialog(new Stage());
        if (imageFile != null) {
            DatabaseConnector.savePhoto(LoginController.email, imageFile);
            Image image = new Image(imageFile.toURI().toString());
            photoCircle.setFill(new ImagePattern(image));
        }
    }

    private void updateUserData() {
        try {
            DatabaseConnector.updateField(LoginController.email, vornameFeld.getText(), "vorname");
            DatabaseConnector.updateField(LoginController.email, nachnameFeld.getText(), "nachname");
            statusLabel.setText("Benutzerdaten erfolgreich aktualisiert.");
            statusLabel.setTextFill(Color.GREEN);
        } catch (SQLException e) {
            statusLabel.setText("Fehler beim Aktualisieren der Benutzerdaten.");
            statusLabel.setTextFill(Color.RED);
            e.printStackTrace(System.err);
        }
    }

    private void updatePassword() {
        if(!altesPasswortFeld.getText().equals(password)){
            statusLabel.setText("Das alte Passwort stimmt nicht. " + password);
            statusLabel.setTextFill(Color.RED);
            return;
        }
        if (!neuesPasswortFeld.getText().equals(neuesPasswortBestätigungFeld.getText())) {
            statusLabel.setText("Die Passwörter stimmen nicht überein.");
            statusLabel.setTextFill(Color.RED);
            return;
        }
        try {
            DatabaseConnector.updateField(LoginController.email, neuesPasswortFeld.getText(), "password");
            statusLabel.setText("Passwort erfolgreich geändert. ");
            statusLabel.setTextFill(Color.GREEN);
        } catch (SQLException e) {
            statusLabel.setText("Fehler beim Aktualisieren des Passworts.");
            statusLabel.setTextFill(Color.RED);
            e.printStackTrace(System.err);
        }
    }

    public void signoutButtonOnAction(ActionEvent event) throws IOException {
        changeScene("/com/jmc/app/login.fxml", 520, 400, "Login", signoutButton);
    }

    public void startSeiteButtonOnAction(ActionEvent event) throws IOException {
        changeScene("/com/jmc/app/Dashboard.fxml", 850, 750, "Startseite", startSeiteButton);
    }

    public void goToProduktSeite(ActionEvent event) throws IOException {
        changeScene("/com/jmc/app/Produktseite.fxml", 850, 750, "Produkte", produktSeiteButton);
    }

    private void changeScene(String fxmlPath, int width, int height, String title, Button button) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Scene scene = new Scene(root, width, height);
        Stage stage = (Stage) button.getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
}