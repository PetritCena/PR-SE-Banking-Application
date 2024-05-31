package com.jmc.app.Models;

public class Transaction {
    private final float betrag;
    private final String eingangAusgang;
    private final String empfaengerIban;
    private final String senderIban;
    private final String verwendungszweck;
    private final int transaktionsnummer;
    private final long kartennummer;

    public Transaction(float betrag, String eingangAusgang, String empfaengerIban, String senderIban, String verwendungszweck, int transaktionsnummer, long kartennummer) {
        this.betrag = betrag;
        this.eingangAusgang = eingangAusgang;
        this.empfaengerIban = empfaengerIban;
        this.senderIban = senderIban;
        this.verwendungszweck = verwendungszweck;
        this.transaktionsnummer = transaktionsnummer;
        this.kartennummer = kartennummer;
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
}
