package com.example.musicapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.activity.ComponentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MainActivity extends ComponentActivity {

    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private ArrayList<Song> songList;
    private MediaPlayer mediaPlayer;
    private TextView currentSongTitle;
    private Button playButton, pauseButton, stopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        currentSongTitle = findViewById(R.id.current_song_title);
        playButton = findViewById(R.id.play_button);
        pauseButton = findViewById(R.id.pause_button);
        stopButton = findViewById(R.id.stop_button);

        songList = new ArrayList<>();
        populateSongList(); // Method to populate the song list

        songAdapter = new SongAdapter(this, songList, new SongAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(Song song) {
            playSong(song); // Method to play the selected song
        }
    });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(songAdapter);

        setButtonListeners();
    }

    private void populateSongList() {
        // Adding sample songs (in real apps, you'd fetch this from storage or a server)
        songList.add(new Song("Dil ka Aalam", R.raw.song1));
        songList.add(new Song("Dil Ibadat", R.raw.song2));
        songList.add(new Song("Dil Sambhal Ja Zara", R.raw.song3));
    }

    private void playSong(Song song) {
        // Stop any currently playing song
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        // Update the current song title
        currentSongTitle.setText(song.getTitle());

        // Create a new MediaPlayer instance
        mediaPlayer = MediaPlayer.create(this, song.getFileResId());

        // Start playing the song
        mediaPlayer.start();
    }

    private void setButtonListeners() {
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                }
            }
        });

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = null;
                    currentSongTitle.setText("No song playing");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
