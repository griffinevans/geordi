package com.example.musicplayer.activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
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
    BroadcastReceiver broadcastReceiver;

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

        //receives a broadcast that the track has been updated from the service
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (mBound) {
                    updateSongInfo();
                }
            }
        };
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction("com.example.broadcast.TRACK_CHANGED");
        this.registerReceiver(broadcastReceiver, filter);
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
        unregisterReceiver(broadcastReceiver);
    }


    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MusicPlayerService.LocalBinder binder = (MusicPlayerService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;

            updateSongInfo();
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


    /**
     * updates the view elements of the player UI. called by the viewmodel.
     */
    public void updateSongInfo() {
        if (mBound && mService.getCurrentTrack() != null) {
            Track track = mService.getCurrentTrack();
            TextView titleView = findViewById(R.id.songNameView);
            TextView artistView = findViewById(R.id.artistNameView);
            TextView albumView = findViewById(R.id.albumNameView);
            //TODO album art

            titleView.setText(track.getTrackName());
            artistView.setText(track.getArtistName());
            albumView.setText(track.getAlbumName());

            //initialize SeekBar
            seekBar = findViewById(R.id.seekBar);
            seekBar.setMax(mService.getSongDuration());
            final Handler seekHandler = new Handler();
            runSeekBar = new Runnable() {
                @Override
                public void run() {
                    seekBar.setProgress(mService.getSongProgress());
                    seekHandler.postDelayed(this, 1000);
                }
            };
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    if (mBound) {
                        int time = seekBar.getProgress();
                        mService.seekTo(time);
                    }
                }
            });
            runSeekBar.run();
        }
    }

    /**
     * Go to previous song if possible
     */
    public void prevSongClick(View view) {
        if (mBound) {
            mService.previous();
        }
    }

    /**
     * go to next song if possible
     */
    public void nextSongClick(View view) {
        if (mBound) {
            mService.next();
        }
    }

    public void mediaPlayButtonClick(View view) {
        if (mBound) {
            mService.togglePlay();
            ImageButton button = findViewById(R.id.mediaPlayButton);
            if (mService.isPlaying()) {
                button.setImageResource(R.drawable.ic_media_pause);
            } else {
                button.setImageResource(R.drawable.ic_media_play);
            }
        } else {
            Log.e(TAG, "Not bound");
        }
    }
}
