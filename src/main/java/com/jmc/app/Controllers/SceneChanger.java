package com.jmc.app.Controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import java.io.IOException;

//das ist eine klasse damit wir nicht in jeder Klasse erneut die changeScene Methode hinzufügen müssen

public class SceneChanger {

    public static void changeScene(String fxmlPath, Stage stage, Object o, Object o2) throws IOException{
        FXMLLoader loader = new FXMLLoader(SceneChanger.class.getResource(fxmlPath));
        Parent root = loader.load();
        if (o != null) {
            Controller controller = loader.getController();
            controller.initialize(o, o2);
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public static void loadLeftFrame(BorderPane borderPane, Object o){
        AnchorPane pane = new AnchorPane();
        pane.setPrefHeight(750);
        pane.setPrefWidth(182);
        pane.setStyle("-fx-background-color: #DAECFB");

        FontAwesomeIconView bank = new FontAwesomeIconView();
        bank.setGlyphName("BANK");
        bank.setFill(Paint.valueOf("#35495a"));
        bank.setLayoutX(55);
        bank.setLayoutY(108);
        bank.setSize("70");

        FontAwesomeIconView profilIcon = new FontAwesomeIconView();
        profilIcon.setGlyphName("USER");
        profilIcon.setFill(Paint.valueOf("#35495a"));
        profilIcon.setLayoutX(80);
        profilIcon.setLayoutY(700);
        profilIcon.setSize("30");
        profilIcon.setCursor(Cursor.OPEN_HAND);
        profilIcon.setOnMouseClicked(mouseEvent -> {
            try {
                changeScene("/com/jmc/app/profil.fxml", (Stage) profilIcon.getScene().getWindow(), o, null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Button startSeite = new Button();
        startSeite.setPrefHeight(40);
        startSeite.setPrefWidth(90);
        startSeite.setStyle("-fx-background-color: #35495a; -fx-background-radius: 4");
        startSeite.setLayoutX(46);
        startSeite.setLayoutY(325);
        startSeite.setText("Startseite");
        startSeite.setTextFill(Paint.valueOf("#daecfb"));
        startSeite.setCursor(Cursor.OPEN_HAND);
        startSeite.setOnMouseClicked(mouseEvent -> {
            try {
                changeScene("/com/jmc/app/Dashboard.fxml", (Stage) startSeite.getScene().getWindow(), o, null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Button produkte = new Button();
        produkte.setPrefHeight(40);
        produkte.setPrefWidth(90);
        produkte.setStyle("-fx-background-color: #35495a; -fx-background-radius: 4");
        produkte.setLayoutX(46);
        produkte.setLayoutY(418);
        produkte.setText("Produkte");
        produkte.setTextFill(Paint.valueOf("#daecfb"));
        produkte.setCursor(Cursor.OPEN_HAND);
        produkte.setOnMouseClicked(mouseEvent -> {
            try {
                changeScene("/com/jmc/app/Produktseite.fxml", (Stage) produkte.getScene().getWindow(), o, null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Button simulatorButton = new Button();
        simulatorButton.setPrefHeight(40);
        simulatorButton.setPrefWidth(90);
        simulatorButton.setStyle("-fx-background-color: #35495a; -fx-background-radius: 4");
        simulatorButton.setLayoutX(46);
        simulatorButton.setLayoutY(511);
        simulatorButton.setText("Simulator");
        simulatorButton.setTextFill(Paint.valueOf("#daecfb"));
        simulatorButton.setCursor(Cursor.OPEN_HAND);
        simulatorButton.setOnMouseClicked(mouseEvent -> {
            try {
                changeScene("/com/jmc/app/Simulator.fxml", (Stage) simulatorButton.getScene().getWindow(), o, null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        pane.getChildren().add(bank);
        pane.getChildren().add(profilIcon);
        pane.getChildren().add(startSeite);
        pane.getChildren().add(produkte);
        pane.getChildren().add(simulatorButton);

        borderPane.setLeft(pane);
    }
}
