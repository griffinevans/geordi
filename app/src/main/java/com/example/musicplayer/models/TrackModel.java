package com.example.musicplayer.models;

public class TrackModel {
    private final long id;
    private final String title;
    private final String artist;
    private final long duration;
    private final String album;
    private final int number;
    public int rating;

    public TrackModel(long id, String name, String artist, long duration, String album, int number) {
        this.id = id;
        this.title = name;
        this.artist = artist;
        this.duration = duration;
        this.album = album;
        this.number = number;
        this.rating = -1;
    }

    public long getId() { return this.id; }
    public String getTitle() { return this.title; }
    public String getArtist() { return this.artist; }
    public long getDuration() { return this.duration; } //in milliseconds
    public String getAlbum() { return this.album; }
    private int getNumber() { return this.number; }
    public int getRating() { return this.rating; }
    public void setRating(int rating) { this.rating = rating; }
}