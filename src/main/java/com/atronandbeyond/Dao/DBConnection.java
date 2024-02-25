package com.atronandbeyond.Dao;

import com.atronandbeyond.Data.Album;
import com.atronandbeyond.Data.Config;

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
             PreparedStatement ps = conn.prepareStatement("select * from " + ALBUMS_TABLE);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                System.out.println(rs.getString("id"));
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void addAlbum(Album album, Config config) throws Exception {
        String id = album.getId();
        String name = album.getName();
        String releaseDate = album.getReleaseDate();
        int totalTracks = album.getTotalTracks();

        System.out.println("connecting to " + getConnectionUrl(config.getMysqlHost()));

        try (Connection conn = DriverManager.getConnection(getConnectionUrl(config.getMysqlHost()), config.getMysqlUsername(), config.getMysqlPassword())) {
            PreparedStatement ps = conn.prepareStatement("insert into " + ALBUMS_TABLE + " (id, name, ReleaseDate, totaltracks) values (?, ?, ?, ?)");
            ps.setString(1, id);
            ps.setString(2, name);
            ps.setString(3, releaseDate);
            ps.setInt(4, totalTracks);

            System.out.println(ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }
}