package com.jmc.app.Models;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Diese Klasse repräsentiert ein Konto mit grundlegenden Informationen
 * wie Name, IBAN, Saldo, Typ, Benutzer, Profilbild, Karten und Transaktionen.
 */
public class Account {
    private final String name;
    private final String iban;
    private float saldo;
    private final String typ;
    private final User user;
    private byte[] pic;
    private ArrayList<Card> cards;
    private final ArrayList<Transaction> transactions;
    private final DatabaseConnector dbConnector;

    /**
     * Konstruktor zur Erstellung eines neuen Kontoobjekts.
     * @param name ist der Name des Kontos.
     * @param iban ist die IBAN des Kontos.
     * @param saldo ist der Anfangssaldo des Kontos.
     * @param typ ist der Typ des Kontos.
     * @param user ist der Benutzer, dem das Konto gehört.
     * @param pic ist das Profilbild des Kontos als Byte-Array.
     * @param cards ist eine Liste von Karten, die mit dem Konto verbunden sind.
     * @param transactions ist eine Liste von Transaktionen, die mit dem Konto verbunden sind.
     * @throws SQLException wird geworfen, wenn this.dbConnector = new DatabaseConnector() einen Fehler zurückgibt.
     */
    public Account(String name, String iban, float saldo, String typ, User user, byte[] pic, ArrayList<Card> cards, ArrayList<Transaction> transactions) throws SQLException {
        this.dbConnector = new DatabaseConnector();
        this.name = name;
        this.iban = iban;
        this.saldo = saldo;
        this.typ = typ;
        this.user = user;
        this.pic = pic;
        this.cards = cards;
        this.transactions = transactions;
    }

    /**
     * Gibt den Namen des Kontos zurück.
     * @return Der Name des Kontos.
     */
    public String getName() { return name; }

    /**
     * Gibt die IBAN des Kontos zurück.
     * @return Die IBAN des Kontos.
     */
    public String getIban() {
        return iban;
    }

    /**
     * Gibt den aktuellen Saldo des Kontos zurück.
     * @return Der aktuelle Saldo des Kontos.
     */
    public float getSaldo() {
        return saldo;
    }

    /**
     * Setzt den Saldo des Kontos.
     * @param saldo Der neue Saldo des Kontos.
     */
    public void setSaldo(float saldo) { this.saldo = saldo; }

    /**
     * Gibt den Typ des Kontos zurück.
     * @return Der Typ des Kontos.
     */
    public String getTyp() { return typ; }

    /**
     * Gibt den Benutzer zurück, dem das Konto gehört.
     * @return Der Benutzer, dem das Konto gehört.
     */
    public User getUser() {
        return user;
    }

    /**
     * Gibt das Profilbild des Kontos als Byte-Array zurück.
     * @return Das Profilbild des Kontos.
     */
    public byte[] getPic() { return pic; }

    /**
     * Setzt das Profilbild des Kontos auf ein neues Bild.
     * @param newPic ist die Datei des neuen Profilbildes.
     * @throws SQLException wird geworfen, wenn dbConnector.savePhoto(this, newPic) einen Fehler zurückgibt.
     * @throws IOException wird geworfen, wenn this.pic = Files.readAllBytes(newPic.toPath()) einen Fehler zurückgibt.
     */
    public void setPic(File newPic) throws SQLException, IOException {
        dbConnector.savePhoto(this, newPic);
        this.pic = Files.readAllBytes(newPic.toPath());
    }

    /**
     * Gibt die Liste der Karten zurück, die mit dem Konto verbunden sind.
     * @return Die Liste der Karten.
     */
    public ArrayList<Card> getCards() {
        return cards;
    }

    /**
     * Setzt die Liste der Karten, die mit dem Konto verbunden sind.
     * @param cards Die neue Liste der Karten.
     */
    public void setCards(ArrayList<Card> cards) { this.cards = cards; }

    /**
     * Gibt die Liste der Transaktionen zurück, die mit dem Konto verbunden sind.
     * @return Die Liste der Transaktionen.
     */
    public ArrayList<Transaction> getTransactions() { return transactions; }

    /**
     * Fügt eine Transaktion zur Liste der Transaktionen hinzu.
     * @param transaction Die hinzuzufügende Transaktion.
     */
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    /**
     * Gibt die IBAN des Kontos als String zurück.
     * @return Die IBAN des Kontos.
     */
    @Override
    public String toString() {
        return iban;
    }
}
