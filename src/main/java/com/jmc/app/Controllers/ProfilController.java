package com.jmc.app.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import static com.jmc.app.Controllers.LoginController.password1;
import static com.jmc.app.Controllers.LoginController.email1;
import static javafx.scene.paint.Color.GREEN;
import static javafx.scene.paint.Color.RED;

public class ProfilController implements Initializable {
    // Datenbankverbindungsparameter (an deine Datenbank anpassen)
    public static final String USER = "admin";
    public static final String PWD = "BigBankSoSe2024";
    private final String URL = "jdbc:oracle:thin:@e4xxmj5ey9kfqzz5_high?TNS_ADMIN=/Users/oemer.t/Downloads/Wallet_E4XXMJ5EY9KFQZZ5";

    public Label statusLabel;
    @FXML
    public TextField vornameFeld;
    public Label statusLabel2;
    public Circle photoCircle;
    public Image image;
    @FXML
    public ImageView profilFoto;
    @FXML
    private Button speichernButton;
    @FXML
    private TextField nachnameFeld;
    @FXML
    private PasswordField altesPasswortFeld;
    @FXML
    private PasswordField neuesPasswortFeld;
    @FXML
    private PasswordField neuesPasswortBestätigungFeld;
    @FXML
    private Button startSeiteButton;
    @FXML
    private Button produktSeiteButton;
    @FXML
    private Button signoutButton;
    String vornameAlt;
    String nachnameAlt;


    // Hilfsmethode um den Vornamen des Users zu bekommen
    private String getVorname(){
        final String SELECT_NAME = "SELECT vorname FROM users WHERE email = ?";
        try (Connection con = DriverManager.getConnection(URL, USER, PWD); PreparedStatement stmt = con.prepareStatement(SELECT_NAME)) {
            stmt.setString(1, email1);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                     String retrievedVorname = rs.getString("vorname");
                     return retrievedVorname;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            System.out.println("Datenbankfehler: " + e.getMessage());
        }
        return "Hat nicht funktioniert";
    }

    // Hilfsmethode um den Nachnamen des Users zu bekommen
    private String getNachname(){
        final String SELECT_NAME = "SELECT nachname FROM users WHERE email = ?";
        try (Connection con = DriverManager.getConnection(URL, USER, PWD); PreparedStatement stmt = con.prepareStatement(SELECT_NAME)) {
            stmt.setString(1, email1);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String retrievedNachname = rs.getString("nachname");
                    return retrievedNachname;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            System.out.println("Datenbankfehler: " + e.getMessage());
        }
        return "Hat nicht funktioniert";
    }

    // wenn man auf Button Speichern drückt, werden die User Daten mit dem neuen Input in der Datenbank geupdatet
    // beim ändern des Passworts, wird gecheckt, ob das alte Passwort mit dem Passwort von der Datenbank übereinstimmt
    // die Felder "neues Passwort" und "neues Passwort bestätigen" müssen gleich sein, um das Passwort zu ändern
    public void datenÄndern(ActionEvent event) {
        if (altesPasswortFeld.getText().equals(password1) && neuesPasswortFeld.getText().equals(neuesPasswortBestätigungFeld.getText())){

            final String UPDATE_PASSWORD = "UPDATE users SET password = ? WHERE email = ?";

            try (Connection con = DriverManager.getConnection(URL, USER, PWD);
                 PreparedStatement stmt = con.prepareStatement(UPDATE_PASSWORD)) {

                stmt.setString(1, neuesPasswortFeld.getText());
                stmt.setString(2, email1);

                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    statusLabel.setTextFill(GREEN);
                    statusLabel.setText("Passwort erfolgreich geändert");
                }
                else {
                    System.out.println("Datenbank konnte nicht geändert werden");
                }
            } catch (SQLException e) {
                e.printStackTrace(System.err);
                //statusLabel.setText("Datenbankfehler: " + e.getMessage());
                System.out.println("Datenbankfehler: " + e.getMessage());
            }
        } else if (neuesPasswortFeld.getText().equals(neuesPasswortBestätigungFeld.getText())==false){
            statusLabel.setTextFill(RED);
            statusLabel.setText("die Passwörter stimmen nicht überein");
        }
        else if (!altesPasswortFeld.getText().equals(password1) && altesPasswortFeld.getText().isEmpty() == false){
            statusLabel.setTextFill(RED);
            statusLabel.setText("falsches Passwort");
        }

        //Vorname ändern
        if (vornameAlt.equals(vornameFeld.getText()) == false){
            final String UPDATE_VORNAME = "UPDATE users SET vorname = ? WHERE email = ?";

            try (Connection con = DriverManager.getConnection(URL, USER, PWD);
                 PreparedStatement stmt = con.prepareStatement(UPDATE_VORNAME)) {

                stmt.setString(1, vornameFeld.getText());
                stmt.setString(2, email1);

                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    statusLabel2.setText("Name erfolgreich geändert");
                }
                else {
                    System.out.println("Datenbank konnte nicht geändert werden");
                }
            } catch (SQLException e) {
                e.printStackTrace(System.err);
                //statusLabel.setText("Datenbankfehler: " + e.getMessage());
                System.out.println("Datenbankfehler: " + e.getMessage());
            }
        }

        //Nachname ändern
        if (nachnameAlt.equals(nachnameFeld.getText()) == false){
            final String UPDATE_NACHNAME = "UPDATE users SET nachname = ? WHERE email = ?";

            try (Connection con = DriverManager.getConnection(URL, USER, PWD);
                 PreparedStatement stmt = con.prepareStatement(UPDATE_NACHNAME)) {

                stmt.setString(1, nachnameFeld.getText());
                stmt.setString(2, email1);

                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    statusLabel2.setText("Name erfolgreich geändert");
                }
                else {
                    System.out.println("Datenbank konnte nicht geändert werden");
                }
            } catch (SQLException e) {
                e.printStackTrace(System.err);
                //statusLabel.setText("Datenbankfehler: " + e.getMessage());
                System.out.println("Datenbankfehler: " + e.getMessage());
            }
        }
    }

    //man drück Button Startseite und wird zur Startseite weitergeleitet
    public void startSeiteButtonOnAction(ActionEvent event) throws IOException {
        Parent fxmlLoader = FXMLLoader.load(getClass().getResource("/com/jmc/app/Dashboard.fxml"));
        Scene scene = new Scene(fxmlLoader, 850, 750);
        Stage stage = (Stage) startSeiteButton.getScene().getWindow();
        stage.setTitle("Startseite");
        stage.setScene(scene);
        stage.show();
    }

    //man drück Button Signout und ausgeloggt und zum Login weitergeleitet
    public void signoutButtonOnAction(ActionEvent event) throws IOException {
        Parent fxmlLoader = FXMLLoader.load(getClass().getResource("/com/jmc/app/login.fxml"));
        Scene scene = new Scene(fxmlLoader, 520, 400);
        Stage stage = (Stage) signoutButton.getScene().getWindow();
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    FileChooser fileChooser = new FileChooser();
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
    }
}



