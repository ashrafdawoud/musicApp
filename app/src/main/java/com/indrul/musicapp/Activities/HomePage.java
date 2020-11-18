package com.indrul.musicapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.indrul.musicapp.Activities.Adapters.AlbumDetailsAdapter;
import com.indrul.musicapp.Activities.Adapters.CommonAlbums;
import com.indrul.musicapp.Activities.Adapters.CommonSongs;
import com.indrul.musicapp.Activities.Adapters.DownloadsAdapter;
import com.indrul.musicapp.Activities.Adapters.LovedAdapter;
import com.indrul.musicapp.Activities.Adapters.SearchAdapter;
import com.indrul.musicapp.Activities.Classes.Constants;
import com.indrul.musicapp.Activities.Classes.DBHelper;
import com.indrul.musicapp.Activities.Classes.InternetConnection;
import com.indrul.musicapp.Activities.Model.AlbumDetailsModel;
import com.indrul.musicapp.Activities.Model.RecommendedAlbumsModel;
import com.indrul.musicapp.Activities.Model.RecommendedSongsModel;
import com.indrul.musicapp.Activities.Model.SearchedSongsModel;
import com.indrul.musicapp.Activities.Services.NotificationService;
import com.indrul.musicapp.Activities.ViewModel.AlbumDetailsViewModel;
import com.indrul.musicapp.Activities.ViewModel.AlbumsViewModel;
import com.indrul.musicapp.Activities.ViewModel.SearchViewModel;
import com.indrul.musicapp.Activities.ViewModel.SongsViewModel;
import com.indrul.musicapp.BuildConfig;
import com.indrul.musicapp.R;
import com.skydoves.elasticviews.ElasticButton;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener , NotificationService.Callbacks , CommonSongs.CommonsongsInterface, SearchAdapter.SearchedsongsInterface , SearchAdapter.Download , CommonAlbums.Showalbum , DownloadsAdapter.DownloadedsongsInterface {
    private BottomSheetBehavior mBottomSheetBehavior;
    private TextView mTextViewState;
    CommonSongs commonSongs;
    AlbumDetailsAdapter albumDetailsAdapter;
    String filename,fileartist;
    SearchAdapter searchAdapter;
    CommonAlbums commonAlbums;
    RelativeLayout collapsed,notcollapsed;
    private ImageView forwardbtn, backwardbtn, pausebtn, playbtn,forwardbtn1, backwardbtn1, pausebtn1, playbtn1;
    private MediaPlayer mPlayer;
    private TextView songName, startTime, songTime;
    private SeekBar songPrgs;
    private static int oTime =0, sTime =0, eTime =0, fTime = 5000, bTime = 5000;
    private Handler hdlr = new Handler();
    NotificationService myService;
    SongsViewModel songsViewModel;
    SearchViewModel searchViewModel;
    AlbumsViewModel albumsViewModel;
    LinearLayout regularcontainer,serachcontainer,downloadcontainer,favouritcontainer;
    View bottomSheet;
    View noInternetView;
    AlertDialog noInternetDialog;
    LinearLayout allviewcontainer,albumdetailscontainer;
    private ProgressDialog pDialog;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    String imageg,trackname;
    public final static int progress_bar_type = 0;
    Intent serviceIntent;
    ServiceConnection mconnection;
    EditText search;
    TextView albumname;
    AlbumDetailsViewModel albumDetailsViewModel;
    ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> songsListResult = new ArrayList<HashMap<String, String>>();
    SwipeRefreshLayout swipeRefreshLayout;
    DBHelper mydb;
    ImageView lovedimage;
    AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mydb = new DBHelper(this);
        if (mydb.getAllCotacts().size()!=0)
        Log.e("allfavorite", String.valueOf(mydb.getAllCotacts().size()));
        regularcontainer=findViewById(R.id.regularContainer);
        serachcontainer=findViewById(R.id.searchContainer);
        allviewcontainer=findViewById(R.id.allviewContainer);
        albumdetailscontainer=findViewById(R.id.albumdetailContainer);
        downloadcontainer=findViewById(R.id.DowloadedContainer);
        swipeRefreshLayout=findViewById(R.id.SwipeRefreshLayout);
        favouritcontainer=findViewById(R.id.LovedContainer);
        Toolbar toolbar = findViewById(R.id.toolbar ) ;
        lovedimage=findViewById(R.id.love);
        setSupportActionBar(toolbar) ;
        search =findViewById(R.id.search);
        DrawerLayout drawer = findViewById(R.id. drawer_layout ) ;
        drawer.closeDrawer(GravityCompat.START );
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer , toolbar , R.string.navigation_drawer_open ,
                R.string.navigation_drawer_close ) ;
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.phocia));
        drawer.addDrawerListener(toggle) ;
        toggle.syncState() ;
        NavigationView navigationView = findViewById(R.id. nav_view ) ;
        navigationView.setNavigationItemSelectedListener( this ) ;
        //////////////////////////////////////////////////////////////////////////////////////
        songsViewModel= ViewModelProviders.of(this).get(SongsViewModel.class);
        albumsViewModel=ViewModelProviders.of(this).get(AlbumsViewModel.class);
        searchViewModel=ViewModelProviders.of(this).get(SearchViewModel.class);
        albumDetailsViewModel=ViewModelProviders.of(this).get(AlbumDetailsViewModel.class);
        albumsViewModel.getRecomendedAlbums();
        songsViewModel.getRecomendedSongs();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                allviewcontainer.setVisibility(View.VISIBLE);
                regularcontainer.setVisibility(View.VISIBLE);
                allviewcontainer.setVisibility(View.VISIBLE);
                albumdetailscontainer.setVisibility(View.GONE);
                downloadcontainer.setVisibility(View.GONE);
                favouritcontainer.setVisibility(View.GONE);
                albumsViewModel.getRecomendedAlbums();
                songsViewModel.getRecomendedSongs();
                search.setText("");
                search.clearFocus();
                swipeRefreshLayout.setEnabled(true);


            }

        });

        /////////////////////////////////////////////////////////////////////////////////////
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.music);
        mBuilder.setContentTitle("Notification Alert, Click Me!");
        mBuilder.setContentText("Hi, This is Android Notification Detail!");
        backwardbtn = findViewById(R.id.btnBackward);
        forwardbtn = findViewById(R.id.btnForward);
        playbtn = findViewById(R.id.btnPlay);
        pausebtn = findViewById(R.id.btnPause);
        backwardbtn1 = findViewById(R.id.btnBackward1);
        forwardbtn1 = findViewById(R.id.btnForward1);
        playbtn1 = findViewById(R.id.btnPlay1);
        pausebtn1 = findViewById(R.id.btnPause1);
        // songName = (TextView)findViewById(R.id.txtSname);
        startTime = (TextView)findViewById(R.id.txtStartTime);
        songTime = (TextView)findViewById(R.id.txtSongTime);
        //songName.setText("Baitikochi Chuste");
        songPrgs = (SeekBar)findViewById(R.id.sBar);
        songPrgs.setClickable(true);
        pausebtn.setEnabled(false);
        // String url = "https://audio.jukehost.co.uk/vx5w0FpbVtqB3Q9Rvlryj5l9KR9GooqN"; // your URL here
        bottomSheet = findViewById(R.id.bottom_sheet);
        collapsed=findViewById(R.id.collapsed);
        notcollapsed=findViewById(R.id.notcollapsed);
        mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        collapsed.setVisibility(View.VISIBLE);
                        notcollapsed.setVisibility(View.GONE);
                        break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        break;
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:

                        break;
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                collapsed.setVisibility(View.GONE);
                notcollapsed.setVisibility(View.VISIBLE);
            }
        });
        //////////////////Recyclerview////////////////////
        RecyclerView Songs=findViewById (R.id.recommendedRecy);
        Songs.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        songsViewModel.songsmutableLiveData.observe(this, new Observer<RecommendedSongsModel>() {
            @Override
            public void onChanged(RecommendedSongsModel recommendedSongsModel) {
                if (recommendedSongsModel!=null)
                commonSongs=new CommonSongs(recommendedSongsModel.getTrack_name(),recommendedSongsModel.getArtist_name(),recommendedSongsModel.getImage(),recommendedSongsModel.getAudio(),recommendedSongsModel.getAudio_downloaded(),HomePage.this);
                Songs.setAdapter (commonSongs);
                commonSongs.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        Songs.setHasFixedSize (true);
        RecyclerView Albums=findViewById (R.id.AlbumRecy);
        Albums.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        albumsViewModel.albummutableLiveData.observe(this, new Observer<RecommendedAlbumsModel>() {
            @Override
            public void onChanged(RecommendedAlbumsModel recommendedAlbumsModel) {
                commonAlbums =new CommonAlbums (recommendedAlbumsModel.getImage(),recommendedAlbumsModel.getName(),recommendedAlbumsModel.getSingerName(),recommendedAlbumsModel.getTracks(),HomePage.this::callbackShowAlbum);
                Albums.setAdapter (commonAlbums);
                commonAlbums.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        Albums.setHasFixedSize (true);
        RecyclerView search=findViewById(R.id.searchRecy);
        search.setLayoutManager(new LinearLayoutManager(this));
        searchViewModel.searchedSongsModelMutableLiveData.observe(this, new Observer<SearchedSongsModel>() {
            @Override
            public void onChanged(SearchedSongsModel searchedSongsModel) {
                searchAdapter=new SearchAdapter(searchedSongsModel.getTrack_name(),searchedSongsModel.getArtist_name(),searchedSongsModel.getImage(),searchedSongsModel.getAudio(),searchedSongsModel.getAudio_downloaded(),HomePage.this::Callback,HomePage.this::Callback3);
                search.setAdapter(searchAdapter);
                searchAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        search.setHasFixedSize(true);
        RecyclerView DetailsAlbum=findViewById (R.id.albumdetailsRecy);
        DetailsAlbum.setLayoutManager(new LinearLayoutManager(this));
        albumDetailsViewModel.AlbumdetailsModelMutableLiveData.observe(this, new Observer<AlbumDetailsModel>() {
            @Override
            public void onChanged(AlbumDetailsModel albumDetailsModel) {
                Log.e("lalalala",albumDetailsModel.getTrack_name().get(0)+" ");
                albumDetailsAdapter=new AlbumDetailsAdapter(albumDetailsModel.getTrack_name(),albumDetailsModel.getArtist_name(),albumDetailsModel.getImage(),albumDetailsModel.getAudio(),albumDetailsModel.getAudio_downloaded(),HomePage.this::Callback,HomePage.this::Callback3);
                DetailsAlbum.setAdapter (albumDetailsAdapter);
                albumDetailsAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        DetailsAlbum.setHasFixedSize (true);
        /////////////////////
        playbtn.setOnClickListener(this);
        forwardbtn.setOnClickListener(this);
        backwardbtn.setOnClickListener(this);
        pausebtn.setOnClickListener(this);
        playbtn1.setOnClickListener(this);
        forwardbtn1.setOnClickListener(this);
        backwardbtn1.setOnClickListener(this);
        pausebtn1.setOnClickListener(this);
        SearchFunction();
        /////////////////////////////////////
         mconnection=new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                // Toast.makeText(HomePage.this, "onServiceConnected called", Toast.LENGTH_SHORT).show();
                // We've binded to LocalService, cast the IBinder and get LocalService instance
                NotificationService.LocalBinder binder = (NotificationService.LocalBinder) iBinder;
                myService = binder.getServiceInstance(); //Get instance of your service!
                myService.registerClient(HomePage.this);
                // Log.e("onServiceConnected","onServiceConnected");
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        /////////////
         serviceIntent = new Intent(HomePage.this, NotificationService.class);
        //this.stopService(serviceIntent);
        serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        //////////////
        //startService(serviceIntent);
       // bindService(serviceIntent, mconnection,Context.BIND_AUTO_CREATE); //Binding to the service*/
        /////////////////////////////////////
        songsListResult=getPlayList();

    }
    private Runnable UpdateSongTime = new Runnable() {
        @Override
        public void run() {
            if (mPlayer!=null);
            sTime = mPlayer.getCurrentPosition();
            startTime.setText(String.format("%d : %d ", TimeUnit.MILLISECONDS.toMinutes(sTime),
                    TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(sTime))) );
            songPrgs.setProgress(sTime);
            hdlr.postDelayed(this, 100);
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnBackward:
            case R.id.btnBackward1:
                if((sTime - bTime) > 0)
                {
                    sTime = sTime - bTime;
                    mPlayer.seekTo(sTime);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Cannot jump backward 5 seconds", Toast.LENGTH_SHORT).show();
                }
                if(!playbtn.isEnabled()){
                    playbtn.setEnabled(true);
                }
                break;
            case R.id.btnForward:
            case R.id.btnForward1:
                if((sTime + fTime) <= eTime)
                {
                    sTime = sTime + fTime;
                    Log.e("stimestime",sTime+"");
                    mPlayer.seekTo(sTime);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Cannot jump forward 5 seconds", Toast.LENGTH_SHORT).show();
                }
                if(!playbtn.isEnabled()){
                    playbtn.setEnabled(true);
                }
                break;
            case R.id.btnPlay:
            case R.id.btnPlay1:
                ///////////////
                Log.e("123456","123456");
               play(imageg,trackname);


                break;
            case R.id.btnPause:
            case R.id.btnPause1:
                pausebtn.setVisibility(View.GONE);
                playbtn.setVisibility(View.VISIBLE);
                pausebtn1.setVisibility(View.GONE);
                playbtn1.setVisibility(View.VISIBLE);
                mPlayer.pause();
                pausebtn.setEnabled(false);
                playbtn.setEnabled(true);
                serviceIntent.putExtra("play","pause");
                startService(serviceIntent);
                bindService(serviceIntent, mconnection,Context.BIND_AUTO_CREATE); //Binding to the service*/
                break;

        }
    }


    @Override
    public void updateClient(int data) {
        Log.e("servescr","from");
        pausebtn.setVisibility(View.GONE);
        playbtn.setVisibility(View.VISIBLE);
        pausebtn1.setVisibility(View.GONE);
        playbtn1.setVisibility(View.VISIBLE);
        playbtn.setEnabled(true);
        playbtn1.setEnabled(true);
        mPlayer.pause();
    }

    @Override
    protected void onPause() {
        super.onPause();
       /* ServiceConnection mconnection=new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                // Toast.makeText(HomePage.this, "onServiceConnected called", Toast.LENGTH_SHORT).show();
                // We've binded to LocalService, cast the IBinder and get LocalService instance
                NotificationService.LocalBinder binder = (NotificationService.LocalBinder) iBinder;
                myService = binder.getServiceInstance(); //Get instance of your service!
                myService.registerClient(HomePage.this);
                // Log.e("onServiceConnected","onServiceConnected");
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };
        /////////////
        Intent serviceIntent = new Intent(HomePage.this, NotificationService.class);
        serviceIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        //////////////
        startService(serviceIntent);
        bindService(serviceIntent, mconnection,Context.BIND_AUTO_CREATE); //Binding to the service!*/
    }

    @Override
    protected void onDestroy() {
        oTime=0;
        serviceIntent.putExtra("play","pause");
        startService(serviceIntent);
        bindService(serviceIntent, mconnection,Context.BIND_AUTO_CREATE); //Binding to the service*/
        mPlayer.stop();
        super.onDestroy();

        Log.e("distroid","distroid");
    }
    String track_name1; String artist_name1; String image1; String audio1; String audio_downloaded1;
    @Override
    public void Callback(String track_name, String artist_name, String image, String audio, String audio_downloaded) {
        ImageView imageView=findViewById(R.id.love);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
        track_name1=track_name;artist_name1=artist_name;image1=image;audio1=audio;audio_downloaded1=audio_downloaded;
        filename=track_name;
        fileartist=artist_name;
        Log.e("respons15",track_name);
        String url=audio;
        if (mPlayer!=null&&mPlayer.isPlaying()){
            mPlayer.stop();
        }
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mPlayer.prepare(); // might take long! (for buffering, etc)
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (mPlayer.isPlaying()){
            mPlayer.stop();
        }
        ImageView profileimage=findViewById(R.id.profile_image);
        ImageView background=findViewById(R.id.backround);
        ImageView background2=findViewById(R.id.backround2);
        ImageView download=findViewById(R.id.dwonload);
        download.setVisibility(View.VISIBLE);
        TextView songe=findViewById(R.id.songName);
        TextView artist=findViewById(R.id.artist);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyStoragePermissions(HomePage.this);
                new DownloadFileFromURL().execute(audio,track_name,artist_name);

            }
        });
        Log.e("image789",image+" 1");
        Picasso.get().load(image).into(profileimage);
        Picasso.get().load(image).into(background);
        Picasso.get().load(image).into(background2);
        songe.setText(filename);
        artist.setText(fileartist);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        imageg=image;
        play(image,track_name);

    }
    void play(String image,String trackname){
        Log.e("courseorororo", String.valueOf(mydb.getData(trackname).getCount()));
        if (mydb.getData(trackname).getCount()!=0){
            lovedimage.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
        }else {
            lovedimage.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
        }
        bottomSheet.setVisibility(View.VISIBLE);
        serviceIntent.putExtra("play","play");
        if (image!=null&&!image.equals(""))
            serviceIntent.putExtra("image",image);
        serviceIntent.putExtra("song",trackname);

        startService(serviceIntent);
        bindService(serviceIntent, mconnection,Context.BIND_AUTO_CREATE); //Binding to the service*/
        pausebtn.setVisibility(View.VISIBLE);
        playbtn.setVisibility(View.GONE);
        pausebtn1.setVisibility(View.VISIBLE);
        playbtn1.setVisibility(View.GONE);
        mPlayer.start();
        eTime = mPlayer.getDuration();
        sTime = mPlayer.getCurrentPosition();
        if(oTime == 0){
            songPrgs.setMax(eTime);
            oTime =1;
        }
        songTime.setText(String.format("%d : %d ", TimeUnit.MILLISECONDS.toMinutes(eTime),
                TimeUnit.MILLISECONDS.toSeconds(eTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(eTime))) );
        startTime.setText(String.format("%d : %d ", TimeUnit.MILLISECONDS.toMinutes(sTime),
                TimeUnit.MILLISECONDS.toSeconds(sTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS. toMinutes(sTime))) );
        songPrgs.setProgress(sTime);
        hdlr.postDelayed(UpdateSongTime, 100);
        pausebtn.setEnabled(true);
        playbtn.setEnabled(false);

    }

    @Override
    public void Callback2(String track_name, String artist_name, String image, String audio, String audio_downloaded) {
        track_name1=track_name;artist_name1=artist_name;image1=image;audio1=audio;audio_downloaded1=audio_downloaded;
        ImageView imageView=findViewById(R.id.love);
        imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
        filename=track_name;
        fileartist=artist_name;
        Log.e("respons15",track_name);
        String url=audio;
        if (mPlayer!=null&&mPlayer.isPlaying()){
            mPlayer.stop();
        }
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mPlayer.prepare(); // might take long! (for buffering, etc)
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (mPlayer.isPlaying()){
            mPlayer.stop();
        }
        ImageView profileimage=findViewById(R.id.profile_image);
        ImageView background=findViewById(R.id.backround);
        ImageView background2=findViewById(R.id.backround2);
        ImageView download=findViewById(R.id.dwonload);
        download.setVisibility(View.VISIBLE);
        TextView songe=findViewById(R.id.songName);
        TextView artist=findViewById(R.id.artist);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyStoragePermissions(HomePage.this);
                new DownloadFileFromURL().execute(audio,track_name,artist_name);

            }
        });
        Picasso.get().load(image).into(profileimage);
        Picasso.get().load(image).into(background);
        Picasso.get().load(image).into(background2);
        songe.setText(filename);
        artist.setText(fileartist);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        play(image,track_name);
    }

    @Override
    public void Callback3(String audio_downloaded) {
        verifyStoragePermissions(HomePage.this);
        new DownloadFileFromURL().execute(audio_downloaded);
    }

    public void back(View view) {
        if (mBottomSheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED){
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);}
        allviewcontainer.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setEnabled(true);
        regularcontainer.setVisibility(View.VISIBLE);
        allviewcontainer.setVisibility(View.VISIBLE);
        albumdetailscontainer.setVisibility(View.GONE);
        favouritcontainer.setVisibility(View.GONE);
        downloadcontainer.setVisibility(View.GONE);

    }

    @Override
    public void callbackShowAlbum(String tracks,String name,String imageg) {
        allviewcontainer.setVisibility(View.GONE);
        albumdetailscontainer.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setEnabled(false);
        TextView textView=findViewById(R.id.albumname);
        textView.setText(name);
        albumDetailsViewModel.getRecomendedSongs(tracks ,imageg,name);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Log.e("aa12356","123");
        int id = menuItem.getItemId() ;
        if (id == R.id.privacy ) {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://s3-ap-southeast-1.amazonaws.com/privacy.msy.os2/privacyOS1-3.html"));
            startActivity(browserIntent);
        }else if (id==R.id.share){
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String shareMessage= "\nLet me recommend you this application\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch(Exception e) {
                //e.toString();
            }
        }else if (id==R.id.downloads){
            ///////////////////////////////////////////////////
            if (mBottomSheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED){
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);}
            regularcontainer.setVisibility(View.GONE);
            serachcontainer.setVisibility(View.GONE);
            allviewcontainer.setVisibility(View.GONE);
            albumdetailscontainer.setVisibility(View.GONE);
            downloadcontainer.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setEnabled(false);
            RecyclerView downloadsRecy=findViewById(R.id.downloadsRecy);
            downloadsRecy.setLayoutManager(new LinearLayoutManager(this));
            DownloadsAdapter downloadsAdapter=new DownloadsAdapter(getPlayList(),HomePage.this);
            downloadsRecy.setAdapter(downloadsAdapter);
            downloadsAdapter.notifyDataSetChanged();
            downloadsRecy.setHasFixedSize(true);
        }
        DrawerLayout drawer = findViewById(R.id. drawer_layout ) ;
        drawer.closeDrawer(GravityCompat.START );
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.e("aa12356","123");

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void Callback5(String track_name, String audio) {
        filename=track_name;
        fileartist="";
        Log.e("respons15",track_name);
        String url=audio;
        if (mPlayer!=null&&mPlayer.isPlaying()){
            mPlayer.stop();
        }
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mPlayer.setDataSource(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mPlayer.prepare(); // might take long! (for buffering, etc)
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (mPlayer.isPlaying()){
            mPlayer.stop();
        }
        ImageView profileimage=findViewById(R.id.profile_image);
        ImageView background=findViewById(R.id.backround);
        ImageView background2=findViewById(R.id.backround2);
        ImageView download=findViewById(R.id.dwonload);
        download.setVisibility(View.GONE);
        TextView songe=findViewById(R.id.songName);
        TextView artist=findViewById(R.id.artist);
        Picasso.get().load(R.drawable.music1).into(profileimage);
        Picasso.get().load(R.drawable.music1).into(background);
        Picasso.get().load(R.drawable.music1).into(background2);
        songe.setText(filename);
        artist.setText(fileartist);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        play("",track_name);
    }

    public void love(View view) {
        if (mydb.getData(track_name1).getCount()==0) {
            mydb.insertContact(track_name1, artist_name1, image1, audio1, audio_downloaded1);
            lovedimage.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_black_24dp));
        }else {
            lovedimage.setImageDrawable(getResources().getDrawable(R.drawable.ic_favorite_border_black_24dp));
            mydb.deleteContact(track_name1);

        }
        ArrayList<ArrayList<String>> containerArray=new ArrayList<>();
        containerArray=mydb.getAllCotacts();
        RecyclerView lovedrecy=findViewById(R.id.LovedRecy);
        lovedrecy.setLayoutManager(new LinearLayoutManager(this));
        if (containerArray.size()!=0) {
            LovedAdapter searchAdapter = new LovedAdapter(containerArray.get(0), containerArray.get(1), containerArray.get(2), containerArray.get(3), containerArray.get(4), HomePage.this::Callback, HomePage.this::Callback3);
            lovedrecy.setAdapter(searchAdapter);
            searchAdapter.notifyDataSetChanged();
        }else {
            LovedAdapter searchAdapter = new LovedAdapter();
            lovedrecy.setAdapter(searchAdapter);
            searchAdapter.notifyDataSetChanged();
        }
        swipeRefreshLayout.setRefreshing(false);
        lovedrecy.setHasFixedSize(true);
    }

    public void favourite(View view) {
        ArrayList<ArrayList<String>> containerArray=new ArrayList<>();
        containerArray=mydb.getAllCotacts();
        RecyclerView lovedrecy=findViewById(R.id.LovedRecy);
        lovedrecy.setLayoutManager(new LinearLayoutManager(this));
        if (containerArray.size()!=0) {
            LovedAdapter searchAdapter = new LovedAdapter(containerArray.get(0), containerArray.get(1), containerArray.get(2), containerArray.get(3), containerArray.get(4), HomePage.this::Callback, HomePage.this::Callback3);
            lovedrecy.setAdapter(searchAdapter);
            searchAdapter.notifyDataSetChanged();
        }
        swipeRefreshLayout.setRefreshing(false);
        lovedrecy.setHasFixedSize(true);
        regularcontainer.setVisibility(View.GONE);
        allviewcontainer.setVisibility(View.GONE);
        serachcontainer.setVisibility(View.GONE);
        favouritcontainer.setVisibility(View.GONE);
        albumdetailscontainer.setVisibility(View.GONE);
        favouritcontainer.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setEnabled(false);


    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                File f = new File(Environment.getExternalStorageDirectory(), "MusicApp");
                if (!f.exists()) {
                    f.mkdirs();
                }
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();
                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = connection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream
                File sdCardRoot= new File(Environment.getExternalStorageDirectory(), "MusicApp");

                OutputStream output = new FileOutputStream(sdCardRoot
                + "/"+fileartist+filename+".mp3");

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));

        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);
            Log.e("URLURL","called");


        }

    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
    public void SearchFunction(){
        search.clearFocus();
        CoordinatorLayout touchInterceptor = findViewById(R.id.bigcontainer);
        touchInterceptor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (search.isFocused()) {
                        Rect outRect = new Rect();
                        search.getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                            search.clearFocus();
                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    }
                }
                return false;
            }
        });
        InterstitialAd mInterstitialAd = new InterstitialAd(HomePage.this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstial_ad_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    if (mInterstitialAd.isLoaded()) {
                        mInterstitialAd.show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Lost the focus", Toast.LENGTH_LONG).show();
                }

            }
        });


        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length()==0){
                    regularcontainer.setVisibility(View.VISIBLE);
                    serachcontainer.setVisibility(View.GONE);
                }else {
                    regularcontainer.setVisibility(View.GONE);
                    serachcontainer.setVisibility(View.VISIBLE);
                    swipeRefreshLayout.setEnabled(false);
                    searchViewModel.getRecomendedSongs(String.valueOf(charSequence));

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        if (mBottomSheetBehavior.getState()==BottomSheetBehavior.STATE_EXPANDED){
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        }else {
            if (regularcontainer.getVisibility() == View.VISIBLE && allviewcontainer.getVisibility() == View.VISIBLE)
                moveTaskToBack(true);
            else {
                regularcontainer.setVisibility(View.VISIBLE);
                allviewcontainer.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setEnabled(true);
                serachcontainer.setVisibility(View.GONE);
                albumdetailscontainer.setVisibility(View.GONE);
                search.setText("");
                search.clearFocus();
            }
        }

    }
    public ArrayList<HashMap<String, String>> getPlayList() {
        songsList.clear();
        File home =new File(Environment.getExternalStorageDirectory(), "MusicApp");
        if (home != null) {
            File[] listFiles = home.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    System.out.println(file.getAbsolutePath());
                    if (file.isDirectory()) {
                        scanDirectory(file);
                    } else {
                        addSongToList(file);
                    }
                }
            }
        }
        // return songs list array
        return songsList;
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);

    }
    public void NoInternetConnection() {
        Rect displayRectangle = new Rect();
        Window window = HomePage.this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this, R.style.CustomAlertDialog);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        noInternetView = LayoutInflater.from(HomePage.this).inflate(R.layout.no_internent_conneection, viewGroup, false);

        builder.setView(noInternetView);
        noInternetDialog = builder.create();
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                finishAffinity();
            }
        });
        WindowManager.LayoutParams wm = new WindowManager.LayoutParams();
        wm.copyFrom(noInternetDialog.getWindow().getAttributes());
        wm.width = (int) (displayRectangle.width() * 0.9f);
        //wm.width=WindowManager.LayoutParams.MATCH_PARENT;
        wm.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wm.gravity = Gravity.CENTER_HORIZONTAL;
        wm.dimAmount = 0.60f;

        ElasticButton btnClickOk = noInternetView.findViewById(R.id.internetok);
        ImageView imgre = noInternetView.findViewById(R.id.retrynetwork);
        imgre.setOnClickListener(view ->
        {
            noInternetDialog.dismiss();
            if (InternetConnection.checkConnection(this)) {
                Toast.makeText(this, "connected_to_the_internet", Toast.LENGTH_SHORT).show();
                noInternetDialog.dismiss();
            } else {
                Toast.makeText(this, "no_internet_connection", Toast.LENGTH_SHORT).show();
            }
        });
        btnClickOk.setOnClickListener(view ->
        {
            noInternetDialog.dismiss();
            startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));

        });


        noInternetDialog.show();
        noInternetDialog.getWindow().setAttributes(wm);
        noInternetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        noInternetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        noInternetDialog.setCanceledOnTouchOutside(false);
        noInternetDialog.setOnCancelListener(dialog -> finishAffinity());
    }

    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("app","context");
            if ((!InternetConnection.checkConnection(getApplicationContext()))) {
                NoInternetConnection();
                swipeRefreshLayout.setRefreshing(false);
            } else {
                if (noInternetDialog!=null){
                    noInternetDialog.dismiss();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        }
    };
    private void scanDirectory(File directory) {
        if (directory != null) {
            File[] listFiles = directory.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    if (file.isDirectory()) {
                        scanDirectory(file);
                    } else {
                        addSongToList(file);
                    }

                }
            }
        }
    }

    private void addSongToList(File song) {
        if (song.getName().endsWith(".mp3")) {
            HashMap<String, String> songMap = new HashMap<String, String>();
            songMap.put("songTitle",
                    song.getName().substring(0, (song.getName().length() - 4)));
            songMap.put("songPath", song.getPath());

            // Adding each song to SongList
            songsList.add(songMap);
        }
    }

}
