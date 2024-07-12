package com.jmc.app.Models;

import java.util.ArrayList;

/**
 * Diese Klasse repräsentiert eine Karte mit grundlegenden Informationen
 * wie IBAN, Kartenlimit, Kartennummer, Folgnummer, Typ, Geheimzahl, Farbe und Transaktionen.
 */
public class Card {
    private final String iban;
    private float kartenLimit;
    private final long kartenNummer;
    private final int folgeNummer;
    private final String typ;
    private final int geheimZahl;
    private final int[] farbe;
    private ArrayList<Transaction> transactions;

    /**
     * Konstruktor zur Erstellung eines neuen Kartenobjekts.
     * @param iban ist die IBAN der Karte.
     * @param kartenLimit ist das Limit der Karte.
     * @param kartenNummer ist die Nummer der Karte.
     * @param folgeNummer ist die Folgnummer der Karte.
     * @param typ ist der Typ der Karte.
     * @param geheimZahl ist die Geheimzahl der Karte.
     * @param farbe ist die Farbe der Karte als Array von RGB-Werten.
     */
    public Card(String iban, float kartenLimit, long kartenNummer, int folgeNummer, String typ, int geheimZahl, int[] farbe) {
        this.iban = iban;
        this.kartenLimit = kartenLimit;
        this.kartenNummer = kartenNummer;
        this.folgeNummer = folgeNummer;
        this.typ = typ;
        this.geheimZahl = geheimZahl;
        this.farbe = farbe;
    }

    /**
     * Gibt die IBAN der Karte zurück.
     * @return Die IBAN der Karte.
     */
    public String getIban() {
        return iban;
    }

    /**
     * Gibt das Limit der Karte zurück.
     * @return Das Limit der Karte.
     */
    public float getKartenLimit() {
        return kartenLimit;
    }

    /**
     * Gibt die Nummer der Karte zurück.
     * @return Die Nummer der Karte.
     */
    public long getKartenNummer() {
        return kartenNummer;
    }

    /**
     * Gibt die Folgnummer der Karte zurück.
     * @return Die Folgnummer der Karte.
     */
    public int getFolgeNummer() {
        return folgeNummer;
    }

    /**
     * Gibt den Typ der Karte zurück.
     * @return Der Typ der Karte.
     */
    public String getTyp() {
        return typ;
    }

    /**
     * Gibt die Geheimzahl der Karte zurück.
     * @return Die Geheimzahl der Karte.
     */
    public int getGeheimZahl() {
        return geheimZahl;
    }

    /**
     * Setzt das Limit der Karte.
     * @param zahl ist das neue Limit der Karte.
     */
    public void setKartenLimit(float zahl) {
        kartenLimit = zahl;
    }

    /**
     * Gibt die Farbe der Karte als Array von RGB-Werten zurück.
     * @return Die Farbe der Karte.
     */
    public int[] getFarbe() {
        return farbe;
    }

    /**
     * Gibt die Liste der Transaktionen zurück, die mit der Karte verbunden sind.
     * @return Die Liste der Transaktionen.
     */
    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    /**
     * Setzt die Liste der Transaktionen, die mit der Karte verbunden sind.
     * @param transactions ist die neue Liste der Transaktionen.
     */
    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    /**
     * Gibt die Kartennummer als String zurück.
     * @return Die Kartennummer der Karte als String.
     */
    @Override
    public String toString() {
        return kartenNummer + "";
    }
}
