package com.jmc.app.Controllers;

import com.jmc.app.Models.Account;
import com.jmc.app.Models.DatabaseConnector;
import com.jmc.app.Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;

public class KarteBestellenController implements Controller{
    @FXML
    private Button startSeiteButton;
    @FXML
    private ComboBox<String> accountComboBox;
    private User user;
    private boolean b = true;

    @FXML
    public void initialize(User user) {
        this.user = user;
    }

    public void accountList(MouseEvent event){
        if(b) {
            for (Account account : user.getAccounts()) {
                accountComboBox.getItems().add(account.toString());
            }
        }
        b = false;
    }

    public void startSeiteButtonOnAction(MouseEvent event) throws IOException {
        SceneChanger.changeScene("/com/jmc/app/Dashboard.fxml", 850, 750, startSeiteButton, user);
    }

    public void profilOnAction(MouseEvent event) throws IOException{
        SceneChanger.changeScene("/com/jmc/app/profil.fxml", 850, 750, startSeiteButton, user);
    }

    public void produktSeiteButtonOnAction(MouseEvent event) throws IOException {
       SceneChanger.changeScene("/com/jmc/app/Produktseite.fxml", 850, 750, startSeiteButton, user);
    }

    public void neinButtonOnAction(MouseEvent event) throws IOException {
        produktSeiteButtonOnAction(event);
    }

    public void jaButtonOnAction(MouseEvent event) throws IOException, SQLException {
        karteKaufen();
        startSeiteButtonOnAction(event);
    }

    private void karteKaufen() throws SQLException{
        DatabaseConnector dbConnector = new DatabaseConnector();
        String iban = accountComboBox.getValue();
        dbConnector.karteBestellen(iban, 400, "Debit");
    }
}
