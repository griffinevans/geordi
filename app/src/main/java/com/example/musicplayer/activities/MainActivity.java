package com.example.musicplayer.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;
import com.example.musicplayer.adapters.AlbumsAdapter;
import com.example.musicplayer.models.AlbumModel;
import com.example.musicplayer.utils.MusicLibrary;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AlbumsAdapter.ItemClickListener {

    MusicLibrary musicLibrary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        setContentView(R.layout.activity_main);

        musicLibrary = new MusicLibrary();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<AlbumModel> albumModelList = musicLibrary.getAllAlbums(this);
        recyclerView.setHasFixedSize(true);

        AlbumsAdapter adapter = new AlbumsAdapter(this, albumModelList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

    }

    public void toPlayer(View view) {
        Intent i = new Intent(this, PlayerActivity.class);
        startActivity(i);
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}