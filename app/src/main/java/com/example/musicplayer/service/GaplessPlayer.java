/**
 * GaplessPlayer handles a list of songs and uses 3 MediaPlayers to switch between songs quickly.
 */
package com.example.musicplayer.service;

import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;

import com.example.musicplayer.models.Track;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

public class GaplessPlayer {

    /**
     * MediaPlayers for current track, next track (if any) and previous track (if any)
     */
    private MediaPlayer currentMediaPlayer = null;
    private MediaPlayer previousMediaPlayer = null;
    private MediaPlayer nextMediaPlayer = null;

    private boolean currentPlayerPrepared = false;

    MusicPlayerService mPlayerService;

    /**
     * List of songs functions as queue
     */
    private LinkedList<Track> linkedList;

    /**
     * Iterator to traverse queue
     */
    private Iterator<Track> iterator;

    /**
     *
     */
    public GaplessPlayer(MusicPlayerService service) {
        mPlayerService = service;
    }

    public void prepareFromTrack(MediaPlayer mediaPlayer, Track track) {
        //build mediaPlayer if it is null
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();

            mediaPlayer.setAudioAttributes(
                    new AudioAttributes.Builder()
                            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                            .setUsage(AudioAttributes.USAGE_MEDIA)
                            .build()
            );
        }

        try {
            mediaPlayer.setDataSource(mPlayerService.getApplicationContext(), track.getUri());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        if (currentMediaPlayer != null && currentPlayerPrepared) {
            currentMediaPlayer.start();
        }
    }
}
