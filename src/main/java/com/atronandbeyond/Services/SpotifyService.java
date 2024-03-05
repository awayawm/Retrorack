package com.atronandbeyond.Services;

import com.atronandbeyond.Dao.DBConnection;
import com.atronandbeyond.Data.Album;
import com.atronandbeyond.Data.AlbumImage;
import com.atronandbeyond.Data.Artist;
import com.atronandbeyond.Data.SearchResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SpotifyService {

    String tokenApi = "https://accounts.spotify.com/api/token";
    String searchApi = "https://api.spotify.com/v1/search";
    String albumsApi = "https://api.spotify.com/v1/albums/";

    public SpotifyService() {
    }

    public String getToken(ConfigService configService) throws IOException, InterruptedException {

        String clientId = configService.getConfig().getSpotifyClientId();
        String clientSecret = configService.getConfig().getSpotifyClientSecret();

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

        if (statusCode != 200) {
            System.out.println(body);
        }

        JSONObject jsonObject = new JSONObject(body);
        return jsonObject.getString("access_token");
    }

    public String search(String token, String q) throws IOException, InterruptedException {
        String search = URLEncoder.encode(q, StandardCharsets.UTF_8);
        String url = searchApi + "?q=" + search + "&type=album";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        String body = response.body();
//        System.out.println(body);

        return body;
    }

    public String getAlbumById(String token, String id) throws IOException, InterruptedException {
        String url = albumsApi + id;

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
            album.setHref(next.getString("href"));
            album.setUri(next.getString("uri"));
            album.setReleaseDate(next.getString("release_date"));
            album.setReleaseDatePrecision(next.getString("release_date_precision"));
            album.setTotalTracks(next.getInt("total_tracks"));
            album.setId(next.getString("id"));


            List<AlbumImage> images = new ArrayList<>();
            JSONArray imagesArray = next.getJSONArray("images");
            Iterator imagesIterator = imagesArray.iterator();
            while (imagesIterator.hasNext()) {
                JSONObject nextImage = (JSONObject) imagesIterator.next();
                AlbumImage albumImage = new AlbumImage();
                albumImage.setUrl(nextImage.getString("url"));
                albumImage.setHeight(nextImage.getInt("height"));
                albumImage.setWidth(nextImage.getInt("width"));
                images.add(albumImage);
            }
            album.getAlbumImages().addAll(images);

            List<Artist> artists = new ArrayList<>();
            JSONArray artistsArray = next.getJSONArray("artists");
            Iterator artistsIterator = artistsArray.iterator();
            while (artistsIterator.hasNext()) {
                JSONObject nextArtist = (JSONObject) artistsIterator.next();
                Artist artist = new Artist();
                artist.setId(nextArtist.getString("id"));
                artist.setName(nextArtist.getString("name"));
                artist.setUri(nextArtist.getString("uri"));
                artist.setHref(nextArtist.getString("href"));
                artists.add(artist);
            }
            album.getArtists().addAll(artists);
            searchResponse.getAlbums().add(album);
        }

        return searchResponse;
    }

    public void getNonPopulatedIds(ConfigService configService) {
        ArrayList<String> ids = DBConnection.getNonPopulatedIds(configService.getConfig());
        for (String id : ids) {
            try {
                System.out.println("getNonPopulatedIds");
                System.out.println(getAlbumById(getToken(configService), id));
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
