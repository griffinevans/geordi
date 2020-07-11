package com.example.musicplayer.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.example.musicplayer.models.AlbumModel;
import com.example.musicplayer.models.TrackModel;

import java.util.ArrayList;
import java.util.List;

/**
 * this sucks
 */
public class MusicLibrary {

    public List<AlbumModel> getAllAlbums(Context context) {

        final ArrayList<AlbumModel> albums = new ArrayList<>();

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                null,
                null,
                null,
                null);

        // Cache column indices.
        int idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums._ID);
        int nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM);
        //int albumIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ALBUM_ID);
        int artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST);
        int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.NUMBER_OF_SONGS);

        while (cursor.moveToNext()) {
            // Get values of columns for a given album.
            long id = cursor.getLong(idColumn);
            String name = cursor.getString(nameColumn);
            //int albumID = cursor.getInt(albumIdColumn);
            String artist = cursor.getString(artistColumn);
            int size = cursor.getInt(sizeColumn);

            // Stores column values and the contentUri in a local object
            // that represents the media file.
            albums.add(new AlbumModel(id, name, artist, size));
        }
        cursor.close();
        return albums;
    }

    public List<TrackModel> getTracksForAlbum(Context context, long ID) {
        ArrayList<TrackModel> tracks = new ArrayList<>();
        String selection = MediaStore.Audio.Media.ALBUM_ID + "=" + ID;

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                null,
                selection,
                null,
                null);

        if(cursor != null) {
            while(cursor.moveToNext()) {
                long id = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                long duration = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
                String album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                int number = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.TRACK));


                tracks.add(new TrackModel(id, title, artist, duration, album, number));
            }

        }
        return tracks;
    }
}
