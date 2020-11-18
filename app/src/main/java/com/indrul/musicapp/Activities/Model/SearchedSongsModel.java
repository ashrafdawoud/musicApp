package com.indrul.musicapp.Activities.Model;

import java.util.ArrayList;

public class SearchedSongsModel {
    ArrayList<String> track_name=new ArrayList<>();
    ArrayList<String>artist_name=new ArrayList<>();
    ArrayList<String>image=new ArrayList<>();
    ArrayList<String>audio=new ArrayList<>();
    ArrayList<String>audio_downloaded=new ArrayList<>();

    public SearchedSongsModel   (ArrayList<String> track_name, ArrayList<String> artist_name, ArrayList<String> image, ArrayList<String> audio, ArrayList<String> audio_downloaded) {
        this.track_name = track_name;
        this.artist_name = artist_name;
        this.image = image;
        this.audio = audio;
        this.audio_downloaded = audio_downloaded;
    }

    public ArrayList<String> getTrack_name() {
        return track_name;
    }

    public void setTrack_name(ArrayList<String> track_name) {
        this.track_name = track_name;
    }

    public ArrayList<String> getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(ArrayList<String> artist_name) {
        this.artist_name = artist_name;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    public ArrayList<String> getAudio() {
        return audio;
    }

    public void setAudio(ArrayList<String> audio) {
        this.audio = audio;
    }

    public ArrayList<String> getAudio_downloaded() {
        return audio_downloaded;
    }

    public void setAudio_downloaded(ArrayList<String> audio_downloaded) {
        this.audio_downloaded = audio_downloaded;
    }
}
