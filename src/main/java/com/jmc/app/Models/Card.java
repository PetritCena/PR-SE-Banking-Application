package com.jmc.app.Models;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static javafx.scene.paint.Color.rgb;

public class Card {
    private final String iban;
    private float kartenLimit;
    private final long kartenNummer;
    private final int folgeNummer;
    private final String typ;
    private final int geheimZahl;

    //private ArrayList<int[]> farben = new ArrayList<>(Arrays.asList(new int[]{0,51,102}, new int[]{192,192,192}, new int[]{211,211,211}, new int[]{176,224,230}, new int[]{163,100,100}));

    public int[] getFarbe() {
        return farbe;
    }

    private final int[] farbe;
    Random random = new Random();
    public Card(String iban, float kartenLimit, long kartenNummer, int folgeNummer, String typ, int geheimZahl, int[] farbe) {
        this.iban = iban;
        this.kartenLimit = kartenLimit;
        this.kartenNummer = kartenNummer;
        this.folgeNummer = folgeNummer;
        this.typ = typ;
        this.geheimZahl = geheimZahl;
        //this.farbe = farben.get(random.nextInt(5));
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
}
