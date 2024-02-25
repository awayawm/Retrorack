package com.atronandbeyond.Dao;

import java.sql.*;

public class DBConnection {
    private static Connection conn;

    private static String DATABASE = "retrorack";
    private static String ALBUMS_TABLE = "rr_albums";

    private static String getConnectionUrl(String host) {
        return "jdbc:mysql://" + host + ":3306/" + DATABASE + "?serverTimezone=UTC";
    }

    public static boolean testConnection(String host, String user, String password) {
        try (Connection conn = DriverManager.getConnection(getConnectionUrl(host), user, password);
             Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery("select * from " + ALBUMS_TABLE)) {

            while (rs.next()) {
//                long id = rs.getLong("ID");
//                String firstName = rs.getString("FIRST_NAME");
//                String lastName = rs.getString("LAST_NAME");
                // Process the extracted data...
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions
        }
        return false;
    }

}
