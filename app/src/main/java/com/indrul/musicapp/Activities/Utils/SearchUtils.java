package com.indrul.musicapp.Activities.Utils;

import android.util.Log;

import com.indrul.musicapp.Activities.Model.RecommendedSongsModel;
import com.indrul.musicapp.Activities.Model.SearchedSongsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchUtils {
    private final static String Result_JSON_KEY = "results";
    private final static String Tracks_JSON_KEY = "tracks";
    private final static String NameArtist_JSON_KEY = "name";
    private final static String NameTrack_JSON_KEY = "name";
    private final static String Image_JSON_KEY = "image";
    private final static String Audio_JSON_KEY = "audio";
    private final static String AudioDownladed_JSON_KEY = "audiodownload";

    public static SearchedSongsModel songsParseing(String json){
        SearchedSongsModel searchedSongsModel=null;
        ArrayList<String> NameArtist=new ArrayList<>();
        ArrayList<String>NameTrack=new ArrayList<>();
        ArrayList<String>Image=new ArrayList<>();
        ArrayList<String>Audio=new ArrayList<>();
        ArrayList<String>udioDownladed=new ArrayList<>();
        Log.e("respons2",json);
        if (json != null && !json.isEmpty ( )) {
            JSONObject sandwichJSON = null;
            try {
                sandwichJSON = new JSONObject (json);
                JSONArray resultsArray=sandwichJSON.getJSONArray(Result_JSON_KEY);
                for (int i=0;i<resultsArray.length();i++) {
                    JSONObject AlbumsJason = resultsArray.optJSONObject(i);
                    NameArtist.add(AlbumsJason.optString(NameArtist_JSON_KEY));
                    Log.e("resresrs2", AlbumsJason.optString("name"));
                    JSONArray tracksArray = AlbumsJason.getJSONArray(Tracks_JSON_KEY);
                    for (int i2=0;i2<tracksArray.length();i2++){
                        NameTrack.add(tracksArray.optJSONObject(i2).optString(NameTrack_JSON_KEY));
                        Image.add(tracksArray.optJSONObject(i2).optString(Image_JSON_KEY));
                        Audio.add(tracksArray.optJSONObject(i2).optString(Audio_JSON_KEY));
                        udioDownladed.add(tracksArray.optJSONObject(i2).optString(AudioDownladed_JSON_KEY));
                        Log.e("resresres", tracksArray.optJSONObject(i2).optString(NameTrack_JSON_KEY));
                    }

                }
                searchedSongsModel=new SearchedSongsModel(NameTrack,NameArtist,Image,Audio,udioDownladed);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return searchedSongsModel;
    }
}
