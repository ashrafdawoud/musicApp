package com.indrul.musicapp.Activities.ViewModel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.indrul.musicapp.Activities.Model.RecommendedAlbumsModel;
import com.indrul.musicapp.Activities.Model.RecommendedSongsModel;
import com.indrul.musicapp.Activities.Utils.AlbumsUtils;
import com.indrul.musicapp.Activities.Utils.SongsJsonUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AlbumsViewModel extends ViewModel {
    OkHttpClient client;
    RecommendedAlbumsModel recommendedAlbumsModel;
    public MutableLiveData<RecommendedAlbumsModel> albummutableLiveData=new MutableLiveData<>();

    public void getRecomendedAlbums(){
        // songsmutableLiveData.setValue(connectAPI());
        // Log.e("asasas",connectAPI().getArtist_name().get(0));
        ConnectApi();


    }
    private RecommendedAlbumsModel ConnectApi(){
        client=new OkHttpClient.Builder ().callTimeout (10, TimeUnit.SECONDS).build ();
        String url="https://api.jamendo.com/v3.0/albums/tracks/?format=jsonpretty&datebetween=2020-01-01_2020-08-01&client_id=0a1179b6&limit=50";
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
                    recommendedAlbumsModel= AlbumsUtils.albumsParseing(respons);
                    setvalue();


                }
            } });
        return recommendedAlbumsModel;
    }
    void  setvalue(){
        Log.e("plplplplp",recommendedAlbumsModel.toString());
        albummutableLiveData.postValue(recommendedAlbumsModel);
    }
}
