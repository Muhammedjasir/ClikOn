package com.tids.clikonservice.activity.merchant;

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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;
import com.tids.clikonservice.R;
import com.tids.clikonservice.Utils.Constant;
import com.tids.clikonservice.Utils.Helper.PrefManager;

public class StaffHomeActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private PrefManager pref;

    private static final int TIME_DELAY = 2000;
    private static long backPressed;

    LinearLayout btnRegistration, btnServiceStatus, btnCustomerService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_home);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        pref = new PrefManager(this);
        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        TextView tv_name = findViewById(R.id.tv_name);
        ImageView iv_profile = findViewById(R.id.iv_profile);

        String name = sp.getString(Constant.USER_USERNAME, "");
        tv_name.setText(name);

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

        btnRegistration = findViewById(R.id.btn_registration);
        btnServiceStatus = findViewById(R.id.btn_service_status);
        btnCustomerService = findViewById(R.id.btn_customer_service);

        btnCustomerService.setOnClickListener(v -> Toast.makeText(StaffHomeActivity.this,"Future Updates",Toast.LENGTH_SHORT).show());

        btnRegistration.setOnClickListener(v -> {
            Intent intent = new Intent(StaffHomeActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });

        btnServiceStatus.setOnClickListener(v -> {
            Intent intent = new Intent(StaffHomeActivity.this, ServiceStatusSvActivity.class);
            startActivity(intent);
        });

        iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StaffHomeActivity.this, MerchantProfileActivity.class);
                startActivity(intent);
            }
        });
    }

    //double back press to exit
    @Override
    public void onBackPressed() {
        if (backPressed + TIME_DELAY > System.currentTimeMillis()) {
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