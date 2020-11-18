package com.indrul.musicapp.Activities.ViewModel;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.indrul.musicapp.Activities.Model.AlbumDetailsModel;
import com.indrul.musicapp.Activities.Model.SearchedSongsModel;
import com.indrul.musicapp.Activities.Utils.AlbumDetailsUtils;
import com.indrul.musicapp.Activities.Utils.AlbumsUtils;
import com.indrul.musicapp.Activities.Utils.SearchUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AlbumDetailsViewModel extends ViewModel {
    OkHttpClient client;
    AlbumDetailsModel searchedSongsModel;
    Context context;
    public MutableLiveData<AlbumDetailsModel> AlbumdetailsModelMutableLiveData=new MutableLiveData<>();


    public void getRecomendedSongs(String text ,String image,String name){
        // songsmutableLiveData.setValue(connectAPI());
        // Log.e("asasas",connectAPI().getArtist_name().get(0));
        connectAPI(text,image ,name);


    }
    private AlbumDetailsModel connectAPI(String text,String image , String name){
        searchedSongsModel= AlbumDetailsUtils.songsParseing(text,image, name);
        Log.e("plaplapl2a",searchedSongsModel.toString());

        setvalue();

        return searchedSongsModel;
    }
    void  setvalue(){
        AlbumdetailsModelMutableLiveData.postValue(searchedSongsModel);
    }
}
