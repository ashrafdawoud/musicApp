package com.indrul.musicapp.Activities.Utils;

import android.util.Log;

import com.indrul.musicapp.Activities.Model.AlbumDetailsModel;
import com.indrul.musicapp.Activities.Model.SearchedSongsModel;
import com.indrul.musicapp.Activities.ViewModel.AlbumsViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AlbumDetailsUtils {
    private final static String Result_JSON_KEY = "results";
    private final static String Tracks_JSON_KEY = "tracks";
    private final static String NameArtist_JSON_KEY = "name";
    private final static String NameTrack_JSON_KEY = "name";
    private final static String Image_JSON_KEY = "image";
    private final static String Audio_JSON_KEY = "audio";
    private final static String AudioDownladed_JSON_KEY = "audiodownload";
    public static AlbumDetailsModel  songsParseing(String json,String image ,String name){
        AlbumDetailsModel albumDetailsModel = null;
        ArrayList<String> NameArtist=new ArrayList<>();
        ArrayList<String>NameTrack=new ArrayList<>();
        ArrayList<String>Image=new ArrayList<>();
        ArrayList<String>Audio=new ArrayList<>();
        ArrayList<String>udioDownladed=new ArrayList<>();
        Log.e("respons2",json);
        if (json != null && !json.isEmpty ( )) {
            JSONArray tracksArray= null;
            try {
                     tracksArray = new JSONArray(json);
                    for (int i2=0;i2<tracksArray.length();i2++){
                        NameTrack.add(tracksArray.optJSONObject(i2).optString(NameTrack_JSON_KEY));
                        Image.add(image);
                        NameArtist.add(name);
                        Audio.add(tracksArray.optJSONObject(i2).optString(Audio_JSON_KEY));
                        udioDownladed.add(tracksArray.optJSONObject(i2).optString(AudioDownladed_JSON_KEY));
                        Log.e("NameArtist",NameTrack.get(i2));
                        Log.e("NameAlbum",tracksArray.optJSONObject(i2).optString(Image_JSON_KEY));
                        Log.e("Image",tracksArray.optJSONObject(i2).optString(Audio_JSON_KEY));
                        Log.e("Tracks",String.valueOf(tracksArray.optJSONObject(i2).optString(AudioDownladed_JSON_KEY)));
                    }
                albumDetailsModel=new AlbumDetailsModel(NameTrack,NameArtist,Image,Audio,udioDownladed);
                Log.e("plaplapl2a",albumDetailsModel.getArtist_name().size()+"");

            } catch (JSONException e) {
                e.printStackTrace();

            }
        }
        return albumDetailsModel;
    }
}
