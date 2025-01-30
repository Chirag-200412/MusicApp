package com.example.musicapp;

public class Song {
    private String title;
    private int fileResId;

    public Song(String title, int fileResId) {
        this.title = title;
        this.fileResId = fileResId;
    }

    public String getTitle() {
        return title;
    }

    public int getFileResId() {
        return fileResId;
    }
}
