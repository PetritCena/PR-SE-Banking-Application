package com.jmc.app.Models;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;

public class User {
    private final DatabaseConnector dbConnector;
    private final String email;
    private String password;
    private String firstName;
    private String lastName;
    private byte[] pic;
    private ArrayList<Account> accounts;


    public User(String firstName, String lastName, String email, String password, byte[] pic, ArrayList<Account> accounts) {
        this.dbConnector = new DatabaseConnector();
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pic = pic;
        this.accounts = accounts;
    }

    public String getEmail() { return email; }

    public String getPassword() { return password; }
    public void setPassword(String newPassword) throws SQLException {
        dbConnector.updateField(email, newPassword, "password");
        this.password = newPassword;
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String newFirstName) throws SQLException {
        dbConnector.updateField(email, newFirstName, "vorname");
        this.firstName = newFirstName;
    }

    public String getLastName() { return lastName; }
    public void setLastName(String newLastName) throws SQLException {
        dbConnector.updateField(email, newLastName, "nachname");
        this.lastName = lastName;
    }

    public byte[] getPic() { return pic;}
    public void setPic(File newPic) throws SQLException, IOException {
        dbConnector.savePhoto(email, newPic);
        this.pic = Files.readAllBytes(newPic.toPath());
    }
    public ArrayList<Account> getAccounts() { return accounts; }
    public void setAccounts(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }

    public Account getHauptkonto(){
        for (Account account : accounts) {
            if(account.getTyp().equals("Hauptkonto")) return account;
        }
        return null;
    }
}