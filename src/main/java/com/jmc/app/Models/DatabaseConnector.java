package com.jmc.app.Models;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class DatabaseConnector {

    private final String CONNECTION_STRING = "jdbc:oracle:thin:@e4xxmj5ey9kfqzz5_high?TNS_ADMIN=/Users/petritcena/Desktop/Wallet_E4XXMJ5EY9KFQZZ5";
    private final String USER = "admin";
    private final String PWD = "BigBankSoSe2024";

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
                    return new User(rs.getString("vorname"), rs.getString("nachname"), email, password, rs.getBytes("photo"), getAllAccounts(email));
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

    public ArrayList<Account> getAllAccounts(String email) throws SQLException {
        ArrayList<Account> accounts = new ArrayList<>();
        final String QUERY = "SELECT * FROM accounts WHERE user_email = ?";
        try (Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(QUERY)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String iban = rs.getString("iban");
                float saldo = rs.getFloat("saldo");
                String typ = rs.getString("typ");
                accounts.add(new Account(iban, saldo, typ, email, getAllCards(iban)));
            }
        }
        return accounts;
    }

    public ArrayList<Card> getAllCards(String iban) throws SQLException {
        ArrayList<Card> cards = new ArrayList<>();
        final String QUERY = "SELECT * FROM cards WHERE iban = ?";
        try (Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(QUERY)) {
            stmt.setString(1, iban);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                float kartenlimit = rs.getFloat("kartenlimit");
                float kartennummer = rs.getFloat("kartennummer");
                int folgenummer = rs.getInt("folgenummer");
                String typ = rs.getString("typ");
                int geheimzahl = rs.getInt("geheimzahl");
                cards.add(new Card(iban, kartenlimit, kartennummer, folgenummer, typ, geheimzahl));
            }
        }
        return cards;
    }

    public void createSpace(float saldo, String typ, String email) throws SQLException {
        final String INSERT_QUERY = "INSERT INTO accounts (saldo, typ, user_email) VALUES (?, ?, ?)";
        try (Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(INSERT_QUERY)) {
            stmt.setFloat(1, saldo);
            stmt.setString(2, typ);
            stmt.setString(3, email);
            stmt.executeUpdate();
        }
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

}