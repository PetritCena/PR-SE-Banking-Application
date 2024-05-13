package com.jmc.app.Controllers;

import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import java.io.IOException;

public class HauptkontoAnlegenController {
    public Button startSeiteButton;

    public void startSeiteButtonOnAction(MouseEvent event) throws IOException {
        SceneChanger.changeScene("/com/jmc/app/Dashboard.fxml", 850, 750, "Startseite", startSeiteButton);
    }
    public void profilOnAction(MouseEvent event) throws IOException{
        SceneChanger.changeScene("/com/jmc/app/profil.fxml", 850, 750, "Profilseite", startSeiteButton);
    }
    public void produktSeiteButtonOnAction(MouseEvent event) throws IOException {
        SceneChanger.changeScene("/com/jmc/app/Produktseite.fxml", 850, 750, "Produkte", startSeiteButton);
    }
}
