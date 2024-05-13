package com.jmc.app.Models;

import java.util.ArrayList;

public class Account {
    private final String iban;
    private final float saldo;
    private final String typ;
    private final String user_email;
    private final ArrayList<Card> cards;

    public Account(String iban, float saldo, String typ, String user_email, ArrayList<Card> cards) {
        this.iban = iban;
        this.saldo = saldo;
        this.typ = typ;
        this.user_email = user_email;
        this.cards = cards;
    }

    public String getIban() {
        return iban;
    }
    public float getSaldo() {
        return saldo;
    }
    public String getTyp() {
        return typ;
    }
    public String getUser_email() {
        return user_email;
    }
    public ArrayList<Card> getCards() {
        return cards;
    }
    public String toString() {
        return iban;
    }
}