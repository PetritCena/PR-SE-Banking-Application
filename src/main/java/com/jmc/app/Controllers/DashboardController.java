package com.jmc.app.Controllers;
import com.jmc.app.Models.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;

public class DashboardController {
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