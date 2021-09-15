package com.tids.clikonservice.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.tids.clikonservice.R;
import com.tids.clikonservice.Utils.Constant;
import com.tids.clikonservice.Utils.Helper.PrefManager;
import com.tids.clikonservice.Utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class StartServiceActivity extends AppCompatActivity implements View.OnClickListener {

    private PrefManager pref;
    private SharedPreferences sp;
    private Utils utils;

    private ImageView ivBack;
    private TextView tv_product_name,tv_product_serial_number,tv_product_batch_number,tv_product_complaint,
            tv_product_service_registered,tv_product_received,tv_product_service_started,tv_estimate_date;
    private AppCompatButton bt_service_completed,btn_pause,bt_update_estimatetime,btn_Technician_remarks;
    private TextInputEditText ed_pouse_reason,ed_find_problems;
    private RelativeLayout mainLayout;

    int mYear,mMonth,mDay;
    String authorization = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_service);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        pref = new PrefManager(getApplicationContext());
        authorization = "Bearer " + sp.getString(Constant.USER_AUTHORIZATION, "");

        ivBack=findViewById(R.id.back_btn);
        ivBack.setOnClickListener(v -> onBackPressed());

        tv_product_name = findViewById(R.id.product_name);
        tv_product_serial_number = findViewById(R.id.product_serial_number);
        tv_product_batch_number = findViewById(R.id.product_batch_number);
        tv_product_complaint = findViewById(R.id.tv_product_complaint);
        tv_product_service_registered = findViewById(R.id.tv_product_service_registered);
        tv_product_received = findViewById(R.id.tv_product_received);
        tv_product_service_started = findViewById(R.id.product_service_started);
        bt_service_completed = findViewById(R.id.bt_service_completed);
        btn_pause = findViewById(R.id.btn_pause);
        ed_pouse_reason = findViewById(R.id.tv_pouse_reason);
        tv_estimate_date = findViewById(R.id.tv_estimate_date);
        mainLayout = findViewById(R.id.mainlayout);
        ed_find_problems = findViewById(R.id.ed_find_problems);
        bt_update_estimatetime = findViewById(R.id.bt_update_estimatetime);
        btn_Technician_remarks = findViewById(R.id.btn_Technician_remarks);

        getProductData();

        tv_estimate_date.setOnClickListener(this);
        bt_update_estimatetime.setOnClickListener(this);
        btn_pause.setOnClickListener(this);
        bt_service_completed.setOnClickListener(this);
        bt_update_estimatetime.setOnClickListener(this);
        btn_Technician_remarks.setOnClickListener(this);
    }

    private void setDate(){
        // TODO Auto-generated method stub
        // To show current date in the datepicker
        Calendar mcurrentDate = Calendar.getInstance();
        mYear = mcurrentDate.get(Calendar.YEAR);
        mMonth = mcurrentDate.get(Calendar.MONTH);
        mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog mDatePicker = new DatePickerDialog(StartServiceActivity.this, (datepicker, selectedyear, selectedmonth, selectedday) -> {
            Calendar myCalendar = Calendar.getInstance();
            myCalendar.set(Calendar.YEAR, selectedyear);
            myCalendar.set(Calendar.MONTH, selectedmonth);
            myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
            String myFormat = "dd/MMM/yy hh:mm aaa"; //Change as you need
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
            tv_estimate_date.setText(sdf.format(myCalendar.getTime()));

            mDay = selectedday;
            mMonth = selectedmonth;
            mYear = selectedyear;
        }, mYear, mMonth, mDay);
        //mDatePicker.setTitle("Select date");
        mDatePicker.show();
    }

    private void getProductData() {
        try {
            Log.e("TechnicianProductId::",pref.getTechnicianProductId());
            String condition = "SM_CTI_SYS_ID='" + pref.getTechnicianProductId() + "'";

            AndroidNetworking.get(Constant.BASE_URL + "GetTable")
                    .addQueryParameter("table_name", Constant.SERVICE_PRODUCT_INFO)
                    .addQueryParameter("condition", condition)
                    .addHeaders("Authorization", authorization)
                    .setTag(this)
                    .setPriority(Priority.LOW)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.e("rsp:", response.toString());

                                if (response.getBoolean("status")) {

                                    mainLayout.setVisibility(View.VISIBLE);

                                    //Get the instance of JSONArray that contains JSONObjects
                                    JSONArray jsonArray = response.getJSONArray("data");
                                    if (jsonArray.length() != 0) {

                                        String product_serial_number = jsonArray.getJSONObject(0).getString("SM_SERIAL_NO");
                                        String product_batch_number = jsonArray.getJSONObject(0).getString("SM_BATCH_CODE");

                                        tv_product_name.setText(pref.getTechnicianProductName());
                                        tv_product_serial_number.setText(product_serial_number);
                                        tv_product_batch_number.setText(product_batch_number);

                                        if (jsonArray.getJSONObject(0).getString("SM_REMARKS")!=null)
                                            tv_product_complaint.setText(jsonArray.getJSONObject(0).getString("SM_REMARKS"));

                                        if ((jsonArray.getJSONObject(0).getString("SM_DET_COMP")!=null))
                                            ed_find_problems.setText(jsonArray.getJSONObject(0).getString("SM_DET_COMP"));

                                        if (jsonArray.getJSONObject(0).getString("SM_DLY_RSN")!=null)
                                            ed_pouse_reason.setText(jsonArray.getJSONObject(0).getString("SM_DLY_RSN"));

                                        if (jsonArray.getJSONObject(0).getString("SM_CR_DT") != null){
                                            String product_service_registered_date = utils.parseServerDateTime(jsonArray.getJSONObject(0).getString("SM_CR_DT"));
                                            tv_product_service_registered.setText(product_service_registered_date);
                                        }
                                        if (jsonArray.getJSONObject(0).getString("SM_STRT_DT") != null){
                                            String product_start_date = utils.parseServerDateTime(jsonArray.getJSONObject(0).getString("SM_STRT_DT"));
                                            tv_product_service_started.setText(product_start_date);
                                        }
                                        if (jsonArray.getJSONObject(0).getString("SM_ESTIM_DT") != null){
                                            String product_estimate_date = utils.parseServerDateTime(jsonArray.getJSONObject(0).getString("SM_ESTIM_DT"));
                                            tv_estimate_date.setText(product_estimate_date);
                                        }
                                        if (jsonArray.getJSONObject(0).getString("SM_CM_IN_DT") != null){
                                            String product_received_date = utils.parseServerDateTime(jsonArray.getJSONObject(0).getString("SM_CM_IN_DT"));
                                            tv_product_received.setText(product_received_date);
                                        }
                                    }
                                }else {
                                    mainLayout.setVisibility(View.GONE);
                                }
                            } catch (Exception e) {
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

    @Override
    public void onClick(View v) {
        if (v == tv_estimate_date){
            setDate();
        }
        if (v == btn_pause){
            String estimateDate = tv_estimate_date.getText().toString().trim();
            if (estimateDate != null || !estimateDate.isEmpty()){
                pouseProduct();
            }
        }
        if (v == bt_update_estimatetime){
            updateEstimateTime();
        }
        if (v == bt_service_completed){
            String estimateDate = tv_estimate_date.getText().toString().trim();
            if (estimateDate != null || !estimateDate.isEmpty()){
                updateStatusComplete();
            }
        }
        if (v == btn_Technician_remarks){
            updateTechnicianRemarks();
        }
    }

    private void updateTechnicianRemarks() {
        String technician_remarks = ed_find_problems.getText().toString().trim();
        if (!technician_remarks.isEmpty()){
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("SM_DET_COMP",technician_remarks);

                AndroidNetworking.put(Constant.BASE_URL + Constant.SERVICE_PRODUCT_INFO + "/" +
                        pref.getTechnicianProductDocId())
                        .addHeaders("Authorization", authorization)
                        .addJSONObjectBody(jsonObject)
                        .setTag(this)
                        .setPriority(Priority.LOW)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Log.e("rsp:", response.toString());

                                    customToast("Remarks updated");

                                } catch (Exception e) {
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
    }

    private void updateStatusComplete() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("SM_STS_CODE","SERVFIN");

            AndroidNetworking.put(Constant.BASE_URL + Constant.SERVICE_PRODUCT_INFO + "/" +
                    pref.getTechnicianProductDocId())
                    .addHeaders("Authorization", authorization)
                    .addJSONObjectBody(jsonObject)
                    .setTag(this)
                    .setPriority(Priority.LOW)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.e("rsp:", response.toString());

                                pref.setTechnicianProductStatus("");
                                pref.setTechnicianProductId("");
                                pref.setTechnicianProductName("");
                                mainLayout.setVisibility(View.GONE);

                                customToast("Completed");

                            } catch (Exception e) {
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

    private void updateEstimateTime() {
        String estimateTime = tv_estimate_date.getText().toString().trim();
        if (estimateTime != null || !estimateTime.isEmpty()){
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("SM_ESTIM_DT",estimateTime);

                Log.e("url:",Constant.BASE_URL + Constant.SERVICE_PRODUCT_INFO + "/" +
                        pref.getTechnicianProductDocId());
                Log.e("jsonbody:",jsonObject.toString());

                AndroidNetworking.put(Constant.BASE_URL + Constant.SERVICE_PRODUCT_INFO + "/" +
                        pref.getTechnicianProductDocId())
                        .addHeaders("Authorization", authorization)
                        .addJSONObjectBody(jsonObject)
                        .setTag(this)
                        .setPriority(Priority.LOW)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Log.e("rsp:", response.toString());

                                    customToast("Estimate time updated");

                                } catch (Exception e) {
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
    }

    private void pouseProduct() {

        String pause_reason = ed_find_problems.getText().toString().trim();
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("SM_STS_CODE","SERVPUS");
            jsonObject.put("SM_DLY_RSN",pause_reason);

            AndroidNetworking.put(Constant.BASE_URL + Constant.SERVICE_PRODUCT_INFO + "/" +
                    pref.getTechnicianProductDocId())
                    .addHeaders("Authorization", authorization)
                    .addJSONObjectBody(jsonObject)
                    .setTag(this)
                    .setPriority(Priority.LOW)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        Log.e("rsp:", response.toString());

                        pref.setTechnicianProductStatus("");
                        pref.setTechnicianProductId("");
                        pref.setTechnicianProductName("");
                        mainLayout.setVisibility(View.GONE);

                        customToast("paused");


                    } catch (Exception e) {
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
        Toast.makeText(StartServiceActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
        Log.e("Error :: ", anError.getErrorBody());
    }

    private void customToast(String message){
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).setBackgroundTint(getResources().getColor(R.color.colorPrimary)).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),TechnicianHomeActivity.class);
        startActivity(intent);
        finish();
    }

}