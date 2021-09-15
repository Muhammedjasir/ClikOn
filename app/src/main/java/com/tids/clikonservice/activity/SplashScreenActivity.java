package com.tids.clikonservice.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.VideoView;

import com.tids.clikonservice.R;
import com.tids.clikonservice.Utils.Helper.PrefManager;

public class SplashScreenActivity extends AppCompatActivity {

    VideoView splashVideo;
    PrefManager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // If the Android version is lower than Jellybean, use this call to hide
        // the status bar.
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_splash_screen);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        pref = new PrefManager(this);

        splashVideo = (VideoView) findViewById(R.id.videoView);

        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.clikon_spash_anim);

        splashVideo.setVideoURI(uri);
        splashVideo.start();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if (pref.isLoggedIn()) {
                    Intent i = new Intent(SplashScreenActivity.this, TechnicianHomeActivity.class);
                    startActivity(i);
                    finish();
                }else {
                    Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, 5000);
    }
}