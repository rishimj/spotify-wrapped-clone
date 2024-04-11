package com.example.spotifywrapped;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifywrapped.Song;

import java.util.ArrayList;

public class TopSongs extends AppCompatActivity {
    public static ArrayList<Song> songList = new ArrayList<>();
    private RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_songs);
        rv = findViewById(R.id.song_recycler);

        setAdapter();
    }


    private void setAdapter() {
        SongAdapter adapter = new SongAdapter(songList, TopSongs.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(layoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(adapter);
    }
}
