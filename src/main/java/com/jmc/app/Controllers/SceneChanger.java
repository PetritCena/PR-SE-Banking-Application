package com.jmc.app.Controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

//das ist eine klasse damit wir nicht in jeder Klasse erneut die changeScene Methode hinzufügen müssen

public class SceneChanger {
    public static void changeScene(String fxmlPath, int width, int height, String title, Button button) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneChanger.class.getResource(fxmlPath));
        Parent root = loader.load();
        Scene scene = new Scene(root, width, height);
        Stage stage = (Stage) button.getScene().getWindow();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
}
