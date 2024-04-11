package com.example.spotifywrapped;

public class Song {

    private String name;
    private int duration;
    private String artist;
    private String preview_url;
    private String image_url;

    public Song(String name, int duration, String artist, String image_url, String preview_url) {
        this.name = name;
        this.artist = artist;
        this.duration = duration;
        this.preview_url = preview_url;
        this.image_url = image_url;
    }

    public Song(String name, int duration, String artist, String image_url){
        this(name, duration, artist, image_url, null);
    }
    public Song(String name){
        this(name, 0, null, null, null);
    }
    public Song(String name, String image_url, String preview_url){
        this(name, 0, null, image_url, preview_url);
    }

    public String getName() {
        return name;
    }

    public int getDuration() {
        return duration;
    }

    public String getArtist() {
        return artist;
    }

    public String getPreview_url() {
        return preview_url;
    }

    public String getImage_url() {
        return image_url;
    }
}