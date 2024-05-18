package com.jmc.app.Controllers;

import com.jmc.app.Models.DatabaseConnector;
import com.jmc.app.Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;

public class SpaceAnlegenController implements Controller{
    @FXML
    private Button startSeiteButton;
    private User user;

    @FXML
    public void initialize(User user) {
        this.user = user;
    }
    public void startSeiteButtonOnAction(MouseEvent event) throws IOException {
        SceneChanger.changeScene("/com/jmc/app/Dashboard.fxml", 850, 750, startSeiteButton, user);
    }
    public void profilOnAction(MouseEvent event) throws IOException{
        SceneChanger.changeScene("/com/jmc/app/profil.fxml", 850, 750, startSeiteButton, user);
    }
    public void produktSeiteButtonOnAction(MouseEvent event) throws IOException {
        SceneChanger.changeScene("/com/jmc/app/Produktseite.fxml", 850, 750, startSeiteButton, user);
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
        dbConnector.geldAbziehen(user.getEmail());
    }
}
