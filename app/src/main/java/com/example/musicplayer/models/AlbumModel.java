package com.example.musicplayer.models;

import android.net.Uri;

public class AlbumModel {
    private final Uri uri;
    private final String name;
    private final String artist;
    private final int size;

    public AlbumModel(Uri uri, String name, String artist, int size) {
        this.uri = uri;
        this.name = name;
        this.artist = artist;
        this.size = size;
    }

    public Uri uri() { return uri; }
    public String name() { return name; }
    public String artist() { return artist; }
    public int size() { return size; }
}
