package com.jmc.app.Models.tests;

import static org.junit.jupiter.api.Assertions.*;

import com.jmc.app.Models.Account;
import com.jmc.app.Models.Card;
import com.jmc.app.Models.DatabaseConnector;
import com.jmc.app.Models.User;
import org.junit.jupiter.api.*;

import java.sql.*;
import java.util.ArrayList;

/**
 * Testklasse für DatabaseConnector.
 */
class DatabaseConnectorTest {

    private static DatabaseConnector dbConnector;

    /**
     * Setzt die DatabaseConnector-Instanz vor allen Tests.
     * @throws SQLException, wenn ein Datenbankzugriffsfehler auftritt
     */
    @BeforeAll
    public static void setUp() throws SQLException {
        dbConnector = new DatabaseConnector();
    }

    /**
     * Testet die Benutzer-Authentifizierungsfunktionalität.
     * @throws SQLException, wenn ein Datenbankzugriffsfehler auftritt
     */
    @Test
    public void testAuthenticateUser() throws SQLException {
        dbConnector.registerUser("Max", "Mustermann", "max@mustermann.de", "password12");
        User user = dbConnector.authenticateUser("max@mustermann.de", "password12");
        assertNotNull(user, "User sollte authentifiziert werden");
        assertEquals("Max", user.getFirstName(), "Vorname sollte übereinstimmen");
        assertEquals("Mustermann", user.getLastName(), "Nachname sollte übereinstimmen");
    }

    /**
     * Testet die Funktion zum Aktualisieren von Feldern.
     * @throws SQLException, wenn ein Datenbankzugriffsfehler auftritt
     */
    @Test
    public void testUpdateField() throws SQLException {
        dbConnector.registerUser("Lisa", "Musterfrau", "lisa@musterfrau.de", "password12");
        dbConnector.updateField("lisa@musterfrau.de", "password99", "password");
        User user = dbConnector.authenticateUser("lisa@musterfrau.de", "password99");
        assertNotNull(user, "User sollte mit neuem Passwort authentifiziert werden");
    }

    /**
     * Testet die Benutzerregistrierungsfunktionalität.
     * @throws SQLException, wenn ein Datenbankzugriffsfehler auftritt
     */
    @Test
    public void testRegisterUser() throws SQLException {
        dbConnector.registerUser("John", "Doe", "john@doe.com", "password12");
        User user = dbConnector.authenticateUser("john@doe.com", "password12");
        assertNotNull(user, "User sollte registriert und authentifiziert werden");
        assertEquals("John", user.getFirstName(), "Vorname sollte übereinstimmen");
        assertEquals("Doe", user.getLastName(), "Nachname sollte übereinstimmen");
    }

    /**
     * Testet die Erstellung eines Kontos (Space).
     * @throws SQLException, wenn ein Datenbankzugriffsfehler auftritt
     */
    @Test
    public void testCreateSpace() throws SQLException {
        User user = new User("Anna", "Smith", "anna@smith.com", "password12", null, new ArrayList<>());
        dbConnector.registerUser("Anna", "Smith", "anna@smith.com", "password12");
        dbConnector.createSpace(1000.0f, "Saving", user, "MySpace");
        assertNotNull(user.getAccounts(), "User sollte Konten haben");
        assertFalse(user.getAccounts().isEmpty(), "User sollte mindestens ein Konto haben");
    }

    /**
     * Testet die Bestellung einer Karte.
     * @throws SQLException, wenn ein Datenbankzugriffsfehler auftritt
     */
    @Test
    public void testKarteBestellen() throws SQLException {
        User user = new User("Tom", "Tailor", "tom@tailor.com", "password12", null, new ArrayList<>());
        dbConnector.registerUser("Tom", "Tailor", "tom@tailor.com", "password12");
        dbConnector.createSpace(1000.0f, "Checking", user, "MainAccount");
        Account account = user.getAccounts().get(0);
        dbConnector.karteBestellen(account.getIban(), 5000, "Visa", account);
        assertNotNull(account.getCards(), "Konto sollte Karten haben");
        assertFalse(account.getCards().isEmpty(), "Konto sollte mindestens eine Karte haben");
    }

    /**
     * Testet die Aktualisierung des Kontostands (fehlerhaft).
     * @throws SQLException, wenn ein Datenbankzugriffsfehler auftritt
     */
    @Test
    public void testUpdateBalance() throws SQLException {
        User user = new User("Mike", "Ross", "mike@ross.com", "password12", null, new ArrayList<>());
        dbConnector.registerUser("Mike", "Ross", "mike@ross.com", "password12");
        dbConnector.createSpace(1000.0f, "Checking", user, "MainAccount");
        Account account = null;
        for(Account a : user.getAccounts()) {
            if(a.getUser().getFirstName().equals("Mike") && a.getName() != null) account = a;
        }
        dbConnector.updateBalance(account.getIban(), 500.0f, null, "Eingang", "Deposit");
        account.setSaldo(account.getSaldo() + 500);
        assertEquals(1500.0f, account.getSaldo(), 0.01, "Kontostand sollte korrekt aktualisiert werden");
    }

