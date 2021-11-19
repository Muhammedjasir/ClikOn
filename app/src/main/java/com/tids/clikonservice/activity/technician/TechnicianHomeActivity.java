package com.tids.clikonservice.activity.technician;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TechnicianHomeActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private PrefManager pref;

    private TextView tv_hold_services,tv_start_services;

    private static final int TIME_DELAY = 2000;
    private static long backPressed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician_home);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        tv_start_services = findViewById(R.id.tv_start_services);
        tv_hold_services = findViewById(R.id.tv_hold_services);
        ImageView iv_profile = findViewById(R.id.iv_profile);
        ImageView iv_online_status = findViewById(R.id.iv_online_status);
        TextView tv_name = findViewById(R.id.tv_name);

        pref = new PrefManager(this);
        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        String name = sp.getString(Constant.USER_USERNAME, "");
        tv_name.setText(name);

        Log.e("id::",sp.getString(Constant.USER_USERID, ""));

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
            Intent intent = new Intent(getApplicationContext(), TechnicianProfileActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.received_product).setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ReceivedProductActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.start_service).setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), StartServiceActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.search_product).setOnClickListener(v ->{
            Intent intent = new Intent(getApplicationContext(), SearchProductActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.service_status).setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ServiceStatusTechActivity.class);
            startActivity(intent);
        });

        getRecentStartedProduct();
        getHoldProductsCount();
    }

    private void getHoldProductsCount() {
        try {
            String authorization = "Bearer " + sp.getString(Constant.USER_AUTHORIZATION, "");
            String condition = "SELECT COUNT(1) CNT FROM SERVICE_MODULE_VIEW WHERE SM_STS_CODE='SERVPUS' AND SM_SRP_SYS_ID="+
                    sp.getString(Constant.USER_USERID,"");

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
                                    int hold_products = jsonArray.getJSONObject(0).getInt("CNT");
                                    tv_hold_services.setText(hold_products + "");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onError(ANError anError) {
//                            showError(anError);
                        }
                    });


        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void getRecentStartedProduct(){
        try {
            String authorization = "Bearer " + sp.getString(Constant.USER_AUTHORIZATION, "");
            String condition = "SELECT SM_CTI_ITEM_NAME FROM SERVICE_MODULE_VIEW WHERE SM_STS_CODE='SERVSRT' AND SM_SRP_SYS_ID="+
                    sp.getString(Constant.USER_USERID,"");

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
                                    String products_name = jsonArray.getJSONObject(0).getString("SM_CTI_ITEM_NAME");
                                    tv_start_services.setText(products_name);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onError(ANError anError) {
//                            showError(anError);
                        }
                    });


        }catch (Exception ex){
            ex.printStackTrace();
        }
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