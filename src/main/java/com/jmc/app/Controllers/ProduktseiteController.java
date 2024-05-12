package com.jmc.app.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import java.io.IOException;

public class ProduktseiteController {
    @FXML
    public Button startSeiteButton;

    public void startSeiteButtonOnAction(ActionEvent event) throws IOException {
        SceneChanger.changeScene("/com/jmc/app/Dashboard.fxml", 850, 750, "Startseite", startSeiteButton);
    }
    public void hauptKontoOnAction(MouseEvent event) throws IOException {
        SceneChanger.changeScene("/com/jmc/app/hauptkontoAnlegen.fxml", 850, 750, "Hauptkonto", startSeiteButton);
    }
    public void spaceOnAction(MouseEvent event) throws IOException{
        SceneChanger.changeScene("/com/jmc/app/spaceAnlegen.fxml", 850, 750, "Space", startSeiteButton);
    }
    public void profilOnAction(MouseEvent event) throws IOException{
        SceneChanger.changeScene("/com/jmc/app/profil.fxml", 850, 750, "Profilseite", startSeiteButton);
    }
    public void karteOnAction(MouseEvent event) throws IOException{
        SceneChanger.changeScene("/com/jmc/app/karteBestellen.fxml", 850, 750, "Profilseite", startSeiteButton);
    }
}
