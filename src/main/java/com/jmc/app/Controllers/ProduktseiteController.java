package com.jmc.app.Controllers;

import com.jmc.app.Models.User;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ProduktseiteController implements Controller{
    @FXML
    private Button spaceButton, karteButton;
    @FXML
    private BorderPane borderPane;

    private User user;
    
    @FXML
    public void initialize(Object user, Object nulll) {
        this.user = (User) user;
        SceneChanger.loadLeftFrame(borderPane, this.user);
    }

    public void spaceOnAction(MouseEvent event) throws IOException{
        Stage stage = (Stage) spaceButton.getScene().getWindow();
        SceneChanger.changeScene("/com/jmc/app/spaceAnlegen.fxml", stage, user, null);
    }

    public void karteOnAction(MouseEvent event) throws IOException{
        Stage stage = (Stage) karteButton.getScene().getWindow();
        SceneChanger.changeScene("/com/jmc/app/karteBestellen.fxml", stage, user, null);
    }
}
