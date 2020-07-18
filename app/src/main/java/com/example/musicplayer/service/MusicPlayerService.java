package com.example.musicplayer.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.musicplayer.R;
import com.example.musicplayer.activities.PlayerActivity;
import com.example.musicplayer.models.Album;
import com.example.musicplayer.models.Track;
import com.example.musicplayer.utils.MediaStoreUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.ListIterator;

import static android.content.ContentValues.TAG;

public class MusicPlayerService extends Service implements MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {

    private NotificationManager mNM;
    public MediaPlayer mediaPlayer = null;
    private final IBinder mBinder = new LocalBinder();
    MediaStoreUtil mediaStoreUtil;
    private Track currentTrack = null;
    //We use an ArrayList and an Iterator to track where the previous and next methods go.
    private ArrayList<Track> trackQueue;
    ListIterator<Track> listIterator;

    private boolean isPrepared = false;

    private boolean repeatSong = false;

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
            mediaStoreUtil = new MediaStoreUtil();
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            mNM = getSystemService(NotificationManager.class);
            mNM.createNotificationChannel(channel);
        } else {
            mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Display a notification about us starting.  We put an icon in the status bar.
        this.showNotification();

        return START_STICKY;
    }

    /**
     * Release the media player when the service ends
     */
    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);
        isPrepared = false;
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        Toast.makeText(this, R.string.music_service_stopped, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (repeatSong && isPrepared) {
            mediaPlayer.seekTo(0);
            mediaPlayer.start();
        } else {
            //if there is another song in the queue, play it
            if (moveToNext()) {
                playCurrentTrack();
            } else {
                isPrepared = false;
                mediaPlayer.stop();
            }
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        isPrepared = true;
        mediaPlayer.start();
    }

    /**
     * Changes the current track to the next track in the queue, if there is one.
     *
     * @return True if there is another song in the queue to move to
     */
    private boolean moveToNext() {
        if (listIterator.hasNext()) {
            currentTrack = listIterator.next();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Changes the current track to the previous track in the queue if one exists.
     *
     * @return True if current track was moved back, false if no previous track found
     */
    private boolean moveToPrevious() {
        if (listIterator.hasPrevious()) {
            currentTrack = listIterator.previous();
            return true;
        } else {
            return false;
        }
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

    /**
     * Swaps state from playing to paused or vice versa
     */
    public void togglePlay() {
        if (isPlaying()) {
            pause();
        } else {
            resume();
        }
    }

    /**
     * Safe call to MediaPlayer.isPlaying() in case of null satte
     */
    public boolean isPlaying() {
        if (mediaPlayer != null && isPrepared) {
            return mediaPlayer.isPlaying();
        }
        return false;
    }

    public Track currentTrack() {
        return currentTrack;
    }

    private void pause() {
        if (isPlaying()) {
            mediaPlayer.pause();
        }
    }

    private void resume() {
        if (mediaPlayer != null && isPrepared) {
            mediaPlayer.start();
        } else {
            Toast.makeText(this, "Error: mediaPlayer not initialized", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Play the next track in the queue, if there is one.
     */
    public void next() {
        if (moveToNext()) {
            playCurrentTrack();
        }
    }

    public void previous() {
        if (moveToPrevious()) {
            playCurrentTrack();
        }
    }

    /**
     * Makes the track queue to the selected album and points the iterator at the selected track, then calls
     *
     * @param album      album to play
     * @param startTrack track to start playing from
     */
    public void playAlbum(Album album, Track startTrack) {
        Log.d(TAG, "Playing " + startTrack.getTrackName());
        currentTrack = startTrack;
        //make the queue
        trackQueue = (ArrayList<Track>) mediaStoreUtil.getTracksForAlbum(getApplicationContext(), album);
        listIterator = trackQueue.listIterator();
        Track tempTrack = listIterator.next();
        //iterate over album until we find desired track
        while (!tempTrack.getAlbumKey().equals(startTrack.getAlbumKey()) && listIterator.hasNext()) {
            tempTrack = listIterator.next();
        }
        playCurrentTrack();
    }

    /**
     * Creates MediaPlayer with current track and plays it.
     */
    private void playCurrentTrack() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            isPrepared = false;
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioAttributes(
                new AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
        );
        try {
            mediaPlayer.setDataSource(getApplicationContext(), currentTrack.getUri());
        } catch (IOException e) {
            Log.e(TAG, "Unable to parse current track url: " + currentTrack.getUri());
            e.printStackTrace();
        }
        mediaPlayer.setOnErrorListener(this);
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.prepareAsync();
    }

}
