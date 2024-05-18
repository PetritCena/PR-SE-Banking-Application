package com.jmc.app.Controllers;


import com.jmc.app.Models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

public class ProfilController implements Controller{
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
    private User user;

    @FXML
    public void initialize(User user) {
        this.user = user;
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        if (user.getPic() != null && user.getPic().length > 0) {
            Image image = new Image(new ByteArrayInputStream(user.getPic()));
            photoCircle.setFill(new ImagePattern(image));
        }
        else{
            displayInitialsInCircle();
        }
        loadUserData();
    }

    private void loadUserData() {
        vornameFeld.setText(user.getFirstName());
        nachnameFeld.setText(user.getLastName());
    }
    public void datenÄndern(ActionEvent actionEvent) {
        if(!vornameFeld.getText().equals(user.getFirstName()) || !nachnameFeld.getText().equals(user.getLastName())) updateUserData();
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
            user.setPic(imageFile);
            Image image = new Image(imageFile.toURI().toString());
            photoCircle.setFill(new ImagePattern(image));
        }
    }

    private String getInitials(String firstName, String lastName) {
        String initials = "";
        if (firstName != null && !firstName.isEmpty()) {
            initials += firstName.substring(0, 1).toUpperCase();
        }
        if (lastName != null && !lastName.isEmpty()) {
            initials += " " + lastName.substring(0, 1).toUpperCase();
        }
        return initials;
    }

    public void displayInitialsInCircle() {
        Text text = new Text(getInitials(user.getFirstName(), user.getLastName()));
        text.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        text.setFill(Color.rgb(53, 73, 90));

        Circle background = new Circle(photoCircle.getRadius(), Color.rgb(218, 236, 251));
        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(background, text);
        stackPane.setMinSize(photoCircle.getRadius() * 2, photoCircle.getRadius() * 2);
        stackPane.setMaxSize(photoCircle.getRadius() * 2, photoCircle.getRadius() * 2);

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        Image image = stackPane.snapshot(parameters, null);

        photoCircle.setFill(new ImagePattern(image));
    }

    private void updateUserData() {
        try {
            user.setFirstName(vornameFeld.getText());
            user.setLastName(nachnameFeld.getText());
            statusLabel.setText("Benutzerdaten erfolgreich aktualisiert.");
            statusLabel.setTextFill(Color.GREEN);
        } catch (SQLException e) {
            statusLabel.setText("Fehler beim Aktualisieren der Benutzerdaten.");
            statusLabel.setTextFill(Color.RED);
            e.printStackTrace(System.err);
        }
    }

    private void updatePassword() {
        if(!altesPasswortFeld.getText().equals(user.getPassword())){
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
            user.setPassword(neuesPasswortFeld.getText());
            statusLabel.setText("Passwort erfolgreich geändert. ");
            statusLabel.setTextFill(Color.GREEN);
        } catch (SQLException e) {
            statusLabel.setText("Fehler beim Aktualisieren des Passworts.");
            statusLabel.setTextFill(Color.RED);
            e.printStackTrace(System.err);
        }
    }

    public void signoutButtonOnAction(ActionEvent event) throws IOException {
        SceneChanger.changeScene("/com/jmc/app/login.fxml", 520, 400, startSeiteButton);
    }

    public void startSeiteButtonOnAction(ActionEvent event) throws IOException {
        SceneChanger.changeScene("/com/jmc/app/Dashboard.fxml", 850, 750, startSeiteButton, user);
    }

    public void produktSeiteButtonOnAction(MouseEvent event) throws IOException {
        SceneChanger.changeScene("/com/jmc/app/Produktseite.fxml", 850, 750, startSeiteButton, user);
    }
}