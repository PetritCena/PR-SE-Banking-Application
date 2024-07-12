package com.jmc.app.Models.tests;

import com.jmc.app.Models.Account;
import com.jmc.app.Models.Card;
import com.jmc.app.Models.Transaction;
import com.jmc.app.Models.User;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

/**
 * Diese Klasse enthält Unit-Tests für die Account-Klasse.
 */
class AccountTest {

    private User user;
    private Account account;

    /**
     * Richtet die User- und Account-Objekte vor jedem Test ein.
     * @throws SQLException wenn ein Datenbankzugriffsfehler auftritt.
     */
    @BeforeEach
    void setUp() throws SQLException {
        user = new User("John", "Doe", "john.doe@example.com", "password123", null, new ArrayList<>());
        account = new Account("Test Account", "AT12345678901234567890", 1000.0f, "Checking", user, null, new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Testet die getName-Methode.
     */
    @Test
    void testGetName() {
        assertEquals("Test Account", account.getName());
    }

    /**
     * Testet die getIban-Methode.
     */
    @Test
    void testGetIban() {
        assertEquals("AT12345678901234567890", account.getIban());
    }

    /**
     * Testet die getSaldo-Methode.
     */
    @Test
    void testGetSaldo() {
        assertEquals(1000.0f, account.getSaldo());
    }

    /**
     * Testet die setSaldo-Methode.
     */
    @Test
    void testSetSaldo() {
        account.setSaldo(2000.0f);
        assertEquals(2000.0f, account.getSaldo());
    }

    /**
     * Testet die getTyp-Methode.
     */
    @Test
    void testGetTyp() {
        assertEquals("Checking", account.getTyp());
    }

    /**
     * Testet die getUser-Methode.
     */
    @Test
    void testGetUser() {
        assertEquals(user, account.getUser());
    }

    /**
     * Testet die setPic-Methode.
     * @throws SQLException wenn ein Datenbankzugriffsfehler auftritt.
     * @throws IOException wenn ein Ein-/Ausgabefehler auftritt.
     */
    @Test
    void testSetPic() throws SQLException, IOException {
        File pic = new File("test_pic.jpg");
        Files.write(pic.toPath(), new byte[10]); // Dummy-Dateiinhalt
        account.setPic(pic);
        assertNotNull(account.getPic());
        assertArrayEquals(Files.readAllBytes(pic.toPath()), account.getPic());
    }

    /**
     * Testet die getCards-Methode.
     */
    @Test
    void testGetCards() {
        assertNotNull(account.getCards());
    }

    /**
     * Testet die setCards-Methode.
     */
    @Test
    void testSetCards() {
        ArrayList<Card> cards = new ArrayList<>();
        account.setCards(cards);
        assertEquals(cards, account.getCards());
    }

    /**
     * Testet die getTransactions-Methode.
     */
    @Test
    void testGetTransactions() {
        assertNotNull(account.getTransactions());
    }

    /**
     * Testet die addTransaction-Methode.
     */
    @Test
    void testAddTransaction() {
        Transaction transaction = new Transaction(100.0f, "Eingang", "AT98765432101234567890", "AT12345678901234567890", "Test", 1, 1234567890123456L);
        account.addTransaction(transaction);
        assertTrue(account.getTransactions().contains(transaction));
    }

    /**
     * Testet die toString-Methode.
     */
    @Test
    void testToString() {
        assertEquals("AT12345678901234567890", account.toString());
    }
}

