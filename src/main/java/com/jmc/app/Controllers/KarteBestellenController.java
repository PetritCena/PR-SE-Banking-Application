package com.jmc.app.Controllers;

import com.jmc.app.Models.Account;
import com.jmc.app.Models.DatabaseConnector;
import com.jmc.app.Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class KarteBestellenController implements Controller{
    @FXML
    private Button jaButton, neinButton;
    @FXML
    private ComboBox<String> accountComboBox;
    @FXML
    private BorderPane borderPane;

    private User user;
    private boolean b = true;

    @FXML
    public void initialize(Object user) {
        this.user = (User) user;
        SceneChanger.loadLeftFrame(borderPane, this.user);
    }

    public void accountList(MouseEvent event){
        if(b) {
            for (Account account : user.getAccounts()) {
                accountComboBox.getItems().add(account.toString());
            }
        }
        b = false;
    }

    public void neinButtonOnAction(MouseEvent event) throws IOException {
        Stage stage = (Stage) neinButton.getScene().getWindow();
        SceneChanger.changeScene("/com/jmc/app/Produktseite.fxml", stage, this.user);
    }

    public void jaButtonOnAction(MouseEvent event) throws IOException, SQLException {
        karteKaufen();
        Stage stage = (Stage) jaButton.getScene().getWindow();
        SceneChanger.changeScene("/com/jmc/app/Dashboard.fxml", stage, this.user);
    }

    private void karteKaufen() throws SQLException{
        DatabaseConnector dbConnector = new DatabaseConnector();
        String iban = accountComboBox.getValue();
        dbConnector.karteBestellen(iban, 400, "Debit");
    }
}
