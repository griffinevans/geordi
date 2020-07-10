package com.example.musicplayer.models;

import android.net.Uri;

public class TrackModel {
    private final Uri uri;
    private final String name;
    private final String artist;
    private final String  duration;
    private final String album;
    public int rating;

    public TrackModel(Uri uri, String name, String artist, String duration, String album) {
        this.uri = uri;
        this.name = name;
        this.artist = artist;
        this.duration = duration;
        this.album = album;
        this.rating = -1;
    }
}