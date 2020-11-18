package com.indrul.musicapp.Activities.Model;

import java.util.ArrayList;

public class RecommendedAlbumsModel {
    ArrayList<String> image=new ArrayList<>();
    ArrayList<String> name=new ArrayList<>();
    ArrayList<String> singerName=new ArrayList<>();
    ArrayList<String> Tracks=new ArrayList<>();


    public RecommendedAlbumsModel(ArrayList<String> image, ArrayList<String> name, ArrayList<String> singerName, ArrayList<String> Tracks) {
        this.image = image;
        this.name = name;
        this.singerName = singerName;
        this.Tracks=Tracks;
    }

    public ArrayList<String> getTracks() {
        return Tracks;
    }

    public void setTracks(ArrayList<String> tracks) {
        Tracks = tracks;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    public ArrayList<String> getName() {
        return name;
    }

    public void setName(ArrayList<String> name) {
        this.name = name;
    }

    public ArrayList<String> getSingerName() {
        return singerName;
    }

    public void setSingerName(ArrayList<String> singerName) {
        this.singerName = singerName;
    }
}
