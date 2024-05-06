package com.jmc.app.Models;

import javafx.scene.image.Image;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Type;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DatabaseConnector {

    private static final String CONNECTION_STRING = "jdbc:oracle:thin:@e4xxmj5ey9kfqzz5_high?TNS_ADMIN=/Users/perseus/Desktop/Wallet_E4XXMJ5EY9KFQZZ5";
    private static final String USER = "admin";
    private static final String PWD = "BigBankSoSe2024";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(CONNECTION_STRING, USER, PWD);
    }

    public static boolean authenticateUser(String email, String password) {
        final String LOGIN_QUERY = "SELECT password FROM users WHERE email = ?";
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(LOGIN_QUERY)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return password.equals(rs.getString("password"));
            }
        } catch (SQLException e) {
            System.err.println("Datenbankfehler: " + e.getMessage());
            e.printStackTrace(System.err);
        }
        return false;
    }

    public static void updateField(String email, String newValue, String field) throws SQLException {
        String sql = "UPDATE users SET " + field + " = ? WHERE email = ?";
        try (Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, newValue);
            stmt.setString(2, email);
            stmt.executeUpdate();
        }
    }

    public static void savePhoto(String email, File file) throws SQLException {
        byte[] imageBytes = null;
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

    public static Image loadPhoto(String email) throws SQLException {
        String sql = "SELECT photo FROM users WHERE email = ?";
        try (Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                byte[] imageData = rs.getBytes("photo");
                if (imageData != null && imageData.length > 0) {
                    return new Image(new ByteArrayInputStream(imageData));
                }
            }
        }
        return null;
    }

    public static String[] getUserData(String email) throws SQLException {
        final String QUERY = "SELECT vorname, nachname, password, photo FROM users WHERE email = ?";
        String[] result = new String[4];  // Array to hold first name and last name
        try (Connection con = getConnection();
             PreparedStatement stmt = con.prepareStatement(QUERY)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    result[0] = rs.getString("vorname");  // First name
                    result[1] = rs.getString("nachname"); // Last name
                    result[2] = rs.getString("password");
                    result[3] = Arrays.toString(rs.getBytes("photo"));
                }
            }
        }
        return result;
    }

    public static void registerUser(String vorname, String nachname,String email, String password) {
        final String QUERY = "INSERT INTO users (vorname, nachname, email, password) VALUES (?, ?, ?, ?)";
        try (Connection con = getConnection(); PreparedStatement stmt = con.prepareStatement(QUERY)) {
            stmt.setString(1, vorname);
            stmt.setString(2, nachname);
            stmt.setString(3, email);
            stmt.setString(4, password);
            stmt.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace(System.err);
        }
    }





}