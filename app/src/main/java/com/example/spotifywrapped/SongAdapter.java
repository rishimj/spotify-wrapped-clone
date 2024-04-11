package com.example.spotifywrapped;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.contentcapture.ContentCaptureCondition;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifywrapped.Song;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.MyViewHolder> {
    private ArrayList<Song> songList;
    private Context context;

    public SongAdapter(ArrayList<Song> songList, Context context) {
        this.context = context;
        this.songList = songList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView artist;
        private ImageView image;
        private ImageView preview;
        private TextView duration;
        private MediaPlayer mplayer;

        public MyViewHolder(final View view) {
            super(view);
            name = view.findViewById(R.id.name1);
            image = view.findViewById(R.id.imageView);
            preview= view.findViewById(R.id.playBtn);
            artist = view.findViewById(R.id.artist1);
            duration = view.findViewById(R.id.duration1);


            itemView.findViewById(R.id.playBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mplayer != null) {
                        if (mplayer.isPlaying()) {
                            mplayer.pause();
                            preview.setImageResource(R.drawable.play_circle);
                        } else {
                            mplayer.start();
                            preview.setImageResource(R.drawable.pause_circle);
                        }
                    } else {
                        Toast.makeText(context.getApplicationContext(), "Unavailable preview", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

    }
    @NonNull
    @Override
    public SongAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SongAdapter.MyViewHolder holder, int position) {
        holder.name.setText(songList.get(position).getName());
        holder.artist.setText(songList.get(position).getArtist());
        holder.duration.setText("00:"+ Integer.toString(songList.get(position).getDuration()));
        Picasso.get().load(songList.get(position).getImage_url()).into(holder.image);

        if (songList.get(position).getPreview_url() != "null") {
            holder.mplayer = new MediaPlayer();
            try {
                holder.mplayer.setDataSource(songList.get(position).getPreview_url());
                holder.mplayer.prepare();
            } catch (IOException e) {
                Log.d("demo", "can't load media src");
                throw new RuntimeException(e);
            }
        }


    }


    @Override
    public int getItemCount() {
        return songList.size();
    }
}