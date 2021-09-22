package com.tids.clikonservice.activity.driver;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.snackbar.Snackbar;
import com.tids.clikonservice.R;
import com.tids.clikonservice.Utils.Constant;
import com.tids.clikonservice.Utils.Helper.PrefManager;
import com.tids.clikonservice.activity.technician.TechnicianProfileActivity;
import com.tids.clikonservice.adapter.driver.DriverCartAdapter;
import com.tids.clikonservice.adapter.driver.DriverPickupNotificationAdapter;
import com.tids.clikonservice.model.LocationModel;
import com.tids.clikonservice.model.ScannedProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DriversHomeActivity extends AppCompatActivity {

//    CardView demoLink;
    TextView tv_pickup_count,tv_driver_name,tv_cart_count;
    LinearLayout lay_cart,lay_daiy_report,lay_service_registration;
    ImageView iv_profile;
    Switch sw_duty;
    RecyclerView rv_notification;

    SharedPreferences sp;
    PrefManager pref;

    private DriverPickupNotificationAdapter pickupNotificationAdapter;
    private ArrayList<LocationModel> locationModelArrayList = new ArrayList<>();

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

        rv_notification = findViewById(R.id.recycler_view);
        tv_driver_name = findViewById(R.id.tv_driver_name);
        iv_profile = findViewById(R.id.iv_profile);
        lay_cart = findViewById(R.id.lay_cart);
//        demoLink = findViewById(R.id.almadina_hypermarket);
        tv_pickup_count = findViewById(R.id.tv_pickup_count);
        tv_cart_count = findViewById(R.id.tv_cart_count);
        lay_daiy_report = findViewById(R.id.lay_daiy_report);
        lay_service_registration = findViewById(R.id.lay_service_registration);
        sw_duty = findViewById(R.id.sw_duty);

        String name = sp.getString(Constant.USER_USERNAME, "");
        tv_driver_name.setText(name);

//        demoLink.setOnClickListener(v -> {
//            Intent intent = new Intent(DriversHomeActivity.this, StoreActivity.class);
//            startActivity(intent);
//        });

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
//                    .placeholder(R.drawable.ic_profile)
//                    .error(R.drawable.ic_profile)
                    .into(iv_profile);
        }

        pickupNotificationAdapter = new DriverPickupNotificationAdapter(DriversHomeActivity.this, locationModelArrayList);
        rv_notification.setLayoutManager(new LinearLayoutManager(DriversHomeActivity.this,
                LinearLayoutManager.VERTICAL,false));
        rv_notification.setAdapter(pickupNotificationAdapter);
        getPickupNotifications();

    }

    private void getPickupNotifications() {
        try {
            String authorization = "Bearer " + sp.getString(Constant.USER_AUTHORIZATION, "");
            String condition = "" ;

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("query",condition);

            AndroidNetworking.post(Constant.BASE_URL + "GetData")
                    .addHeaders("Authorization", authorization)
                    .addJSONObjectBody(jsonObject)
                    .setTag(this)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("Response::",response.toString());

                            try {
                                if (response.getBoolean("status")) {
                                    //Get the instance of JSONArray that contains JSONObjects
                                    JSONArray jsonArray = response.getJSONArray("data");
                                    if (jsonArray.length() != 0) {
                                        tv_pickup_count.setText(String.valueOf(jsonArray.length()));
                                        for (int i = 0; i< jsonArray.length(); i++){
                                            String productDocId = String.valueOf(jsonArray.getJSONObject(i).getInt("SM_DOC_NO"));
                                            String productName = jsonArray.getJSONObject(i).getString("SM_CTI_ITEM_NAME");
                                            String location = jsonArray.getJSONObject(i).getString("");
                                            String address = jsonArray.getJSONObject(i).getString("");
                                            String productQrCode = jsonArray.getJSONObject(i).getString("SM_CTI_SYS_ID");
                                            String productRefId = jsonArray.getJSONObject(i).getString("SM_CM_REF_NO");

                                            LocationModel locationModel = new LocationModel(productDocId, productRefId, location, address, productName, productQrCode);
                                            locationModelArrayList.add(locationModel);
                                        }
                                        pickupNotificationAdapter.notifyDataSetChanged();
                                    }
                                }else {
                                    customToast("No product available");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onError(ANError anError) {
                            showError(anError);
                        }
                    });

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void showError(ANError anError) {
        Toast.makeText(DriversHomeActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
        Log.e("Error :: ", anError.getErrorBody());
    }

    private void customToast(String message){
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).setBackgroundTint(getResources().getColor(R.color.colorPrimary)).show();
    }

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
}