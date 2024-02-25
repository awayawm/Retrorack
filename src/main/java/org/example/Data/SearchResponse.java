package org.example.Data;

import java.util.ArrayList;
import java.util.List;

public class SearchResponse {
    int limit;
    String next;
    int offset;
    int total;

    String href;
    List<Album> albums;

    public SearchResponse() {
        albums = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "SearchResponse{" +
                "limit=" + limit +
                ", next='" + next + '\'' +
                ", offset=" + offset +
                ", total=" + total +
                ", href='" + href + '\'' +
                ", albums=" + albums +
                '}';
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }
}


