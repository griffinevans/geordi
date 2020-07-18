/**
 * Contains information about a single audio file. This only stores data that needs
 * to be accessed quickly and frequently, such as information needed to populate UI fields
 */

package com.example.musicplayer.models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class Track implements Parcelable {

    private final String mTrackName;
    private final String mArtistName;
    private final String mAlbumName;
    private final String mAlbumKey; //unique key for album of track
    private final String mTrackURL; //path to media file
    private long mTrackDuration; //duration in ms
    private final int mTrackNumber; //position of the track in the album
    private final long mTrackID; //id of track in mediastore
    public int rating;

    public Track(String name, String artistName, String albumName, String albumKey,
                 Uri url, long duration, int trackNumber, long trackId) {

        if (name != null) {
            mTrackName = name;
        } else {
            mTrackName = "";
        }
        if (artistName != null) {
            mArtistName = artistName;
        } else {
            mArtistName = "";
        }
        if (albumName != null) {
            mAlbumName = albumName;
        } else {
            mAlbumName = "";
        }
        if (albumKey != null) {
            mAlbumKey = albumKey;
        } else {
            mAlbumKey = "";
        }
        mTrackDuration = duration;
        mTrackNumber = trackNumber;
        if (url != null) {
            mTrackURL = url.toString();
        } else {
            mTrackURL = "";
        }
        mTrackID = trackId;

        rating = 0;
    }

    public String getTrackName() {
        return mTrackName;
    }

    public String getArtistName() {
        return mArtistName;
    }

    public String getAlbumName() {
        return mAlbumName;
    }

    public String getAlbumKey() {
        return mAlbumKey;
    }

    public long getDuration() {
        return mTrackDuration;
    }

    public int getTrackNumber() {
        return mTrackNumber;
    }

    public Uri getUri() {
        return Uri.parse(mTrackURL);
    }

    public long getTrackId() {
        return mTrackID;
    }

    protected Track(Parcel in) {
        mTrackName = in.readString();
        mArtistName = in.readString();
        mAlbumName = in.readString();
        mAlbumKey = in.readString();
        mTrackURL = in.readString();
        mTrackDuration = in.readLong();
        mTrackNumber = in.readInt();
        mTrackID = in.readLong();
        rating = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTrackName);
        dest.writeString(mArtistName);
        dest.writeString(mAlbumName);
        dest.writeString(mAlbumKey);
        dest.writeString(mTrackURL);
        dest.writeLong(mTrackDuration);
        dest.writeInt(mTrackNumber);
        dest.writeLong(mTrackID);
        dest.writeInt(rating);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Track> CREATOR = new Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };
}