package com.jmc.app.Models;

import java.util.ArrayList;

public class Transactions {
    private float betrag;
    private String eingangAusgang;
    private String empfaengerIban;
    private String senderIban;
    private String verwendungszweck;
    private int transaktionsnummer;
    private long kartennummer;
    private ArrayList<Transactions> transactions;


    public Transactions(float betrag, String eingangAusgang, String empfaengerIban, String senderIban, String verwendungszweck, int transaktionsnummer, long kartennummer, ArrayList<Transactions> transactions) {
        this.betrag = betrag;
        this.eingangAusgang = eingangAusgang;
        this.empfaengerIban = empfaengerIban;
        this.senderIban = senderIban;
        this.verwendungszweck = verwendungszweck;
        this.transaktionsnummer = transaktionsnummer;
        this.kartennummer = kartennummer;
        this.transactions = transactions;
    }

    public float getBetrag() {
        return betrag;
    }
    public String getEingangAusgang() {
        return eingangAusgang;
    }
    public String getEmpfaengerIban() {
        return empfaengerIban;
    }
    public String getSenderIban() {
        return senderIban;
    }
    public String getVerwendungszweck() {
        return verwendungszweck;
    }
    public int getTransaktionsnummer() {
        return transaktionsnummer;
    }
    public long getKartennummer() {
        return kartennummer;
    }
    public ArrayList<Transactions> getTransactions() {
        return transactions;
    }
}
