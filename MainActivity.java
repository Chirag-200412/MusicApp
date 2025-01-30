package com.example.musicapp;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.activity.ComponentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class MainActivity extends ComponentActivity{

    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private ArrayList<Song> songList;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar, volumeBar;
    private TextView currentTime, totalTime, currentSongTitle;
    private Button playButton, pauseButton, stopButton;
    private Handler handler = new Handler();
    private AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        recyclerView = findViewById(R.id.recyclerView);
        currentTime = findViewById(R.id.current_time);
        totalTime = findViewById(R.id.total_time);
        currentSongTitle = findViewById(R.id.current_song_title);
        seekBar = findViewById(R.id.seekBar);
        volumeBar = findViewById(R.id.volumeBar);
        playButton = findViewById(R.id.play_button);
        pauseButton = findViewById(R.id.pause_button);
        stopButton = findViewById(R.id.stop_button);

        // Initialize the AudioManager for volume control
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        setupVolumeControl();

        // Initialize the song list
        songList = new ArrayList<>();
        populateSongList();

        // Setup RecyclerView for song list
        songAdapter = new SongAdapter(this, songList, new SongAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Song song) {
                playSong(song);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(songAdapter);

        // Set button listeners
        setButtonListeners();
    }

    // Populate the song list
    private void populateSongList() {
        songList.add(new Song("Dil Ka Aalam", R.raw.song1));
        songList.add(new Song("Dil Ibadat", R.raw.song2));
        songList.add(new Song("Dil Sambhal Ja Zara", R.raw.song3));
        songList.add(new Song("Apa Fer Milange", R.raw.song4));
        songList.add(new Song("Kali Kali Zulfon Ke", R.raw.song5));
        songList.add(new Song("Husn", R.raw.song6));
        songList.add(new Song("O Bedardeya Rap", R.raw.song7));
        songList.add(new Song("Sach Keh Raha Hai Deewana", R.raw.song8));
        songList.add(new Song("Nadaniyan", R.raw.song9));
        songList.add(new Song("Guilty", R.raw.song10));

    }

    // Play the selected song
    private void playSong(Song song) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }

        mediaPlayer = MediaPlayer.create(MainActivity.this, song.getFileResId());
        currentSongTitle.setText(song.getTitle());
        seekBar.setMax(mediaPlayer.getDuration());
        totalTime.setText(formatTime(mediaPlayer.getDuration()));

        mediaPlayer.start();
        updateSeekBar();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                seekBar.setProgress(0);
                currentTime.setText(formatTime(0));
            }
        });
    }

    // Setup the play, pause, and stop button listeners
    private void setButtonListeners() {
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    mediaPlayer.start();
                    updateSeekBar();
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
                    mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.song1); // Reset to first song
                    seekBar.setProgress(0);
                    currentTime.setText(formatTime(0));
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser && mediaPlayer != null) {
                    mediaPlayer.seekTo(progress);
                    currentTime.setText(formatTime(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    // Update the SeekBar in real-time
    private void updateSeekBar() {
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        currentTime.setText(formatTime(mediaPlayer.getCurrentPosition()));
        if (mediaPlayer.isPlaying()) {
            handler.postDelayed(updateTimeTask, 1000);
        }
    }

    // Runnable to update the SeekBar every second
    private Runnable updateTimeTask = new Runnable() {
        public void run() {
            updateSeekBar();
        }
    };

    // Format time to mm:ss
    private String formatTime(int timeInMillis) {
        int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(timeInMillis);
        int seconds = (int) (TimeUnit.MILLISECONDS.toSeconds(timeInMillis) % 60);
        return String.format("%02d:%02d", minutes, seconds);
    }

    // Setup volume control
    private void setupVolumeControl() {
        volumeBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumeBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
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
