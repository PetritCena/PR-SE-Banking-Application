package com.jmc.app.Controllers;

import com.jmc.app.Models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
    private Button startSeiteButton;
    private final FileChooser fileChooser = new FileChooser();
    private String password;

    @FXML
    public void initialize() {
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        Image image;
        if (User.getPic() != null && User.getPic().length > 0) {
            image = new Image(new ByteArrayInputStream(User.getPic()));
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
        if(!vornameFeld.getText().equals(User.getFirstName()) || !nachnameFeld.getText().equals(User.getLastName())) updateUserData();
        else if(!neuesPasswortFeld.getText().isEmpty()) updatePassword();
        else {
            statusLabel.setText("Es wurde nichts geändert.");
            statusLabel.setTextFill(Color.RED);
        }
    }

    public void choosePhoto(MouseEvent event) throws SQLException {
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File imageFile = fileChooser.showOpenDialog(new Stage());
        if (imageFile != null) {
            User.setPic(imageFile);
            Image image = new Image(imageFile.toURI().toString());
            photoCircle.setFill(new ImagePattern(image));
        }
    }

    private void updateUserData() {
        try {
            User.setFirstName(vornameFeld.getText());
            User.setLastName(nachnameFeld.getText());
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
            statusLabel.setText("Das alte Passwort stimmt nicht. ");
            statusLabel.setTextFill(Color.RED);
            return;
        }
        if (!neuesPasswortFeld.getText().equals(neuesPasswortBestätigungFeld.getText())) {
            statusLabel.setText("Die Passwörter stimmen nicht überein.");
            statusLabel.setTextFill(Color.RED);
            return;
        }
        try {
            User.setPassword(neuesPasswortFeld.getText());
            statusLabel.setText("Passwort erfolgreich geändert. ");
            statusLabel.setTextFill(Color.GREEN);
        } catch (SQLException e) {
            statusLabel.setText("Fehler beim Aktualisieren des Passworts.");
            statusLabel.setTextFill(Color.RED);
            e.printStackTrace(System.err);
        }
    }

    public void signoutButtonOnAction(ActionEvent event) throws IOException {
        SceneChanger.changeScene("/com/jmc/app/login.fxml", 520, 400, "Login", startSeiteButton);
    }
    public void startSeiteButtonOnAction(MouseEvent event) throws IOException {
        SceneChanger.changeScene("/com/jmc/app/Dashboard.fxml", 850, 750, "Startseite", startSeiteButton);
    }
    public void produktSeiteButtonOnAction(ActionEvent event) throws IOException {
        SceneChanger.changeScene("/com/jmc/app/Produktseite.fxml", 850, 750, "Produkte", startSeiteButton);
    }
}