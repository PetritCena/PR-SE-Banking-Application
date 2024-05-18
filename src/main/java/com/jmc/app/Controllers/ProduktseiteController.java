package com.jmc.app.Controllers;

import com.jmc.app.Models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import java.io.IOException;

public class ProduktseiteController implements Controller{
    @FXML
    private Button startSeiteButton;
    private User user;
    
    @FXML
    public void initialize(User user) {
        this.user = user;
    }
    public void startSeiteButtonOnAction(ActionEvent event) throws IOException {
        SceneChanger.changeScene("/com/jmc/app/Dashboard.fxml", 850, 750, startSeiteButton, user);
    }
    public void spaceOnAction(MouseEvent event) throws IOException{
        SceneChanger.changeScene("/com/jmc/app/spaceAnlegen.fxml", 850, 750, startSeiteButton, user);
    }
    public void profilOnAction(MouseEvent event) throws IOException{
        SceneChanger.changeScene("/com/jmc/app/profil.fxml", 850, 750, startSeiteButton, user);
    }
    public void karteOnAction(MouseEvent event) throws IOException{
        SceneChanger.changeScene("/com/jmc/app/karteBestellen.fxml", 850, 750, startSeiteButton, user);
    }
}
