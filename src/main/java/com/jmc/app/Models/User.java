package com.jmc.app.Models;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Diese Klasse repräsentiert einen Benutzer mit grundlegenden Informationen
 * wie Vorname, Nachname, E-Mail, Passwort, Profilbild und Konten.
 */
public class User {
    private final DatabaseConnector dbConnector;
    private final String email;
    private String password;
    private String firstName;
    private String lastName;
    private byte[] pic;
    private ArrayList<Account> accounts;

    /**
     * Konstruktor zur Erstellung eines neuen Benutzerobjekts.
     * @param firstName ist der Vorname des Benutzers.
     * @param lastName ist der Nachname des Benutzers.
     * @param email ist die E-Mail-Adresse des Benutzers.
     * @param password ist das Passwort des Benutzers.
     * @param pic ist das Profilbild des Benutzers als Byte-Array.
     * @param accounts ist eine Liste von Konten, die dem Benutzer gehören.
     * @throws SQLException wird geworfen, wenn this.dbConnector = new DatabaseConnector() einen Fehler zurückgibt.
     */
    public User(String firstName, String lastName, String email, String password, byte[] pic, ArrayList<Account> accounts) throws SQLException {
        this.dbConnector = new DatabaseConnector();
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pic = pic;
        this.accounts = accounts;
    }

    /**
     * Gibt die E-Mail-Adresse des Benutzers zurück.
     * @return Die E-Mail-Adresse des Benutzers.
     */
    public String getEmail() { return email; }

    /**
     * Gibt das Passwort des Benutzers zurück.
     * @return Das Passwort des Benutzers.
     */
    public String getPassword() { return password; }

    /**
     * Setzt das Passwort des Benutzers auf ein neues Passwort.
     * @param newPassword ist das neue Passwort.
     * @throws SQLException wird geworfen, wenn dbConnector.updateField(email, newPassword, "password") einen Fehler zurückgibt.
     */
    public void setPassword(String newPassword) throws SQLException {
        dbConnector.updateField(email, newPassword, "password");
        this.password = newPassword;
    }

    /**
     * Gibt den Vornamen des Benutzers zurück.
     * @return Der Vorname des Benutzers.
     */
    public String getFirstName() { return firstName; }

    /**
     * Setzt den Vornamen des Benutzers auf einen neuen Vornamen.
     * @param newFirstName ist der neue Vorname.
     * @throws SQLException wird geworfen, wenn dbConnector.updateField(email, newFirstName, "vorname") einen Fehler zurückgibt.
     */
    public void setFirstName(String newFirstName) throws SQLException {
        dbConnector.updateField(email, newFirstName, "vorname");
        this.firstName = newFirstName;
    }

    /**
     * Gibt den Nachnamen des Benutzers zurück.
     * @return Der Nachname des Benutzers.
     */
    public String getLastName() { return lastName; }

    /**
     * Setzt den Nachnamen des Benutzers auf einen neuen Nachnamen.
     * @param newLastName ist der neue Nachname.
     * @throws SQLException wird geworfen, wenn dbConnector.updateField(email, newLastName, "nachname") einen Fehler zurückgibt.
     */
    public void setLastName(String newLastName) throws SQLException {
        dbConnector.updateField(email, newLastName, "nachname");
        this.lastName = newLastName;
    }

    /**
     * Gibt das Profilbild des Benutzers als Byte-Array zurück.
     * @return Das Profilbild des Benutzers.
     */
    public byte[] getPic() { return pic;}

    /**
     * Setzt das Profilbild des Benutzers auf ein neues Bild.
     * @param newPic ist die Datei des neuen Profilbildes.
     * @throws SQLException wird geworfen, wenn dbConnector.savePhoto(this, newPic) einen Fehler zurückgibt.
     * @throws IOException wird geworfen, wenn this.pic = Files.readAllBytes(newPic.toPath()) einen Fehler zurückgibt.
     */
    public void setPic(File newPic) throws SQLException, IOException {
        dbConnector.savePhoto(this, newPic);
        this.pic = Files.readAllBytes(newPic.toPath());
    }

    /**
     * Gibt die Liste der Konten des Benutzers zurück.
     * @return Die Liste der Konten des Benutzers.
     */
    public ArrayList<Account> getAccounts() { return accounts; }

    /**
     * Setzt die Liste der Konten des Benutzers.
     * @param accounts Die neue Liste der Konten.
     */
    public void setAccounts(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }

    /**
     * Gibt das Hauptkonto des Benutzers zurück, falls vorhanden.
     * @return Das Hauptkonto des Benutzers oder null, wenn kein Hauptkonto vorhanden ist.
     */
    public Account getHauptkonto() {
        for (Account account : accounts) {
            if (account.getTyp().equals("Hauptkonto")) return account;
        }
        return null;
    }
}
