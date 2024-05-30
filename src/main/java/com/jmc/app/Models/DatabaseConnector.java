package com.jmc.app.Models;

import javafx.scene.control.TextField;

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

    private ArrayList<int[]> farben = new ArrayList<>(Arrays.asList(new int[]{0, 51, 102}, new int[]{192, 192, 192}, new int[]{211, 211, 211}, new int[]{176, 224, 230}, new int[]{163, 100, 100}));


    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(CONNECTION_STRING, USER, PWD);
    }

    public User authenticateUser(String email, String password) {
        final String LOGIN_QUERY = "SELECT vorname, nachname, password, photo FROM users WHERE email = ?";
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(LOGIN_QUERY)) {

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
        try (Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
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
        try (Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setBytes(1, imageBytes);
            stmt.setString(2, email);
            stmt.executeUpdate();
        }
    }

    public void registerUser(String vorname, String nachname, String email, String password) {
        final String QUERY = "INSERT INTO users (vorname, nachname, email, password) VALUES (?, ?, ?, ?)";
        try (Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(QUERY)) {
            stmt.setString(1, vorname);
            stmt.setString(2, nachname);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
    }

    public ArrayList<Account> getAllAccounts(User user) throws SQLException {
        ArrayList<Account> accounts = new ArrayList<>();
        final String QUERY = "SELECT * FROM accounts WHERE user_email = ?";
        try (Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(QUERY)) {
            stmt.setString(1, user.getEmail());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String iban = rs.getString("iban");
                float saldo = rs.getFloat("saldo");
                String typ = rs.getString("typ");
                accounts.add(new Account(iban, saldo, typ, user, getAllCards(iban)));
            }
        }
        return accounts;
    }

    public ArrayList<Card> getAllCards(String iban) throws SQLException { // lieber karte einfach in die liste hinzufügen
        ArrayList<Card> cards = new ArrayList<>();
        final String QUERY = "SELECT * FROM cards WHERE iban = ?";
        try (Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(QUERY)) {
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
        try (Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(INSERT_QUERY)) {
            stmt.setFloat(1, saldo);
            stmt.setString(2, typ);
            stmt.setString(3, user.getEmail());
            stmt.executeUpdate();
        }
        user.setAccounts(getAllAccounts(user));
    }

    public void karteBestellen(String iban, int kartenLimit, String typ) throws SQLException {
        final String INSERT_QUERY = "INSERT INTO cards (iban, kartenlimit, typ) VALUES (?, ?, ?)";
        try (Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(INSERT_QUERY)) {
            stmt.setString(1, iban);
            stmt.setInt(2, kartenLimit);
            stmt.setString(3, typ);
            stmt.executeUpdate();
        }
    }

    public void geldAbziehen(String email) {
        final String UPDATE = "UPDATE ACCOUNTS SET saldo = saldo -10 WHERE typ = 'Hauptkonto' AND user_email = ?";
        try (Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(UPDATE)) {
            stmt.setString(1, email);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void changeCardLimit(Card card, String kartenlimit) throws SQLException {
        final String UPDATE = "UPDATE CARDS SET kartenlimit = ? WHERE kartennummer = ?";
        float kartenlimitFloat = Float.parseFloat(kartenlimit);
        try (Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(UPDATE)) {
            stmt.setFloat(1, kartenlimitFloat);
            stmt.setLong(2, card.getKartenNummer());
            stmt.executeUpdate();
        }
        card.setKartenLimit(kartenlimitFloat);
    }

    public boolean enoughBalance(String fromAccountId, String toAccountId, double amount, String transactionType,
                                 String receiverIban, String senderIban, String purpose, int transactionNumber, long cardNumber) throws SQLException {
        try (Connection con = getConnection()) {

            con.setAutoCommit(false);

            String checkBalanceQuery = "SELECT saldo FROM accounts WHERE iban = ?";
            PreparedStatement checkBalanceStmt = con.prepareStatement(checkBalanceQuery);
            checkBalanceStmt.setString(1, fromAccountId);
            ResultSet rs = checkBalanceStmt.executeQuery();

            if (rs.next()) {
                double balance = rs.getDouble("balance");
                if (balance < amount) {
                    return false;
                }
            }
            return true;
        }
    }

    public void withdrawal(String fromAccountId, String toAccountId, double amount, String transactionType,
                           String receiverIban, String senderIban, String purpose, int transactionNumber, long cardNumber) throws SQLException {
        if (enoughBalance(fromAccountId, toAccountId, amount, transactionType, receiverIban, senderIban, purpose, transactionNumber, cardNumber)) {
            final String withdrawQuery = "UPDATE accounts SET saldo = saldo - ? WHERE iban = ?";
            try (Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(withdrawQuery)) {
                stmt.setDouble(1, amount);
                stmt.setString(2, fromAccountId);
                stmt.executeUpdate();
            }
            insertIntoTransaction(fromAccountId, toAccountId, amount, transactionType, receiverIban, senderIban, purpose, transactionNumber, cardNumber);

        }
    }

    public void insertIntoTransaction(String fromAccountId, String toAccountId, double amount, String transactionType,
                                      String receiverIban, String senderIban, String purpose, int transactionNumber, long cardNumber) throws SQLException {

        final String insertTransactionQuery = "INSERT INTO transactions " +
                "(betrag, ausgangeingang, ibansender, ibanempfeanger, verwendungszweck, kartennummer, transaktionnummer,) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(insertTransactionQuery)) {
            stmt.setDouble(1, amount);
            stmt.setString(2, transactionType);
            stmt.setString(3, fromAccountId);
            stmt.setString(4, toAccountId);
            stmt.setString(5, purpose);
            stmt.setLong(6, cardNumber);
            stmt.setInt(7, transactionNumber);
            stmt.executeUpdate();
        }

    }

    public void deposit(String fromAccountId, String toAccountId, double amount, String transactionType,
                        String receiverIban, String senderIban, String purpose, int transactionNumber, long cardNumber) throws SQLException {
        final String depositQuery = "UPDATE accounts SET saldo = saldo + ? WHERE iban = ?";
        try (Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(depositQuery)) {
            stmt.setDouble(1, amount);
            stmt.setString(2, toAccountId);
            stmt.executeUpdate();
        }
        insertIntoTransaction(fromAccountId, toAccountId, amount, transactionType, receiverIban, senderIban, purpose, transactionNumber, cardNumber);
    }
}




