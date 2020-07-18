/**
 * This is based off of code written by Team Gateship-One for the Odyssey music player.
 */

package com.example.musicplayer.utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.util.Log;

import com.example.musicplayer.models.Album;
import com.example.musicplayer.models.Track;

import java.util.ArrayList;
import java.util.List;

/**
 * MediaStoreUtil gets the
 */
public class MediaStoreUtil {

    private static final String TAG = "MediaStoreUtil";

    /**
     * Projection string used with MediaStore to use only the columns that will be used for the TrackModel
     */
    private static String[] getTrackProjections() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return new String[]{
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.ALBUM_KEY,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.TRACK,
                    BaseColumns._ID,
            };
        } else {
            return new String[]{
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.ALBUM_KEY,
                    MediaStore.Audio.Media.DURATION, //not sure why this has an API issue
                    MediaStore.Audio.Media.TRACK,
                    MediaStore.Audio.Media._ID,
            };
        }
    }

    /**
     * Interface used to select columns with MediaStore
     */
    interface TrackProjections {
        String[] PROJECTION = getTrackProjections();

        String TRACK_NAME = PROJECTION[0];
        String ARTIST = PROJECTION[1];
        String ALBUM = PROJECTION[2];
        String ALBUM_KEY = PROJECTION[3];
        String DURATION = PROJECTION[4];
        String TRACK_NUMBER = PROJECTION[5];
        String ID = PROJECTION[6];

        String IS_MUSIC = MediaStore.Audio.Media.IS_MUSIC;
    }

    /**
     * Projections columns to retrieve data about an album with MediaStore
     */
    private static String[] getAlbumProjections() {
        return new String[]{
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.ALBUM_KEY,
                MediaStore.Audio.Albums.ALBUM_ART,
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS,
        };
    }

    /**
     * Interface to retrieve projections for albums
     */
    interface AlbumProjections {
        String[] PROJECTION = getAlbumProjections();

        String ALBUM = PROJECTION[0];
        String ARTIST = PROJECTION[1];
        String ALBUM_KEY = PROJECTION[2];
        String ALBUM_ART = PROJECTION[3];
        String ID = PROJECTION[4];
        String NUMBER_TRACKS = PROJECTION[5];

    }

    public List<Album> getAllAlbums(Context context) {

        final ArrayList<Album> albums = new ArrayList<>();

        final Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                AlbumProjections.PROJECTION,
                null,
                null,
                null);

        if (cursor != null) {

            //Get column indexes. Not sure to use getColumnIndex vs getColumnIndexOrThrow
            final int albumColumn = cursor.getColumnIndex(AlbumProjections.ALBUM);
            final int artistColumn = cursor.getColumnIndex(AlbumProjections.ARTIST);
            final int albumKeyColumn = cursor.getColumnIndex(AlbumProjections.ALBUM_KEY);
            final int albumArtColumn = cursor.getColumnIndex(AlbumProjections.ALBUM_ART);
            final int idColumn = cursor.getColumnIndex(AlbumProjections.ID);
            final int numberTracksColumn = cursor.getColumnIndex(AlbumProjections.NUMBER_TRACKS);

            if (cursor.moveToFirst()) {

                do {
                    final String album = cursor.getString(albumColumn);
                    final String artist = cursor.getString(artistColumn);
                    final String albumKey = cursor.getString(albumKeyColumn);
                    final String albumArt = cursor.getString(albumArtColumn);
                    final long id = cursor.getLong(idColumn);
                    final int numberOfTracks = cursor.getInt(numberTracksColumn);

                    albums.add(new Album(album, artist, albumKey, albumArt, id, numberOfTracks));
                } while (cursor.moveToNext());

            }
            cursor.close();
        } else {
            Log.e(TAG, "No albums found on device");
        }
        return albums;
    }

    //Returns an ArrayList<TrackModel> from an AlbumModel
    public List<Track> getTracksForAlbum(Context context, Album album) {
        List<Track> tracks = new ArrayList<>();
        String selection = MediaStore.Audio.Media.ALBUM_ID + "=" + album.getID(); //filter selection to only tracks with the albumKey of our album
        final String order = TrackProjections.TRACK_NUMBER; //order by track number

        Cursor cursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                TrackProjections.PROJECTION,
                selection,
                null,
                order);

        if (cursor != null) {
            //Get column indexes.
            final int nameColumn = cursor.getColumnIndex(TrackProjections.TRACK_NAME);
            final int artistColumn = cursor.getColumnIndex(TrackProjections.ARTIST);
            final int albumColumn = cursor.getColumnIndex(TrackProjections.ALBUM);
            final int albumKeyColumn = cursor.getColumnIndex(TrackProjections.ALBUM_KEY);
            final int durationColumn = cursor.getColumnIndex(TrackProjections.DURATION);
            final int trackNumColumn = cursor.getColumnIndex(TrackProjections.TRACK_NUMBER);
            final int idColumn = cursor.getColumnIndex(TrackProjections.ID);

            if (cursor.moveToFirst()) {
                do {
                    final String name = cursor.getString(nameColumn);
                    final String artist = cursor.getString(artistColumn);
                    final String albumName = cursor.getString(albumColumn);
                    final String albumKey = cursor.getString(albumKeyColumn);
                    final long duration = cursor.getLong(durationColumn);
                    final int trackNumber = cursor.getInt(trackNumColumn);
                    final long id = cursor.getLong(idColumn);
                    final Uri url = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);

                    tracks.add(new Track(name, artist, albumName, albumKey, url, duration, trackNumber, id));
                } while (cursor.moveToNext());
            } else {
                Log.e(TAG, "Cursor failed move to first");
            }
            cursor.close();
        } else {
            Log.e(TAG, "No tracks found for album");
        }
        return tracks;
    }

}
