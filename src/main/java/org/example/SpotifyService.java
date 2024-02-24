package org.example;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class SpotifyService {
    String clientId;
    String clientSecret;

    String tokenApi = "https://accounts.spotify.com/api/token";
    String searchApi = "https://api.spotify.com/v1/search";

    void setCredentials(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    String getToken() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(tokenApi))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("grant_type=client_credentials&client_id=" + clientId + "&client_secret=" + clientSecret))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();
        String body = response.body();

//        System.out.println(statusCode);
//        System.out.println(body);


        JSONObject jsonObject = new JSONObject(body);

        return jsonObject.getString("access_token");
    }

    public String search(String token, String q) throws IOException, InterruptedException {
        String search = URLEncoder.encode(q, StandardCharsets.UTF_8);
        String url = searchApi + "?q=" + search + "&type=album";
        System.out.println(url);

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String body = response.body();
        System.out.println(body);

        return body;
    }
}
