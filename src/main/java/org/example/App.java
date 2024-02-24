package org.example;

import org.example.Data.Album;
import org.example.Data.SearchResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Iterator;

public class App {
    String title = "RetroRack";
    SpotifyService service;

    //todo do not commit
    String clientId = "";
    //todo do not commit
    String clientSecret = "";

    App(String[] args) {
        service = new SpotifyService();
        service.setCredentials(clientId, clientSecret);
    }


    public static void main(String[] args) throws IOException, InterruptedException {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new App(args).show();
            }
        });
    }

    SearchResponse parseSearchServiceResponse(String body) {
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

    public void show() {
        JList jlist = new JList();
        jlist.setFixedCellWidth(200);
        JLabel jLabel = new JLabel("Search: ");
        JTextField textField = new JTextField(20);
        JButton jButton = new JButton("Go");
        jButton.addActionListener(e -> {
            try {
                String token = service.getToken();
                String q = textField.getText();
                String response = service.search(token, q);
                SearchResponse parsedResponse = parseSearchServiceResponse(response);
                Object[] list = parsedResponse.getAlbums().stream().map(Album::getName).toList().toArray();
                jlist.setListData(list);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        JFrame f = new JFrame(title);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setPreferredSize(new Dimension(800, 600));

        // search bar
        JPanel top = new JPanel(new FlowLayout());
        top.add(jLabel);
        top.add(textField);
        top.add(jButton);

        // list/detail view
        JPanel bottomHalf = new JPanel();
        bottomHalf.setLayout(new BoxLayout(bottomHalf, BoxLayout.X_AXIS));
        bottomHalf.add(jlist);

        // separate search from list/detail view
        JPanel topBottom = new JPanel();
        topBottom.setLayout(new BoxLayout(topBottom, BoxLayout.Y_AXIS));
        topBottom.add(top);
        topBottom.add(bottomHalf);

        f.getContentPane().add(topBottom);
        f.pack();
        f.setVisible(true);
    }

}