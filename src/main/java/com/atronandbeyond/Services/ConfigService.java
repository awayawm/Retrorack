package com.atronandbeyond.Services;

import com.atronandbeyond.Data.Config;

import java.io.*;
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

    public boolean saveConfigToDisk() {
//        if (hasSpotifyCredentials()) {
            try (PrintWriter writer = new PrintWriter(
                    new FileOutputStream(config.getConfigFilename(), false), true, StandardCharsets.UTF_8)) {
                System.out.println("writing config to file");
                writer.print(config.getSpotifyClientId() + " " + config.getSpotifyClientSecret() + " " + config.getMysqlHost() + " " + config.getMysqlUsername() + " " + config.getMysqlPassword());
                return true;
            } catch (IOException ex) {
                // do nothing
                System.out.println("couldn't write config to file");
                return false;
            }
//        } else {
//            System.out.println("no spotify creds in config");
//            System.out.println(config);
//            return false;
//        }
    }

    boolean loadConfig() {
        System.out.println("Loading config from file: " + config.getConfigFilename());
        try (Scanner scanner = new Scanner(new File(config.getConfigFilename()))) {
            scanner.useDelimiter(" ");

            String clientId = scanner.next();
            String clientSecret = scanner.next();
            String mysqlHost = scanner.next();
            String mysqlUser = scanner.next();
            String mySqlPass = scanner.next();

            config.setSpotifyClientId(clientId);
            config.setSpotifyClientSecret(clientSecret);
            config.setMysqlHost(mysqlHost);
            config.setMysqlUsername(mysqlUser);
            config.setMysqlPassword(mySqlPass);

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
