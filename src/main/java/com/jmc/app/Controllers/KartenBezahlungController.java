package com.jmc.app.Controllers;

import com.jmc.app.Models.User;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

import static com.jmc.app.Controllers.SceneChanger.changeScene;

public class KartenBezahlungController implements Controller {


    public Text saldoHauptkonto;
    public VBox BezahlungButton;
    public Button Back;
    public Button Abhebung;
    @FXML
    private BorderPane borderPane;

    private  User user;


    @FXML
    public void initialize(Object user, Object nulll) {
        this.user = (User) user;
        SceneChanger.loadLeftFrame(borderPane, this.user);
    }


    public void Back(MouseEvent mouseEvent) throws IOException {
        Stage stage = (Stage) Back.getScene().getWindow();
        SceneChanger.changeScene("/com/jmc/app/Simulator.fxml", stage, user, user);
    }

    public void Transaction(MouseEvent mouseEvent) throws IOException {
        Stage stage = (Stage) Abhebung.getScene().getWindow();
        SceneChanger.changeScene("/com/jmc/app/TransactionErfolgreich.fxml", stage, user, user);
    }
}