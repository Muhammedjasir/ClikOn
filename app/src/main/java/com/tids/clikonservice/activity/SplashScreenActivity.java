package com.tids.clikonservice.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.VideoView;

import com.tids.clikonservice.R;
import com.tids.clikonservice.Utils.Constant;
import com.tids.clikonservice.Utils.Helper.PrefManager;
import com.tids.clikonservice.activity.driver.DriversHomeActivity;
import com.tids.clikonservice.activity.merchant.StaffHomeActivity;
import com.tids.clikonservice.activity.technician.TechnicianHomeActivity;

public class SplashScreenActivity extends AppCompatActivity {

    VideoView splashVideo;
    PrefManager pref;
    SharedPreferences sp;

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

        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        pref = new PrefManager(this);

        splashVideo = (VideoView) findViewById(R.id.videoView);

        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.clikon_spash_anim);

        splashVideo.setVideoURI(uri);
        splashVideo.start();

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if (pref.isLoggedIn()) {
                    if (sp.getString(Constant.USER_TYPE, "").equalsIgnoreCase("DRVR")){
                        Intent i = new Intent(SplashScreenActivity.this, DriversHomeActivity.class);
                        startActivity(i);
                        finish();
                    }else if (sp.getString(Constant.USER_TYPE, "").equalsIgnoreCase("TECHN")){
                        Intent intent = new Intent(getApplicationContext(), TechnicianHomeActivity.class);
                        startActivity(intent);
                        finish();
                    }else if (sp.getString(Constant.USER_TYPE, "").equalsIgnoreCase("MRCT")){
                        Intent intent = new Intent(getApplicationContext(), StaffHomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }else {
                    Intent i = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        }, 5000);
    }
}