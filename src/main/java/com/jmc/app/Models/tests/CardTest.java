package com.jmc.app.Models.tests;

import com.jmc.app.Models.Card;
import com.jmc.app.Models.Transaction;
import org.junit.jupiter.api.*;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Diese Klasse enthält Unit-Tests für die Card-Klasse.
 */
class CardTest {

    private Card card;

    /**
     * Richtet das Card-Objekt vor jedem Test ein.
     */
    @BeforeEach
    void setUp() {
        int[] farbe = {0, 51, 102};
        card = new Card("AT12345678901234567890", 1000.0f, 1234567890123456L, 1, "Debit", 1234, farbe);
    }

    /**
     * Testet die getIban-Methode.
     */
    @Test
    void testGetIban() {
        assertEquals("AT12345678901234567890", card.getIban());
    }

    /**
     * Testet die getKartenLimit-Methode.
     */
    @Test
    void testGetKartenLimit() {
        assertEquals(1000.0f, card.getKartenLimit());
    }

    /**
     * Testet die setKartenLimit-Methode.
     */
    @Test
    void testSetKartenLimit() {
        card.setKartenLimit(2000.0f);
        assertEquals(2000.0f, card.getKartenLimit());
    }

    /**
     * Testet die getKartenNummer-Methode.
     */
    @Test
    void testGetKartenNummer() {
        assertEquals(1234567890123456L, card.getKartenNummer());
    }

    /**
     * Testet die getFolgeNummer-Methode.
     */
    @Test
    void testGetFolgeNummer() {
        assertEquals(1, card.getFolgeNummer());
    }

    /**
     * Testet die getTyp-Methode.
     */
    @Test
    void testGetTyp() {
        assertEquals("Debit", card.getTyp());
    }

    /**
     * Testet die getGeheimZahl-Methode.
     */
    @Test
    void testGetGeheimZahl() {
        assertEquals(1234, card.getGeheimZahl());
    }

    /**
     * Testet die getFarbe-Methode.
     */
    @Test
    void testGetFarbe() {
        assertArrayEquals(new int[]{0, 51, 102}, card.getFarbe());
    }

    /**
     * Testet die getTransactions-Methode.
     */
    @Test
    void testGetTransactions() {
        assertNull(card.getTransactions());
    }

    /**
     * Testet die setTransactions-Methode.
     */
    @Test
    void testSetTransactions() {
        ArrayList<Transaction> transactions = new ArrayList<>();
        card.setTransactions(transactions);
        assertEquals(transactions, card.getTransactions());
    }

    /**
     * Testet die toString-Methode.
     */
    @Test
    void testToString() {
        assertEquals("1234567890123456", card.toString());
    }
}