/**
 * Contains information about an album. This only stores data that needs
 * to be accessed quickly and frequently, such as information needed to populate UI fields
 */


package com.example.musicplayer.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Album implements Parcelable {

    private final String mAlbumName;
    private final String mArtistName;
    private final String mAlbumKey;
    private final String mAlbumArtURL;
    private long mAlbumID; //id in media store
    private int mTracksNumber;

    public Album(String name, String artistName, String albumKey, String albumArtURL, long albumID, int tracksNumber) {
        if (name != null) {
            mAlbumName = name;
        } else {
            mAlbumName = "";
        }
        if (artistName != null) {
            mArtistName = artistName;
        } else {
            mArtistName = "";
        }
        if (albumKey != null) {
            mAlbumKey = albumKey;
        } else {
            mAlbumKey = "";
        }
        if (albumArtURL != null) {
            mAlbumArtURL = albumArtURL;
        } else {
            mAlbumArtURL = "";
        }
        mAlbumID = albumID;
        mTracksNumber = tracksNumber;
    }

    protected Album(Parcel in) {
        mAlbumName = in.readString();
        mArtistName = in.readString();
        mAlbumKey = in.readString();
        mAlbumArtURL = in.readString();
        mAlbumID = in.readLong();
        mTracksNumber = in.readInt();
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

    public String getName() {
        return mAlbumName;
    }

    public String getArtist() {
        return mArtistName;
    }

    public String getAlbumKey() {
        return mAlbumKey;
    }

    public String getAlbumArtURL() {
        return mAlbumArtURL;
    }

    public long getID() {
        return mAlbumID;
    }

    public int numberOfTracks() {
        return mTracksNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAlbumName);
        dest.writeString(mArtistName);
        dest.writeString(mAlbumKey);
        dest.writeString(mAlbumArtURL);
        dest.writeLong(mAlbumID);
        dest.writeInt(mTracksNumber);
    }
}
