package com.jmc.app.Controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import java.io.IOException;

public class DashboardController {
    @FXML
    private Button startSeiteButton;

    // man drückt Account Icon und wird zur Profilseite weitergeleitet
    public void produktSeiteButtonOnAction(ActionEvent event) throws IOException {
        SceneChanger.changeScene("/com/jmc/app/Produktseite.fxml", 850, 750, "Produkte", startSeiteButton);
    }
    public void profilOnAction(MouseEvent event) throws IOException{
        SceneChanger.changeScene("/com/jmc/app/profil.fxml", 850, 750, "Profilseite", startSeiteButton);
    }
}


