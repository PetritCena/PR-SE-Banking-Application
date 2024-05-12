package com.jmc.app.Controllers;
import com.jmc.app.Models.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class DashboardController {
    @FXML
    private Circle photoCircle;
    @FXML
    private FontAwesomeIconView profilButton;
    @FXML
    private Label statusLabel;
    @FXML
    private Button startSeiteButton, produktSeiteButton;

    private User user;
    private ArrayList<Account> accounts;
    private ArrayList<Card> cards = new ArrayList<>();

    @FXML
    public void initialize(User user) {
        this.user = user;
        accounts = user.getAccounts();
        for(Account account : accounts) {
            cards.addAll(account.getCards());
        }
        if (user.getPic() != null && user.getPic().length > 0) {
            Image image = new Image(new ByteArrayInputStream(user.getPic()));
            photoCircle.setFill(new ImagePattern(image));
        }
        else{
            displayInitialsInCircle();
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

    private void displayInitialsInCircle() {
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
    // man drückt Account Icon und wird zur Profilseite weitergeleitet
    public void loadProfilView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/jmc/app/profil.fxml"));
            Parent root = loader.load();
            ProfilController controller = loader.getController();
            controller.initialize(user);
            Scene scene = new Scene(root);
            Stage stage = (Stage) profilButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }
    }
}