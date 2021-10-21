package com.tids.clikonservice.activity.technician;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.AutoTransition;
import androidx.transition.TransitionManager;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.tids.clikonservice.adapter.SpinnerAdapter;
import com.tids.clikonservice.adapter.technician.SparepartsAdapter;
import com.tids.clikonservice.model.ResponseModel;
import com.tids.clikonservice.model.SparepartsModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class StartServiceActivity extends AppCompatActivity implements View.OnClickListener {

    private PrefManager pref;
    private SharedPreferences sp;
    private Utils utils;

    private ImageView ivBack,iv_edit,iv_card_Arrow;
    private TextView tv_product_name,tv_product_serial_number,tv_product_batch_number,tv_product_complaint,
            tv_product_service_registered,tv_product_received,tv_product_service_started,tv_estimate_date,
            tv_product_ref_no,tv_product_code,tv_doc_id,tv_customer_code,tv_customer_name;
    private AppCompatButton bt_service_completed,btn_pause,bt_update_estimatetime,btn_Technician_remarks,
            btn_release,btn_remove,bt_update_servicestart;
    private EditText ed_parts_qty,ed_parts_qty_hold;
    private TextInputEditText ed_find_problems;
    private RelativeLayout mainLayout;
    private LinearLayout lay_bt1,lay_bt2,iv_add_parts,parts_layout,missing_parts_layout,iv_add_parts_hold;
    private AutoCompleteTextView auto_reasons,auto_spare_parts,auto_spare_parts_hold;
    private RecyclerView rv_spareparts,rv_spareparts_hold;
    private CardView card_parts;

    private boolean dateFlag = false;
    private int mYear,mMonth,mDay;
    private String authorization = "", productDocId = "";
    private String sparepartsId="",sparepartsName="",sparepartsCode="",
            sparepartsId_hold="",sparepartsName_hold="",sparepartsCode_hold="";

    ArrayAdapter<String> holdSpinnerAdapter;

    private ArrayList<SparepartsModel> sparepartsResponseModelArrayList = new ArrayList<>();;
    private SparepartsAdapter sparepartsSpinnerAdapter;

    private ArrayList<SparepartsModel> sparepartsResponseModelArrayListHold = new ArrayList<>();;
    private SparepartsAdapter sparepartsSpinnerAdapterHold;

    private ArrayList<ResponseModel> allSparepartsResponseModelArrayList;
    private SpinnerAdapter allSparepartsAdapter;

    private ArrayList<ResponseModel> holdSparepartsResponseModelArrayList;
    private SpinnerAdapter holdSparepartsAdapter;

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

        auto_spare_parts_hold = findViewById(R.id.auto_spare_parts_hold);
        missing_parts_layout = findViewById(R.id.missing_parts_layout);
        rv_spareparts_hold = findViewById(R.id.rv_spareparts_hold);
        card_parts = findViewById(R.id.card_parts);
        parts_layout = findViewById(R.id.parts_layout);
        iv_card_Arrow = findViewById(R.id.iv_card_Arrow);
        ed_parts_qty_hold = findViewById(R.id.ed_parts_qty_hold);
        iv_add_parts = findViewById(R.id.iv_add_parts);
        iv_add_parts_hold = findViewById(R.id.iv_add_parts_hold);
        auto_reasons = findViewById(R.id.auto_reasons);
        ed_parts_qty = findViewById(R.id.ed_parts_qty);
        rv_spareparts = findViewById(R.id.rv_spareparts);
        auto_spare_parts = findViewById(R.id.auto_spare_parts);
        iv_edit = findViewById(R.id.iv_edit);
        tv_customer_code = findViewById(R.id.tv_customer_code);
        tv_customer_name = findViewById(R.id.tv_customer_name);
        tv_doc_id = findViewById(R.id.tv_doc_id);
        tv_product_code = findViewById(R.id.tv_product_code);
        tv_product_name = findViewById(R.id.product_name);
        tv_product_serial_number = findViewById(R.id.product_serial_number);
        tv_product_batch_number = findViewById(R.id.product_batch_number);
        tv_product_complaint = findViewById(R.id.tv_product_complaint);
        tv_product_service_registered = findViewById(R.id.tv_product_service_registered);
        tv_product_received = findViewById(R.id.tv_product_received);
        tv_product_service_started = findViewById(R.id.product_service_started);
        bt_service_completed = findViewById(R.id.bt_service_completed);
        btn_pause = findViewById(R.id.btn_pause);
        tv_estimate_date = findViewById(R.id.tv_estimate_date);
        mainLayout = findViewById(R.id.mainlayout);
        ed_find_problems = findViewById(R.id.ed_find_problems);
        bt_update_estimatetime = findViewById(R.id.bt_update_estimatetime);
        bt_update_servicestart = findViewById(R.id.bt_update_servicestart);
        btn_Technician_remarks = findViewById(R.id.btn_Technician_remarks);
        lay_bt1 = findViewById(R.id.bt_lay1);
        lay_bt2 = findViewById(R.id.bt_lay2);
        btn_release = findViewById(R.id.btn_release);
        btn_remove = findViewById(R.id.btn_remove);
        tv_product_ref_no = findViewById(R.id.tv_product_ref_no);

        sparepartsSpinnerAdapter = new SparepartsAdapter(StartServiceActivity.this,
                sparepartsResponseModelArrayList);
        rv_spareparts.setLayoutManager(new LinearLayoutManager(StartServiceActivity.this,
                LinearLayoutManager.VERTICAL,false));
        rv_spareparts.setAdapter(sparepartsSpinnerAdapter);

        sparepartsSpinnerAdapterHold = new SparepartsAdapter(StartServiceActivity.this,
                sparepartsResponseModelArrayListHold);
        rv_spareparts_hold.setLayoutManager(new LinearLayoutManager(StartServiceActivity.this,
                LinearLayoutManager.VERTICAL,false));
        rv_spareparts_hold.setAdapter(sparepartsSpinnerAdapterHold);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            lay_bt1.setVisibility(View.GONE);
            lay_bt2.setVisibility(View.VISIBLE);
            productDocId = extras.getString("product_doc_id");
            getReportProduct();
            // list added spare parts
            listAddedSpareParts();
            listAddedSparePartsHold();
        }else {
            lay_bt1.setVisibility(View.VISIBLE);
            lay_bt2.setVisibility(View.GONE);
            getProductData();
        }

        // fetch technician hold reaons
        getServiceHoldReasons();
        auto_reasons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String reason = auto_reasons.getText().toString();
                if (reason.equalsIgnoreCase("Parts not available")){
                    missing_parts_layout.setVisibility(View.VISIBLE);
                }else {
                    missing_parts_layout.setVisibility(View.GONE);
                }
            }
        });

        tv_estimate_date.setOnClickListener(this);
        tv_product_service_started.setOnClickListener(this);
        bt_update_estimatetime.setOnClickListener(this);
        btn_pause.setOnClickListener(this);
        bt_service_completed.setOnClickListener(this);
        bt_update_servicestart.setOnClickListener(this);
        btn_Technician_remarks.setOnClickListener(this);
        btn_release.setOnClickListener(this);
        btn_remove.setOnClickListener(this);
        iv_edit.setOnClickListener(this);
        iv_add_parts.setOnClickListener(this);
        iv_add_parts_hold.setOnClickListener(this);
        iv_card_Arrow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == tv_estimate_date){
            setDate("estimate");
        }
        if (v == tv_product_service_started){
            setDate("service");
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
        if (v == bt_update_servicestart){
            updateServiceDate();
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
        if (v == btn_remove){
            removeProduct();
        }
        if (v == btn_release){
            checkTechnicianStatus();
        }
        if (v == iv_edit){
            editBatchSerialNo();
        }
        if (v == iv_add_parts){
            try {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }catch (Exception ex){ ex.printStackTrace(); }

            String partsQty = ed_parts_qty.getText().toString().trim();
            if (sparepartsId.equalsIgnoreCase("")){
                customToast("Please select spare parts from the list");
            }else if (partsQty.isEmpty()){
                customToast("Please set spare parts quantity");
            }else {
                addSpareParts(partsQty);
            }
        }
        if (v == iv_add_parts_hold){
            try {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }catch (Exception ex){ ex.printStackTrace(); }

            String partsQtyHold = ed_parts_qty_hold.getText().toString().trim();
            if (sparepartsId_hold.equalsIgnoreCase("")){
                customToast("Please select spare parts from the list");
            }else if (partsQtyHold.isEmpty()){
                customToast("Please set spare parts quantity");
            }else {
                addSparePartsHold(partsQtyHold);
            }
        }
        if (v == iv_card_Arrow){
             if (parts_layout.getVisibility() == View.VISIBLE) {
                    TransitionManager.beginDelayedTransition(card_parts,
                            new AutoTransition());
                    parts_layout.setVisibility(View.GONE);
                    iv_card_Arrow.setImageResource(R.drawable.ic_arrow_up);
                }else {
                    TransitionManager.beginDelayedTransition(card_parts,

                                new AutoTransition());
                    parts_layout.setVisibility(View.VISIBLE);
                    iv_card_Arrow.setImageResource(R.drawable.ic_arrow_down);
                }
        }
    }

    private void updateServiceDate() {
        String serviceDate = tv_product_service_started.getText().toString().trim();
        if (serviceDate != null || !serviceDate.isEmpty()){
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("SM_STRT_DT",serviceDate);
                Log.e("jsonbody:",jsonObject.toString());

                AndroidNetworking.put(Constant.BASE_URL + Constant.SERVICE_PRODUCT_INFO + "/" +
                        productDocId)
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

                                    customToast("Service date updated");
                                    dateFlag = true;

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

    private void addSparePartsHold(String partsQtyHold) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("OT_SP_DOC_NO",productDocId);
            jsonObject.put("OT_SP_PARTSID",sparepartsId_hold);
            jsonObject.put("OT_SP_QTY",partsQtyHold);
            Log.e("body::",jsonObject.toString());

            AndroidNetworking.post(Constant.BASE_URL + "OT_PARTNT_AVLABLE")
                    .addHeaders("Authorization", authorization)
                    .addJSONObjectBody(jsonObject)
                    .setTag(this)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("Response2::",response.toString());

                            try {
                                if (response.getBoolean("status")) {

                                    String tb_id = String.valueOf(response.getDouble("data"));
                                    SparepartsModel sparepartsModel = new
                                            SparepartsModel(tb_id,productDocId,partsQtyHold,sparepartsId_hold,
                                            sparepartsName_hold,sparepartsCode_hold);
                                    sparepartsResponseModelArrayListHold.add(sparepartsModel);
                                    sparepartsSpinnerAdapterHold.notifyDataSetChanged();
                                    sparepartsId_hold="";
                                    sparepartsName_hold="";
                                    sparepartsCode_hold="";
                                    auto_spare_parts_hold.setText("");
                                    ed_parts_qty_hold.setText("");
                                }else {
                                    customToast(response.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onError(ANError anError) {
//                                    showError(anError);
                        }
                    });

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void listAddedSparePartsHold(){
        try {
            String condition = "SELECT S.*,P.PART_NAME,P.PART_CODE FROM OT_PARTNT_AVLABLE S INNER JOIN OM_PARTS P ON S.OT_SP_PARTSID=P.PART_SYS_ID WHERE S.OT_SP_DOC_NO="+productDocId;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("query", condition);
            Log.e("body.parts.hold::",jsonObject.toString());

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
                                        for (int i = 0; i< jsonArray.length(); i++){
                                            String tb_id = String.valueOf(jsonArray.getJSONObject(i).getInt("OT_SYS_ID"));
                                            String product_Id = String.valueOf(jsonArray.getJSONObject(i).getInt("OT_SP_DOC_NO"));
                                            String qty = jsonArray.getJSONObject(i).getString("OT_SP_QTY");
                                            String partsId = String.valueOf(jsonArray.getJSONObject(i).getInt("OT_SP_PARTSID"));
                                            String partsName = jsonArray.getJSONObject(i).getString("PART_NAME");
                                            String partsCode = jsonArray.getJSONObject(i).getString("PART_CODE");

                                            SparepartsModel sparepartsModel = new SparepartsModel(tb_id,product_Id,qty,partsId,partsName,partsCode);
                                            sparepartsResponseModelArrayListHold.add(sparepartsModel);
                                        }
                                        sparepartsSpinnerAdapterHold.notifyDataSetChanged();
                                    }
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

    private void listAddedSpareParts() {
        try {
            String condition = "SELECT S.*,P.PART_NAME,P.PART_CODE FROM OT_SERV_PARTS S INNER JOIN " +
                    "OM_PARTS P ON S.SP_PART_SYS_ID=P.PART_SYS_ID WHERE S.SP_SM_DOC_NO="+productDocId;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("query", condition);
            Log.e("addedlist.body::",jsonObject.toString());

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
                                        for (int i = 0; i< jsonArray.length(); i++){
                                            String tb_id = String.valueOf(jsonArray.getJSONObject(i).getInt("SP_SYS_ID"));
                                            String product_Id = String.valueOf(jsonArray.getJSONObject(i).getInt("SP_SM_DOC_NO"));
                                            String qty = jsonArray.getJSONObject(i).getString("SP_QTY");
                                            String partsId = String.valueOf(jsonArray.getJSONObject(i).getInt("SP_PART_SYS_ID"));
                                            String partsName = jsonArray.getJSONObject(i).getString("PART_NAME");
                                            String partsCode = jsonArray.getJSONObject(i).getString("PART_CODE");

                                            SparepartsModel sparepartsModel = new SparepartsModel(tb_id,product_Id,qty,partsId,partsName,partsCode);
                                            sparepartsResponseModelArrayList.add(sparepartsModel);
                                        }
                                        sparepartsSpinnerAdapter.notifyDataSetChanged();
                                    }
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

    private void addSpareParts(String partsQty) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("SP_SM_DOC_NO",productDocId);
            jsonObject.put("SP_PART_SYS_ID",sparepartsId);
            jsonObject.put("SP_QTY",partsQty);
            Log.e("body::",jsonObject.toString());

            AndroidNetworking.post(Constant.BASE_URL + "OT_SERV_PARTS")
                    .addHeaders("Authorization", authorization)
                    .addJSONObjectBody(jsonObject)
                    .setTag(this)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("Response2::",response.toString());

                            try {
                                if (response.getBoolean("status")) {

                                    String tb_id = String.valueOf(response.getDouble("data"));
                                    SparepartsModel sparepartsModel = new
                                            SparepartsModel(tb_id,productDocId,partsQty,sparepartsId,sparepartsName,sparepartsCode);
                                    sparepartsResponseModelArrayList.add(sparepartsModel);
                                    sparepartsSpinnerAdapter.notifyDataSetChanged();
                                    sparepartsId="";
                                    sparepartsName="";
                                    sparepartsCode="";
                                    auto_spare_parts.setText("");
                                    ed_parts_qty.setText("");
                                }else {
                                    customToast(response.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onError(ANError anError) {
//                                    showError(anError);
                        }
                    });

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void getAllSpareParts(String product_code) {
        allSparepartsResponseModelArrayList = new ArrayList<>();
        holdSparepartsResponseModelArrayList = new ArrayList<>();
        try {
            String condition = "SELECT * FROM OM_PARTS WHERE PART_ITEM = '"+product_code+"'";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("query",condition);
            Log.e("spare parts qry:",jsonObject.toString());

            AndroidNetworking.post(Constant.BASE_URL + "GetData")
                    .addHeaders("Authorization", authorization)
                    .addJSONObjectBody(jsonObject)
                    .setTag(this)
                    .setPriority(Priority.LOW)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("spare parts::",response.toString());
                            try {
                                if (response.getBoolean("status")) {
                                    JSONArray jsonArray = response.getJSONArray("data");

                                    if (jsonArray.length() != 0) {
                                        for (int i = 0; i< jsonArray.length(); ++i){
                                            String parts_id = String.valueOf(jsonArray.getJSONObject(i).getDouble("PART_SYS_ID"));
                                            String parts_code = jsonArray.getJSONObject(i).getString("PART_CODE");
                                            String parts_name = jsonArray.getJSONObject(i).getString("PART_NAME");

                                            ResponseModel responseModel = new ResponseModel(parts_id,parts_name,parts_code);
                                            allSparepartsResponseModelArrayList.add(responseModel);
                                            holdSparepartsResponseModelArrayList.add(responseModel);

                                        }
                                        allSparepartsAdapter = new SpinnerAdapter(StartServiceActivity.this,
                                                R.layout.activity_start_service, R.layout.row_spinner_adapter, allSparepartsResponseModelArrayList);
                                        auto_spare_parts.setAdapter(allSparepartsAdapter);

                                        holdSparepartsAdapter = new SpinnerAdapter(StartServiceActivity.this,
                                                R.layout.activity_start_service, R.layout.row_spinner_adapter, holdSparepartsResponseModelArrayList);
                                        auto_spare_parts_hold.setAdapter(holdSparepartsAdapter);

                                        auto_spare_parts.setOnTouchListener((view, motionEvent) -> {
                                            auto_spare_parts.showDropDown();
                                            return false;
                                        });
                                        auto_spare_parts_hold.setOnTouchListener((view, motionEvent) -> {
                                            auto_spare_parts_hold.showDropDown();
                                            return false;
                                        });

                                        auto_spare_parts.setOnItemClickListener((adapterView, view, i, l) -> {
                                            String text = allSparepartsResponseModelArrayList.get(i).getName()+"-"+
                                                    allSparepartsResponseModelArrayList.get(i).getCode();
                                            auto_spare_parts.setText(text);
                                            sparepartsId = allSparepartsResponseModelArrayList.get(i).getId()+"";
                                            sparepartsName = allSparepartsResponseModelArrayList.get(i).getName();
                                            sparepartsCode = allSparepartsResponseModelArrayList.get(i).getCode();
                                        });

                                        auto_spare_parts_hold.setOnItemClickListener((adapterView, view, i, l) -> {
                                            String text = holdSparepartsResponseModelArrayList.get(i).getName()+"-"+
                                                    holdSparepartsResponseModelArrayList.get(i).getCode();
                                            auto_spare_parts_hold.setText(text);
                                            sparepartsId_hold= holdSparepartsResponseModelArrayList.get(i).getId()+"";
                                            sparepartsName_hold = holdSparepartsResponseModelArrayList.get(i).getName();
                                            sparepartsCode_hold = holdSparepartsResponseModelArrayList.get(i).getCode();
                                        });
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onError(ANError anError) {
//                                    showError(anError);
                        }
                    });

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void getServiceHoldReasons() {
        try {
            holdSpinnerAdapter = new ArrayAdapter<String>(StartServiceActivity.this,
                    R.layout.row_spinner_adapter, Constant.PRODUCT_HOLD_REASONS);
            auto_reasons.setAdapter(holdSpinnerAdapter);
            auto_reasons.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    auto_reasons.showDropDown();
                    return false;
                }
            });
        }catch (Exception ex){
            Log.e("error:",ex.toString());
        }
    }

    private void setDate(String type){
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
            if (type.equalsIgnoreCase("estimate")){
                tv_estimate_date.setText(sdf.format(myCalendar.getTime()));
            }else if (type.equalsIgnoreCase("service")){
                tv_product_service_started.setText(sdf.format(myCalendar.getTime()));
            }

            mDay = selectedday;
            mMonth = selectedmonth;
            mYear = selectedyear;
        }, mYear, mMonth, mDay);
        //mDatePicker.setTitle("Select date");
        mDatePicker.show();
    }

    private void getReportProduct() {
        try {
            String condition = "SELECT V.*,S.SM_ESTIM_DT,S.SM_STRT_DT FROM SERVICE_MODULE_VIEW V INNER " +
                    "JOIN OT_SERVICE_MODULE S  ON V.SM_DOC_NO=S.SM_DOC_NO WHERE V.SM_DOC_NO ="+productDocId;

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("query",condition);
            Log.e("query::",jsonObject.toString());

            AndroidNetworking.post(Constant.BASE_URL + "GetData")
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

                                if (response.getBoolean("status")) {

                                    mainLayout.setVisibility(View.VISIBLE);

                                    //Get the instance of JSONArray that contains JSONObjects
                                    JSONArray jsonArray = response.getJSONArray("data");
                                    if (jsonArray.length() != 0) {

                                        String product_serial_number = jsonArray.getJSONObject(0).getString("SM_SERIAL_NO");
                                        tv_product_serial_number.setText(product_serial_number);
                                        String product_batch_number = jsonArray.getJSONObject(0).getString("SM_BATCH_CODE");
                                        tv_product_batch_number.setText(product_batch_number);
                                        String product_name = jsonArray.getJSONObject(0).getString("SM_CTI_ITEM_NAME");
                                        tv_product_name.setText(product_name);
                                        String product_ref_id = jsonArray.getJSONObject(0).getString("SM_CM_REF_NO");
                                        tv_product_ref_no.setText(product_ref_id);
                                        String product_doc_id = jsonArray.getJSONObject(0).getString("SM_DOC_NO");
                                        tv_doc_id.setText("#"+product_doc_id);
                                        productDocId = product_doc_id;
                                        String product_code = jsonArray.getJSONObject(0).getString("SM_CTI_ITEM_CODE");
                                        tv_product_code.setText(product_code);
                                        getAllSpareParts(product_code);
                                        String customer_name = jsonArray.getJSONObject(0).getString("SM_CM_CUST_NAME");
                                        tv_customer_name.setText(customer_name);
                                        String customer_code = jsonArray.getJSONObject(0).getString("SM_CM_CUST_CODE");
                                        tv_customer_code.setText(customer_code);

                                        if (jsonArray.getJSONObject(0).getString("SM_REMARKS")!=null)
                                            tv_product_complaint.setText(jsonArray.getJSONObject(0).getString("SM_REMARKS"));

                                        if ((jsonArray.getJSONObject(0).getString("SM_DET_COMP")!=null))
                                            ed_find_problems.setText(jsonArray.getJSONObject(0).getString("SM_DET_COMP"));

                                        if (!jsonArray.getJSONObject(0).getString("SM_DLY_RSN").equalsIgnoreCase("null")){
                                            if (jsonArray.getJSONObject(0).getString("SM_DLY_RSN").equalsIgnoreCase("Parts not available")){
                                                auto_reasons.setText(holdSpinnerAdapter.getItem(0));
                                                missing_parts_layout.setVisibility(View.VISIBLE);
                                            }else {
                                                auto_reasons.setText(holdSpinnerAdapter.getItem(1));
                                                missing_parts_layout.setVisibility(View.GONE);
                                            }
                                            getServiceHoldReasons();
                                        }
                                        if (jsonArray.getJSONObject(0).getString("SM_CR_DT") != null){
                                            String product_service_registered_date = utils.parseServerDateTime(jsonArray.getJSONObject(0).getString("SM_CR_DT"));
                                            tv_product_service_registered.setText(product_service_registered_date);
                                        }
                                        if (jsonArray.getJSONObject(0).getString("SM_CM_IN_DT") != null){
                                            String product_received_date = utils.parseServerDateTime(jsonArray.getJSONObject(0).getString("SM_CM_IN_DT"));
                                            tv_product_received.setText(product_received_date);
                                        }
                                        String estDate = jsonArray.getJSONObject(0).getString("SM_ESTIM_DT");
                                        if (estDate == null || estDate.equalsIgnoreCase("null")){
                                            dateFlag = false;
                                        }else {
                                            String product_estimate_date = utils.parseServerDateTime(estDate);
                                            tv_estimate_date.setText(product_estimate_date);
                                            dateFlag = true;
                                        }
                                        if (jsonArray.getJSONObject(0).getString("SM_STRT_DT") != null){
                                            String product_start_date = utils.parseServerDateTime(jsonArray.getJSONObject(0).getString("SM_STRT_DT"));
                                            tv_product_service_started.setText(product_start_date);
                                        }
                                    }
                                }else {
                                    mainLayout.setVisibility(View.GONE);
                                    dateFlag = true;
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

    private void getProductData() {

        try {
            String condition = "SELECT V.*,S.SM_ESTIM_DT,S.SM_STRT_DT FROM SERVICE_MODULE_VIEW V INNER " +
                    "JOIN OT_SERVICE_MODULE S  ON V.SM_DOC_NO=S.SM_DOC_NO WHERE V.SM_STS_CODE = 'SERVSRT' " +
                    "AND V.SM_SRP_SYS_ID ="+sp.getString(Constant.USER_USERID,"");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("query",condition);
            Log.e("query::",jsonObject.toString());

            AndroidNetworking.post(Constant.BASE_URL + "GetData")
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

                                if (response.getBoolean("status")) {

                                    mainLayout.setVisibility(View.VISIBLE);

                                    //Get the instance of JSONArray that contains JSONObjects
                                    JSONArray jsonArray = response.getJSONArray("data");
                                    if (jsonArray.length() != 0) {

                                        String product_serial_number = jsonArray.getJSONObject(0).getString("SM_SERIAL_NO");
                                        tv_product_serial_number.setText(product_serial_number);
                                        String product_batch_number = jsonArray.getJSONObject(0).getString("SM_BATCH_CODE");
                                        tv_product_batch_number.setText(product_batch_number);
                                        String product_name = jsonArray.getJSONObject(0).getString("SM_CTI_ITEM_NAME");
                                        tv_product_name.setText(product_name);
                                        String product_ref_id = jsonArray.getJSONObject(0).getString("SM_CM_REF_NO");
                                        tv_product_ref_no.setText(product_ref_id);
                                        String product_doc_id = jsonArray.getJSONObject(0).getString("SM_DOC_NO");
                                        tv_doc_id.setText("#"+product_doc_id);
                                        productDocId = product_doc_id;
                                        String product_code = jsonArray.getJSONObject(0).getString("SM_CTI_ITEM_CODE");
                                        tv_product_code.setText(product_code);

                                        // list added spare parts
                                        listAddedSpareParts();
                                        listAddedSparePartsHold();
                                        // fetch spare parts list
                                        getAllSpareParts(product_code);

                                        String customer_name = jsonArray.getJSONObject(0).getString("SM_CM_CUST_NAME");
                                        tv_customer_name.setText(customer_name);
                                        String customer_code = jsonArray.getJSONObject(0).getString("SM_CM_CUST_CODE");
                                        tv_customer_code.setText(customer_code);

                                        if (jsonArray.getJSONObject(0).getString("SM_REMARKS")!=null)
                                            tv_product_complaint.setText(jsonArray.getJSONObject(0).getString("SM_REMARKS"));

                                        if (!jsonArray.getJSONObject(0).getString("SM_DET_COMP").equalsIgnoreCase("null"))
                                            ed_find_problems.setText(jsonArray.getJSONObject(0).getString("SM_DET_COMP"));

                                        if (jsonArray.getJSONObject(0).getString("SM_CR_DT") != null){
                                            String product_service_registered_date = utils.parseServerDateTime(jsonArray.getJSONObject(0).getString("SM_CR_DT"));
                                            tv_product_service_registered.setText(product_service_registered_date);
                                        }
                                        if (jsonArray.getJSONObject(0).getString("SM_CM_IN_DT") != null){
                                            String product_received_date = utils.parseServerDateTime(jsonArray.getJSONObject(0).getString("SM_CM_IN_DT"));
                                            tv_product_received.setText(product_received_date);
                                        }
                                        String estDate = jsonArray.getJSONObject(0).getString("SM_ESTIM_DT");
                                        if (estDate == null || estDate.equalsIgnoreCase("null")){
                                            dateFlag = false;
                                        }else {
                                            String product_estimate_date = utils.parseServerDateTime(estDate);
                                            tv_estimate_date.setText(product_estimate_date);
                                            dateFlag = true;
                                        }
                                        if (jsonArray.getJSONObject(0).getString("SM_STRT_DT") != null){
                                            String product_start_date = utils.parseServerDateTime(jsonArray.getJSONObject(0).getString("SM_STRT_DT"));
                                            tv_product_service_started.setText(product_start_date);
                                        }
                                    }
                                }else {
                                    mainLayout.setVisibility(View.GONE);
                                    dateFlag = true;
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

    private void checkTechnicianStatus() {
        try {
            String condition = "SELECT * FROM SERVICE_MODULE_VIEW WHERE SM_STS_CODE = 'SERVSRT' AND" +
                    " SM_SRP_SYS_ID ="+sp.getString(Constant.USER_USERID,"");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("query",condition);

            AndroidNetworking.post(Constant.BASE_URL + "GetData")
                    .addHeaders("Authorization", authorization)
                    .addJSONObjectBody(jsonObject)
                    .setTag(this)
                    .setPriority(Priority.LOW)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("Response1::",response.toString());

                            try {
                                if (!response.getBoolean("status")) {

                                    releaseToSatart();
                                }else {
                                    customToast("Please close the service you have already started. And try again");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onError(ANError anError) {
//                                    showError(anError);
                        }
                    });

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void editBatchSerialNo() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(StartServiceActivity.this);
        alertDialogBuilder.setCancelable(false);
        LayoutInflater layoutInflater = LayoutInflater.from(StartServiceActivity.this);
        View popupInputDialogView = layoutInflater.inflate(R.layout.row_edit_serial_batch_number, null);
        alertDialogBuilder.setView(popupInputDialogView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        Button bt_update = popupInputDialogView.findViewById(R.id.bt_update);
        Button btn_cancel = popupInputDialogView.findViewById(R.id.bt_cancel);
        TextInputEditText edt_serial_num = popupInputDialogView.findViewById(R.id.edt_serial_num);
        TextInputEditText edt_batch_number = popupInputDialogView.findViewById(R.id.edt_batch_number);

        edt_serial_num.setText(tv_product_serial_number.getText().toString());
        edt_batch_number.setText(tv_product_batch_number.getText().toString());

        btn_cancel.setOnClickListener(v -> {
            alertDialog.cancel();
        });
        bt_update.setOnClickListener(v -> {
            try {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }catch (Exception e){
                e.printStackTrace();
            }
            String serial_no = edt_serial_num.getText().toString();
            String batch_no = edt_batch_number.getText().toString();
            alertDialog.cancel();
            updateBatchSerialNo(serial_no,batch_no);
        });
    }

    private void updateBatchSerialNo(String serial_no, String batch_no) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("SM_SERIAL_NO",serial_no);
            jsonObject.put("SM_BATCH_CODE",batch_no);
            Log.e("body::",jsonObject.toString());

            AndroidNetworking.put(Constant.BASE_URL + Constant.SERVICE_PRODUCT_INFO + "/" +
                    productDocId)
                    .addHeaders("Authorization", authorization)
                    .addJSONObjectBody(jsonObject)
                    .setTag(this)
                    .setPriority(Priority.LOW)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("Response2::",response.toString());

                            try {
                                if (response.getBoolean("status")) {
                                    tv_product_batch_number.setText(batch_no);
                                    tv_product_serial_number.setText(serial_no);
                                }else {
                                    customToast(response.getString("message"));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onError(ANError anError) {
//                                    showError(anError);
                        }
                    });

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void releaseToSatart() {
        if (!pref.getTechnicianProductStatus().equalsIgnoreCase("start")){
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("SM_STS_CODE","SERVSRT");
                jsonObject.put("SM_STS_SYS_ID","6");
                Log.e("body::",jsonObject.toString());

                AndroidNetworking.put(Constant.BASE_URL + Constant.SERVICE_PRODUCT_INFO + "/" +
                        productDocId)
                        .addHeaders("Authorization", authorization)
                        .addJSONObjectBody(jsonObject)
                        .setTag(this)
                        .setPriority(Priority.LOW)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.e("Response::",response.toString());

                                try {
                                    if (response.getBoolean("status")) {
                                        lay_bt1.setVisibility(View.VISIBLE);
                                        lay_bt2.setVisibility(View.GONE);
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
    }

    private void removeProduct() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("SM_STS_CODE","PENSERV");
            jsonObject.put("SM_STS_SYS_ID","2");
            jsonObject.put("SM_SRP_SYS_ID","");

            AndroidNetworking.put(Constant.BASE_URL + Constant.SERVICE_PRODUCT_INFO + "/" +
                    getIntent().getStringExtra("product_doc_id"))
                    .addHeaders("Authorization", authorization)
                    .addJSONObjectBody(jsonObject)
                    .setTag(this)
                    .setPriority(Priority.LOW)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("Response::",response.toString());

                            try {
                                if (response.getBoolean("status")) {
                                    dateFlag = true;
                                    onBackPressed();
                                }else {
                                    customToast(response.getString("message"));
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

    private void updateTechnicianRemarks() {
        String technician_remarks = ed_find_problems.getText().toString().trim();
        if (!technician_remarks.isEmpty()){
            try {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("SM_DET_COMP",technician_remarks);

                Log.e("product id == ",productDocId+"");

                AndroidNetworking.put(Constant.BASE_URL + Constant.SERVICE_PRODUCT_INFO + "/" +
                        productDocId)
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
            jsonObject.put("SM_STS_CODE","PENDLV");
            jsonObject.put("SM_STS_SYS_ID","4");

            AndroidNetworking.put(Constant.BASE_URL + Constant.SERVICE_PRODUCT_INFO + "/" +
                    productDocId)
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

                                mainLayout.setVisibility(View.GONE);
                                pref.setTechnicianProductName("");
                                customToast("Completed");

                                Intent intent = new Intent(StartServiceActivity.this,TechnicianHomeActivity.class);
                                startActivity(intent);
                                finish();

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
                Log.e("jsonbody:",jsonObject.toString());

                AndroidNetworking.put(Constant.BASE_URL + Constant.SERVICE_PRODUCT_INFO + "/" +
                        productDocId)
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
                                    dateFlag = true;

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

        String pause_reason = auto_reasons.getText().toString().trim();
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("SM_STS_CODE","SERVPUS");
            jsonObject.put("SM_STS_SYS_ID","7");
            jsonObject.put("SM_DLY_RSN",pause_reason);

            AndroidNetworking.put(Constant.BASE_URL + Constant.SERVICE_PRODUCT_INFO + "/" +
                    productDocId)
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

                        mainLayout.setVisibility(View.GONE);
                        customToast("paused");
                        pref.setTechnicianProductName("");


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
        if (dateFlag){
            super.onBackPressed();
            Intent intent = new Intent(getApplicationContext(), TechnicianHomeActivity.class);
            startActivity(intent);
            finish();
        }else {
            customToast("Please update estimate date and try again");
        }
    }

}