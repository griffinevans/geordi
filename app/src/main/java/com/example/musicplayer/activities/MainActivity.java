package com.example.musicplayer.activities;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.musicplayer.R;
import com.example.musicplayer.service.MusicPlayerService;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    MusicPlayerService mService;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //request permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, MusicPlayerService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
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

    @Override
    protected void onStop() {
        super.onStop();

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

    public void musicBarClick(View view) {
        Intent i = new Intent(this, PlayerActivity.class);
        startActivity(i);
    }

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
}