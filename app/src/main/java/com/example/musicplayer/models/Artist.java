package com.example.musicplayer.models;

import android.net.Uri;

public class Artist {
    private final Uri uri;
    private final String name;
    private final String artist;
    private final int size;

    public Artist(Uri uri, String name, String artist, int size) {
        this.uri = uri;
        this.name = name;
        this.artist = artist;
        this.size = size;
    }
}