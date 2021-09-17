package com.tids.clikonservice.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;
import com.tids.clikonservice.R;
import com.tids.clikonservice.Utils.Constant;
import com.tids.clikonservice.Utils.Helper.PrefManager;

public class TechnicianHomeActivity extends AppCompatActivity {

    SharedPreferences sp;
    PrefManager pref;

    private static final int TIME_DELAY = 2000;
    private static long backPressed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician_home);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        TextView tv_start_services = findViewById(R.id.tv_start_services);
        TextView tv_hold_services = findViewById(R.id.tv_hold_services);
        ImageView iv_profile = findViewById(R.id.iv_profile);
        ImageView iv_online_status = findViewById(R.id.iv_online_status);
        TextView tv_name = findViewById(R.id.tv_name);

        pref = new PrefManager(this);
        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        tv_start_services.setText(pref.getStartServices());
        tv_hold_services.setText(pref.getHoldServices());

        String name = sp.getString(Constant.USER_USERNAME, "");
        tv_name.setText(name);

//        String online_status = sp.getString(Constant.USER_ONLINE_STATUS, "");
//        if (online_status.equalsIgnoreCase("1")){
//            Glide.with(getApplicationContext())
//                    .load(R.drawable.ic_round_dot_green)
//                    .into(iv_online_status);
//        }else {
//            Glide.with(getApplicationContext())
//                    .load(R.drawable.ic_round_dot)
//                    .into(iv_online_status);
//        }

        String user_profile = sp.getString(Constant.USER_PROFILE, "");
        if (!user_profile.equalsIgnoreCase("") || user_profile != null){
            Glide.with(getApplicationContext())
                    .load(user_profile)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
//                    .placeholder(R.drawable.ic_profile)
//                    .error(R.drawable.ic_profile)
                    .into(iv_profile);
        }

        TextView tv_recently_started = findViewById(R.id.tv_start_services);
        TextView tv_num_hold_services = findViewById(R.id.tv_hold_services);
        tv_recently_started.setText(pref.getTechnicianProductName());
        tv_num_hold_services.setText(pref.getHoldProductCount());


        findViewById(R.id.layout_profile).setOnClickListener(v ->{
            Intent intent = new Intent(getApplicationContext(),TechnicianProfileActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.received_product).setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),ReceivedProductActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.start_service).setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),StartServiceActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.search_product).setOnClickListener(v ->{
            Intent intent = new Intent(getApplicationContext(),SearchProductActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.service_status).setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),ServiceStatusTechActivity.class);
            startActivity(intent);
        });

    }

    //double back press to exit
    @Override
    public void onBackPressed() {
        if (backPressed + TIME_DELAY > System.currentTimeMillis()) {

//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//            finish();
            finishAffinity();

        } else {
            customToast(getString(R.string.press_once_again_to_exit));
        }
        backPressed = System.currentTimeMillis();
    }

    private void customToast(String message){
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).setBackgroundTint(getResources().getColor(R.color.colorPrimary)).show();
    }
}