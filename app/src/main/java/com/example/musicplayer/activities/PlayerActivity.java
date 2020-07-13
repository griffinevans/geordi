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
import com.example.musicplayer.models.TrackModel;
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

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = new Intent(this, MusicPlayerService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            MusicPlayerService.LocalBinder binder = (MusicPlayerService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;

            updateSongInfo(mService.currentTrack());
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    //updates the view elements of the player UI. called when track changes.
    public void updateSongInfo(TrackModel model) {
        TextView titleView = (TextView) findViewById(R.id.songNameView);
        TextView artistView = (TextView) findViewById(R.id.artistNameView);
        TextView albumView = (TextView) findViewById(R.id.albumNameView);
        //TODO album art

        titleView.setText(model.getTitle());
        artistView.setText(model.getArtist());
        albumView.setText(model.getAlbum());

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
}
