package com.jmc.app.Models;

import com.jmc.app.Controllers.LoginController;

import java.io.File;
import java.sql.SQLException;

public class User {
    private static String email;
    private static String password;
    private static String firstName;
    private static String lastName;
    private static byte[] pic;
    // Weitere Attribute können hier hinzugefügt werden

    public User(String firstName, String lastName, String email, String password, byte[] pic) {
        User.email = email;
        User.password = password;
        User.firstName = firstName;
        User.lastName = lastName;
        User.pic = pic;
    }

    // Getter und Setter
    public static String getEmail() { return email; }

    public static String getPassword() { return password; }
    public static void setPassword(String newPassword) throws SQLException {
        DatabaseConnector.updateField(User.email, newPassword, "password");
        User.password = newPassword;
    }

    public static String getFirstName() { return firstName; }
    public static void setFirstName(String newFirstName) throws SQLException {
        DatabaseConnector.updateField(User.email, newFirstName, "vorname");
        User.firstName = newFirstName;
    }

    public static String getLastName() { return lastName; }
    public static void setLastName(String newLastName) throws SQLException {
        DatabaseConnector.updateField(User.email, newLastName, "nachname");
        User.lastName = lastName;
    }

    public static byte[] getPic() { return pic;}
    public static void setPic(File newPic) throws SQLException {
        DatabaseConnector.savePhoto(LoginController.email, newPic);
        User.pic = new byte[(int) newPic.length()];
    }
}
