package com.example.musicplayer.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.musicplayer.R;
import com.example.musicplayer.models.Track;
import com.example.musicplayer.service.MusicPlayerService;

public class PlayerActivity extends AppCompatActivity {

    private MusicPlayerService mService;
    boolean mBound = false;
    private static final String TAG = "PlayerActivity";
    SeekBar seekBar;
    Runnable runSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_player, menu);
        return true;
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

            if (mService.currentTrack() != null) {
                updateSongInfo();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


    /**
     * updates the view elements of the player UI. called when track changes.
     */
    public void updateSongInfo() {
        if (mBound) {
            Track track = mService.currentTrack();
            TextView titleView = (TextView) findViewById(R.id.songNameView);
            TextView artistView = (TextView) findViewById(R.id.artistNameView);
            TextView albumView = (TextView) findViewById(R.id.albumNameView);
            //TODO album art

            titleView.setText(track.getTrackName());
            artistView.setText(track.getArtistName());
            albumView.setText(track.getAlbumName());

            //initialize SeekBar
            seekBar = (SeekBar) findViewById(R.id.seekBar);
            seekBar.setMax(mService.mediaPlayer.getDuration());
            final Handler seekHandler = new Handler();
            runSeekBar = new Runnable() {
                @Override
                public void run() {
                    seekBar.setProgress(mService.mediaPlayer.getCurrentPosition());
                    seekHandler.postDelayed(this, 1000);
                }
            };
            runSeekBar.run();
        }
    }

    public void mediaPlayButtonClick(View v) {
        if (mBound) {
            mService.togglePlay();
            ImageButton button = (ImageButton) findViewById(R.id.mediaPlayButton);
            if (mService.isPlaying()) {
                button.setImageResource(R.drawable.ic_media_pause);
            } else {
                button.setImageResource(R.drawable.ic_media_play);
            }
        }
    }

    public void musicBarClick(View view) {
        Intent i = new Intent(this, PlayerActivity.class);
        startActivity(i);
    }

    public void musicBarControls(View view) {
        if (mBound) {
            mService.togglePlay();
        }
    }

    /**
     * Go to previous song if possible
     */
    public void prevSongClick(View view) {
        if (mBound) {
            mService.previous();
            updateSongInfo();
        }
    }

    /**
     * go to next song if possible
     */
    public void nextSongClick(View view) {
        if (mBound) {
            mService.next();
            updateSongInfo();
        }
    }
}
