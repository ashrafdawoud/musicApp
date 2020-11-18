package com.indrul.musicapp.Activities.ViewModel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.indrul.musicapp.Activities.Model.RecommendedSongsModel;
import com.indrul.musicapp.Activities.Utils.SongsJsonUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SongsViewModel extends ViewModel {
    OkHttpClient client;
    RecommendedSongsModel recommendedSongsModel;
    Context context;
    public MutableLiveData<RecommendedSongsModel>songsmutableLiveData=new MutableLiveData<>();


    public void getRecomendedSongs(){
       // songsmutableLiveData.setValue(connectAPI());
       // Log.e("asasas",connectAPI().getArtist_name().get(0));
        connectAPI();


    }
    private RecommendedSongsModel connectAPI(){
        client=new OkHttpClient.Builder ().callTimeout (10, TimeUnit.SECONDS).build ();
        String url="https://api.jamendo.com/v3.0/artists/tracks/?client_id=0a1179b6&format=jsonpretty&order=track_name_desc&album_datebetween=2020-01-01_2020-08-01\n" +
                "&fullcount=true&offset=20&limit=50";
        final Request request=new Request .Builder ().url (url).build ();
        client.newCall (request).enqueue (new Callback( ) {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.i("err","fails in details connection");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful ( )) {
                    String respons = response.body ( ).string ( );
                    recommendedSongsModel= SongsJsonUtils.songsParseing(respons);
                    setvalue();


                }
            } });
        return recommendedSongsModel;
    }
    void  setvalue(){
        Log.e("plaplapla",recommendedSongsModel.toString());
        songsmutableLiveData.postValue(recommendedSongsModel);
    }

}
