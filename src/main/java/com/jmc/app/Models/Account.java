package com.jmc.app.Models;

import java.util.ArrayList;

public class Account {
    private final String iban;
    private final float saldo;
    private final String typ;
    private final User user;
    private final ArrayList<Card> cards;

    public Account(String iban, float saldo, String typ, User user, ArrayList<Card> cards) {
        this.iban = iban;
        this.saldo = saldo; //Bug
        this.typ = typ;
        this.user = user;
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
    public User getUser() {
        return user;
    }
    public ArrayList<Card> getCards() {
        return cards;
    }
    public String toString() {
        return iban;
    }
}