package org.example.Services;

import org.example.Data.Album;
import org.example.Data.SearchResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class SpotifyService {

    String tokenApi = "https://accounts.spotify.com/api/token";
    String searchApi = "https://api.spotify.com/v1/search";

    public SpotifyService() {
    }

    public String getToken(String clientId, String clientSecret) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        String postBody = "grant_type=client_credentials&client_id=" + clientId + "&client_secret=" + clientSecret;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(tokenApi))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(postBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();
        String body = response.body();

//        System.out.println(statusCode);
        if (statusCode != 200) {
            System.out.println(body);
        }

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

    public SearchResponse parseSearchServiceResponse(String body) {
        SearchResponse searchResponse = new SearchResponse();

        JSONObject jsonObject = new JSONObject(body);
        searchResponse.setTotal(jsonObject.getJSONObject("albums").getInt("total"));
        searchResponse.setOffset(jsonObject.getJSONObject("albums").getInt("offset"));
        searchResponse.setLimit(jsonObject.getJSONObject("albums").getInt("limit"));
        searchResponse.setNext(jsonObject.getJSONObject("albums").getString("next"));
        searchResponse.setHref(jsonObject.getJSONObject("albums").getString("href"));

        JSONArray jsonArray = jsonObject.getJSONObject("albums").getJSONArray("items");
        Iterator iterator = jsonArray.iterator();
        while (iterator.hasNext()) {
            JSONObject next = (JSONObject) iterator.next();
            Album album = new Album();
            album.setName(next.getString("name"));
            album.setAlbumType(next.getString("album_type"));
            album.setUrl(next.getString("href"));
            album.setUri(next.getString("uri"));
            album.setReleaseDate(next.getString("release_date"));
            album.setReleaseDatePrecision(next.getString("release_date_precision"));
            album.setTotalTracks(next.getInt("total_tracks"));
            searchResponse.getAlbums().add(album);
        }

        return searchResponse;
    }
}
