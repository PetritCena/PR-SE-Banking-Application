package com.jmc.app.Controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Diese Klasse dient zur Erstellung der linken Leiste mit den Funktionsbuttons und zum Wechseln der Seiten im UI.
 */
public class SceneChanger {

    private static final int MAX_CLICKS = 2;
    private static final int TIME_LIMIT_MINUTES = 1;

    private static AtomicInteger clickCount = new AtomicInteger(0);
    private static LocalDateTime firstClickTime;

    /**
     * Diese Methode wechselt die aktuelle Seite mit der gegebenen Seite (fxmlPath) aus.
     * @param fxmlPath ist die Seite, zu der gewechselt werden soll.
     * @param stage ist der Auslöser (zb. Button, der gedrückt wird).
     * @param o ist entweder eine User-Instanz, Account-Instanz oder Card-Instanz.
     * @param o2 ist entweder eine User-Instanz oder Account-Instanz.
     * @throws IOException wird geworfen, wenn Parent root = loader.load() einen Fehler zurückgibt.
     */
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

    /**
     * Diese Methode erstellt die linke Leiste mit den Funktionsbuttons.
     * @param borderPane ist eine BorderPane-Instanz, worauf die Leiste erstellt wird.
     * @param o ist eine User-Instanz.
     */
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
        startSeite.setLayoutY(250);
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
        produkte.setLayoutY(343);
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
        simulatorButton.setLayoutY(436);
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

        Button kryptoButton = new Button();
        kryptoButton.setPrefHeight(40);
        kryptoButton.setPrefWidth(90);
        kryptoButton.setStyle("-fx-background-color: #35495a; -fx-background-radius: 4");
        kryptoButton.setLayoutX(46);
        kryptoButton.setLayoutY(529);
        kryptoButton.setText("Krypto");
        kryptoButton.setTextFill(Paint.valueOf("#daecfb"));
        kryptoButton.setCursor(Cursor.OPEN_HAND);
        kryptoButton.setOnMouseClicked(mouseEvent -> {
            if (firstClickTime == null || firstClickTime.plusMinutes(TIME_LIMIT_MINUTES).isBefore(LocalDateTime.now())) {
                firstClickTime = LocalDateTime.now();
                clickCount.set(0);
                kryptoButton.setDisable(false);
            }

            if (clickCount.incrementAndGet() > MAX_CLICKS) {
                kryptoButton.setDisable(true);
                Timeline enableButtonTimeline = new Timeline(new KeyFrame(Duration.minutes(TIME_LIMIT_MINUTES), event -> {
                    kryptoButton.setDisable(false);
                    clickCount.set(0);
                    firstClickTime = null;
                }));
                enableButtonTimeline.setCycleCount(1);
                enableButtonTimeline.play();
            } else {
                try {
                    changeScene("/com/jmc/app/KryptoSeite.fxml", (Stage) kryptoButton.getScene().getWindow(), o, null);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });


        pane.getChildren().add(bank);
        pane.getChildren().add(profilIcon);
        pane.getChildren().add(startSeite);
        pane.getChildren().add(produkte);
        pane.getChildren().add(simulatorButton);
        pane.getChildren().add(kryptoButton);

        borderPane.setLeft(pane);
    }
}
