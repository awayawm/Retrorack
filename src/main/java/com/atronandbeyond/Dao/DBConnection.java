package com.atronandbeyond.Dao;

import com.atronandbeyond.Data.Album;
import com.atronandbeyond.Data.Config;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBConnection {
    private static Connection conn;

    private static String DATABASE = "retrorack";
    private static String SEARCH_RESULT_TABLE = "rr_search";
    private static String ALBUM_INFORMATION = "rr_album_information";

    private static String getConnectionUrl(String host) {
        return "jdbc:mysql://" + host + ":3306/" + DATABASE + "?serverTimezone=UTC";
    }

    public static boolean testConnection(String host, String user, String password) {
        try (Connection conn = DriverManager.getConnection(getConnectionUrl(host), user, password);
             PreparedStatement ps = conn.prepareStatement("select * from " + SEARCH_RESULT_TABLE);
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
            PreparedStatement ps = conn.prepareStatement("insert into " + SEARCH_RESULT_TABLE + " (id, name, ReleaseDate, totaltracks) values (?, ?, ?, ?)");
            ps.setString(1, id);
            ps.setString(2, name);
            ps.setString(3, releaseDate);
            ps.setInt(4, totalTracks);

//            System.out.println(ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static Object[][] getAlbums(Config config) {
        try (Connection conn = DriverManager.getConnection(
                getConnectionUrl(config.getMysqlHost()), config.getMysqlUsername(), config.getMysqlPassword())) {
            Statement statement = conn.createStatement();
            ResultSet cntRs = statement.executeQuery("select count(*) from " + SEARCH_RESULT_TABLE);
            cntRs.next();
            int count = cntRs.getInt(1);
            System.out.println("count: " + count);

            PreparedStatement ps = conn.prepareStatement("select * from " + SEARCH_RESULT_TABLE + " order by createdate desc");
            ResultSet rs = ps.executeQuery();


            int NUM_COLUMNS = 5; // adjust if columns are added to ALBUMS_TABLE
            Object[][] rows = new Object[count][NUM_COLUMNS];
            int rowCnt = 0;
            while (rs.next()) {
                rows[rowCnt++] = new Object[]{rs.getString("id"), rs.getString("name"), rs.getString("releaseDate"), rs.getInt("totaltracks"), rs.getString("createDate")};
            }
            return rows;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getNumberAlbums(Config config) {
        try (Connection conn = DriverManager.getConnection(getConnectionUrl(config.getMysqlHost()), config.getMysqlUsername(), config.getMysqlPassword())) {
            Statement statement = conn.createStatement();
            ResultSet cntRs = statement.executeQuery("select count(*) from " + SEARCH_RESULT_TABLE);
            cntRs.next();
            return cntRs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static ArrayList<String> getNonPopulatedIds(Config config) {
        ArrayList<String> ids = new ArrayList();
        try (Connection conn = DriverManager.getConnection(getConnectionUrl(config.getMysqlHost()), config.getMysqlUsername(), config.getMysqlPassword())) {
            Statement statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("select * from " + SEARCH_RESULT_TABLE + " search left join " + ALBUM_INFORMATION + " album on search.id=album.id where label is null order by rand() limit 1");
            while (rs.next()) {
                ids.add(rs.getString("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ids;
    }

}
