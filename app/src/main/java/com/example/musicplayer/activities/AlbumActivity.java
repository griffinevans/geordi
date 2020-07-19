/**
 * This activity shows a list of tracks from a selected album. You can select a track from the album, and
 * it will be played.
 */
package com.example.musicplayer.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.musicplayer.R;
import com.example.musicplayer.models.Album;
import com.example.musicplayer.models.Track;
import com.example.musicplayer.service.MusicPlayerService;

public class AlbumActivity extends AppCompatActivity {

    private static final String TAG = "AlbumActivity";

    MusicPlayerService mService;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_album, menu);
        return true;
    }

    public void musicBarClick(View view) {
        Intent i = new Intent(this, PlayerActivity.class);
        startActivity(i);
    }

    /**
     * We bind to the service when the activity resumes
     */
    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = new Intent(this, MusicPlayerService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    /**
     * Unbind the service when the activity is paused
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (connection != null) {
            unbindService(connection);
            mBound = false;
        }
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MusicPlayerService.LocalBinder binder = (MusicPlayerService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    public void musicBarControls(View view) {
        if (mBound) {
            mService.togglePlay();
            ImageButton button = findViewById(R.id.musicbar_play_controls);
            if (mService.isPlaying()) {
                button.setImageResource(R.drawable.ic_media_pause);
            } else {
                button.setImageResource(R.drawable.ic_media_play);
            }
        } else {
            Log.e(TAG, "Not bound");
        }
    }

    public void onTrackSelect(Track model) {
        Album album = (Album) getIntent().getExtras().get("ALBUM");
        if (album != null) {
            mService.playAlbum(album, model);
        } else {
            Log.e(TAG, "Error:Album not found");
        }
    }

}