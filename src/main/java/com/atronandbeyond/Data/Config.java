package com.atronandbeyond.Data;

public class Config {
    String spotifyClientId;
    String spotifyClientSecret;
    String configFilename;

    String mysqlUsername;
    String mysqlPassword;
    String mysqlHost;

    public String getMysqlUsername() {
        return mysqlUsername;
    }

    public void setMysqlUsername(String mysqlUsername) {
        this.mysqlUsername = mysqlUsername;
    }

    public String getMysqlPassword() {
        return mysqlPassword;
    }

    public void setMysqlPassword(String mysqlPassword) {
        this.mysqlPassword = mysqlPassword;
    }

    public String getMysqlHost() {
        return mysqlHost;
    }

    public void setMysqlHost(String mysqlHost) {
        this.mysqlHost = mysqlHost;
    }

    public String getConfigFilename() {
        return configFilename;
    }

    public void setConfigFilename(String configFilename) {
        this.configFilename = configFilename;
    }

    public String getSpotifyClientId() {
        return spotifyClientId;
    }

    public void setSpotifyClientId(String spotifyClientId) {
        this.spotifyClientId = spotifyClientId;
    }

    public String getSpotifyClientSecret() {
        return spotifyClientSecret;
    }

    public void setSpotifyClientSecret(String spotifyClientSecret) {
        this.spotifyClientSecret = spotifyClientSecret;
    }

    @Override
    public String toString() {
        return "Config{" +
                "spotifyClientId='" + spotifyClientId + '\'' +
                ", spotifyClientSecret='" + spotifyClientSecret + '\'' +
                ", configFilename='" + configFilename + '\'' +
                ", mysqlUsername='" + mysqlUsername + '\'' +
                ", mysqlPassword='" + mysqlPassword + '\'' +
                ", mysqlHost='" + mysqlHost + '\'' +
                '}';
    }
}
