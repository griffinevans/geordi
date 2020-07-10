package com.example.musicplayer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;
import com.example.musicplayer.adapters.AlbumsAdapter;
import com.example.musicplayer.models.AlbumModel;
import com.example.musicplayer.utils.MusicLibrary;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * //TODO make tabs swipeable
 */
public class AlbumsListFragment extends Fragment implements AlbumsAdapter.ItemClickListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AlbumsListFragment() {
    }

    public static AlbumsListFragment newInstance(int columnCount) {
        AlbumsListFragment fragment = new AlbumsListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            List<AlbumModel> albumModelList = new MusicLibrary().getAllAlbums(getContext());
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setHasFixedSize(true);
            AlbumsAdapter adapter = new AlbumsAdapter(getContext(), albumModelList);
            adapter.setClickListener(this);
            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    /**
     * Select an album to view
     */
    @Override
    public void onItemClick(View view, int position) {

    }
}