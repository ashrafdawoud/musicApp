package com.indrul.musicapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.LoadAdError;
import com.indrul.musicapp.R;

public class SplashScreenActivity extends AppCompatActivity {
    InterstitialAd mInterstitialAd;
    boolean shown=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Animation animation= AnimationUtils.loadAnimation(this, R.anim.fade_in);
        ImageView logo=findViewById(R.id.logo);
        logo.startAnimation(animation);
        mInterstitialAd = new InterstitialAd(SplashScreenActivity.this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstial_ad_id));
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                mInterstitialAd.show();
                shown=true;
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the interstitial ad is closed.
                Intent intent=new Intent(SplashScreenActivity.this,HomePage.class);
                startActivity(intent);
            }
        });
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();

    }
}
