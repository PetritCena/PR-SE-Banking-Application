package com.jmc.app.Models;

public class Card {
    private final String iban;
    private final float kartenLimit;
    private final float kartenNummer;
    private final int folgeNummer;
    private final String typ;
    private final int geheimZahl;

    public Card(String iban, float kartenLimit, float kartenNummer, int folgeNummer, String typ, int geheimZahl) {
        this.iban = iban;
        this.kartenLimit = kartenLimit;
        this.kartenNummer = kartenNummer;
        this.folgeNummer = folgeNummer;
        this.typ = typ;
        this.geheimZahl = geheimZahl;
    }

    public String getIban() {
        return iban;
    }
    public float getKartenLimit() {
        return kartenLimit;
    }
    public float getKartenNummer() {
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
}
