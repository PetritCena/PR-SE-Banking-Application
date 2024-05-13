package com.jmc.app.Controllers;

import com.jmc.app.Models.DatabaseConnector;
import com.jmc.app.Models.User;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class SpaceAnlegenController {
    @FXML
    private Button startSeiteButton, produktSeiteButton, neinButton, jaButton;
    @FXML
    private FontAwesomeIconView profilIcon;

    User user;

    @FXML
    public void initialize(User user) {
        this.user = user;
    }
    public void startSeiteButtonOnAction(MouseEvent event) throws IOException {
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
    public void produktSeiteButtonOnAction(MouseEvent event) throws IOException {
        //SceneChanger.changeScene("/com/jmc/app/Produktseite.fxml", 850, 750, "Produkte", startSeiteButton);
        FXMLLoader loader = new FXMLLoader(SceneChanger.class.getResource("/com/jmc/app/Produktseite.fxml"));
        Parent root = loader.load();
        ProduktseiteController controller = loader.getController();
        controller.initialize(user);
        Scene scene = new Scene(root);
        Stage stage = (Stage) produktSeiteButton.getScene().getWindow();
        stage.setTitle("Startseite");
        stage.setScene(scene);
        stage.show();
    }

    public void neinButtonOnAction(MouseEvent event) throws IOException {
        produktSeiteButtonOnAction(event);
    }

    public void jaButtonOnAction(MouseEvent event) throws IOException, SQLException {
        spaceKaufen();
        startSeiteButtonOnAction(event);
    }

    private void spaceKaufen() throws SQLException{
        DatabaseConnector dbConnector = new DatabaseConnector();
        dbConnector.createSpace(0, "Spacekonto", user.getEmail());
    }
}
