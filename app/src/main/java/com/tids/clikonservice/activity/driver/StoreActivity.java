package com.tids.clikonservice.activity.driver;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
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
import com.tids.clikonservice.adapter.driver.DriverProductsAdapter;
import com.tids.clikonservice.model.ProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StoreActivity extends AppCompatActivity {

    private ImageView iv_track_location;
    private TextView tv_place,tv_full_address;
    private RecyclerView rv_orders;

    private DriverProductsAdapter driverProductsAdapter;
    private ArrayList<ProductModel> productModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        ImageView ivBack = findViewById(R.id.back_btn);
        ivBack.setOnClickListener(v -> onBackPressed());

        tv_place=findViewById(R.id.tv_place);
        tv_full_address=findViewById(R.id.tv_full_address);
        iv_track_location=findViewById(R.id.iv_track_location);
        rv_orders=findViewById(R.id.rv_orders);

        driverProductsAdapter = new DriverProductsAdapter(StoreActivity.this,
                productModelArrayList);
        rv_orders.setLayoutManager(new LinearLayoutManager(StoreActivity.this,
                LinearLayoutManager.VERTICAL,false));
        rv_orders.setAdapter(driverProductsAdapter);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            String productDocId = extras.getString("consumerShopDocId");
            String place = extras.getString("shopName");
            String full_address = extras.getString("shopAddress");
            String type = extras.getString("type");
            loadProducts(productDocId,type);

            tv_place.setText(place);
            tv_full_address.setText(full_address);
        }else {
            onBackPressed();
        }

//        edtRegistration.setOnClickListener(v -> {
//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(StoreActivity.this);
//            alertDialogBuilder.setCancelable(true);
//            LayoutInflater layoutInflater = LayoutInflater.from(StoreActivity.this);
//            View popupInputDialogView = layoutInflater.inflate(R.layout.row_edit_product_details_dialog_box, null);
//            alertDialogBuilder.setView(popupInputDialogView);
//            final AlertDialog alertDialog = alertDialogBuilder.create();
//            alertDialog.show();
//        });

    }

    private void loadProducts(String productDocId, String type) {
        try {
            JSONObject jsonObject = new JSONObject();
            String condition = "";
            if (type.equalsIgnoreCase("merchant_pickup")){
                 condition = "SELECT COLLECTION.*,ITEM.ITEM_NAME from OT_CLCTN_ITEMS COLLECTION INNER " +
                         "JOIN OM_ITEM ITEM ON COLLECTION.CTI_ITEM_CODE=ITEM.ITEM_CODE WHERE " +
                         "COLLECTION.CTI_CM_DOC_NO= '"+productDocId+"' AND " +
                         "COLLECTION.CTI_STS_CODE = 'DVRETR'  ORDER BY COLLECTION.CTI_SYS_ID DESC";
            }else {
                condition = "SELECT COLLECTION.*,ITEM.ITEM_NAME from OT_CLCTN_ITEMS COLLECTION INNER " +
                        "JOIN OM_ITEM ITEM ON COLLECTION.CTI_ITEM_CODE=ITEM.ITEM_CODE WHERE " +
                        "COLLECTION.CTI_CM_DOC_NO= '"+productDocId+"' AND " +
                        "COLLECTION.CTI_STS_CODE = 'SERVFIN'  ORDER BY COLLECTION.CTI_SYS_ID DESC";
            }
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
                                            String product_date = type;
//                                            if (!product_date.equalsIgnoreCase("null")){
//                                                product_date = utils.parseServerDateTime(product_date);
//                                            }

                                            ProductModel productModel = new ProductModel(id, product_code, product_name, product_date, product_status,
                                                    product_serial_number, product_batch_number);
                                            productModelArrayList.add(productModel);
                                        }
                                        driverProductsAdapter.notifyDataSetChanged();
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
        Toast.makeText(StoreActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
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