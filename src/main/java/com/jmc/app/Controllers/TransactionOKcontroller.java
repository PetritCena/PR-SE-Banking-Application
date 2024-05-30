package com.jmc.app.Controllers;

import com.jmc.app.Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;

public class TransactionOKcontroller implements Controller {

    @FXML
    public Button BackToProduct;
    public Button Back;
    @FXML
    private BorderPane borderPane;
    private  User user;


    @FXML
    public void initialize(Object user, Object nulll) {
        this.user = (User) user;
        SceneChanger.loadLeftFrame(borderPane, this.user);
    }


    public void BackToProduct(MouseEvent mouseEvent) throws IOException {
    Stage stage = (Stage) BackToProduct.getScene().getWindow();
        SceneChanger.changeScene("/com/jmc/app/Dashboard.fxml", stage, user, user);
}

}