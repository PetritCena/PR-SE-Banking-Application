package com.jmc.app.Controllers;

import com.jmc.app.Models.DatabaseConnector;
import com.jmc.app.Models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class SpaceAnlegenController implements Controller{
    @FXML
    private Button jaButton, neinButton;
    @FXML
    private BorderPane borderPane;

    private User user;

    @FXML
    public void initialize(Object user) {
        this.user = (User) user;
        SceneChanger.loadLeftFrame(borderPane, this.user);
    }

    public void neinButtonOnAction(MouseEvent event) throws IOException {
        Stage stage = (Stage) neinButton.getScene().getWindow();
        SceneChanger.changeScene("/com/jmc/app/Produktseite.fxml", stage, this.user);
    }

    public void jaButtonOnAction(MouseEvent event) throws IOException, SQLException {
        spaceKaufen();
        Stage stage = (Stage) jaButton.getScene().getWindow();
        SceneChanger.changeScene("/com/jmc/app/Dashboard.fxml", stage, this.user);
    }

    private void spaceKaufen() throws SQLException{
        DatabaseConnector dbConnector = new DatabaseConnector();
        dbConnector.createSpace(0, "Spacekonto", user);
        dbConnector.geldAbziehen(user.getEmail());
    }
}
