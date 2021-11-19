package com.tids.clikonservice.activity.driver;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.tids.clikonservice.R;
import com.tids.clikonservice.Utils.Constant;
import com.tids.clikonservice.Utils.Helper.PrefManager;
import com.tids.clikonservice.Utils.RoomDb.ClikonModel;
import com.tids.clikonservice.Utils.RoomDb.ClikonViewModel;
import com.tids.clikonservice.adapter.SpinnerSearchAdapter;
import com.tids.clikonservice.adapter.merchant.AddProductAdapter;
import com.tids.clikonservice.model.ResponseModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ProductRegistrationActivity extends AppCompatActivity implements View.OnClickListener,
        AddProductAdapter.ItemClickListener, CompoundButton.OnCheckedChangeListener {

    private PrefManager pref;
    private SharedPreferences sp;

    private ClikonViewModel clikonViewModel;
    private ClikonModel selectAdapterModel;

    private String authorization="", MERCHANT_ID="",productCode="",productName="",merchant_name ="",
            merchant_code="",areaCode="",areaName="",merchantAreaCode="",merchantAreaName="";
    int add_product_id = -1;
    private String OT_COLLECTION_MODULE_DOC_NO="", OT_CLCTN_ITEMS_DOC_NO="",OT_DVR_REQ_ALLCTN_SYS_ID="";

    private ArrayList<ResponseModel> searchProductsArrayList;
    private SpinnerSearchAdapter searchProductsAdapter;

    private ArrayList<ResponseModel> searchMerchantArrayList;
    private SpinnerSearchAdapter searchMerchantAdapter;

    private ArrayList<ResponseModel> searchAreaArrayList;
    private SpinnerSearchAdapter searchAreaAdapter;

    private ArrayList<ResponseModel> searchMerchantAreaArrayList;
    private SpinnerSearchAdapter searchMerchantAreaAdapter;

    private AddProductAdapter addProductAdapter;

    private LinearLayout add_product_lay;
    private RecyclerView rv_products;
    private AutoCompleteTextView act_search_product,act_search_merchant,act_search_area,
            act_search_merchant_area;
    private TextInputEditText edt_serial_num,edt_batch_number,edt_complaint,ed_customer_name,
            ed_customer_contact,ed_customer_email,ed_customer_address,edt_reference_number;
    private AppCompatButton btn_delete,btn_add,btn_register;
    private SwitchMaterial sw_shop_consumer,sw_home_shop;
    private CardView cv_customer_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_registration);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        ImageView ivBack = findViewById(R.id.back_btn);
        ivBack.setOnClickListener(v -> onBackPressed());

        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        pref = new PrefManager(getApplicationContext());
        authorization = "Bearer " + sp.getString(Constant.USER_AUTHORIZATION, "");
        MERCHANT_ID = sp.getString(Constant.USER_USERID,"");
        Log.e("MERCHANT_ID=",MERCHANT_ID);

        edt_reference_number = findViewById(R.id.edt_reference_number);
        add_product_lay = findViewById(R.id.add_product_lay);
        rv_products = findViewById(R.id.rv_products);
        act_search_product = findViewById(R.id.edt_search_product);
        act_search_merchant = findViewById(R.id.edt_search_merchant);
        edt_serial_num = findViewById(R.id.edt_serial_num);
        edt_batch_number = findViewById(R.id.edt_batch_number);
        edt_complaint = findViewById(R.id.edt_complaint);
        btn_delete = findViewById(R.id.btn_delete);
        btn_add = findViewById(R.id.btn_add);
        sw_shop_consumer = findViewById(R.id.sw_shop_consumer);
        cv_customer_details = findViewById(R.id.cv_customer_details);
        sw_home_shop = findViewById(R.id.sw_home_shop);
        ed_customer_name = findViewById(R.id.ed_customer_name);
        ed_customer_contact = findViewById(R.id.ed_customer_contact);
        ed_customer_email = findViewById(R.id.ed_customer_email);
        act_search_area = findViewById(R.id.act_search_area);
        ed_customer_address = findViewById(R.id.ed_customer_address);
        btn_register = findViewById(R.id.btn_register);
        act_search_merchant_area = findViewById(R.id.act_search_merchant_area);

        //configuring recycler view
        rv_products.setLayoutManager(new LinearLayoutManager(ProductRegistrationActivity.this,
                LinearLayoutManager.HORIZONTAL,false));
        rv_products.setHasFixedSize(true);
        // setting adapter in recycler view
        addProductAdapter=new AddProductAdapter();
        rv_products.setAdapter(addProductAdapter);

        //getting access to view model class
        clikonViewModel= ViewModelProviders.of(this).get(ClikonViewModel.class);
        //add observer to for view model getProductlist()(LiveData)
        clikonViewModel.getProductlist().observe(this, new Observer<List<ClikonModel>>() {
            @Override
            public void onChanged(List<ClikonModel> todoModels) {
                //to show list
                addProductAdapter.submitList(todoModels);
            }
        });

        //interface method body of list adapter
        addProductAdapter.setClickListener(this);

        btn_add.setOnClickListener(this);
        btn_delete.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        add_product_lay.setOnClickListener(this);

        sw_shop_consumer.setOnCheckedChangeListener(this);

        // initialize search model
        searchProductsArrayList = new ArrayList<>();
        act_search_product.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listProducts(s);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        // initialize search model
        searchAreaArrayList = new ArrayList<>();
        act_search_area.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadAreas(s);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });


        // initialize search model
        searchMerchantArrayList = new ArrayList<>();
        act_search_merchant.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listMerchant(s);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        searchMerchantAreaArrayList = new ArrayList<>();
        act_search_merchant_area.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                listMerchantArea(s);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    private void listMerchantArea(CharSequence area) {
        try {
            String condition = "SELECT ARE_CODE,ARE_NAME FROM OM_AREA WHERE (UPPER(ARE_CODE) LIKE UPPER('%"+
                    area+"%') OR UPPER(ARE_NAME) LIKE UPPER('%"+area+"%')) AND ARE_COU_CODE='UAE'";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("query", condition);

            AndroidNetworking.post(Constant.BASE_URL + "GetData")
                    .addHeaders("Authorization", authorization)
                    .addJSONObjectBody(jsonObject)
                    .setTag(this)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @SuppressLint("ClickableViewAccessibility")
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("area.rsp::",response.toString());

                            try {
                                if (response.getBoolean("status")) {
                                    JSONArray jsonArray = response.getJSONArray("data");

                                    if (jsonArray.length() != 0) {
                                        //clear arraylist
                                        searchMerchantAreaArrayList.clear();

                                        for (int i = 0; i< jsonArray.length(); ++i){
                                            String area_id = "";
                                            String area_code = jsonArray.getJSONObject(i).getString("ARE_CODE");
                                            String area_name = jsonArray.getJSONObject(i).getString("ARE_NAME");

                                            ResponseModel responseModel = new ResponseModel(area_id,area_name,area_code);
                                            searchMerchantAreaArrayList.add(responseModel);
                                        }
                                        searchMerchantAreaAdapter = new SpinnerSearchAdapter(ProductRegistrationActivity.this,
                                                R.layout.row_spinner_adapter, R.id.text1, searchMerchantAreaArrayList);
                                        act_search_merchant_area.setAdapter(searchMerchantAreaAdapter);

                                        act_search_merchant_area.setOnItemClickListener((adapterView, view, i, l) -> {
                                            merchantAreaCode = searchMerchantAreaArrayList.get(i).getCode();
                                            merchantAreaName = searchMerchantAreaArrayList.get(i).getName();
                                            Log.e("area.code&p.name==",merchantAreaCode+"-"+merchantAreaName);
                                        });
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

    private void listMerchant(CharSequence param) {
        try {
            String word = param.toString().trim();
            String condition = "";
            if (act_search_merchant_area.getText().toString().trim().isEmpty() ||
                    merchantAreaCode.equalsIgnoreCase("")){
                merchantAreaCode = "";
                condition = "SELECT  CUST_CODE,CUST_NAME FROM OM_CUSTOMER WHERE (UPPER(CUST_CODE) " +
                        "LIKE UPPER('%"+word+"%') OR UPPER(CUST_NAME) LIKE UPPER('%"+word+"%'))";
            }else {
                condition = "SELECT  CUST_CODE,CUST_NAME FROM OM_CUSTOMER WHERE (UPPER(CUST_CODE) " +
                        "LIKE UPPER('%"+word+"%') OR UPPER(CUST_NAME) LIKE UPPER('%"+word+"%')) " +
                        "AND CUST_ARE_CODE = '"+merchantAreaCode+"'";
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("query", condition);
            Log.e("search::",jsonObject.toString());

            AndroidNetworking.post(Constant.BASE_URL + "GetData")
                    .addHeaders("Authorization", authorization)
                    .addJSONObjectBody(jsonObject)
                    .setTag(this)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @SuppressLint("ClickableViewAccessibility")
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("prd.rsp::",response.toString());

                            try {
                                if (response.getBoolean("status")) {
                                    JSONArray jsonArray = response.getJSONArray("data");

                                    if (jsonArray.length() != 0) {
                                        //clear arraylist
                                        searchMerchantArrayList.clear();

                                        for (int i = 0; i< jsonArray.length(); ++i){
                                            String parts_id = "";
                                            String merchant_code = jsonArray.getJSONObject(i).getString("CUST_CODE");
                                            String merchant_name = jsonArray.getJSONObject(i).getString("CUST_NAME");

                                            ResponseModel responseModel = new ResponseModel(parts_id,merchant_name,merchant_code);
                                            searchMerchantArrayList.add(responseModel);
                                        }
                                        searchMerchantAdapter = new SpinnerSearchAdapter(ProductRegistrationActivity.this,
                                                R.layout.row_spinner_adapter, R.id.text1, searchMerchantArrayList);
                                        act_search_merchant.setAdapter(searchMerchantAdapter);

                                        act_search_merchant.setOnItemClickListener((adapterView, view, i, l) -> {
                                            merchant_name = searchMerchantArrayList.get(i).getName();
                                            merchant_code = searchMerchantArrayList.get(i).getCode();
                                            Log.e("p.code&p.name==",merchant_code+"-"+merchant_name);
                                        });
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

    @Override
    public void onEditItem(ClikonModel todoModel) {

        btn_delete.setVisibility(View.VISIBLE);

        add_product_id = todoModel.getId();
        productCode = todoModel.getProduct_code();
        productName= todoModel.getProduct_name();
        String serial_no = todoModel.getSerial_no();
        String batch_number = todoModel.getBatch_no();
        String complaint = todoModel.getComplaint();

        edt_serial_num.setText(serial_no);
        edt_batch_number.setText(batch_number);
        edt_complaint.setText(complaint);
        act_search_product.setText(productName+" - "+productCode);

        selectAdapterModel = todoModel;

    }

    private void listProducts(CharSequence param) {
        try {
            String word = param.toString().trim();
            String condition = "SELECT  ITEM_CODE,ITEM_NAME FROM OM_ITEM WHERE (UPPER(ITEM_CODE) LIKE UPPER('%"+
                    word+"%') OR UPPER(ITEM_NAME) LIKE UPPER('%"+word+"%'))";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("query", condition);

            AndroidNetworking.post(Constant.BASE_URL + "GetData")
                    .addHeaders("Authorization", authorization)
                    .addJSONObjectBody(jsonObject)
                    .setTag(this)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @SuppressLint("ClickableViewAccessibility")
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("prd.rsp::",response.toString());

                            try {
                                if (response.getBoolean("status")) {
                                    JSONArray jsonArray = response.getJSONArray("data");

                                    if (jsonArray.length() != 0) {
                                        //clear arraylist
                                        searchProductsArrayList.clear();

                                        for (int i = 0; i< jsonArray.length(); ++i){
                                            String parts_id = "";
                                            String parts_code = jsonArray.getJSONObject(i).getString("ITEM_CODE");
                                            String parts_name = jsonArray.getJSONObject(i).getString("ITEM_NAME");

                                            ResponseModel responseModel = new ResponseModel(parts_id,parts_name,parts_code);
                                            searchProductsArrayList.add(responseModel);
                                        }
                                        searchProductsAdapter = new SpinnerSearchAdapter(ProductRegistrationActivity.this,
                                                R.layout.row_spinner_adapter, R.id.text1, searchProductsArrayList);
                                        act_search_product.setAdapter(searchProductsAdapter);

                                        act_search_product.setOnItemClickListener((adapterView, view, i, l) -> {
                                            productCode = searchProductsArrayList.get(i).getCode();
                                            productName = searchProductsArrayList.get(i).getName();
                                            Log.e("p.code&p.name==",productCode+"-"+productName);
                                        });
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

    private void loadAreas(CharSequence area){
        try {
            String condition = "SELECT ARE_CODE,ARE_NAME FROM OM_AREA WHERE (UPPER(ARE_CODE) LIKE UPPER('%"+
                    area+"%') OR UPPER(ARE_NAME) LIKE UPPER('%"+area+"%')) AND ARE_COU_CODE='UAE'";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("query", condition);

            AndroidNetworking.post(Constant.BASE_URL + "GetData")
                    .addHeaders("Authorization", authorization)
                    .addJSONObjectBody(jsonObject)
                    .setTag(this)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @SuppressLint("ClickableViewAccessibility")
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("area.rsp::",response.toString());

                            try {
                                if (response.getBoolean("status")) {
                                    JSONArray jsonArray = response.getJSONArray("data");

                                    if (jsonArray.length() != 0) {
                                        //clear arraylist
                                        searchAreaArrayList.clear();

                                        for (int i = 0; i< jsonArray.length(); ++i){
                                            String area_id = "";
                                            String area_code = jsonArray.getJSONObject(i).getString("ARE_CODE");
                                            String area_name = jsonArray.getJSONObject(i).getString("ARE_NAME");

                                            ResponseModel responseModel = new ResponseModel(area_id,area_name,area_code);
                                            searchAreaArrayList.add(responseModel);
                                        }
                                        searchAreaAdapter = new SpinnerSearchAdapter(ProductRegistrationActivity.this,
                                                R.layout.row_spinner_adapter, R.id.text1, searchAreaArrayList);
                                        act_search_area.setAdapter(searchAreaAdapter);

                                        act_search_area.setOnItemClickListener((adapterView, view, i, l) -> {
                                            areaCode = searchAreaArrayList.get(i).getCode();
                                            areaName = searchAreaArrayList.get(i).getName();
                                            Log.e("area.code&p.name==",areaCode+"-"+areaName);
                                        });
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

    @Override
    public void onClick(View v) {
        if (v == btn_add){
            addProduct();
        }
        if (v == btn_delete){
            deleteSelectedProduct();
        }
        if (v == btn_register){
            // Hide keyboard after button click
            try {
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {
                // TODO: handle exception
            }
            checkProducts();
        }
        if (v == add_product_lay){
            loadNewProductTab();
        }
    }

    private void checkProducts() {
        //add observer to for view model getProductCount()(LiveData)
        // check roomdb product table count
        clikonViewModel.getProductCount().observe(ProductRegistrationActivity.this, integer -> {
            int count = integer;
            if (count != 0){
                if (merchant_code.equalsIgnoreCase("")){
                    customToast("Please select the merchant");
                }else {
                    if (OT_COLLECTION_MODULE_DOC_NO.equalsIgnoreCase("")){
                        String ref_no = edt_reference_number.getText().toString().trim();
                        getDocNumber(ref_no);
                    }else {
                        getENteredData();
                    }
                }
            }
        });
    }

    private void getDocNumber(String ref_no) {
        try {
            String myFormat = "dd/MMM/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
            String todaydate = sdf.format(Calendar.getInstance().getTime());

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("CM_DOC_DT",todaydate);
            jsonObject.put("CM_IN_DT",todaydate);
            jsonObject.put("CM_REF_NO",ref_no);
            jsonObject.put("CM_CUST_CODE",merchant_code);
            jsonObject.put("CM_AREA",merchantAreaCode);
            Log.e("body::",jsonObject.toString());

            AndroidNetworking.post(Constant.BASE_URL + Constant.OT_COLLECTION_MODULE)
                    .addHeaders("Authorization", authorization)
                    .addJSONObjectBody(jsonObject)
                    .setTag(this)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("docno.Response::",response.toString());

                            try {
                                if (response.getBoolean("status")) {
                                    OT_COLLECTION_MODULE_DOC_NO = String.valueOf(response.getInt("data"));
                                    getENteredData();
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView == sw_shop_consumer){
            if (isChecked){
                cv_customer_details.setVisibility(View.VISIBLE);
            }else {
                cv_customer_details.setVisibility(View.GONE);
            }
        }
    }

    private void loadNewProductTab() {
        btn_delete.setVisibility(View.INVISIBLE);
        add_product_id = -1;
        productName = "";
        productCode = "";
        act_search_product.setText("");
        edt_serial_num.setText("");
        edt_batch_number.setText("");
        edt_complaint.setText("");
    }

    private void deleteSelectedProduct() {
        //Alert Dialog for deleting all todo
        new AlertDialog.Builder(ProductRegistrationActivity.this)
                .setTitle("Delete")
                .setMessage("Are you sure you want to delete?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // to delete list
                        clikonViewModel.delete(selectAdapterModel);
                        Log.d("response", "Deleted Item"+selectAdapterModel.toString()+"");
                        customToast("Product deleted");
                        // clear cardview and assigned values
                        loadNewProductTab();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        })
                .setCancelable(true)
                .show();
    }

    private void deleteAllProducts() {
        clikonViewModel.deleteAll();
    }

    private void addProduct() {
        String serial_no = edt_serial_num.getText().toString().trim();
        String batch_no = edt_batch_number.getText().toString().trim();
        String compaint = edt_complaint.getText().toString().trim();

        if (productCode.trim().isEmpty()){
            customToast("Please select a product");
        }else {
            ClikonModel clikonModel = new ClikonModel(productCode, productName, serial_no, batch_no, compaint);
            clikonViewModel.insert(clikonModel);
            Toast.makeText(this, "Product added", Toast.LENGTH_SHORT).show();
            loadNewProductTab();
        }
    }

    private void getENteredData() {
        String unit = "";
        if (sw_shop_consumer.isChecked()){
            unit = "CONSUMER";
            String cust_name = ed_customer_name.getText().toString().trim();
            String cust_number = ed_customer_contact.getText().toString().trim();
            String cust_email = ed_customer_email.getText().toString().trim();
            String cust_address = ed_customer_address.getText().toString().trim();
            String cust_homeShop = "";
            if (sw_home_shop.isChecked()){
                cust_homeShop = "HMSRVC";
            }else {
                cust_homeShop = "SHPSRVC";
            }
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("CTI_CM_DOC_NO", OT_COLLECTION_MODULE_DOC_NO);
                jsonObject.put("CTI_MERCHT_ID", MERCHANT_ID);
                jsonObject.put("CTI_SHP_CONS_UNIT", unit);
                jsonObject.put("CTI_CONS_SRVC", cust_homeShop);
                jsonObject.put("CTI_CUSTOMER_NAME", cust_name);
                jsonObject.put("CTI_CUSTOMER_MOBILE", cust_number);
                jsonObject.put("CTI_CUSTOMER_EMAIL", cust_email);
                jsonObject.put("CTI_AREA_CODE", areaName);
                jsonObject.put("CTI_CNSMR_ADDRSS", cust_address);
                //rigister data
                register(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            unit = "SHOP";
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("CTI_CM_DOC_NO", OT_COLLECTION_MODULE_DOC_NO);
                jsonObject.put("CTI_MERCHT_ID", MERCHANT_ID);
                jsonObject.put("CTI_SHP_CONS_UNIT", unit);
                //rigister data
                register(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void register(JSONObject jsonObject) {
        //add observer to for view model getTodolist()(LiveData)
        // get all product from roomdb product table
        clikonViewModel.getProductlist().observe(ProductRegistrationActivity.this, new Observer<List<ClikonModel>>() {
            @Override
            public void onChanged(List<ClikonModel> clikonModels) {
                // check list size is not zero
                if (clikonModels.size() != 0){
                    for (int i=0;i<clikonModels.size();i++){
                        try {
                            jsonObject.put("CTI_ITEM_CODE", clikonModels.get(i).getProduct_code());
                            jsonObject.put("CTI_SERIAL_NO", clikonModels.get(i).getSerial_no());
                            jsonObject.put("CTI_BATCH", clikonModels.get(i).getBatch_no());
                            jsonObject.put("CTI_REPORTED_COMP", clikonModels.get(i).getComplaint());
                            jsonObject.put("CTI_QTY", "1");
                            jsonObject.put("CTI_STS_CODE", "DRDASN");
                            jsonObject.put("CTI_STS_SYS_ID", "11");
                            Log.e("reg.ip::",jsonObject.toString());
                            int finalI = i;
                            AndroidNetworking.post(Constant.BASE_URL + Constant.OT_CLCTN_ITEMS)
                                    .addHeaders("Authorization", authorization)
                                    .addJSONObjectBody(jsonObject)
                                    .setTag(this)
                                    .setPriority(Priority.MEDIUM)
                                    .build()
                                    .getAsJSONObject(new JSONObjectRequestListener() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.e("opno.Response::",response.toString());
                                            try {
                                                if (response.getBoolean("status")) {
                                                    if (finalI == (clikonModels.size()-1)){
                                                        deleteAllProducts();
                                                        customToast(response.getString("message"));

                                                        int sys_id = response.getInt("data");
                                                        getDocNumberSysid(sys_id);
//                                                        onBackPressed();
                                                    }
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

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private void getDocNumberSysid(int sys_id) {

        String myFormat = "dd/MMM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        String todaydate = sdf.format(Calendar.getInstance().getTime());
        Log.e("dt-tm::", todaydate);

        try {
            String condition = "SELECT CTI_CM_DOC_NO FROM OT_CLCTN_ITEMS WHERE CTI_SYS_ID = "+sys_id;
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("query", condition);

            AndroidNetworking.post(Constant.BASE_URL + "GetData")
                    .addHeaders("Authorization", authorization)
                    .addJSONObjectBody(jsonObject1)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("DOCNO.rsp::",response.toString());
                            try {
                                if (response.getBoolean("status")) {
                                    JSONArray jsonArray = response.getJSONArray("data");
                                    OT_CLCTN_ITEMS_DOC_NO = String.valueOf(jsonArray.getJSONObject(0).getInt("CTI_CM_DOC_NO"));
                                    Log.e("OT_CLCTN_ITEMS_DOC_NO==",OT_CLCTN_ITEMS_DOC_NO);
                                    update_OT_DVR_CLCTN();
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

            JSONObject jsonObject2 = new JSONObject();
            jsonObject2.put("DV_DVR_CODE", MERCHANT_ID);
            jsonObject2.put("DV_DATE", todaydate);
            jsonObject2.put("DV_CRUSR", MERCHANT_ID);
            jsonObject2.put("DV_PK_DLV", "Pickup");

            AndroidNetworking.post(Constant.BASE_URL + Constant.OT_DVR_REQ_ALLCTN)
                    .addHeaders("Authorization", authorization)
                    .addJSONObjectBody(jsonObject2)
                    .setTag(this)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("driverId.Response::",response.toString());
                            try {
                                if (response.getBoolean("status")) {
                                    OT_DVR_REQ_ALLCTN_SYS_ID = String.valueOf(response.getInt("data"));
                                    Log.e("OT_DVR_REQ_ALLCTN_SYS_ID==",OT_DVR_REQ_ALLCTN_SYS_ID);
                                    update_OT_DVR_CLCTN();
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

    private void update_OT_DVR_CLCTN() {
        if (!OT_CLCTN_ITEMS_DOC_NO.equalsIgnoreCase("") ||
                !OT_DVR_REQ_ALLCTN_SYS_ID.equalsIgnoreCase("")){
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("DVR_DV_SYS_ID", OT_DVR_REQ_ALLCTN_SYS_ID);
                jsonObject.put("DVR_CLCN_DOCNO", OT_CLCTN_ITEMS_DOC_NO);
                jsonObject.put("DVR_CRUSR", MERCHANT_ID);
                jsonObject.put("DVR_CUST_CODE", merchant_name);
                jsonObject.put("DVR_AREA_CODE", merchantAreaCode);

                AndroidNetworking.post(Constant.BASE_URL + Constant.OT_DVR_CLCTN)
                        .addHeaders("Authorization", authorization)
                        .addJSONObjectBody(jsonObject)
                        .setTag(this)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.e("driverId.Response::",response.toString());
                                try {
                                    if (response.getBoolean("status")) {
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
    }

    private void showError(ANError anError) {
        Toast.makeText(ProductRegistrationActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
        Log.e("Error :: ", anError.getErrorBody());
    }

    private void customToast(String message){
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).setBackgroundTint(getResources().getColor(R.color.colorPrimary)).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProductRegistrationActivity.this,DriversHomeActivity.class);
        startActivity(intent);
        finish();
    }
}