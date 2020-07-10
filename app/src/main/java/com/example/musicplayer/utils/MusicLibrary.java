package com.example.musicplayer.utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.musicplayer.models.AlbumModel;

import java.util.ArrayList;
import java.util.List;


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
            int artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.ARTIST);
            int sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Albums.NUMBER_OF_SONGS);

            while (cursor.moveToNext()) {
                // Get values of columns for a given album.
                long id = cursor.getLong(idColumn);
                String name = cursor.getString(nameColumn);
                String artist = cursor.getString(artistColumn);
                int size = cursor.getInt(sizeColumn);

                Uri contentUri = ContentUris.withAppendedId(
                        MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, id);

                // Stores column values and the contentUri in a local object
                // that represents the media file.
                albums.add(new AlbumModel(contentUri, name, artist, size));
            }
        cursor.close();
        return albums;
    }
}
