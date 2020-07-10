package com.example.musicplayer.models;

import android.net.Uri;

public class ArtistModel {
    private final Uri uri;
    private final String name;
    private final String artist;
    private final int size;

    public ArtistModel(Uri uri, String name, String artist, int size) {
        this.uri = uri;
        this.name = name;
        this.artist = artist;
        this.size = size;
    }
}