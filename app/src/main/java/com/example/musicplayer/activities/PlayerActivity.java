package com.example.musicplayer.activities;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.musicplayer.R;
import com.example.musicplayer.service.MusicPlayerService;

public class PlayerActivity extends AppCompatActivity {

    private MusicPlayerService mService;
    boolean mBound = false;
    private static final String TAG = "PlayerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String nowPlaying = "";
        toolbar.setTitle("Playing from: " + nowPlaying);

    }

    public void menuButtonClick(View v) {
        //TODO
    }

    public void mediaPlayButtonClick(View v) {
        if (mBound) {
            Boolean bool = mService.playButtonClick();
            ImageButton button = (ImageButton) findViewById(R.id.mediaPlayButton);
            if (bool) {
                button.setImageResource(R.drawable.ic_media_play);
            } else {
                button.setImageResource(R.drawable.ic_media_pause);
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_player,menu);
        return true;
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        // Called when the connection with the service is established
        public void onServiceConnected(ComponentName className, IBinder service) {
            // Because we have bound to an explicit
            // service that is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            MusicPlayerService.LocalBinder binder = (MusicPlayerService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        // Called when the connection with the service disconnects unexpectedly
        public void onServiceDisconnected(ComponentName className) {
            Log.e(TAG, "onServiceDisconnected");
            mBound = false;
        }
    };

}
