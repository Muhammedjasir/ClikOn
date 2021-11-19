package com.tids.clikonservice.activity.driver;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.snackbar.Snackbar;
import com.tids.clikonservice.R;
import com.tids.clikonservice.Utils.Constant;
import com.tids.clikonservice.Utils.Helper.PrefManager;
import com.tids.clikonservice.activity.merchant.ServiceStatusSvActivity;
import com.tids.clikonservice.activity.technician.ServiceStatusTechActivity;
import com.tids.clikonservice.adapter.driver.DailyReportAdapter;
import com.tids.clikonservice.model.ProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class DailyReportActivity extends AppCompatActivity implements View.OnClickListener {

    private PrefManager pref;
    private SharedPreferences sp;

    private String authorization="",driverID="";
    private int mYear,mMonth,mDay;

//    private ImageView iv_filter;
//    ArrayAdapter<String> productStatus;

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

//        iv_filter = findViewById(R.id.iv_filter);
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

//        iv_filter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
//        if (v == iv_filter){
//            showFilterPopupbox();
//        }
    }

//    private void showFilterPopupbox() {
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DailyReportActivity.this);
//        alertDialogBuilder.setCancelable(false);
//        LayoutInflater layoutInflater = LayoutInflater.from(DailyReportActivity.this);
//        View popupInputDialogView = layoutInflater.inflate(R.layout.row_filter_dialog, null);
//        alertDialogBuilder.setView(popupInputDialogView);
//        final AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
//
//        LinearLayout status_lay = popupInputDialogView.findViewById(R.id.status_lay);
//        AutoCompleteTextView act_status = popupInputDialogView.findViewById(R.id.tv_status);
//        ImageView iv_cancel = popupInputDialogView.findViewById(R.id.iv_cancel);
//        TextView tv_from_date = popupInputDialogView.findViewById(R.id.tv_from_date);
//        TextView tv_to_date = popupInputDialogView.findViewById(R.id.tv_to_date);
//        AppCompatButton bt_filter = popupInputDialogView.findViewById(R.id.bt_filter);
//
//        status_lay.setVisibility(View.VISIBLE);
//
//        iv_cancel.setOnClickListener(v -> alertDialog.cancel());
//
//        tv_from_date.setOnClickListener(v -> {
//            // To show current date in the datepicker
//            Calendar mcurrentDate = Calendar.getInstance();
//            mYear = mcurrentDate.get(Calendar.YEAR);
//            mMonth = mcurrentDate.get(Calendar.MONTH);
//            mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
//
//            DatePickerDialog mDatePicker = new DatePickerDialog(DailyReportActivity.this,
//                    (datepicker, selectedyear, selectedmonth, selectedday) -> {
//                Calendar calendar = Calendar.getInstance();
//                Calendar myCalendar = Calendar.getInstance();
//                myCalendar.set(Calendar.YEAR, selectedyear);
//                myCalendar.set(Calendar.MONTH, selectedmonth);
//                myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
//                String myFormat = "dd/MMM/yy"; //Change as you need
//                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
//                tv_from_date.setText(sdf.format(myCalendar.getTime()));
//
//
//                if (calendar.getTimeInMillis() < myCalendar.getTimeInMillis()) {//check from date is always less than to date
//                    calendar.set(Calendar.YEAR, selectedyear);
//                    calendar.set(Calendar.MONTH, selectedmonth);
//                    calendar.set(Calendar.DAY_OF_MONTH, selectedday);
//                }
//                mDay = selectedday;
//                mMonth = selectedmonth;
//                mYear = selectedyear;
//
//            }, mYear, mMonth, mDay);
//            //mDatePicker.setTitle("Select date");
//            // disable future dates
//            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
//            mDatePicker.show();
//        });
//
//        tv_to_date.setOnClickListener(v -> {
//            // To show current date in the datepicker
//            Calendar mcurrentDate = Calendar.getInstance();
//            mYear = mcurrentDate.get(Calendar.YEAR);
//            mMonth = mcurrentDate.get(Calendar.MONTH);
//            mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);
//
//            DatePickerDialog mDatePicker = new DatePickerDialog(DailyReportActivity.this,
//                    (datepicker, selectedyear, selectedmonth, selectedday) -> {
//                Calendar myCalendar = Calendar.getInstance();
//                myCalendar.set(Calendar.YEAR, selectedyear);
//                myCalendar.set(Calendar.MONTH, selectedmonth);
//                myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
//                String myFormat = "dd/MMM/yy"; //Change as you need
//                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
//                tv_to_date.setText(sdf.format(myCalendar.getTime()));
//
//                mDay = selectedday;
//                mMonth = selectedmonth;
//                mYear = selectedyear;
//            }, mYear, mMonth, mDay);
//            //mDatePicker.setTitle("Select date");
//            // disable future dates
//            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
//            mDatePicker.show();
//        });
//
//        try {
//            productStatus = new ArrayAdapter<String>(DailyReportActivity.this,
//                    R.layout.row_spinner_adapter, Constant.DRIVER_PRODUCT_STATUS);
//            act_status.setAdapter(productStatus);
//            act_status.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View view, MotionEvent motionEvent) {
//                    act_status.showDropDown();
//                    return false;
//                }
//            });
//        }catch (Exception ex){
//            Log.e("error:",ex.toString());
//        }
//
////        bt_filter.setOnClickListener(v -> {
////            String from_date = tv_from_date.getText().toString().trim();
////            String to_date = tv_to_date.getText().toString().trim();
////            if (!from_date.isEmpty() && !to_date.isEmpty()){
////                alertDialog.cancel();
////                loadFilterData(from_date,to_date);
////            }
////        });
//    }

    private void getDailyReport(String selected_date) {
        try {
            String condition = "SELECT COLLECTION.*,ITEM.ITEM_NAME,ITEM.ITEM_UPD_DT FROM OT_CLCTN_ITEMS COLLECTION " +
                    "INNER JOIN OM_ITEM ITEM ON COLLECTION.CTI_ITEM_CODE=ITEM.ITEM_CODE WHERE " +
                    "COLLECTION.CTI_STS_CODE = 'DLV' AND COLLECTION.CTI_CM_DOC_NO IN " +
                    "(SELECT DVR_CLCN_DOCNO FROM OT_DVR_CLCTN WHERE DVR_DV_SYS_ID IN " +
                    "(SELECT DV_SYS_ID FROM OT_DVR_REQ_ALLCTN WHERE DV_DVR_CODE = '"+driverID+"'))";
            JSONObject jsonObject = new JSONObject();
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
        Intent intent = new Intent(DailyReportActivity.this,DriversHomeActivity.class);
        startActivity(intent);
        finish();
    }
}