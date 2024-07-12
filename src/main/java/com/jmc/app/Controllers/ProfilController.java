package com.jmc.app.Controllers;

import com.jmc.app.Models.User;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
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

/**
 * Diese Klasse entspricht dem Controller für die Profilseite.
 */
public class ProfilController implements Controller{
    @FXML
    private TextField vornameFeld, nachnameFeld, visibleOldPasswordTextField, visibleNewPasswordTextField, visibleConfirmNewPasswordTextField;
    @FXML
    private PasswordField altesPasswortFeld, neuesPasswortFeld, neuesPasswortBestätigungFeld;
    @FXML
    private Label statusLabel;
    @FXML
    private Circle photoCircle;
    @FXML
    private Button signoutButton;
    @FXML
    private BorderPane borderPane;

    private final FileChooser fileChooser = new FileChooser();
    private User user;

    /**
     *
     * @param user ist eine User-Instanz.
     * @param nulll wird hier nicht benutzt, da eine Account-Instanz hier nicht notwendig ist.
     */
    @FXML
    public void initialize(Object user, Object nulll) {
        this.user = (User) user;
        SceneChanger.loadLeftFrame(borderPane, this.user);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        if (this.user.getPic() != null && this.user.getPic().length > 0) {
            Image image = new Image(new ByteArrayInputStream(this.user.getPic()));
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

    /**
     * Diese Methode ändert die User-Daten.
     */
    public void datenÄndern() {
        if(!vornameFeld.getText().equals(user.getFirstName()) || !nachnameFeld.getText().equals(user.getLastName())) updateUserData();
        else if(!neuesPasswortFeld.getText().isEmpty()) updatePassword();
        else {
            statusLabel.setText("Es wurde nichts geändert.");
            statusLabel.setTextFill(Color.RED);
        }
    }

    /**
     * Diese Methode übernimmt das Auswählen eines Fotos für das Profil.
     * @throws SQLException wird geworfen, wenn user.setPic(imageFile) eine Fehler zurückgibt.
     * @throws IOException wird geworfen, wenn user.setPic(imageFile) eine Fehler zurückgibt.
     */
    public void choosePhoto() throws SQLException, IOException {
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

    /**
     * Diese Methode zeigt die Initialen des Users an, falls kein Profilbild vorhanden ist.
     */
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

    /**
     * Diese Methode bringt den User wieder zur Login-Seite zurück.
     * @throws IOException wird geworfen, wenn SceneChanger.changeScene("/com/jmc/app/login.fxml", stage, null, null) einen Fehelr zurückgibt.
     */
    public void signoutButtonOnAction() throws IOException {
        Stage stage = (Stage) signoutButton.getScene().getWindow();
        SceneChanger.changeScene("/com/jmc/app/login.fxml", stage, null, null);
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

        if ("toggleOldPasswordVisibilityButton".equals(btn.getId())) {
            pwdField = altesPasswortFeld;
            txtField = visibleOldPasswordTextField;
        } else if ("toggleNewPasswordVisibilityButton".equals(btn.getId())) {
            pwdField = neuesPasswortFeld;
            txtField = visibleNewPasswordTextField;
        } else if ("toggleConfirmNewPasswordVisibilityButton".equals(btn.getId())) {
            pwdField = neuesPasswortBestätigungFeld;
            txtField = visibleConfirmNewPasswordTextField;
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

}
