package com.atronandbeyond.Data;

import java.util.ArrayList;
import java.util.List;

public class Album {
    List<Artist> artists;
    List<AlbumImage> albumImages;
    String albumType;
    String name;
    String releaseDate;
    String releaseDatePrecision;
    int totalTracks;
    String uri;
    String href;
    String id;

    public Album() {
        artists = new ArrayList<>();
        albumImages = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Album{" +
                "artists=" + artists +
                ", albumImages=" + albumImages +
                ", albumType='" + albumType + '\'' +
                ", name='" + name + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", releaseDatePrecision='" + releaseDatePrecision + '\'' +
                ", totalTracks=" + totalTracks +
                ", uri='" + uri + '\'' +
                ", href='" + href + '\'' +
                ", id='" + id + '\'' +
                '}';
    }

    public List<AlbumImage> getAlbumImages() {
        return albumImages;
    }

    public void setAlbumImages(List<AlbumImage> albumImages) {
        this.albumImages = albumImages;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public String getAlbumType() {
        return albumType;
    }

    public void setAlbumType(String albumType) {
        this.albumType = albumType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getReleaseDatePrecision() {
        return releaseDatePrecision;
    }

    public void setReleaseDatePrecision(String releaseDatePrecision) {
        this.releaseDatePrecision = releaseDatePrecision;
    }

    public int getTotalTracks() {
        return totalTracks;
    }

    public void setTotalTracks(int totalTracks) {
        this.totalTracks = totalTracks;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}
