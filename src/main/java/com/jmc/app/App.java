package com.jmc.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {

        // Set system properties for Oracle JDBC connection
        System.setProperty("oracle.net.tns_admin", "/Users/petritcena/Desktop/Wallet_E4XXMJ5EY9KFQZZ5");

        // Now try to connect using the JDBC URL
        try {
            Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@e4xxmj5ey9kfqzz5_high", "admin", "BigBankSoSe2024");
            // Do something with the connection
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Parent fxmlLoader = FXMLLoader.load(getClass().getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader, 520, 400);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
