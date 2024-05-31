package com.jmc.app.Models;

import java.util.ArrayList;
import java.util.Random;


public class Card {
    private final String iban;
    private float kartenLimit;
    private final long kartenNummer;
    private final int folgeNummer;
    private final String typ;
    private final int geheimZahl;
    private final int[] farbe;
    private ArrayList<Transaction> transactions;

    public Card(String iban, float kartenLimit, long kartenNummer, int folgeNummer, String typ, int geheimZahl, int[] farbe) {
        this.iban = iban;
        this.kartenLimit = kartenLimit;
        this.kartenNummer = kartenNummer;
        this.folgeNummer = folgeNummer;
        this.typ = typ;
        this.geheimZahl = geheimZahl;
        this.farbe = farbe;
    }

    public String getIban() {
        return iban;
    }
    public float getKartenLimit() {
        return kartenLimit;
    }
    public long getKartenNummer() {
        return kartenNummer;
    }
    public int getFolgeNummer() {
        return folgeNummer;
    }
    public String getTyp() {
        return typ;
    }
    public int getGeheimZahl() {
        return geheimZahl;
    }
    public void setKartenLimit(float zahl){
        kartenLimit = zahl;
    }
    public int[] getFarbe() {
        return farbe;
    }
    public ArrayList<Transaction> getTransactions() { return transactions; }
    public void setTransactions(ArrayList<Transaction> transactions) { this.transactions = transactions; }
    public String toString(){ return kartenNummer + ""; }
}
