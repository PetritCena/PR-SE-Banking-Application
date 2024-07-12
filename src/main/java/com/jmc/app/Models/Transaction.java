package com.jmc.app.Models;

/**
 * Diese Klasse repräsentiert eine Transaktion mit grundlegenden Informationen
 * wie Betrag, Eingangs-/Ausgangskennzeichnung, Empfänger-IBAN, Sender-IBAN,
 * Verwendungszweck, Transaktionsnummer und Kartennummer.
 */
public class Transaction {
    private final float betrag;
    private final String eingangAusgang;
    private final String empfaengerIban;
    private final String senderIban;
    private final String verwendungszweck;
    private final int transaktionsnummer;
    private final long kartennummer;

    /**
     * Konstruktor zur Erstellung eines neuen Transaktionsobjekts.
     * @param betrag ist der Betrag der Transaktion.
     * @param eingangAusgang Kennzeichnung, ob es sich um eine Eingangs- oder Ausgangstransaktion handelt.
     * @param empfaengerIban ist die IBAN des Empfängers.
     * @param senderIban ist die IBAN des Senders.
     * @param verwendungszweck ist der Verwendungszweck der Transaktion.
     * @param transaktionsnummer ist die eindeutige Transaktionsnummer.
     * @param kartennummer ist die Kartennummer, die für die Transaktion verwendet wurde.
     */
    public Transaction(float betrag, String eingangAusgang, String empfaengerIban, String senderIban, String verwendungszweck, int transaktionsnummer, long kartennummer) {
        this.betrag = betrag;
        this.eingangAusgang = eingangAusgang;
        this.empfaengerIban = empfaengerIban;
        this.senderIban = senderIban;
        this.verwendungszweck = verwendungszweck;
        this.transaktionsnummer = transaktionsnummer;
        this.kartennummer = kartennummer;
    }

    /**
     * Gibt den Betrag der Transaktion zurück.
     * @return Der Betrag der Transaktion.
     */
    public float getBetrag() {
        return betrag;
    }

    /**
     * Gibt die Kennzeichnung zurück, ob es sich um eine Eingangs- oder Ausgangstransaktion handelt.
     * @return Die Kennzeichnung der Transaktion.
     */
    public String getEingangAusgang() {
        return eingangAusgang;
    }

    /**
     * Gibt die IBAN des Empfängers zurück.
     * @return Die IBAN des Empfängers.
     */
    public String getEmpfaengerIban() {
        return empfaengerIban;
    }

    /**
     * Gibt die IBAN des Senders zurück.
     * @return Die IBAN des Senders.
     */
    public String getSenderIban() {
        return senderIban;
    }

    /**
     * Gibt den Verwendungszweck der Transaktion zurück.
     * @return Der Verwendungszweck der Transaktion.
     */
    public String getVerwendungszweck() {
        return verwendungszweck;
    }

    /**
     * Gibt die eindeutige Transaktionsnummer zurück.
     * @return Die Transaktionsnummer.
     */
    public int getTransaktionsnummer() {
        return transaktionsnummer;
    }

    /**
     * Gibt die Kartennummer zurück, die für die Transaktion verwendet wurde.
     * @return Die Kartennummer.
     */
    public long getKartennummer() {
        return kartennummer;
    }
}

