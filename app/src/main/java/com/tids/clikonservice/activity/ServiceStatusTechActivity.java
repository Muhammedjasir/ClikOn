package com.tids.clikonservice.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.snackbar.Snackbar;
import com.tids.clikonservice.R;
import com.tids.clikonservice.Utils.Constant;
import com.tids.clikonservice.adapter.technician.TechnicianHoldProductsAdapter;
import com.tids.clikonservice.model.ScannedProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ServiceStatusTechActivity extends AppCompatActivity {

    private SharedPreferences sp;
    private String authorization = "";

    private TextView tv_hold_prd_count, tv_notstart;
    private RecyclerView recyclerView;
    private TechnicianHoldProductsAdapter technicianHoldProductsAdapter;
    private ArrayList<ScannedProductModel> scannedProductModelArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_status_tech);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        authorization = "Bearer " + sp.getString(Constant.USER_AUTHORIZATION, "");

        CardView cv_overlimit = findViewById(R.id.card_overlimit);
        CardView cv_notstarted = findViewById(R.id.card_notstarted);
        CardView cv_hold = findViewById(R.id.card_hold);
        findViewById(R.id.back_btn).setOnClickListener(v -> onBackPressed());

        tv_notstart = findViewById(R.id.tv_notstart);
        tv_hold_prd_count = findViewById(R.id.tv_hold_prd_count);
        recyclerView = findViewById(R.id.recycler_view);
        technicianHoldProductsAdapter = new TechnicianHoldProductsAdapter(ServiceStatusTechActivity.this,
                scannedProductModelArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(ServiceStatusTechActivity.this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(technicianHoldProductsAdapter);

        loadHoldServices();

        cv_overlimit.setOnClickListener(v -> {

            scannedProductModelArrayList.clear();
            technicianHoldProductsAdapter.notifyDataSetChanged();

            cv_notstarted.setRadius(10);
            cv_hold.setRadius(10);
            cv_overlimit.setRadius(10);

            cv_notstarted.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            cv_hold.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            cv_overlimit.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_gray_transparant));
            loadOverlimitProducts();
        });
        cv_notstarted.setOnClickListener(v -> {

            scannedProductModelArrayList.clear();
            technicianHoldProductsAdapter.notifyDataSetChanged();

            cv_notstarted.setRadius(10);
            cv_hold.setRadius(10);
            cv_overlimit.setRadius(10);

            cv_notstarted.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_gray_transparant));
            cv_hold.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            cv_overlimit.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            notStartedService();
        });
        cv_hold.setOnClickListener(v -> {

            scannedProductModelArrayList.clear();
            technicianHoldProductsAdapter.notifyDataSetChanged();

            cv_notstarted.setRadius(10);
            cv_hold.setRadius(10);
            cv_overlimit.setRadius(10);

            cv_notstarted.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            cv_hold.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_gray_transparant));
            cv_overlimit.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            loadHoldServices();
        });

    }

    private void notStartedService() {
        try {
            String condition = "SELECT * FROM SERVICE_MODULE_VIEW WHERE SM_STS_CODE='PENSERV'";

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("query", condition);

            AndroidNetworking.post(Constant.BASE_URL + "GetData")
                    .addHeaders("Authorization", authorization)
                    .addJSONObjectBody(jsonObject)
                    .setTag(this)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("Response::", response.toString());

                            try {
                                if (response.getBoolean("status")) {
                                    //Get the instance of JSONArray that contains JSONObjects
                                    JSONArray jsonArray = response.getJSONArray("data");
                                    if (jsonArray.length() != 0) {
                                        tv_notstart.setText(String.valueOf(jsonArray.length()));
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            String product_doc_id = String.valueOf(jsonArray.getJSONObject(i).getInt("SM_DOC_NO"));
                                            String product_name = jsonArray.getJSONObject(i).getString("SM_CTI_ITEM_NAME");
                                            String product_serial_number = jsonArray.getJSONObject(i).getString("SM_SERIAL_NO");
                                            String product_batch_number = jsonArray.getJSONObject(i).getString("SM_BATCH_CODE");
                                            String product_complaint = jsonArray.getJSONObject(i).getString("SM_REMARKS");
                                            String product_qrcode_data = jsonArray.getJSONObject(i).getString("SM_CTI_SYS_ID");
                                            String flag = "";

                                            ScannedProductModel scannedProductModel = new ScannedProductModel(product_doc_id, product_qrcode_data, product_name,
                                                    product_serial_number, product_batch_number, product_complaint, flag);
                                            scannedProductModelArrayList.add(scannedProductModel);
                                        }
                                        technicianHoldProductsAdapter.notifyDataSetChanged();
                                    }
                                } else {
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

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadHoldServices() {
        try {
            String condition = "SELECT * FROM SERVICE_MODULE_VIEW WHERE SM_STS_CODE='SERVPUS' AND SM_SRP_SYS_ID=" +
                    sp.getString(Constant.USER_USERID, "");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("query", condition);

            AndroidNetworking.post(Constant.BASE_URL + "GetData")
                    .addHeaders("Authorization", authorization)
                    .addJSONObjectBody(jsonObject)
                    .setTag(this)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("Response::", response.toString());

                            try {
                                if (response.getBoolean("status")) {
                                    //Get the instance of JSONArray that contains JSONObjects
                                    JSONArray jsonArray = response.getJSONArray("data");
                                    if (jsonArray.length() != 0) {
                                        tv_hold_prd_count.setText(String.valueOf(jsonArray.length()));
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            String product_doc_id = String.valueOf(jsonArray.getJSONObject(i).getInt("SM_DOC_NO"));
                                            String product_name = jsonArray.getJSONObject(i).getString("SM_CTI_ITEM_NAME");
                                            String product_serial_number = jsonArray.getJSONObject(i).getString("SM_SERIAL_NO");
                                            String product_batch_number = jsonArray.getJSONObject(i).getString("SM_BATCH_CODE");
                                            String product_complaint = jsonArray.getJSONObject(i).getString("SM_REMARKS");
                                            String product_qrcode_data = jsonArray.getJSONObject(i).getString("SM_CTI_SYS_ID");
                                            String flag = "hold";

                                            ScannedProductModel scannedProductModel = new ScannedProductModel(product_doc_id, product_qrcode_data, product_name,
                                                    product_serial_number, product_batch_number, product_complaint, flag);
                                            scannedProductModelArrayList.add(scannedProductModel);
                                        }
                                        technicianHoldProductsAdapter.notifyDataSetChanged();
                                    }
                                } else {
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

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadOverlimitProducts() {

        String myFormat = "dd/MMM/yy hh:mm aaa";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        String todaydate = sdf.format(Calendar.getInstance().getTime());
        Log.e("dt-tm::", todaydate);

        try {
            String condition = "SELECT * FROM OT_SERVICE_MODULE WHERE SM_ESTIM_DT<'" + todaydate + "' AND SM_SRP_SYS_ID=" +
                    sp.getString(Constant.USER_USERID, "");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("query", condition);

            AndroidNetworking.post(Constant.BASE_URL + "GetData")
                    .addHeaders("Authorization", authorization)
                    .addJSONObjectBody(jsonObject)
                    .setTag(this)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("Response::", response.toString());

                            try {
                                if (response.getBoolean("status")) {
                                    //Get the instance of JSONArray that contains JSONObjects
                                    JSONArray jsonArray = response.getJSONArray("data");
                                    if (jsonArray.length() != 0) {
                                        tv_hold_prd_count.setText(String.valueOf(jsonArray.length()));
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            String product_doc_id = String.valueOf(jsonArray.getJSONObject(i).getInt("SM_DOC_NO"));
                                            String product_name = jsonArray.getJSONObject(i).getString("SM_CTI_ITEM_NAME");
                                            String product_serial_number = jsonArray.getJSONObject(i).getString("SM_SERIAL_NO");
                                            String product_batch_number = jsonArray.getJSONObject(i).getString("SM_BATCH_CODE");
                                            String product_complaint = jsonArray.getJSONObject(i).getString("SM_REMARKS");
                                            String product_qrcode_data = jsonArray.getJSONObject(i).getString("SM_CTI_SYS_ID");
                                            String flag = "over";

                                            ScannedProductModel scannedProductModel = new ScannedProductModel(product_doc_id, product_qrcode_data, product_name,
                                                    product_serial_number, product_batch_number, product_complaint, flag);
                                            scannedProductModelArrayList.add(scannedProductModel);
                                        }
                                        technicianHoldProductsAdapter.notifyDataSetChanged();
                                    }
                                } else {
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

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void showError(ANError anError) {
        Toast.makeText(ServiceStatusTechActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
        Log.e("Error :: ", anError.getErrorBody());
    }

    private void customToast(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).setBackgroundTint(getResources().getColor(R.color.colorPrimary)).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}