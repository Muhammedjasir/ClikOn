package com.tids.clikonservice.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.snackbar.Snackbar;
import com.tids.clikonservice.R;
import com.tids.clikonservice.Utils.Constant;
import com.tids.clikonservice.adapter.technician.TechnicianCompletedProductAdapter;
import com.tids.clikonservice.model.ScannedProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchProductActivity extends AppCompatActivity {

    private RecyclerView rv_CompletedProducts;

    private SharedPreferences sp;
    private TechnicianCompletedProductAdapter technicianCompletedProductAdapter;
    private ArrayList<ScannedProductModel> scannedProductModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        ImageView ivBack = findViewById(R.id.back_btn);
        ivBack.setOnClickListener(v -> onBackPressed());

        rv_CompletedProducts = findViewById(R.id.recycler_view);

        technicianCompletedProductAdapter = new TechnicianCompletedProductAdapter(SearchProductActivity.this,
                scannedProductModelArrayList);
        rv_CompletedProducts.setLayoutManager(new LinearLayoutManager(SearchProductActivity.this,
                LinearLayoutManager.VERTICAL,false));
        rv_CompletedProducts.setAdapter(technicianCompletedProductAdapter);
        loadProducts();
    }

    private void loadProducts() {
        try {
            String authorization = "Bearer " + sp.getString(Constant.USER_AUTHORIZATION, "");
            String condition = "SELECT * FROM SERVICE_MODULE_VIEW WHERE SM_STS_CODE='SERVFIN' AND SM_SRP_SYS_ID="+
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
                                    if (jsonArray.length() != 0) {
                                        for (int i = 0; i< jsonArray.length(); i++){
                                            String product_doc_id = String.valueOf(jsonArray.getJSONObject(i).getInt("SM_DOC_NO"));
                                            String product_name = jsonArray.getJSONObject(i).getString("SM_CTI_ITEM_NAME");
                                            String product_serial_number = jsonArray.getJSONObject(i).getString("SM_SERIAL_NO");
                                            String product_batch_number = jsonArray.getJSONObject(i).getString("SM_BATCH_CODE");
                                            String product_complaint = jsonArray.getJSONObject(i).getString("SM_REMARKS");
                                            String product_qrcode_data = jsonArray.getJSONObject(i).getString("SM_CTI_SYS_ID");

                                            ScannedProductModel scannedProductModel = new ScannedProductModel(product_doc_id, product_qrcode_data, product_name,
                                                    product_serial_number, product_batch_number, product_complaint);
                                            scannedProductModelArrayList.add(scannedProductModel);
                                        }
                                        technicianCompletedProductAdapter.notifyDataSetChanged();
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
        Toast.makeText(SearchProductActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
        Log.e("Error :: ", anError.getErrorBody());
    }

    private void customToast(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).setBackgroundTint(getResources().getColor(R.color.colorPrimary)).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(getApplicationContext(), TechnicianHomeActivity.class);
        startActivity(intent);
    }

}