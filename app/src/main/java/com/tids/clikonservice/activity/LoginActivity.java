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
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

import com.airbnb.lottie.LottieAnimationView;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.tids.clikonservice.R;
import com.tids.clikonservice.Utils.Constant;
import com.tids.clikonservice.Utils.Helper.Device;
import com.tids.clikonservice.Utils.Helper.PrefManager;
import com.tids.clikonservice.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        AndroidNetworking.initialize(getApplicationContext());

        pref = new PrefManager(this);
        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        getDeviceId();

        mLogin.setOnClickListener(this);

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

                        getToken(username,password);
                    } else {
                        customToast(getString(R.string.no_network_connection));
                    }
                }

            }
        }
    }

    private void getToken(String username, String password) {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", username);
            jsonObject.put("encryppassword", password);

            AndroidNetworking.post(Constant.BASE_URL)
                    .addJSONObjectBody(jsonObject)
                    .setTag("login")
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("Response::",response.toString());

                            try {
                                String token =  response.getString("token");
                                String tokenExpireTime =  response.getString("token_expire_time");

                                Log.e("token::",token);
                                Log.e("exp.date::",tokenExpireTime);

                                getLogin(token,username);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            showError(anError);
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getLogin(String token,String username) {
        try {

            AndroidNetworking.get(Constant.BASE_URL+"GetTable")
                    .addPathParameter("table_name", "ADM_USER")
                    .addQueryParameter("condition", "USER_ID='"+username+"'")
                    .addHeaders("Authorization", "Bearer "+token)
                    .setTag("user_data")
                    .setPriority(Priority.LOW)
                    .build()
                    .getAsJSONArray(new JSONArrayRequestListener() {
                        @Override
                        public void onResponse(JSONArray response) {

                            Log.e("Response::",response.toString());
//                            Intent intent = new Intent(LoginActivity.this,DemoPagenationActivity.class);
//                            startActivity(intent);
                        }

                        @Override
                        public void onError(ANError anError) {
                            showError(anError);
                        }
                    });

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void customToast(String message){
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).setBackgroundTint(getResources().getColor(R.color.colorPrimary)).show();
    }

    private void showError(ANError anError) {
        Toast.makeText(LoginActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
        Log.d("Error :: ", anError.getErrorBody());
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