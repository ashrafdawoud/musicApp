package com.indrul.musicapp.Activities.ViewModel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.indrul.musicapp.Activities.Model.RecommendedSongsModel;
import com.indrul.musicapp.Activities.Model.SearchedSongsModel;
import com.indrul.musicapp.Activities.Utils.SearchUtils;
import com.indrul.musicapp.Activities.Utils.SongsJsonUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchViewModel extends ViewModel {
    OkHttpClient client;
    SearchedSongsModel searchedSongsModel;
    Context context;
    public MutableLiveData<SearchedSongsModel> searchedSongsModelMutableLiveData=new MutableLiveData<>();


    public void getRecomendedSongs(String text){
        // songsmutableLiveData.setValue(connectAPI());
        // Log.e("asasas",connectAPI().getArtist_name().get(0));
        connectAPI(text);


    }
    private SearchedSongsModel connectAPI(String text){
        client=new OkHttpClient.Builder ().callTimeout (10, TimeUnit.SECONDS).build ();
        String url="https://api.jamendo.com/v3.0/artists/tracks/?format=jsonpretty&track_name="+text+"&limit=10&client_id=0a1179b6&order=track_name_desc";
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
                    searchedSongsModel= SearchUtils.songsParseing(respons);
                    setvalue();


                }
            } });
        return searchedSongsModel;
    }
    void  setvalue(){
        Log.e("plaplapla",searchedSongsModel.toString());
        searchedSongsModelMutableLiveData.postValue(searchedSongsModel);
    }
}
