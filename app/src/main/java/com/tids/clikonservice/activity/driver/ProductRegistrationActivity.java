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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
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
        AddProductAdapter.ItemClickListener, CompoundButton.OnCheckedChangeListener{

    private PrefManager pref;
    private SharedPreferences sp;
    private ClikonViewModel clikonViewModel;
    private ClikonModel selectAdapterModel;

    private String authorization="", MERCHANT_ID="",productCode="",productName="",merchant_name ="",
            merchant_code="";
    int add_product_id = -1;

    private ArrayList<ResponseModel> searchProductsArrayList;
    private SpinnerSearchAdapter searchProductsAdapter;

    private ArrayList<ResponseModel> searchMerchantArrayList;
    private SpinnerSearchAdapter searchMerchantAdapter;

    private AddProductAdapter addProductAdapter;

    private LinearLayout add_product_lay;
    private RecyclerView rv_products;
    private AutoCompleteTextView act_search_product,act_search_merchant;
    private TextInputEditText edt_serial_num,edt_batch_number,edt_complaint,ed_customer_name,
            ed_customer_contact,ed_customer_email,ed_customer_pobox,ed_customer_address;
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
        ed_customer_pobox = findViewById(R.id.ed_customer_pobox);
        ed_customer_address = findViewById(R.id.ed_customer_address);
        btn_register = findViewById(R.id.btn_register);

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


    }

    private void listMerchant(CharSequence param) {
        try {
            String word = param.toString().trim();
            String condition = "SELECT  CUST_CODE,CUST_NAME FROM OM_CUSTOMER WHERE (UPPER(CUST_CODE) " +
                    "LIKE UPPER('%"+word+"%') OR UPPER(CUST_NAME) LIKE UPPER('%"+word+"%'))";
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
                                            merchant_name = searchMerchantArrayList.get(i).getCode();
                                            merchant_code = searchMerchantArrayList.get(i).getName();
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

    @Override
    public void onClick(View v) {
        if (v == btn_add){
            addProduct();
        }
        if (v == btn_delete){
            deleteSelectedProduct();
        }
        if (v == btn_register){
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
                    getDocNumber();
                }
            }
        });
    }

    private void getDocNumber() {
        try {
            String myFormat = "dd/MMM/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
            String todaydate = sdf.format(Calendar.getInstance().getTime());

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("CM_DOC_DT",todaydate);
            jsonObject.put("CM_REF_NO",MERCHANT_ID);
            jsonObject.put("CM_CUST_CODE",merchant_code);
            Log.e("body::",jsonObject.toString());

            AndroidNetworking.post(Constant.BASE_URL + "OT_COLLECTION_MODULE")
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

                                    String doc_no = String.valueOf(response.getInt("data"));
                                    getENteredData(doc_no);
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

    private void getENteredData(String docNo) {
        String unit = "";
        if (sw_shop_consumer.isChecked()){
            unit = "CONSUMER";
            String cust_name = ed_customer_name.getText().toString().trim();
            String cust_number = ed_customer_contact.getText().toString().trim();
            String cust_email = ed_customer_email.getText().toString().trim();
            String cust_pobox = ed_customer_pobox.getText().toString().trim();
            String cust_address = ed_customer_address.getText().toString().trim();
            String cust_homeShop = "";
            if (sw_home_shop.isChecked()){
                cust_homeShop = "HMSRVC";
            }else {
                cust_homeShop = "SHPSRVC";
            }
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("CTI_CM_DOC_NO", docNo);
                jsonObject.put("CTI_MERCHT_ID", MERCHANT_ID);
                jsonObject.put("CTI_SHP_CONS_UNIT", unit);
                jsonObject.put("CTI_CONS_SRVC", cust_homeShop);
                jsonObject.put("CTI_CUSTOMER_NAME", cust_name);
                jsonObject.put("CTI_CUSTOMER_MOBILE", cust_number);
                jsonObject.put("CTI_CUSTOMER_EMAIL", cust_email);
                jsonObject.put("CTI_PO_BOX", cust_pobox);
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
                jsonObject.put("CTI_CM_DOC_NO", docNo);
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
                            jsonObject.put("CTI_STS_CODE", "DVRETR");
                            jsonObject.put("CTI_STS_SYS_ID", "8");
                            Log.e("reg.ip::",jsonObject.toString());
                            int finalI = i;
                            AndroidNetworking.post(Constant.BASE_URL + "OT_CLCTN_ITEMS")
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
                                                        onBackPressed();
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
        finish();
    }
}