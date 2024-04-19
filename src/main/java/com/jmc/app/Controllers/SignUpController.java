package com.jmc.app.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;


public class SignUpController {

    // Datenbankverbindungsparameter (an deine Datenbank anpassen)
    private final String USER = "admin";
    private final String PWD = "BigBankSoSe2024";
    private final String URL = "jdbc:oracle:thin:@e4xxmj5ey9kfqzz5_high?TNS_ADMIN=/Users/petritcena/Desktop/Wallet_E4XXMJ5EY9KFQZZ5";

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private PasswordField passwordAgainTextField;

    @FXML
    private Button registerButton;

   @FXML
   private Label messageLabel;

   @FXML
   private Button haveAcccountButton;

   public void registerButtonAction(ActionEvent actionEvent) throws IOException {
       if(firstNameTextField.getText().isBlank() || lastNameTextField.getText().isBlank() || emailTextField.getText().isBlank() || passwordTextField.getText().isBlank() || passwordAgainTextField.getText().isBlank()) {
           messageLabel.setText("Please fill in all the fields");
           return;
       }
       else if(!passwordTextField.getText().equals(passwordAgainTextField.getText())) {
           messageLabel.setText("Passwords do not match");
           return;
       }

       messageLabel.setText("");

       String firstName = firstNameTextField.getText();
       String lastName = lastNameTextField.getText();
       String email = emailTextField.getText();
       String password = passwordTextField.getText();

       try(Connection con = DriverManager.getConnection(URL, USER, PWD)) {
           String query = "INSERT INTO users (vorname, nachname, email, password) \n"
                   + "VALUES (?, ?, ?, ?)";

           try (PreparedStatement insert = con.prepareStatement(query)) {
               String select = "SELECT email \n"
                       + "FROM users";

               try(PreparedStatement stmt = con.prepareStatement(select)){
                   ResultSet rs = stmt.executeQuery();
                   while(rs.next()) {
                       if(email.equals(rs.getString("email"))) {
                           messageLabel.setText("Email is already in use");
                           return;
                       }
                   }
               }catch (SQLException e) {
                   System.out.println("Could not select");
                   e.printStackTrace(System.err);
               }

               messageLabel.setText("");
               insert.setString(1, firstName);
               insert.setString(2, lastName);
               insert.setString(3, email);
               insert.setString(4, password);
               insert.executeUpdate();
               //con.commit();

           }catch (SQLException e) {
               System.out.println("Could not insert into users");
               e.printStackTrace(System.err);
               try {
                   System.out.println("Rolling back transaction");
                   con.rollback();
               }catch (SQLException e1) {
                   System.out.println("Rolling back failed");
                   e1.printStackTrace(System.err);
               }
           }
       }
       catch(SQLException ex){
           System.out.println("failed1");
           ex.printStackTrace(System.err);
       }
       Stage stage = (Stage) registerButton.getScene().getWindow();
       loadLoginView(stage);
   }

    private void loadLoginView(Stage stage) throws IOException {
       Parent fxmlLoader = FXMLLoader.load(getClass().getResource("/com/jmc/app/login.fxml"));
       Scene scene = new Scene(fxmlLoader, 520, 400);
       stage.setTitle("Signup");
       stage.setScene(scene);
       stage.show();

    }

    public void haveAcccountButtonAction(ActionEvent actionEvent) throws IOException {
       Stage stage = (Stage) haveAcccountButton.getScene().getWindow();
       loadLoginView(stage);
    }
}