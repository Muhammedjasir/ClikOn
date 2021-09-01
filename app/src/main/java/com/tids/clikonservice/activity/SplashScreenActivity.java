package com.tids.clikonservice.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.VideoView;

import com.tids.clikonservice.R;

public class SplashScreenActivity extends AppCompatActivity {

    VideoView splashVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        splashVideo = (VideoView) findViewById(R.id.videoView);



        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.clikon_spash_anim);

        splashVideo.setVideoURI(uri);
        splashVideo.start();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, 5000);
    }
}