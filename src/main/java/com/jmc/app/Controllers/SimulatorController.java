package com.jmc.app.Controllers;

import com.jmc.app.Models.User;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class SimulatorController implements Controller {
    @FXML
    private VBox EinzahlungButton, KartenZahlungButton, AbhebungButton;
    @FXML
    private BorderPane borderPane;

    private User user;
    @FXML
    public void initialize(Object user, Object nulll) {
        this.user = (User) user;
        SceneChanger.loadLeftFrame(borderPane, this.user);
    }

    public void handleEinzahlung(MouseEvent event) throws IOException {
        Stage stage = (Stage) EinzahlungButton.getScene().getWindow();
        SceneChanger.changeScene("/com/jmc/app/Einzahlung.fxml", stage, user, null);
    }

    public void HandleAbhebung(MouseEvent mouseEvent) throws IOException {
        Stage stage = (Stage) AbhebungButton.getScene().getWindow();
        SceneChanger.changeScene("/com/jmc/app/Abhebung.fxml", stage, user, null);
    }

    public void HandleKartenZahlung(MouseEvent mouseEvent) throws IOException {
        Stage stage = (Stage) KartenZahlungButton.getScene().getWindow();
        SceneChanger.changeScene("/com/jmc/app/KartenZahlung.fxml", stage, user, null);
    }



}

