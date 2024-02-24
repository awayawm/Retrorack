package org.example.Data;

public class Config {
    String spotifyClientId;
    String spotifyClientSecret;
    String configFilename;

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
                '}';
    }
}
