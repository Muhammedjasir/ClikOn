package com.tids.clikonservice.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.VideoView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.tids.clikonservice.R;
import com.tids.clikonservice.Utils.Constant;
import com.tids.clikonservice.Utils.Helper.Device;
import com.tids.clikonservice.Utils.Helper.PrefManager;
import com.tids.clikonservice.Utils.Utils;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private VideoView mVideoView;
    LottieAnimationView lockerAnim;
    Button mLogin;
    private TextInputEditText edt_username,edt_password;
    private Button btn_login;

    String deviceId = "";
    PrefManager pref;
    SharedPreferences sp;
    Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        mVideoView = findViewById(R.id.videoView);
        lockerAnim = findViewById(R.id.lotie_lock_open);
        mLogin = findViewById(R.id.btn_login);
        edt_username = findViewById(R.id.edt_username);
        edt_password = findViewById(R.id.edt_password);
        btn_login = findViewById(R.id.btn_login);

//        FirebaseApp.initializeApp(getApplicationContext());

        pref = new PrefManager(this);
        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
//        email = sp.getString(Constant.USER_EMAIL, "");

        getDeviceId();

        mLogin.setOnClickListener(this);

        mLogin.setOnClickListener(new View.OnClickListener() {
            boolean isAnimated=false;
            @Override
            public void onClick(View v) {
                if (!isAnimated) {
                    lockerAnim.playAnimation();
                    lockerAnim.setSpeed(3f);
                    isAnimated=true;}
                else {
                    isAnimated=false;
                }
                if (v == mLogin){

                    String username = edt_username.getText().toString().trim();
                    String password = edt_password.getText().toString().trim();

                    if (username.isEmpty()){

                    }else if (password.isEmpty()){

                    }else {


                        Intent intent = new Intent(LoginActivity.this,DemoPagenationActivity.class);
                        startActivity(intent);
                    }

                }
            }


        });

        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.bgvideo);

        mVideoView.setVideoURI(uri);
        mVideoView.start();

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }

    private String getDeviceId() {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            deviceId = Settings.Secure.getString(
                    this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);

        } else {
            deviceId = Device.getSerialNumber();
        }
        return deviceId;
    }

    @Override
    public void onClick(View v) {
        if (v == mLogin){
            boolean isAnimated=false;

            if (!isAnimated) {
                lockerAnim.playAnimation();
                lockerAnim.setSpeed(3f);
                isAnimated=true;}
            else {
                isAnimated=false;
            }
            if (v == mLogin){

                // Hide keyboard after button click
                try {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }

                String username = edt_username.getText().toString().trim();
                String password = edt_password.getText().toString().trim();

                if (username.isEmpty()){
                    customToast(getString(R.string.invalid_number));
                }else if (password.isEmpty()){
                    customToast(getString(R.string.invalid_password));
                }else {
                    if (utils.isNetworkAvailable(LoginActivity.this)) {

                        // prevent button double click
                        btn_login.setEnabled(false);
                        // get fcm tocken
//                        GetDeviceToken(username,password);
                    } else {
                        customToast(getString(R.string.no_network_connection));
                    }

                    Intent intent = new Intent(LoginActivity.this,DemoPagenationActivity.class);
                    startActivity(intent);
                }

            }
        }
    }

    private void customToast(String message){
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).setBackgroundTint(getResources().getColor(R.color.colorPrimary)).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }

}