package com.jmc.app.Models;

public class User {
    private static String email;
    private static String password;
    private static String firstName;
    private static String lastName;
    private static String pic;
    // Weitere Attribute können hier hinzugefügt werden

    public User(String firstName, String lastName, String email, String password, String pic) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pic = pic;
    }

    // Getter und Setter
    public static String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public static String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public static String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public static String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public static String getPic() { return pic;}
    public void setPic(String pic) { this.pic = pic; }
}
