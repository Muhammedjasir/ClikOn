package com.tids.clikonservice.activity.technician;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.tids.clikonservice.R;
import com.tids.clikonservice.Utils.Constant;
import com.tids.clikonservice.adapter.technician.TechnicianScannedProductAdapter;
import com.tids.clikonservice.model.ScannedProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReceivedProductActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ReceivedProductActivity.class.getSimpleName();
    private ImageView ivBack, iv_scanner, iv_serial_num_add;
    private TextInputEditText edt_serial_num;
    private RecyclerView rv_scanned_products;

    private SharedPreferences sp;

    private TechnicianScannedProductAdapter scannedProductAdapter;
    private ArrayList<ScannedProductModel> scannedProductModelArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_product);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        iv_scanner = findViewById(R.id.iv_scanner);
        iv_serial_num_add = findViewById(R.id.iv_serial_num_add);
        edt_serial_num = findViewById(R.id.edt_serial_num);
        rv_scanned_products = findViewById(R.id.rv_scanned_products);

        ivBack = findViewById(R.id.back_btn);
        ivBack.setOnClickListener(v -> onBackPressed());

        scannedProductAdapter = new TechnicianScannedProductAdapter(ReceivedProductActivity.this,
                scannedProductModelArrayList);
        rv_scanned_products.setLayoutManager(new LinearLayoutManager(ReceivedProductActivity.this,
                LinearLayoutManager.VERTICAL,false));
        rv_scanned_products.setAdapter(scannedProductAdapter);
        loadProducts();

        iv_scanner.setOnClickListener(this);
        iv_serial_num_add.setOnClickListener(this);
    }

    private void loadProducts() {
        try {
            String authorization = "Bearer " + sp.getString(Constant.USER_AUTHORIZATION, "");
            String page_pagination = "/1/25";

            AndroidNetworking.get(Constant.BASE_URL + Constant.SERVICE_PRODUCTS + page_pagination)
                    .addQueryParameter("table_name", Constant.SERVICE_PRODUCTS)
                    .addQueryParameter("condition", "SM_STS_CODE='PENSERV'")
                    .addHeaders("Authorization", authorization)
                    .setTag(this)
                    .setPriority(Priority.LOW)
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
                                            String productReferId = jsonArray.getJSONObject(i).getString("SM_CM_REF_NO");
                                            String productCode = jsonArray.getJSONObject(i).getString("SM_CTI_ITEM_CODE");
                                            String customerName = jsonArray.getJSONObject(i).getString("SM_CM_CUST_NAME");
                                            String customerCode = jsonArray.getJSONObject(i).getString("SM_CM_CUST_CODE");

                                            ScannedProductModel scannedProductModel = new ScannedProductModel(product_doc_id, product_qrcode_data, product_name,
                                                    product_serial_number, product_batch_number, product_complaint,productReferId,productCode,customerName,customerCode);
                                            scannedProductModelArrayList.add(scannedProductModel);
                                        }
                                        scannedProductAdapter.notifyDataSetChanged();
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
        if (v == iv_scanner) {
            // we need to create the object
            // of IntentIntegrator class
            // which is the class of QR library
            try {
                IntentIntegrator intentIntegrator = new IntentIntegrator(this);
                intentIntegrator.setPrompt("Scan QR Code");
                intentIntegrator.setOrientationLocked(true);

                intentIntegrator.setCameraId(0);
                intentIntegrator.setCaptureActivity(CaptureActivity.class);
                intentIntegrator.initiateScan();
            } catch (Exception ex) {
                Log.e("Error:::", ex.toString());
            }
        }
        if (v == iv_serial_num_add) {

            String scanned_data = edt_serial_num.getText().toString().trim();
            if (!scanned_data.isEmpty() || !scanned_data.equalsIgnoreCase("")) {
                getProductDetails(scanned_data);
            } else {
                customToast("Enter code");
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                customToast("Cancelled");
            } else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                String scanned_data = intentResult.getContents();

//                char[] split = scanned_data.toCharArray();
//                txtotp1.setText(String.valueOf(split[0]));

                getProductDetails(scanned_data);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void getProductDetails(String scannedData) {
        try {

            String authorization = "Bearer " + sp.getString(Constant.USER_AUTHORIZATION, "");
            String condition = "SELECT * FROM SERVICE_MODULE_VIEW WHERE (SM_CTI_SYS_ID LIKE '%"+
                    scannedData+"%' OR SM_CM_REF_NO LIKE '%"+scannedData+"%' OR SM_DOC_NO LIKE '%"+scannedData+"%' " +
                    "OR SM_CTI_ITEM_CODE LIKE '%"+scannedData+"%') AND SM_STS_CODE = 'PENSERV'";

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
                            try {
                                Log.e("rsp:", response.toString());

                                edt_serial_num.setText("");

                                if (response.getBoolean("status")) {

                                    scannedProductModelArrayList.clear();
                                    scannedProductAdapter.notifyDataSetChanged();

                                    //Get the instance of JSONArray that contains JSONObjects
                                    JSONArray jsonArray = response.getJSONArray("data");
                                    if (jsonArray.length() != 0) {

                                        for (int i=0;i<jsonArray.length();++i){
                                            String product_doc_id = String.valueOf(jsonArray.getJSONObject(i).getInt("SM_DOC_NO"));
                                            String product_name = jsonArray.getJSONObject(i).getString("SM_CTI_ITEM_NAME");
                                            String product_serial_number = jsonArray.getJSONObject(i).getString("SM_SERIAL_NO");
                                            String product_batch_number = jsonArray.getJSONObject(i).getString("SM_BATCH_CODE");
                                            String product_complaint = jsonArray.getJSONObject(i).getString("SM_REMARKS");
                                            String product_qrcode_data = jsonArray.getJSONObject(i).getString("SM_CTI_SYS_ID");
                                            String productReferId = jsonArray.getJSONObject(i).getString("SM_CM_REF_NO");
                                            String productCode = jsonArray.getJSONObject(i).getString("SM_CTI_ITEM_CODE");
                                            String customerName = jsonArray.getJSONObject(i).getString("SM_CM_CUST_NAME");
                                            String customerCode = jsonArray.getJSONObject(i).getString("SM_CM_CUST_CODE");

                                            ScannedProductModel scannedProductModel = new ScannedProductModel(product_doc_id, product_qrcode_data, product_name,
                                                    product_serial_number, product_batch_number, product_complaint,productReferId,productCode,customerName,customerCode);
                                            scannedProductModelArrayList.add(scannedProductModel);
                                        }
                                        scannedProductAdapter.notifyDataSetChanged();
                                    }
                                } else {
                                    customToast("No product available");
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showError(ANError anError) {
        Toast.makeText(ReceivedProductActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
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