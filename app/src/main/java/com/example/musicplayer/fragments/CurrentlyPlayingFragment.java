package com.example.musicplayer.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.musicplayer.R;
import com.example.musicplayer.models.Track;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CurrentlyPlayingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentlyPlayingFragment extends Fragment {

    private static final String TAG = "CurrentlyPlayingFragment";
    private Track currentTrack;
    BroadcastReceiver broadcastReceiver;

    public CurrentlyPlayingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static CurrentlyPlayingFragment newInstance() {
        return new CurrentlyPlayingFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_currently_playing, container, false);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        //receives a broadcast that the track has been updated from the service
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Track track = (Track) intent.getExtras().get("TRACK");

                currentTrack = track;
                updateTrack();
            }
        };
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction("com.example.broadcast.TRACK_CHANGED");
        context.registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getContext().unregisterReceiver(broadcastReceiver);
    }

    private void updateTrack() {
        if (currentTrack != null) {
            TextView trackName = getView().findViewById(R.id.current_track_name);
            TextView albumName = getView().findViewById(R.id.current_album_name);
            TextView artistName = getView().findViewById(R.id.current_artist_name);

            trackName.setText(currentTrack.getTrackName());
            albumName.setText(currentTrack.getAlbumName());
            artistName.setText(currentTrack.getArtistName());
        }
    }

    public void musicBarControls(View view) {
        Log.e(TAG, "music bar controls clicked");
    }
}