package com.jmc.app.Controllers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    // Database connection parameters
    private static final String USER = "admin";
    private static final String PASSWORD = "BigBankSoSe2024";
    private static final String TNS_ADMIN_PATH = "C:\\Workspace\\Wallet_E4XXMJ5EY9KFQZZ5";
    private static final String CONNECTION_STRING = "jdbc:oracle:thin:@e4xxmj5ey9kfqzz5_high";

    static {
        // Set the TNS_ADMIN property to point to the directory of the Oracle Wallet
        System.setProperty("oracle.net.tns_admin", TNS_ADMIN_PATH);

        // Optionally, you could load the driver class if you're not on JDBC 4.0+
        // But this is not typically necessary for modern JDBC drivers.
        // try {
        //     Class.forName("oracle.jdbc.driver.OracleDriver");
        // } catch (ClassNotFoundException e) {
        //     e.printStackTrace();
        // }
    }

    /**
     * Establishes a connection to the database using the connection parameters.
     *
     * @return A Connection object to the database.
     * @throws SQLException If a database access error occurs or the url is null.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(CONNECTION_STRING, USER, PASSWORD);
    }}

    // Other utility methods related to database operations can be added here...

    // Example usage


