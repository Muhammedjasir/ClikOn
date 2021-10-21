package com.tids.clikonservice.adapter.driver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.tids.clikonservice.R;
import com.tids.clikonservice.Utils.Constant;
import com.tids.clikonservice.model.ProductModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DriverProductsAdapter extends RecyclerView.Adapter<DriverProductsAdapter.MyViewHolder> {

    private Context mContext;
    private List<ProductModel> modelList;
    private String authorization ="";
    private SharedPreferences sp;

    public DriverProductsAdapter(Context mContext, List<ProductModel> modelList) {
        this.mContext = mContext;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public DriverProductsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_driver_products_adapter, parent, false);

        return new DriverProductsAdapter.MyViewHolder(itemView);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull DriverProductsAdapter.MyViewHolder holder, int position) {
        ProductModel model = modelList.get(position);

        sp = mContext.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        authorization = "Bearer " + sp.getString(Constant.USER_AUTHORIZATION, "");

        holder.tv_product_code.setText(model.getProduct_code());
        holder.tv_product_name.setText(model.getProduct_name());
        holder.tv_product_serial_number.setText(model.getProduct_serial_number());
        holder.tv_product_batch_number.setText(model.getProduct_batch_number());

//        if (model.getProduct_status().equalsIgnoreCase("PENDLV")){
//            Glide.with(mContext).load(getImage("ic_green_tick")).into(holder.iv_tick);
//        }else {
//            Glide.with(mContext).load(getImage("ic_default_tick")).into(holder.iv_tick);
//        }

        if (model.getProduct_date().equalsIgnoreCase("merchant_pickup")){ // where date contain pickup type value (value: merchant_pickup or technician_pickup)
            holder.iv_qrcode.setVisibility(View.VISIBLE);
            holder.iv_tick.setVisibility(View.INVISIBLE);
        }else {
            holder.iv_qrcode.setVisibility(View.INVISIBLE);
            holder.iv_tick.setVisibility(View.VISIBLE);
        }

        holder.cv_lay.setOnClickListener(v -> {
            if (model.getProduct_date().equalsIgnoreCase("merchant_pickup")) { // where date contain pickup type value (value: merchant_pickup or technician_pickup)
                // generate QR Code using product code
                generateQrCode(model.getId(),model.getProduct_code(),model.getProduct_name(),position);
            }
//            else {
//                // pickup product from technician
//                if (!model.getProduct_status().equalsIgnoreCase("PENDLV")){ // check it's already pickedup
//                    pickupProduct(model.getId(),position);
//                }
//            }
        });
    }

//    private void pickupProduct(int id, int position) {
//        try {
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put("CTI_STS_CODE","PENDLV");
//            Log.e("body::",jsonObject.toString());
//
//            AndroidNetworking.put(Constant.BASE_URL + "OT_CLCTN_ITEMS/" +
//                    id)
//                    .addHeaders("Authorization", authorization)
//                    .addJSONObjectBody(jsonObject)
//                    .setTag(this)
//                    .setPriority(Priority.LOW)
//                    .build()
//                    .getAsJSONObject(new JSONObjectRequestListener() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            Log.e("Response2::",response.toString());
//
//                            try {
//                                if (response.getBoolean("status")) {
//                                    modelList.get(position).setProduct_status("PENDLV");
//                                    notifyDataSetChanged();
//                                    Toast.makeText(mContext, "Product Pickedup", Toast.LENGTH_SHORT).show();
//                                }else {
//                                    Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        @Override
//                        public void onError(ANError anError) {
////                                    showError(anError);
//                        }
//                    });
//
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
//    }

    private void generateQrCode(int id, String product_code,String product_name,int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setCancelable(true);
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View popupInputDialogView = layoutInflater.inflate(R.layout.row_qrcode_generate_dialog_box, null);
        alertDialogBuilder.setView(popupInputDialogView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        ImageView iv_qr_code = popupInputDialogView.findViewById(R.id.zxing_barcode_scanner);
        TextView tv_product_name = popupInputDialogView.findViewById(R.id.tv_product_name);
        TextView tv_product_code = popupInputDialogView.findViewById(R.id.tv_product_code);
        AppCompatButton btn_print = popupInputDialogView.findViewById(R.id.btn_print);

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

        btn_print.setOnClickListener(v -> {
            alertDialog.cancel();
            printQRCode(id,position);
        });
    }

    private void printQRCode(int id,int position) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("CTI_STS_CODE","DVRPCP");
            Log.e("body::",jsonObject.toString());

            AndroidNetworking.put(Constant.BASE_URL + "OT_CLCTN_ITEMS/" +
                    id)
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
                                    modelList.remove(position);
                                    notifyDataSetChanged();
                                    Toast.makeText(mContext, "Product Pickedup", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(mContext, response.getString("message"), Toast.LENGTH_SHORT).show();
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
    public int getItemCount() {
        return modelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_product_code,tv_product_name,tv_product_serial_number,tv_product_batch_number;
        private CardView cv_lay;
        private ImageView iv_qrcode,iv_tick;

        public MyViewHolder(View view) {
            super(view);

            cv_lay = itemView.findViewById(R.id.cv_lay);
            tv_product_code = itemView.findViewById(R.id.tv_product_code);
            iv_qrcode = itemView.findViewById(R.id.btn_print_qr_code);
            iv_tick = itemView.findViewById(R.id.iv_tick);
            tv_product_name = itemView.findViewById(R.id.tv_product_name);
            tv_product_serial_number = itemView.findViewById(R.id.tv_product_serial_number);
            tv_product_batch_number = itemView.findViewById(R.id.tv_product_batch_number);
        }
    }
}