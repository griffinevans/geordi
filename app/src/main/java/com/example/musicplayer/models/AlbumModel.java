package com.example.musicplayer.models;

public class AlbumModel {
    private final long id;
    private final String name;
    private final String artist;
    //private final int albumID;
    private final int size;

    public AlbumModel(long id, String name, String artist, int size) {
        this.id = id;
        this.name = name;
        this.artist = artist;
        //this.albumID = albumID;
        this.size = size;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public String getArtist() { return artist; }
    //public int getAlbumID() { return albumID; }
    public int getSize() { return size; }
}