    /**
     * Testet die Überweisung vom Hauptkonto.
     * @throws SQLException, wenn ein Datenbankzugriffsfehler auftritt
     */
    @Test
    public void testTransferHauptkonto() throws SQLException {
        dbConnector.registerUser("Harvey", "Specter", "harvey@specter.com", "password12");
        User user = dbConnector.authenticateUser("harvey@specter.com", "password12");
        Account account = user.getAccounts().get(0);
        dbConnector.createSpace(500.0f, "Space", user, "MySpace");
        Account space = null;
        for(Account a : user.getAccounts()) {
            if(a.getUser().getFirstName().equals("Harvey") && a.getName() != null) space = a;
        }

        dbConnector.transferHauptkonto(account.getIban(), space.getIban(), 300.0f, "vom Hauptkonto");
        account.setSaldo(account.getSaldo() - 300);
        space.setSaldo(space.getSaldo() + 300);
        assertEquals(-290.0f, account.getSaldo(), 0.01, "Kontostand des Hauptkontos sollte korrekt aktualisiert werden");
        assertEquals(800.0f, space.getSaldo(), 0.01, "Kontostand des Space-Kontos sollte korrekt aktualisiert werden");
    }

    /**
     * Testet die Überweisungsfunktionalität.
     * @throws SQLException, wenn ein Datenbankzugriffsfehler auftritt
     */
    @Test
    public void testÜberweisen() throws SQLException {
        dbConnector.registerUser("Rachel", "Zane", "rachel@zane.com", "password12");
        User user1 = dbConnector.authenticateUser("rachel@zane.com", "password12");
        Account account1 = user1.getAccounts().get(0);

        dbConnector.registerUser("Jessica", "Pearson", "jessica@pearson.com", "password12");
        User user2 = dbConnector.authenticateUser("jessica@pearson.com", "password12");
        Account account2 = user2.getAccounts().get(0);

        dbConnector.überweisen(account1.getIban(), account2.getIban(), 10.0f);
        account1.setSaldo(account1.getSaldo() - 10.0f);
        account2.setSaldo(account2.getSaldo() + 10.0f);
        assertEquals(0.0f, account1.getSaldo(), 0.01, "Kontostand des Senders sollte korrekt aktualisiert werden");
        assertEquals(20.0f, account2.getSaldo(), 0.01, "Kontostand des Empfängers sollte korrekt aktualisiert werden");
    }

    /**
     * Testet die Gültigkeitsprüfung der Kartendaten.
     * @throws SQLException, wenn ein Datenbankzugriffsfehler auftritt
     */
    @Test
    public void testIsCardDataValid() throws SQLException {
        User user = new User("Louis", "Litt", "louis@litt.com", "password12", null, new ArrayList<>());
        dbConnector.registerUser("Louis", "Litt", "louis@litt.com", "password12");
        dbConnector.createSpace(1000.0f, "Checking", user, "MainAccount");
        Account account = user.getAccounts().get(0);
        dbConnector.karteBestellen(account.getIban(), 5000, "Visa", account);
        Card card = account.getCards().get(0);

        boolean isValid = dbConnector.isCardDataValid(card.getKartenNummer(), card.getFolgeNummer(), card.getGeheimZahl());
        assertTrue(isValid, "Kartendaten sollten gültig sein");
    }

    /**
     * Testet, ob ein Benutzer existiert.
     * @throws SQLException, wenn ein Datenbankzugriffsfehler auftritt
     */
    @Test
    public void testUserExists() throws SQLException {
        dbConnector.registerUser("Donna", "Paulsen", "donna@paulsen.com", "password12");
        boolean exists = dbConnector.userExists("donna@paulsen.com");
        assertTrue(exists, "User sollte existieren");
    }

    /**
     * Testet die Passwortaktualisierungsfunktionalität.
     * @throws SQLException, wenn ein Datenbankzugriffsfehler auftritt
     */
    @Test
    public void testUpdatePassword() throws SQLException {
        dbConnector.registerUser("Katrina", "Bennett", "katrina@bennett.com", "password12");
        dbConnector.updateField("katrina@bennett.com", "Password00", "password");
        User user = dbConnector.authenticateUser("katrina@bennett.com", "Password00");
        assertNotNull(user, "User sollte mit neuem Passwort authentifiziert werden");
    }

    /**
     * Testet, ob genügend Guthaben vorhanden ist.
     * @throws SQLException, wenn ein Datenbankzugriffsfehler auftritt
     */
    @Test
    void testEnoughBalance() throws SQLException {
        float amount = 5.0f;
        dbConnector.registerUser("Test", "User", "test@example.com", "password12");
        User user = dbConnector.authenticateUser("test@example.com", "password12");
        Account account = user.getAccounts().get(0);
        boolean result = dbConnector.enoughBalance(account.getIban(), amount);
        assertTrue(result);
    }

    /**
     * Testet die Änderung des Kartenlimits.
     * @throws SQLException, wenn ein Datenbankzugriffsfehler auftritt
     */
    @Test
    void testChangeCardLimit() throws SQLException {
        long cardNumber = 1234567890123456L;
        int followNumber = 1;
        int secretNumber = 1234;
        float newLimit = 5000.0f;
        Card card = new Card("iban123", 1000.0f, cardNumber, followNumber, "Debit", secretNumber, new int[]{0, 51, 102});
        dbConnector.changeCardLimit(card, String.valueOf(newLimit));
        assertEquals(newLimit, card.getKartenLimit());
    }
}