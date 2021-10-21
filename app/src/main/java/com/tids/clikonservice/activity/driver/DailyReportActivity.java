package com.tids.clikonservice.activity.driver;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.snackbar.Snackbar;
import com.tids.clikonservice.R;
import com.tids.clikonservice.Utils.Constant;
import com.tids.clikonservice.Utils.Helper.PrefManager;
import com.tids.clikonservice.adapter.driver.DailyReportAdapter;
import com.tids.clikonservice.model.ProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DailyReportActivity extends AppCompatActivity {

    private PrefManager pref;
    private SharedPreferences sp;

    private String authorization="",driverID="";

    private RecyclerView rvReport;
    private DailyReportAdapter dailyReportAdapter;
    private ArrayList<ProductModel> dailyReportModelArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_report);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        pref = new PrefManager(getApplicationContext());
        authorization = "Bearer " + sp.getString(Constant.USER_AUTHORIZATION, "");
        driverID = sp.getString(Constant.USER_USERID,"");

        findViewById(R.id.back_btn).setOnClickListener(v -> {
            onBackPressed();
        });

        rvReport = findViewById(R.id.recycler_view);
        dailyReportAdapter = new DailyReportAdapter(DailyReportActivity.this,dailyReportModelArrayList);
        rvReport.setLayoutManager(new LinearLayoutManager(DailyReportActivity.this,
                LinearLayoutManager.VERTICAL,false));
        rvReport.setAdapter(dailyReportAdapter);

        String myFormat = "dd/MMM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        String todaydate = sdf.format(Calendar.getInstance().getTime());
        Log.e("dt-tm::", todaydate);
        getDailyReport(todaydate);
    }

    private void getDailyReport(String selected_date) {
        try {
            JSONObject jsonObject = new JSONObject();
            String condition = "SELECT COLLECTION.*,ITEM.ITEM_NAME,ITEM.ITEM_UPD_DT from OT_CLCTN_ITEMS COLLECTION " +
                    "INNER JOIN OM_ITEM ITEM ON COLLECTION.CTI_ITEM_CODE=ITEM.ITEM_CODE WHERE" +
                    " COLLECTION.CTI_STS_CODE = 'PENSERV' OR COLLECTION.CTI_STS_CODE = 'DLV'AND" +
                    " ITEM.ITEM_UPD_DT ='"+selected_date+"' ORDER BY COLLECTION.CTI_SYS_ID DESC";
            jsonObject.put("query",condition);

            SharedPreferences sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
            String authorization = "Bearer " + sp.getString(Constant.USER_AUTHORIZATION, "");

            AndroidNetworking.post(Constant.BASE_URL + "GetData")
                    .addHeaders("Authorization", authorization)
                    .addJSONObjectBody(jsonObject)
                    .setTag(this)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("list.Response::",response.toString());

                            try {
                                if (response.getBoolean("status")) {
                                    //Get the instance of JSONArray that contains JSONObjects
                                    JSONArray jsonArray = response.getJSONArray("data");
                                    if (jsonArray.length() != 0) {
                                        for (int i = 0; i< jsonArray.length(); i++){
                                            int id = jsonArray.getJSONObject(i).getInt("CTI_SYS_ID");
                                            String product_name = jsonArray.getJSONObject(i).getString("ITEM_NAME");
                                            String product_code = jsonArray.getJSONObject(i).getString("CTI_ITEM_CODE");
                                            String product_serial_number = jsonArray.getJSONObject(i).getString("CTI_SERIAL_NO");
                                            String product_batch_number = jsonArray.getJSONObject(i).getString("CTI_BATCH");
                                            String product_status = jsonArray.getJSONObject(i).getString("CTI_STS_CODE");
                                            String product_date = "";

                                            ProductModel productModel = new ProductModel(id, product_code, product_name, product_date, product_status,
                                                    product_serial_number, product_batch_number);
                                            dailyReportModelArrayList.add(productModel);
                                        }
                                        dailyReportAdapter.notifyDataSetChanged();
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
        Toast.makeText(DailyReportActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
        Log.e("Error :: ", anError.getErrorBody());
    }

    private void customToast(String message){
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).setBackgroundTint(getResources().getColor(R.color.colorPrimary)).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}