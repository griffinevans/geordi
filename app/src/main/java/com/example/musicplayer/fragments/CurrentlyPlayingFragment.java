package com.example.musicplayer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.musicplayer.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CurrentlyPlayingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CurrentlyPlayingFragment extends Fragment {

    public CurrentlyPlayingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    // TODO: Rename and change types and number of parameters
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

}