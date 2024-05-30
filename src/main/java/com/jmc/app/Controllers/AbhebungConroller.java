package com.jmc.app.Controllers;

import com.jmc.app.Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;

public class AbhebungConroller implements Controller {

    public Text saldoHauptkonto;
    @FXML
    public Button Abhebung;
    public Button Back;
    @FXML
    private BorderPane borderPane;
    private  User user;


    @FXML
    public void initialize(Object user, Object nulll) {
        this.user = (User) user;
        SceneChanger.loadLeftFrame(borderPane, this.user);
    }




    public void Transaction(MouseEvent mouseEvent) throws IOException {
        Stage stage = (Stage) Abhebung.getScene().getWindow();
        SceneChanger.changeScene("/com/jmc/app/TransactionErfolgreich.fxml", stage, user, user);
    }

    public void Back(MouseEvent mouseEvent) throws IOException {
        Stage stage = (Stage) Back.getScene().getWindow();
        SceneChanger.changeScene("/com/jmc/app/Simulator.fxml", stage, user, user);
    }
}
