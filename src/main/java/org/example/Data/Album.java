package org.example.Data;

import java.util.List;

public class Album {
    List<Artist> artists;
    String albumType;
    String name;
    String releaseDate;
    String releaseDatePrecision;
    int totalTracks;
    String uri;
    String url;

    @Override
    public String toString() {
        return "Album{" +
                "artists=" + artists +
                ", albumType='" + albumType + '\'' +
                ", name='" + name + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", releaseDatePrecision='" + releaseDatePrecision + '\'' +
                ", totalTracks=" + totalTracks +
                ", uri='" + uri + '\'' +
                ", url='" + url + '\'' +
                '}';
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
