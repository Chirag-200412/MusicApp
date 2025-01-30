package com.example.musicapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private Context context;
    private ArrayList<Song> songList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Song song);
    }

    public SongAdapter(Context context, ArrayList<Song> songList, OnItemClickListener listener) {
        this.context = context;
        this.songList = songList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.song_item, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songList.get(position);
        holder.songTitle.setText(song.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(song);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder {
        TextView songTitle;

        public SongViewHolder(View itemView) {
            super(itemView);
            songTitle = itemView.findViewById(R.id.song_title);
        }
    }
}
