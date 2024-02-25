package org.example.Services;

import org.example.Data.Album;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class DetailService {
    int index = 0;

    List<Album> albums;

    public DetailService() {
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Album getCurrentAlbum() {
        return albums.get(index);
    }

    public int getIndex() {
        return index;
    }

    public void updateDetails(JLabel albumImageLabel, JLabel albumNameLabel, JLabel releaseDateLabel, JLabel totalTracksLabel, JLabel albumIdLabel) {
        System.out.println(getCurrentAlbum());
        String imageUrl = getCurrentAlbum().getAlbumImages().stream().filter(x -> x.getWidth() == 300).toList().get(0).getUrl();
        albumNameLabel.setText(getCurrentAlbum().getName());
        releaseDateLabel.setText(getCurrentAlbum().getReleaseDate());
        totalTracksLabel.setText(String.valueOf(getCurrentAlbum().getTotalTracks()));
        albumIdLabel.setText(getCurrentAlbum().getId());
        try {
            albumImageLabel.setIcon(new ImageIcon(new URL(imageUrl).openStream().readAllBytes()));
            albumImageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        } catch (IOException ex) {
            System.out.println("could not read image: " + imageUrl);
        }
    }
}
