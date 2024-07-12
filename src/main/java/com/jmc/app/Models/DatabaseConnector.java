package com.jmc.app.Models;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * Diese Klasse dient zur Verbindung mit der Oracle Datenbank.
 */
public class DatabaseConnector {

    private final String CONNECTION_STRING = "jdbc:oracle:thin:@e4xxmj5ey9kfqzz5_high?TNS_ADMIN=db/Wallet_E4XXMJ5EY9KFQZZ5";
    private final String USER = "admin";
    private final String PWD = "BigBankSoSe2024";

    Random random = new Random();
    private ArrayList<int[]> farben = new ArrayList<>(Arrays.asList(new int[]{0, 51, 102}, new int[]{192, 192, 192}, new int[]{211, 211, 211}, new int[]{176, 224, 230}, new int[]{163, 100, 100}));
    private final Connection con;

    /**
     * Dieser Konstruktor erstellt eine Instanz von DatabaseConnector. Connection con bekommt einen Wert.
     * @throws SQLException wird geworfen, wenn keine Verbindung zur Datenbank aufgebaut werden kann.
     */
    public DatabaseConnector() throws SQLException {
        this.con = DriverManager.getConnection(CONNECTION_STRING, USER, PWD);
    }

    /**
     * Diese Methode überprüft, ob jener User existiert.
     * @param email ist die Email des Users.
     * @param password ist das Passwort des Users.
     * @throws SQLException wird geworfen, wenn dieser User nicht existiert.
     * @return Wenn der User in der Datenbank existiert, wird eine User-Instanz erstellt und zurückgegeben.
     */
    public User authenticateUser(String email, String password) throws SQLException {
        final String LOGIN_QUERY = "SELECT vorname, nachname, password, photo FROM users WHERE email = ?";
        try (PreparedStatement stmt = con.prepareStatement(LOGIN_QUERY)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                if (password.equals(rs.getString("password"))) {
                    User user = new User(rs.getString("vorname"), rs.getString("nachname"), email, password, rs.getBytes("photo"), null);
                    user.setAccounts(getAllAccounts(user));
                    return user;
                }
            }
        }
        return null;
    }

    /**
     * Diese Methode ändert das entsprechende Feld (field) des Users in der Datenbank.
     * @param email ist die Email des Users.
     * @param newValue ist der neue Wert des Feldes.
     * @param field ist das Feld, das geändert wird
     * @throws SQLException wird geworfen, wenn email oder field nicht existiert.
     */
    public void updateField(String email, String newValue, String field) throws SQLException {
        String sql = "UPDATE users SET " + field + " = ? WHERE email = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, newValue);
            stmt.setString(2, email);
            stmt.executeUpdate();
        }
    }

    /**
     * Diese Methode speichert das Profilfoto des Users oder des Kontos in die Datenbank.
     * @param o ist entweder eine User-Instanz oder Account-Instanz.
     * @param file ist Datei für das Profilfoto.
     * @throws SQLException wird geworfen, wenn User oder Account nicht existiert.
     */
    public void savePhoto(Object o, File file) throws SQLException {
        byte[] imageBytes;
        try (FileInputStream fis = new FileInputStream(file)) {
            imageBytes = new byte[(int) file.length()];
            fis.read(imageBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(o instanceof User) {
            User user = (User) o;
            String sql = "UPDATE users SET photo = ? WHERE email = ?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setBytes(1, imageBytes);
                stmt.setString(2, user.getEmail());
                stmt.executeUpdate();
            }
            return;
        }
        Account account = (Account) o;
        String sql = "UPDATE accounts SET photo = ? WHERE iban = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setBytes(1, imageBytes);
            stmt.setString(2, account.getIban());
            stmt.executeUpdate();
        }
    }

    /**
     * Diese Methode speichert den sich registrierenden User in die Datenbank.
     * @param vorname ist der Vorname des Users.
     * @param nachname ist der Nachname des Users.
     * @param email ist die Email des Users.
     * @param password ist das Passwort des Users.
     * @throws SQLException wird geworfen, wenn die Werte ungültig sind.
     */
    public void registerUser(String vorname, String nachname, String email, String password) throws SQLException {
        final String QUERY = "INSERT INTO users (vorname, nachname, email, password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(QUERY)) {
            stmt.setString(1, vorname);
            stmt.setString(2, nachname);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.executeUpdate();
        }
    }

    private ArrayList<Account> getAllAccounts(User user) throws SQLException {
        ArrayList<Account> accounts = new ArrayList<>();
        final String QUERY = "SELECT * FROM accounts WHERE user_email = ?";
        try (PreparedStatement stmt = con.prepareStatement(QUERY)) {
            stmt.setString(1, user.getEmail());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String iban = rs.getString("iban");
                float saldo = rs.getFloat("saldo");
                String typ = rs.getString("typ");
                String name = rs.getString("name");
                byte[] photo = rs.getBytes("photo");

                accounts.add(new Account(name, iban, saldo, typ, user, photo, getAllCards(iban), getAllTransactions(iban)));
            }
        }
        return accounts;
    }

    private ArrayList<Card> getAllCards(String iban) throws SQLException { // lieber karte einfach in die liste hinzufügen
        ArrayList<Card> cards = new ArrayList<>();
        final String QUERY = "SELECT * FROM cards WHERE iban = ?";
        try (PreparedStatement stmt = con.prepareStatement(QUERY)) {
            stmt.setString(1, iban);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                float kartenlimit = rs.getFloat("kartenlimit");
                long kartennummer = rs.getLong("kartennummer");
                int folgenummer = rs.getInt("folgenummer");
                String typ = rs.getString("typ");
                int geheimzahl = rs.getInt("geheimzahl");
                int[] farbe = farben.get(random.nextInt(5));
                cards.add(new Card(iban, kartenlimit, kartennummer, folgenummer, typ, geheimzahl, farbe));
            }
        }
        return cards;
    }

    /**
     * Diese Methode speichert ein Spacekonto in die Datenbank.
     * @param saldo ist das Saldo des Spacekontos.
     * @param typ ist der Typ des Spacekontos.
     * @param user ist der User des Spacekontos.
     * @param nameSpace ist der Name des Spacekontos.
     * @throws SQLException wird geworfen, wenn die Werte ungültig sind.
     */
    public void createSpace(float saldo, String typ, User user, String nameSpace) throws SQLException {
        final String INSERT_QUERY = "INSERT INTO accounts (saldo, typ, user_email, name) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(INSERT_QUERY)) {
            stmt.setFloat(1, saldo);
            stmt.setString(2, typ);
            stmt.setString(3, user.getEmail());
            stmt.setString(4, nameSpace);
            stmt.executeUpdate();
        }
        user.setAccounts(getAllAccounts(user));
    }

    /**
     * Diese Methode speichert eine Karte in die Datenbank.
     * @param iban ist die IBAN der Karte.
     * @param kartenLimit ist der Kartenlimit des Karte.
     * @param typ ist der Typ der Karte.
     * @param account ist das Konto der Karte.
     * @throws SQLException wird geworfen, wenn die Werte ungültig sind.
     */
    public void karteBestellen(String iban, int kartenLimit, String typ, Account account) throws SQLException {
        final String INSERT_QUERY = "INSERT INTO cards (iban, kartenlimit, typ) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(INSERT_QUERY)) {
            stmt.setString(1, iban);
            stmt.setInt(2, kartenLimit);
            stmt.setString(3, typ);
            stmt.executeUpdate();
        }
        account.setCards(getAllCards(iban));
    }

    /**
     * Diese Methode ändert das Kartenlimit einer Karte.
     * @param card ist die betroffene Karte.
     * @param kartenlimit ist das neue Kartenlimit
     * @throws SQLException wird geworfen, wenn die Karte nicht existiert oder die Werte ungültig sind.
     */
    public void changeCardLimit(Card card, String kartenlimit) throws SQLException {
        final String UPDATE = "UPDATE CARDS SET kartenlimit = ? WHERE kartennummer = ?";
        float kartenlimitFloat = Float.parseFloat(kartenlimit);
        try (PreparedStatement stmt = con.prepareStatement(UPDATE)) {
            stmt.setFloat(1, kartenlimitFloat);
            stmt.setLong(2, card.getKartenNummer());
            stmt.executeUpdate();
        }
        card.setKartenLimit(kartenlimitFloat);
    }

    /**
     * Diese Methode überprüft, ob das Konto genug Saldo besitzt.
     * @param senderIban ist die IBAN des Kontos.
     * @param betrag ist der Betrag
     * @return Gib true zurück, falls genug Guthaben, andernfalls false.
     * @throws SQLException wird geworfen, wenn Konto nicht existiert.
     */
    public boolean enoughBalance(String senderIban, float betrag) throws SQLException {
        final String checkBalanceQuery = "SELECT saldo FROM accounts WHERE iban = ?";
        try (PreparedStatement checkBalanceStmt = con.prepareStatement(checkBalanceQuery);) {
            checkBalanceStmt.setString(1, senderIban);
            ResultSet rs = checkBalanceStmt.executeQuery();
            if (rs.next()) {
                float balance = rs.getFloat("saldo");
                if (balance < betrag) {
                    return false;
                }
            }
            return true;
        }
    }

