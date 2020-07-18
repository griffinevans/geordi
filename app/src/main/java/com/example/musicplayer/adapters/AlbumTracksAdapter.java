package com.example.musicplayer.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;
import com.example.musicplayer.activities.AlbumActivity;
import com.example.musicplayer.models.Track;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Track}.
 */
public class AlbumTracksAdapter extends RecyclerView.Adapter<AlbumTracksAdapter.ViewHolder> {

    private static final String TAG = "AlbumTracksAdapter";

    private final List<Track> mValues;

    private Context mContext;


    public AlbumTracksAdapter(List<Track> items, Context context) {
        mValues = items;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_track_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlbumTracksAdapter.ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mTitleView.setText(mValues.get(position).getTrackName());
        holder.mArtistView.setText(mValues.get(position).getArtistName());
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "On Click");
                //This calls onTrackSelect in the AlbumActivity
                if (mContext instanceof AlbumActivity) {
                    Track selectedTrack = mValues.get(position);
                    ((AlbumActivity) mContext).onTrackSelect(selectedTrack);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
        public final TextView mArtistView;
        public Track mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.song_name);
            mArtistView = (TextView) view.findViewById(R.id.artist_name);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mArtistView.getText() + "'";
        }

    }

    public interface OnTrackSelectedListener {
        void onTrackSelected(Track track);
    }

}