package com.jmc.app.Models.tests;

import com.jmc.app.Models.Transaction;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Diese Klasse enthält Unit-Tests für die Transaction-Klasse.
 */
class TransactionTest {

    private Transaction transaction;

    /**
     * Richtet das Transaction-Objekt vor jedem Test ein.
     */
    @BeforeEach
    void setUp() {
        transaction = new Transaction(100.0f, "Eingang", "AT98765432101234567890", "AT12345678901234567890", "Test", 1, 1234567890123456L);
    }

    /**
     * Testet die getBetrag-Methode.
     */
    @Test
    void testGetBetrag() {
        assertEquals(100.0f, transaction.getBetrag());
    }

    /**
     * Testet die getEingangAusgang-Methode.
     */
    @Test
    void testGetEingangAusgang() {
        assertEquals("Eingang", transaction.getEingangAusgang());
    }

    /**
     * Testet die getEmpfaengerIban-Methode.
     */
    @Test
    void testGetEmpfaengerIban() {
        assertEquals("AT98765432101234567890", transaction.getEmpfaengerIban());
    }

    /**
     * Testet die getSenderIban-Methode.
     */
    @Test
    void testGetSenderIban() {
        assertEquals("AT12345678901234567890", transaction.getSenderIban());
    }

    /**
     * Testet die getVerwendungszweck-Methode.
     */
    @Test
    void testGetVerwendungszweck() {
        assertEquals("Test", transaction.getVerwendungszweck());
    }

    /**
     * Testet die getTransaktionsnummer-Methode.
     */
    @Test
    void testGetTransaktionsnummer() {
        assertEquals(1, transaction.getTransaktionsnummer());
    }

    /**
     * Testet die getKartennummer-Methode.
     */
    @Test
    void testGetKartennummer() {
        assertEquals(1234567890123456L, transaction.getKartennummer());
    }
}