//deposit withdrawal und kartenzahlung in einer methode

    /**
     * Diese Methode ändert das Saldo des Kontos entsprechend des Transaktionstypen und des Betrag und speichert
     * die Transaktion in die Datenbank.
     * @param iban ist die IBAN des Kontos.
     * @param betrag ist der Betrag.
     * @param kartennummer Kommt bei einer Kartenzahlung zum Einsatz, sonst null.
     * @param transaktionsTyp ist entweder Eingang oder Ausgang.
     * @param verwendungszweck ist der Verwendungszweck der Transaktion.
     * @throws SQLException
     */
    public void updateBalance(String iban, float betrag, Long kartennummer, String transaktionsTyp, String verwendungszweck) throws SQLException {
        String query = "";
        if (transaktionsTyp.equals("Ausgang")){
            if (enoughBalance(iban,betrag)) {
                query = "UPDATE accounts SET saldo = saldo - ? WHERE iban = ?";
                if(kartennummer != null) insertIntoTransaction(betrag, "Ausgang", null, iban, verwendungszweck, kartennummer);
                else insertIntoTransactionTransfer(betrag, "Ausgang", null, iban, verwendungszweck);
            }
        }
        else{
            query = "UPDATE accounts SET saldo = saldo + ? WHERE iban = ?";
            if(kartennummer != null) insertIntoTransaction(betrag, "Eingang", iban, null, verwendungszweck, kartennummer);
            else insertIntoTransactionTransfer(betrag, "Eingang", iban, null, verwendungszweck);
        }
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setFloat(1, betrag);
            stmt.setString(2, iban);
            stmt.executeUpdate();
        }
    }

    /**
     * Diese Methode übernimmt Transfers zwischen Spaces und Hauptkonto und speichert sie als Transaktionen in der Datenbank.
     * @param hauptkontoIban ist die IBAN des Hauptkontos.
     * @param spaceIban ist die IBAN des Spaces.
     * @param betrag ist der Betrag.
     * @param transferTyp ist entweder vom Hauptkonto oder zum Hauptkonto.
     * @throws SQLException wird geworfen, wenn Hauptkonto oder Space nicht existiert oder Werte ungültig sind.
     */
    public void transferHauptkonto(String hauptkontoIban, String spaceIban, float betrag, String transferTyp) throws SQLException {
        final String query = "UPDATE accounts SET saldo = saldo + ? WHERE iban = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setFloat(1, betrag);
            if (transferTyp.equals("vom Hauptkonto")){
                stmt.setString(2, spaceIban);
            }
            else if (transferTyp.equals("zum Hauptkonto")) {
                stmt.setString(2, hauptkontoIban);
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        final String query2 = "UPDATE accounts SET saldo = saldo - ? WHERE iban = ?";
        try (PreparedStatement stmt = con.prepareStatement(query2)) {
            stmt.setFloat(1, betrag);
            if (transferTyp.equals("vom Hauptkonto")){
                stmt.setString(2, hauptkontoIban);
            }
            else if (transferTyp.equals("zum Hauptkonto")) {
                stmt.setString(2, spaceIban);
            }
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        if (transferTyp.equals("vom Hauptkonto")){
            insertIntoTransactionTransfer(betrag, "Eingang", spaceIban, hauptkontoIban,  "Transfer");
        }
        else if (transferTyp.equals("zum Hauptkonto")) {
            insertIntoTransactionTransfer(betrag, "Ausgang", hauptkontoIban, spaceIban, "Transfer");
        }
    }

    /**
     * Diese Methode übernimmt Überweisungen zwischen Konten zwei verschiedener User.
     * @param senderIban ist die IBAN des Sender-User.
     * @param empfängerIban ist die IBAN des Empfänger-User.
     * @param betrag ist der Betrag.
     * @throws SQLException wird geworfen, wenn Sender-User oder Empfänger-User nicht existiert.
     */
    public void überweisen(String senderIban, String empfängerIban, float betrag) throws SQLException {
        final String query = "UPDATE accounts SET saldo = saldo + ? WHERE iban = ?";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setFloat(1, betrag);
            stmt.setString(2, empfängerIban);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        final String query2 = "UPDATE accounts SET saldo = saldo - ? WHERE iban = ?";
        try (PreparedStatement stmt = con.prepareStatement(query2)) {
            stmt.setFloat(1, betrag);
            stmt.setString(2, senderIban);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        insertIntoTransactionTransfer(betrag, "Ausgang", empfängerIban, senderIban,  "Überweisung");
    }

    private void insertIntoTransaction(float betrag, String eingangAusgang, String empfaengerIban, String senderIban,
                                       String verwendungszweck, long kartennummer) throws SQLException {

        final String insertTransactionQuery = "INSERT INTO transactions " +
                "(betrag, eingangAusgang, empfaengerIban, senderIban, verwendungszweck, kartennummer) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(insertTransactionQuery)) {
            stmt.setDouble(1, betrag);
            stmt.setString(2, eingangAusgang);
            stmt.setString(3, empfaengerIban);
            stmt.setString(4, senderIban);
            stmt.setString(5, verwendungszweck);
            stmt.setLong(6, kartennummer);
            stmt.executeUpdate();
        }
    }

    private void insertIntoTransactionTransfer(float betrag, String eingangAusgang, String empfaengerIban, String senderIban,
                                       String verwendungszweck) throws SQLException {

        final String insertTransactionQuery = "INSERT INTO transactions " +
                "(betrag, eingangAusgang, empfaengerIban, senderIban, verwendungszweck) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(insertTransactionQuery)) {
            stmt.setDouble(1, betrag);
            stmt.setString(2, eingangAusgang);
            stmt.setString(3, empfaengerIban);
            stmt.setString(4, senderIban);
            stmt.setString(5, verwendungszweck);
            stmt.executeUpdate();
        }
    }

    private ArrayList<Transaction> getAllWithdrawals(String iban) throws SQLException {
        ArrayList<Transaction> withdrawals = new ArrayList<>();
        final String query = "SELECT * FROM transactions WHERE senderIban = ? ";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, iban);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                float betrag = rs.getFloat("betrag");
                String eingangAusgang = rs.getString("eingangAusgang");
                String empfaengerIban = rs.getString("empfaengerIban");
                String senderIban = rs.getString("senderIban");
                String verwendungszweck = rs.getString("verwendungszweck");
                int transaktionsnummer = rs.getInt("transaktionsNummer");
                long kartennummer = rs.getLong("kartennummer");

                withdrawals.add(new Transaction(betrag, eingangAusgang, empfaengerIban, senderIban, verwendungszweck, transaktionsnummer, kartennummer));
            }
        }
        return withdrawals;
    }

    private ArrayList<Transaction> getAllDeposits(String iban) throws SQLException {
        ArrayList<Transaction> deposits = new ArrayList<>();
        final String query = "SELECT * FROM transactions WHERE empfaengerIban = ? ";
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, iban);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                float betrag = rs.getFloat("betrag");
                String eingangAusgang = rs.getString("eingangAusgang");
                String empfaengerIban = rs.getString("empfaengerIban");
                String senderIban = rs.getString("senderIban");
                String verwendungszweck = rs.getString("verwendungszweck");
                int transaktionsnummer = rs.getInt("transaktionsNummer");
                long kartennummer = rs.getLong("kartennummer");

                deposits.add(new Transaction(betrag, eingangAusgang, empfaengerIban, senderIban, verwendungszweck, transaktionsnummer, kartennummer));
            }
        }
        return deposits;
    }

    private ArrayList<Transaction> getAllTransactions(String iban) throws SQLException {
        ArrayList<Transaction> allTransactions = new ArrayList<>();
        allTransactions.addAll(getAllWithdrawals(iban));
        allTransactions.addAll(getAllDeposits(iban));
        return allTransactions;
    }

    /**
     * Diese Methode überprüft, ob die Karte existiert.
     * @param kartennummer ist die Kartennummer der Karte.
     * @param folgenummer ist die Folgenummer der Karte.
     * @param geheimzahl ist der PIN der Karte.
     * @return Falls Karte existiert, wird true, sonst false zurückgegeben.
     */
    public boolean isCardDataValid(long kartennummer, int folgenummer, int geheimzahl) {
        final String QUERY = "SELECT COUNT(*) FROM cards WHERE kartennummer = ? AND folgenummer = ? AND geheimzahl = ?";
        try (PreparedStatement stmt = con.prepareStatement(QUERY)) {
            stmt.setLong(1, kartennummer);
            stmt.setInt(2, folgenummer);
            stmt.setInt(3, geheimzahl);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Datenbankfehler: " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    /**
     * Diese Methode überprüft, ob dieser User bereits existiert.
     * @param email ist die Email des Users..
     * @return Falls Karte existiert, wird true, sonst false zurückgegeben.
     */
    public boolean userExists(String email) throws SQLException {
        final String CHECK_USER_QUERY = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (PreparedStatement stmt = con.prepareStatement(CHECK_USER_QUERY)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
}