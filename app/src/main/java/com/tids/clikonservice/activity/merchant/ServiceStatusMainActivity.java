package com.tids.clikonservice.activity.merchant;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.tids.clikonservice.R;
import com.tids.clikonservice.Utils.Constant;
import com.tids.clikonservice.model.ProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ServiceStatusMainActivity extends AppCompatActivity {

    private String authorization="", MERCHANT_ID="", customer_number ="", product_code="", product_name="";
    private SharedPreferences sp;

    private ImageView ivBack, ivQr, iv_customer_call;
    private TextView tv_product_name,tv_serial_number,tv_batch_number,tv_complaint,tv_cust_name,
            tv_cust_number,tv_cust_email,tv_cust_pobox,tv_cust_address;
    private LinearLayout customer_data_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_status_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        customer_data_layout=findViewById(R.id.customer_data_layout);
        iv_customer_call=findViewById(R.id.iv_customer_call);
        tv_cust_address=findViewById(R.id.tv_cust_address);
        tv_cust_pobox=findViewById(R.id.tv_cust_pobox);
        tv_cust_email=findViewById(R.id.tv_cust_email);
        tv_cust_number=findViewById(R.id.tv_cust_number);
        tv_cust_name=findViewById(R.id.tv_cust_name);
        tv_complaint=findViewById(R.id.tv_complaint);
        tv_batch_number=findViewById(R.id.tv_batch_number);
        tv_serial_number=findViewById(R.id.tv_serial_number);
        tv_product_name=findViewById(R.id.tv_product_name);
        ivQr=findViewById(R.id.btn_qr);
        ivBack=findViewById(R.id.back_btn);
        ivBack.setOnClickListener(v -> onBackPressed());

        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        authorization = "Bearer " + sp.getString(Constant.USER_AUTHORIZATION, "");
        MERCHANT_ID = sp.getString(Constant.USER_USERID,"");

        getProductDetails();

        iv_customer_call.setOnClickListener(v -> {
            if(!customer_number.equalsIgnoreCase("") || customer_number.equalsIgnoreCase("nul")){
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + customer_number));//change the number
                startActivity(callIntent);
            }
        });

        ivQr.setOnClickListener(v -> {
            if (!product_code.equalsIgnoreCase("")){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ServiceStatusMainActivity.this);
                alertDialogBuilder.setCancelable(true);
                LayoutInflater layoutInflater = LayoutInflater.from(ServiceStatusMainActivity.this);
                View popupInputDialogView = layoutInflater.inflate(R.layout.row_view_qr, null);
                alertDialogBuilder.setView(popupInputDialogView);
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

                ImageView iv_cancel = popupInputDialogView.findViewById(R.id.iv_cancel);
                ImageView iv_qr_code = popupInputDialogView.findViewById(R.id.zxing_barcode_scanner);
                TextView tv_product_code = popupInputDialogView.findViewById(R.id.tv_product_code);
                TextView tv_product_name = popupInputDialogView.findViewById(R.id.tv_product_name);
                AppCompatButton btn_share = popupInputDialogView.findViewById(R.id.btn_delete);

                iv_cancel.setOnClickListener(v1 -> alertDialog.cancel());
                tv_product_name.setText(product_name);
                tv_product_code.setText(product_code);

                String text=product_code; // Whatever you need to encode in the QR code
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE,200,200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    iv_qr_code.setImageBitmap(bitmap);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getProductDetails(){
        try {
            String productId = getIntent().getStringExtra("product_id").trim();
            String condition = "SELECT COLLECTION.*,ITEM.ITEM_NAME from OT_CLCTN_ITEMS COLLECTION INNER " +
                    "JOIN OM_ITEM ITEM ON COLLECTION.CTI_ITEM_CODE=ITEM.ITEM_CODE WHERE COLLECTION.CTI_MERCHT_ID='"+
                    MERCHANT_ID +"' AND COLLECTION.CTI_SYS_ID ="+productId;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("query",condition);
            Log.e("query:",jsonObject.toString());

            AndroidNetworking.post(Constant.BASE_URL + "GetData")
                    .addHeaders("Authorization", authorization)
                    .addJSONObjectBody(jsonObject)
                    .setTag(this)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("data.Response::",response.toString());

                            try {
                                if (response.getBoolean("status")) {
                                    //Get the instance of JSONArray that contains JSONObjects
                                    JSONArray jsonArray = response.getJSONArray("data");
                                    if (jsonArray.length() != 0) {
                                        product_name = jsonArray.getJSONObject(0).getString("ITEM_NAME");
                                        product_code = jsonArray.getJSONObject(0).getString("CTI_ITEM_CODE");
                                        String product_serial_number = jsonArray.getJSONObject(0).getString("CTI_SERIAL_NO");
                                        String product_batch_number = jsonArray.getJSONObject(0).getString("CTI_BATCH");
                                        String product_status = jsonArray.getJSONObject(0).getString("CTI_STS_CODE");
                                        String product_date = jsonArray.getJSONObject(0).getString("CTI_CR_DT");
                                        String product_complaint = jsonArray.getJSONObject(0).getString("CTI_REPORTED_COMP");
                                        String customer_name = jsonArray.getJSONObject(0).getString("CTI_CUSTOMER_NAME");
                                        customer_number = jsonArray.getJSONObject(0).getString("CTI_CUSTOMER_MOBILE");
                                        String customer_email = jsonArray.getJSONObject(0).getString("CTI_CUSTOMER_EMAIL");
                                        String customer_pobox = jsonArray.getJSONObject(0).getString("CTI_AREA_CODE");
                                        String customer_address = jsonArray.getJSONObject(0).getString("CTI_CNSMR_ADDRSS");
                                        String unit_type = jsonArray.getJSONObject(0).getString("CTI_SHP_CONS_UNIT");

                                        tv_product_name.setText(product_name);
                                        tv_serial_number.setText("Serial NO: "+product_serial_number);
                                        tv_batch_number.setText("Batch No: "+product_batch_number);
                                        tv_complaint.setText(product_complaint);

                                        if (unit_type.equalsIgnoreCase("CONSUMER")){

                                            customer_data_layout.setVisibility(View.VISIBLE);
                                            tv_cust_name.setText(customer_name);
                                            tv_cust_number.setText(customer_number);
                                            tv_cust_email.setText(customer_email);
                                            tv_cust_pobox.setText(customer_pobox);
                                            tv_cust_address.setText(customer_address);
                                        }else {
                                            customer_data_layout.setVisibility(View.GONE);
                                        }
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
        Toast.makeText(ServiceStatusMainActivity.this, R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
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