package com.jmc.app.Models;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class DatabaseConnector {

    private final String CONNECTION_STRING = "jdbc:oracle:thin:@e4xxmj5ey9kfqzz5_high?TNS_ADMIN=/Users/oemer.t/Downloads/Wallet_E4XXMJ5EY9KFQZZ5";
    private final String USER = "admin";
    private final String PWD = "BigBankSoSe2024";

    Random random = new Random();
    private ArrayList<Transaction> transactions = new ArrayList<>();
    private ArrayList<int[]> farben = new ArrayList<>(Arrays.asList(new int[]{0, 51, 102}, new int[]{192, 192, 192}, new int[]{211, 211, 211}, new int[]{176, 224, 230}, new int[]{163, 100, 100}));

    private final Connection con;

    public DatabaseConnector() throws SQLException {
        this.con = DriverManager.getConnection(CONNECTION_STRING, USER, PWD);
    }

    public User authenticateUser(String email, String password) {
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
        } catch (SQLException e) {
            System.err.println("Datenbankfehler: " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return null;
    }

    public void updateField(String email, String newValue, String field) throws SQLException {
        String sql = "UPDATE users SET " + field + " = ? WHERE email = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, newValue);
            stmt.setString(2, email);
            stmt.executeUpdate();
        }
    }

    public void savePhoto(String email, File file) throws SQLException {
        byte[] imageBytes;
        try (FileInputStream fis = new FileInputStream(file)) {
            imageBytes = new byte[(int) file.length()];
            fis.read(imageBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String sql = "UPDATE users SET photo = ? WHERE email = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setBytes(1, imageBytes);
            stmt.setString(2, email);
            stmt.executeUpdate();
        }
    }

    public void registerUser(String vorname, String nachname, String email, String password) {
        final String QUERY = "INSERT INTO users (vorname, nachname, email, password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(QUERY)) {
            stmt.setString(1, vorname);
            stmt.setString(2, nachname);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
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

                getAllWithdrawals(iban);
                getAllDeposits(iban);
                accounts.add(new Account(iban, saldo, typ, user, getAllCards(iban), transactions));
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

    public void createSpace(float saldo, String typ, User user) throws SQLException {
        final String INSERT_QUERY = "INSERT INTO accounts (saldo, typ, user_email) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(INSERT_QUERY)) {
            stmt.setFloat(1, saldo);
            stmt.setString(2, typ);
            stmt.setString(3, user.getEmail());
            stmt.executeUpdate();
        }
        user.setAccounts(getAllAccounts(user));
    }

    public void karteBestellen(String iban, int kartenLimit, String typ) throws SQLException {
        final String INSERT_QUERY = "INSERT INTO cards (iban, kartenlimit, typ) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = con.prepareStatement(INSERT_QUERY)) {
            stmt.setString(1, iban);
            stmt.setInt(2, kartenLimit);
            stmt.setString(3, typ);
            stmt.executeUpdate();
        }
    }

    public void geldAbziehen(String email) {
        final String UPDATE = "UPDATE ACCOUNTS SET saldo = saldo -10 WHERE typ = 'Hauptkonto' AND user_email = ?";
        try (PreparedStatement stmt = con.prepareStatement(UPDATE)) {
            stmt.setString(1, email);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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
    public void updateBalance(String iban, float betrag, long kartennummer, String transaktionsTyp) throws SQLException {
        String query = "";

            if (transaktionsTyp.equals("Abhebung")){
                if (enoughBalance(iban,betrag)) {
                    query = "UPDATE accounts SET saldo = saldo - ? WHERE iban = ?";
                    insertIntoTransaction(betrag, "Ausgang", null, iban, "Abhebung", kartennummer);
                }
            } else if (transaktionsTyp.equals("Kartenzahlung")) {
                if (enoughBalance(iban,betrag)) {
                    query = "UPDATE accounts SET saldo = saldo - ? WHERE iban = ?";
                    insertIntoTransaction(betrag, "Ausgang", null, iban, "Kartenzahlung", kartennummer);
                }
            } else if (transaktionsTyp.equals("Einzahlung")) {
            query = "UPDATE accounts SET saldo = saldo + ? WHERE iban = ?";
            insertIntoTransaction(betrag, "Eingang",iban, null,  "Einzahlung", kartennummer);
        }
        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setFloat(1, betrag);
            stmt.setString(2, iban);
            stmt.executeUpdate();
        }
    }
    /*public void withdrawal(String senderIban, float betrag, long kartennummer) throws SQLException {
        if (enoughBalance(senderIban, betrag)) {
            final String withdrawQuery = "UPDATE accounts SET saldo = saldo - ? WHERE iban = ?";
            try (PreparedStatement stmt = con.prepareStatement(withdrawQuery)) {
                stmt.setFloat(1, betrag);
                stmt.setString(2, senderIban);
                stmt.executeUpdate();
            }
            insertIntoTransaction(betrag, "Ausgang", null, senderIban, "Abhebung", kartennummer);
        }
    }

    public void deposit(String empfaengerIban, float betrag, long kartennummer) throws SQLException {
        final String depositQuery = "UPDATE accounts SET saldo = saldo + ? WHERE iban = ?";
        try (PreparedStatement stmt = con.prepareStatement(depositQuery)) {
            stmt.setFloat(1, betrag);
            stmt.setString(2, empfaengerIban);
            stmt.executeUpdate();
        }
        insertIntoTransaction(betrag, "Eingang", empfaengerIban, null,  "Einzahlung", kartennummer);
    }*/
    public void transferHauptkonnto(String hauptkontoIban, String spaceIban, float betrag, String transferTyp) throws SQLException {
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
            insertIntoTransaction(betrag, "Eingang", spaceIban, hauptkontoIban,  "Transfer", 0);
        }
        else if (transferTyp.equals("zum Hauptkonto")) {
            insertIntoTransaction(betrag, "Ausgang", hauptkontoIban, spaceIban,  "Transfer", 0);
        }



    }
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
        insertIntoTransaction(betrag, "Ausgang", empfängerIban, senderIban,  "Überweisung", 0);
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

    /*private void insertIntoTransaction2(float betrag, String eingangAusgang, String empfaengerIban, String senderIban,
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
    }*/

    private void getAllWithdrawals(String iban) throws SQLException {
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

                transactions.add(new Transaction(betrag, eingangAusgang, empfaengerIban, senderIban, verwendungszweck, transaktionsnummer, kartennummer));
            }
        }
    }

    private void getAllDeposits(String iban) throws SQLException {
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

                transactions.add(new Transaction(betrag, eingangAusgang, empfaengerIban, senderIban, verwendungszweck, transaktionsnummer, kartennummer));
            }
        }
    }

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


}