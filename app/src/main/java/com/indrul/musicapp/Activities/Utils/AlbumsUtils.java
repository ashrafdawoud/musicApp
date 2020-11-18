package com.indrul.musicapp.Activities.Utils;

import android.util.Log;

import com.indrul.musicapp.Activities.Model.RecommendedAlbumsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AlbumsUtils {
    private final static String Result_JSON_KEY = "results";
    private final static String Tracks_JSON_KEY = "tracks";
    private final static String Album_JSON_KEY = "name";
    private final static String Artist_JSON_KEY = "artist_name";
    private final static String Image_JSON_KEY = "image";

    public static RecommendedAlbumsModel albumsParseing(String json) {
        RecommendedAlbumsModel recommendedAlbumsModel = null;
        ArrayList<String> NameArtist = new ArrayList<>();
        ArrayList<String> NameAlbum = new ArrayList<>();
        ArrayList<String> Image = new ArrayList<>();
        ArrayList<String> Tracks = new ArrayList<>();
        if (json != null && !json.isEmpty()) {
            JSONObject sandwichJSON = null;
            try {
                sandwichJSON = new JSONObject(json);
                JSONArray resultsArray = sandwichJSON.getJSONArray(Result_JSON_KEY);
                for (int i = 0; i < resultsArray.length(); i++) {
                        JSONObject AlbumsJason = resultsArray.optJSONObject(i);
                        NameArtist.add(AlbumsJason.optString(Artist_JSON_KEY));
                        NameAlbum.add(AlbumsJason.optString(Album_JSON_KEY));
                        Image.add(AlbumsJason.optString(Image_JSON_KEY));
                        Tracks.add(String.valueOf(AlbumsJason.getJSONArray(Tracks_JSON_KEY)));

                }
                recommendedAlbumsModel=new RecommendedAlbumsModel(Image,NameAlbum,NameArtist,Tracks);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
            return recommendedAlbumsModel;
        }

}
