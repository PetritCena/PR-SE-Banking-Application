package com.jmc.app.Controllers;

import com.jmc.app.Models.User;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class ProduktseiteController {
    @FXML
    private FontAwesomeIconView profilIcon;
    @FXML
    private Button startSeiteButton, produktseiteButton, spaceButton, karteButton;

    private User user;
    
    @FXML
    public void initialize(User user) {
        this.user = user;
    }
    public void startSeiteButtonOnAction(ActionEvent event) throws IOException {
        //SceneChanger.changeScene("/com/jmc/app/Dashboard.fxml", 850, 750, "Startseite", startSeiteButton);
        FXMLLoader loader = new FXMLLoader(SceneChanger.class.getResource("/com/jmc/app/Dashboard.fxml"));
        Parent root = loader.load();
        DashboardController controller = loader.getController();
        controller.initialize(user);
        Scene scene = new Scene(root);
        Stage stage = (Stage) startSeiteButton.getScene().getWindow();
        stage.setTitle("Startseite");
        stage.setScene(scene);
        stage.show();
    }

    public void spaceOnAction(MouseEvent event) throws IOException{
        //SceneChanger.changeScene("/com/jmc/app/spaceAnlegen.fxml", 850, 750, "Space", startSeiteButton);
        FXMLLoader loader = new FXMLLoader(SceneChanger.class.getResource("/com/jmc/app/spaceAnlegen.fxml"));
        Parent root = loader.load();
        SpaceAnlegenController controller = loader.getController();
        controller.initialize(user);
        Scene scene = new Scene(root);
        Stage stage = (Stage) spaceButton.getScene().getWindow();
        stage.setTitle("Startseite");
        stage.setScene(scene);
        stage.show();
    }
    public void profilOnAction(MouseEvent event) throws IOException{
        //SceneChanger.changeScene("/com/jmc/app/profil.fxml", 850, 750, "Profilseite", startSeiteButton);
        FXMLLoader loader = new FXMLLoader(SceneChanger.class.getResource("/com/jmc/app/profil.fxml"));
        Parent root = loader.load();
        ProfilController controller = loader.getController();
        controller.initialize(user);
        Scene scene = new Scene(root);
        Stage stage = (Stage) profilIcon.getScene().getWindow();
        stage.setTitle("Startseite");
        stage.setScene(scene);
        stage.show();
    }
    public void karteOnAction(MouseEvent event) throws IOException{
        //SceneChanger.changeScene("/com/jmc/app/karteBestellen.fxml", 850, 750, "Profilseite", startSeiteButton);
        FXMLLoader loader = new FXMLLoader(SceneChanger.class.getResource("/com/jmc/app/karteBestellen.fxml"));
        Parent root = loader.load();
        KarteBestellenController controller = loader.getController();
        controller.initialize(user);
        Scene scene = new Scene(root);
        Stage stage = (Stage) karteButton.getScene().getWindow();
        stage.setTitle("Startseite");
        stage.setScene(scene);
        stage.show();
    }
}
