package com.tids.clikonservice.activity.merchant;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.material.textfield.TextInputEditText;
import com.tids.clikonservice.R;
import com.tids.clikonservice.Utils.Constant;
import com.tids.clikonservice.Utils.Utils;
import com.tids.clikonservice.adapter.merchant.MerchantProductAdapter;
import com.tids.clikonservice.model.ProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ServiceStatusSvActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivBack;
    private LinearLayout btn_filter;
    private RecyclerView rv_products;
    private TextInputEditText edt_search_product;

    private int mYear,mMonth,mDay;
    private String authorization="", MERCHANT_ID="";
    private SharedPreferences sp;
    private Utils utils;

    private MerchantProductAdapter merchantProductAdapter;
    private ArrayList<ProductModel> productModelArrayList = new ArrayList<>();

    ArrayAdapter<String> productStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_status_sv);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        ivBack=findViewById(R.id.back_btn);
        btn_filter = findViewById(R.id.btn_filter);
        rv_products = findViewById(R.id.recycler_view);
        edt_search_product = findViewById(R.id.edt_search_product);

        ivBack.setOnClickListener(v -> onBackPressed());

        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        authorization = "Bearer " + sp.getString(Constant.USER_AUTHORIZATION, "");
        MERCHANT_ID = sp.getString(Constant.USER_USERID,"");

        merchantProductAdapter = new MerchantProductAdapter(ServiceStatusSvActivity.this,
                productModelArrayList);
        rv_products.setLayoutManager(new LinearLayoutManager(ServiceStatusSvActivity.this,
                LinearLayoutManager.VERTICAL,false));
        rv_products.setAdapter(merchantProductAdapter);
        getProducts();

        btn_filter.setOnClickListener(this);

        edt_search_product.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 3){
                    getSearch(s);
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    private void getProducts() {
        try {
            String condition = "SELECT COLLECTION.*,ITEM.ITEM_NAME from OT_CLCTN_ITEMS COLLECTION INNER " +
                    "JOIN OM_ITEM ITEM ON COLLECTION.CTI_ITEM_CODE=ITEM.ITEM_CODE WHERE COLLECTION.CTI_MERCHT_ID= '" +
                    MERCHANT_ID + "' ORDER BY COLLECTION.CTI_SYS_ID DESC";
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
                                            String product_date = jsonArray.getJSONObject(i).getString("CTI_CR_DT");
                                            if (!product_date.equalsIgnoreCase("null")){
                                                product_date = utils.parseServerDateTime(product_date);
                                            }

                                            ProductModel productModel = new ProductModel(id, product_code, product_name, product_date, product_status,
                                                    product_serial_number, product_batch_number);
                                            productModelArrayList.add(productModel);
                                        }
                                        merchantProductAdapter.notifyDataSetChanged();
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

    @Override
    public void onClick(View v) {
        if (v == btn_filter){
           filterAlertbox();
        }
    }

    private void filterAlertbox() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ServiceStatusSvActivity.this);
        alertDialogBuilder.setCancelable(false);
        LayoutInflater layoutInflater = LayoutInflater.from(ServiceStatusSvActivity.this);
        View popupInputDialogView = layoutInflater.inflate(R.layout.row_filter_dialog, null);
        alertDialogBuilder.setView(popupInputDialogView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        ImageView iv_cancel = popupInputDialogView.findViewById(R.id.iv_cancel);
        TextView tv_from_date = popupInputDialogView.findViewById(R.id.tv_from_date);
        TextView tv_to_date = popupInputDialogView.findViewById(R.id.tv_to_date);
        AppCompatButton bt_filter = popupInputDialogView.findViewById(R.id.bt_filter);
        AutoCompleteTextView act_status = popupInputDialogView.findViewById(R.id.tv_status);
        LinearLayout status_lay = popupInputDialogView.findViewById(R.id.status_lay);

        status_lay.setVisibility(View.VISIBLE);

        iv_cancel.setOnClickListener(v -> alertDialog.cancel());
        tv_from_date.setOnClickListener(v -> {
            // To show current date in the datepicker
            Calendar mcurrentDate = Calendar.getInstance();
            mYear = mcurrentDate.get(Calendar.YEAR);
            mMonth = mcurrentDate.get(Calendar.MONTH);
            mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker = new DatePickerDialog(ServiceStatusSvActivity.this, (datepicker, selectedyear, selectedmonth, selectedday) -> {
                Calendar calendar = Calendar.getInstance();
                Calendar myCalendar = Calendar.getInstance();
                myCalendar.set(Calendar.YEAR, selectedyear);
                myCalendar.set(Calendar.MONTH, selectedmonth);
                myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                String myFormat = "dd/MMM/yy"; //Change as you need
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
                tv_from_date.setText(sdf.format(myCalendar.getTime()));

                if (calendar.getTimeInMillis() < myCalendar.getTimeInMillis()) {//check from date is always less than to date
                    calendar.set(Calendar.YEAR, selectedyear);
                    calendar.set(Calendar.MONTH, selectedmonth);
                    calendar.set(Calendar.DAY_OF_MONTH, selectedday);
                }
                mDay = selectedday;
                mMonth = selectedmonth;
                mYear = selectedyear;

            }, mYear, mMonth, mDay);
            //mDatePicker.setTitle("Select date");
            // disable future dates
            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePicker.show();
        });

        tv_to_date.setOnClickListener(v -> {
            // To show current date in the datepicker
            Calendar mcurrentDate = Calendar.getInstance();
            mYear = mcurrentDate.get(Calendar.YEAR);
            mMonth = mcurrentDate.get(Calendar.MONTH);
            mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker = new DatePickerDialog(ServiceStatusSvActivity.this, (datepicker, selectedyear, selectedmonth, selectedday) -> {
                Calendar myCalendar = Calendar.getInstance();
                myCalendar.set(Calendar.YEAR, selectedyear);
                myCalendar.set(Calendar.MONTH, selectedmonth);
                myCalendar.set(Calendar.DAY_OF_MONTH, selectedday);
                String myFormat = "dd/MMM/yy"; //Change as you need
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
                tv_to_date.setText(sdf.format(myCalendar.getTime()));

                mDay = selectedday;
                mMonth = selectedmonth;
                mYear = selectedyear;
            }, mYear, mMonth, mDay);
            //mDatePicker.setTitle("Select date");
            // disable future dates
            mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
            mDatePicker.show();
        });

        try {
            productStatus = new ArrayAdapter<String>(ServiceStatusSvActivity.this,
                    R.layout.row_spinner_adapter, Constant.PRODUCT_STATUS);
            act_status.setAdapter(productStatus);
            act_status.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    act_status.showDropDown();
                    return false;
                }
            });
        }catch (Exception ex){
            Log.e("error:",ex.toString());
        }

        bt_filter.setOnClickListener(v -> {
            String from_date = tv_from_date.getText().toString().trim();
            String to_date = tv_to_date.getText().toString().trim();
            if (!from_date.isEmpty() && !to_date.isEmpty()){
                alertDialog.cancel();
                String pd_status = act_status.getText().toString().trim();
                loadFilterData(from_date,to_date,pd_status);
            }
        });
    }

    private void loadFilterData(String from_date, String to_date, String pd_status) {

        String status = pd_status;
        if (!pd_status.isEmpty()){
            if (status.equalsIgnoreCase("pickup")){
                status = "DVRPCP";
            }else if (status.equalsIgnoreCase("entered")){
                status = "DVRETR";
            }else if (status.equalsIgnoreCase("collected")){
                status = "CLTD";
            }else if (status.equalsIgnoreCase("pending in service")){
                status = "PENSERV";
            }else if (status.equalsIgnoreCase("service finished")){
                status = "SERVFIN";
            }else if (status.equalsIgnoreCase("pending in delivery")){
                status = "PENDLV";
            }else if (status.equalsIgnoreCase("delivered")){
                status = "DLV";
            }else if (status.equalsIgnoreCase("service started")){
                status = "SERVSRT";
            }else if (status.equalsIgnoreCase("service paused")){
                status = "SERVPUS";
            }
        }
        getFilteredProducts(from_date,to_date,status);

    }

    private void getFilteredProducts(String fromDate,String toDate,String status) {
        try {
            String condition = "SELECT COLLECTION.*,ITEM.ITEM_NAME from OT_CLCTN_ITEMS COLLECTION INNER " +
                    "JOIN OM_ITEM ITEM ON COLLECTION.CTI_ITEM_CODE=ITEM.ITEM_CODE WHERE COLLECTION.CTI_MERCHT_ID='"+
                    MERCHANT_ID+"'AND COLLECTION.CTI_MERCHT_ID ='"+status
                    +"' AND  TRUNC(COLLECTION.CTI_CR_DT) BETWEEN '"+fromDate+"' AND '"+toDate+"'";
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
                                    productModelArrayList.clear();
                                    merchantProductAdapter.notifyDataSetChanged();
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
                                            String product_date = jsonArray.getJSONObject(i).getString("CTI_CR_DT");
                                            if (!product_date.equalsIgnoreCase("null")){
                                                product_date = utils.parseServerDateTime(product_date);
                                            }

                                            ProductModel productModel = new ProductModel(id, product_code, product_name, product_date, product_status,
                                                    product_serial_number, product_batch_number);
                                            productModelArrayList.add(productModel);
                                        }
                                        merchantProductAdapter.notifyDataSetChanged();
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

    private void getSearch(CharSequence word){
        try {
            String condition = "SELECT COLLECTION.*,ITEM.ITEM_NAME from OT_CLCTN_ITEMS COLLECTION INNER " +
                    "JOIN OM_ITEM ITEM ON COLLECTION.CTI_ITEM_CODE=ITEM.ITEM_CODE WHERE (UPPER(COLLECTION.CTI_ITEM_CODE) LIKE UPPER('%"+
                    word+"%') OR UPPER(COLLECTION.CTI_SERIAL_NO) LIKE UPPER('%"+word+"%') OR UPPER(COLLECTION.CTI_BATCH) LIKE UPPER('%"+
                    word+"%') OR UPPER(ITEM.ITEM_NAME) LIKE UPPER('%"+word+"%')) AND COLLECTION.CTI_CM_DOC_NO="+
                    MERCHANT_ID;
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
                                    productModelArrayList.clear();
                                    merchantProductAdapter.notifyDataSetChanged();
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
                                            String product_date = jsonArray.getJSONObject(i).getString("CTI_CR_DT");
                                            if (!product_date.equalsIgnoreCase("null")){
                                                product_date = utils.parseServerDateTime(product_date);
                                            }

                                            ProductModel productModel = new ProductModel(id, product_code, product_name, product_date, product_status,
                                                    product_serial_number, product_batch_number);
                                            productModelArrayList.add(productModel);
                                        }
                                        merchantProductAdapter.notifyDataSetChanged();
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
        Toast.makeText(ServiceStatusSvActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
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