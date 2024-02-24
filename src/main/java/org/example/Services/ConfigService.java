package org.example.Services;

import org.example.Data.Config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class ConfigService {
    Config config;

    public ConfigService(String configFile) {
        config = new Config();
        config.setConfigFilename(configFile);
        if (configExist()) {
            System.out.println("found config ... loading");
            loadConfig();
        }
    }

    public Config getConfig() {
        return config;
    }

    boolean configExist() {
        return Files.exists(Paths.get(config.getConfigFilename()));
    }

    public boolean saveSpotifySettingsToConfig(String clientId, String clientSecret) {
        if (hasSpotifyCredentials()) {
            try (PrintWriter writer = new PrintWriter(config.getConfigFilename(), StandardCharsets.UTF_8)) {
                System.out.println("writing spotify creds to file");
                writer.println(clientId + " " + clientSecret);
                return true;
            } catch (IOException ex) {
                // do nothing
                System.out.println("couldn't write spotify creds to file");
                return false;
            }
        } else {
            System.out.println("no spotify creds in config");
            System.out.println(config);
            return false;
        }
    }

    boolean loadConfig() {
        try (Scanner scanner = new Scanner(new File(config.getConfigFilename()))) {
            scanner.useDelimiter(" ");
            config.setSpotifyClientId(scanner.next());
            config.setSpotifyClientSecret(scanner.next());
            return true;
        } catch (FileNotFoundException e) {
            // do nothing
            return false;
        }
    }

    public boolean hasSpotifyCredentials() {
        return config.getSpotifyClientId() != null && config.getSpotifyClientSecret() != null;
    }

}
