package com.jmc.app.Models;

import java.util.ArrayList;

public class Account {
    private final String iban;
    private float saldo;
    private final String typ;
    private final User user;
    private final ArrayList<Card> cards;
    private final ArrayList<Transaction> transactions;

    public Account(String iban, float saldo, String typ, User user, ArrayList<Card> cards, ArrayList<Transaction> transactions) {
        this.iban = iban;
        this.saldo = saldo;
        this.typ = typ;
        this.user = user;
        this.cards = cards;
        this.transactions = transactions;
    }

    public String getIban() {
        return iban;
    }
    public float getSaldo() {
        return saldo;
    }
    public void setSaldo(float saldo) { this.saldo = saldo; }
    public String getTyp() { return typ; }
    public User getUser() {
        return user;
    }
    public ArrayList<Card> getCards() {
        return cards;
    }
    public ArrayList<Transaction> getTransactions() { return transactions; }
    public String toString() {
        return iban;
    }
}