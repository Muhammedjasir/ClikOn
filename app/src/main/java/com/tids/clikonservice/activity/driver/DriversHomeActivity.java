package com.tids.clikonservice.activity.driver;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.tids.clikonservice.R;
import com.tids.clikonservice.Utils.Constant;
import com.tids.clikonservice.Utils.Helper.PrefManager;
import com.tids.clikonservice.adapter.driver.PickupPagerAdapter;

public class DriversHomeActivity extends AppCompatActivity {

    TextView tv_driver_name,tv_cart_count;
    LinearLayout lay_cart,lay_service_registration,profile_lay;
    ImageView iv_profile;
    Switch sw_duty;

    SharedPreferences sp;
    PrefManager pref;

    private static final int TIME_DELAY = 2000;
    private static long backPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivers_home);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        pref = new PrefManager(this);
        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        tv_driver_name = findViewById(R.id.tv_driver_name);
        iv_profile = findViewById(R.id.iv_profile);
        lay_cart = findViewById(R.id.lay_cart);
        profile_lay = findViewById(R.id.profile_lay);
        tv_cart_count = findViewById(R.id.tv_cart_count);
        lay_service_registration = findViewById(R.id.lay_service_registration);
        sw_duty = findViewById(R.id.sw_duty);

        String name = sp.getString(Constant.USER_USERNAME, "");
        tv_driver_name.setText(name);


        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Merchant Pickup"));
        tabLayout.addTab(tabLayout.newTab().setText("Technician Pickup"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        final ViewPager viewPager = findViewById(R.id.pager);
        final PickupPagerAdapter adapter = new PickupPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

        lay_cart.setOnClickListener(v -> {
            Intent intent = new Intent(DriversHomeActivity.this, CartActivity.class);
            startActivity(intent);
        });

        String user_profile = sp.getString(Constant.USER_PROFILE, "");
        if (!user_profile.equalsIgnoreCase("") || user_profile != null){
            Glide.with(getApplicationContext())
                    .load(user_profile)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(iv_profile);
        }

        profile_lay.setOnClickListener(v ->{
            Intent intent = new Intent(getApplicationContext(), ProfileDriverActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.lay_service_registration).setOnClickListener(v -> {
            Intent intent = new Intent(DriversHomeActivity.this, ProductRegistrationActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.lay_daiy_report).setOnClickListener(v -> {
            Intent intent = new Intent(DriversHomeActivity.this, DailyReportActivity.class);
            startActivity(intent);
        });

    }

    private void customToast(String message){
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).setBackgroundTint(getResources().getColor(R.color.colorPrimary)).show();
    }

    @Override
    public void onBackPressed() {
        if (backPressed + TIME_DELAY > System.currentTimeMillis()) {
            finishAffinity();

        } else {
            customToast(getString(R.string.press_once_again_to_exit));
        }
        backPressed = System.currentTimeMillis();
    }
}