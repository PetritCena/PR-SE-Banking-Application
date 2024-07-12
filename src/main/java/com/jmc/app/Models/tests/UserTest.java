package com.jmc.app.Models.tests;

import com.jmc.app.Models.Account;
import com.jmc.app.Models.User;
import org.junit.jupiter.api.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Diese Klasse enthält Unit-Tests für die User-Klasse.
 */
class UserTest {

    private User user;

    /**
     * Richtet das User-Objekt vor jedem Test ein.
     * @throws SQLException wenn ein Datenbankzugriffsfehler auftritt.
     */
    @BeforeEach
    void setUp() throws SQLException {
        user = new User("John", "Doe", "john.doe@example.com", "password123", null, new ArrayList<>());
    }

    /**
     * Testet die getEmail-Methode.
     */
    @Test
    void testGetEmail() {
        assertEquals("john.doe@example.com", user.getEmail());
    }

    /**
     * Testet die getPassword-Methode.
     */
    @Test
    void testGetPassword() {
        assertEquals("password123", user.getPassword());
    }

    /**
     * Testet die setPassword-Methode.
     *
     * @throws SQLException wenn ein Datenbankzugriffsfehler auftritt.
     */
    @Test
    void testSetPassword() throws SQLException {
        user.setPassword("newPassword456");
        assertEquals("newPassword456", user.getPassword());
    }

    /**
     * Testet die getFirstName-Methode.
     */
    @Test
    void testGetFirstName() {
        assertEquals("John", user.getFirstName());
    }

    /**
     * Testet die setFirstName-Methode.
     *
     * @throws SQLException wenn ein Datenbankzugriffsfehler auftritt.
     */
    @Test
    void testSetFirstName() throws SQLException {
        user.setFirstName("Jane");
        assertEquals("Jane", user.getFirstName());
    }

    /**
     * Testet die getLastName-Methode.
     */
    @Test
    void testGetLastName() {
        assertEquals("Doe", user.getLastName());
    }

    /**
     * Testet die setLastName-Methode.
     * @throws SQLException wenn ein Datenbankzugriffsfehler auftritt.
     */
    @Test
    void testSetLastName() throws SQLException {
        user.setLastName("Smith");
        assertEquals("Smith", user.getLastName());
    }

    /**
     * Testet die getPic-Methode.
     */
    @Test
    void testGetPic() {
        assertNull(user.getPic());
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
        user.setPic(pic);
        assertNotNull(user.getPic());
        assertArrayEquals(Files.readAllBytes(pic.toPath()), user.getPic());
    }

    /**
     * Testet die getAccounts-Methode.
     */
    @Test
    void testGetAccounts() {
        assertNotNull(user.getAccounts());
    }

    /**
     * Testet die setAccounts-Methode.
     */
    @Test
    void testSetAccounts() {
        ArrayList<Account> accounts = new ArrayList<>();
        user.setAccounts(accounts);
        assertEquals(accounts, user.getAccounts());
    }

    /**
     * Testet die getHauptkonto-Methode.
     * @throws SQLException wenn ein Datenbankzugriffsfehler auftritt.
     */
    @Test
    void testGetHauptkonto() throws SQLException {
        Account hauptkonto = new Account("Hauptkonto", "DE98765432109876543210", 5000.0f, "Hauptkonto", user, null, new ArrayList<>(), new ArrayList<>());
        ArrayList<Account> accounts = new ArrayList<>();
        accounts.add(hauptkonto);
        user.setAccounts(accounts);
        assertEquals(hauptkonto, user.getHauptkonto());
    }
}