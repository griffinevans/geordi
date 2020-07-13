package com.example.musicplayer.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.Toast;

import com.example.musicplayer.R;
import com.example.musicplayer.activities.PlayerActivity;
import com.example.musicplayer.models.AlbumModel;
import com.example.musicplayer.models.TrackModel;
import com.example.musicplayer.utils.MusicLibrary;

import java.io.IOException;
import java.util.ArrayList;

public class MusicPlayerService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    private Uri currentSongUri;
    private NotificationManager mNM;
    private static final String ACTION_PLAY = "com.example.action.PLAY";
    public MediaPlayer mediaPlayer = null;
    private final IBinder mBinder = new LocalBinder();
    private MusicLibrary musicLibrary;
    private TrackModel currentTrack;

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private static final int NOTIFICATION = R.string.music_service_started;
    private static final String CHANNEL_ID = "GEORDI PLAYER CHANNEL ID";


    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        public MusicPlayerService getService() {
            return MusicPlayerService.this;
        }
    }

    @Override
    public void onCreate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            mNM = getSystemService(NotificationManager.class);
            mNM.createNotificationChannel(channel);
        } else {
            mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }

        //loads in the first song in the library
        musicLibrary = new MusicLibrary();
        ArrayList<AlbumModel> list = (ArrayList) musicLibrary.getAllAlbums(this);
        currentTrack = musicLibrary.getTracksForAlbum(this, list.get(0).getId()).get(0);
        currentSongUri = currentTrack.getUri();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        //TODO
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //retrieve uri
        //initialize MediaPlayer
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );
        try {
            mediaPlayer.setDataSource(getApplicationContext(), currentSongUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setOnErrorListener(this);
        // Display a notification about us starting.  We put an icon in the status bar.
        this.showNotification();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);

        // Tell the user we stopped.
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        Toast.makeText(this, R.string.music_service_stopped, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        //TODO
        return false;
    }

    /**
     * Show a notification while this service is running.
     */
    private void showNotification() {
        // In this sample, we'll use the same text for the ticker and the expanded notification
        CharSequence text = getText(R.string.music_service_started);

        // The PendingIntent to launch our activity if the user selects this notification
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, PlayerActivity.class), 0);

        //use channel if version over 26
        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_media_play)  // the status icon
                    .setTicker(text)  // the status text
                    .setWhen(System.currentTimeMillis())  // the time stamp
                    .setContentTitle(getText(R.string.music_service_started))  // the label of the entry
                    .setContentText(text)  // the contents of the entry
                    .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                    .build();
        } else {
            // Set the info for the views that show in the notification panel.
            notification = new Notification.Builder(this)
                    .setSmallIcon(R.drawable.ic_media_play)  // the status icon
                    .setTicker(text)  // the status text
                    .setWhen(System.currentTimeMillis())  // the time stamp
                    .setContentTitle(getText(R.string.music_service_started))  // the label of the entry
                    .setContentText(text)  // the contents of the entry
                    .setContentIntent(contentIntent)  // The intent to send when the entry is clicked
                    .build();
        }


        // Send the notification.
        startForeground(NOTIFICATION, notification);
        //mNM.notify(NOTIFICATION, notification);
    }

    public void togglePlay() {
        if (isPlaying()) {
            mediaPlayer.pause();
        } else {
            if (mediaPlayer != null) {
                mediaPlayer.start();
            } else {
                Toast.makeText(this, "Error: mediaPlayer not initialized", Toast.LENGTH_LONG).show();
            }
        }
    }

    public boolean isPlaying() {
        if (mediaPlayer != null) {
            return mediaPlayer.isPlaying();
        }
        return false;
    }

    public TrackModel currentTrack() {
        return currentTrack;
    }

}
