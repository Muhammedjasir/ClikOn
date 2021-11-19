package com.tids.clikonservice.adapter.driver;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.tids.clikonservice.R;
import com.tids.clikonservice.Utils.Constant;
import com.tids.clikonservice.Utils.Z91_smart_POS.BluetoothUtil;
import com.tids.clikonservice.Utils.Z91_smart_POS.PrinterFunction;
import com.tids.clikonservice.activity.technician.ServiceStatusTechActivity;
import com.tids.clikonservice.model.ProductModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class DeliveredCartAdapter extends RecyclerView.Adapter<DeliveredCartAdapter.MyViewHolder> {

    private Context mContext;
    private List<ProductModel> modelList;
    private String authorization ="";
    private SharedPreferences sp;

    public DeliveredCartAdapter(Context mContext, List<ProductModel> modelList) {
        this.mContext = mContext;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public DeliveredCartAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_cart_adapter, parent, false);

        return new DeliveredCartAdapter.MyViewHolder(itemView);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull DeliveredCartAdapter.MyViewHolder holder, int position) {
        ProductModel model = modelList.get(position);

        sp = mContext.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        authorization = "Bearer " + sp.getString(Constant.USER_AUTHORIZATION, "");

        holder.tv_product_code.setText(model.getProduct_code());
        holder.tv_product_name.setText(model.getProduct_name());
        holder.tv_product_serial_number.setText(model.getProduct_serial_number());
        holder.tv_product_batch_number.setText(model.getProduct_batch_number());

        holder.cv_lay.setOnClickListener(v -> {
            confirmationAlert(model.getId(),model.getProduct_date(),position,model.getProduct_name(),
                    model.getProduct_code());
        });
    }

    private void confirmationAlert(int id, String type, int position,String product_name,
                                   String product_code){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
        alertDialogBuilder.setCancelable(true);
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View popupInputDialogView = layoutInflater.inflate(R.layout.row_confirm_alert, null);
        alertDialogBuilder.setView(popupInputDialogView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        AppCompatButton bt_reprint = popupInputDialogView.findViewById(R.id.bt_reprint);
        AppCompatButton bt_deliver = popupInputDialogView.findViewById(R.id.bt_deliver);

        bt_reprint.setOnClickListener(v -> {
            alertDialog.cancel();
            printLayout(id,product_code,product_name);
        });
        bt_deliver.setOnClickListener(v -> {
            alertDialog.cancel();
            deliveryProduct(id,type,position);
        });

    }

    private void deliveryProduct(int id, String type, int position) {
        try {

            String condition ="";
            if (type.equalsIgnoreCase("merchant_delivery")){
                condition = "UPDATE OT_CLCTN_ITEMS SET CTI_STS_CODE='DLV', CTI_STS_SYS_ID=5 " +
                        "WHERE CTI_SYS_ID="+id;
            }else {
                condition = "UPDATE OT_CLCTN_ITEMS SET CTI_STS_CODE='PENSERV', CTI_STS_SYS_ID=2 " +
                        "WHERE CTI_SYS_ID="+id;
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("query",condition);
            Log.e("body::",jsonObject.toString());

            AndroidNetworking.post(Constant.BASE_URL + "UpdateData")
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
                                    modelList.remove(position);
                                    notifyDataSetChanged();
                                    Toast.makeText(mContext, "Product delivered", Toast.LENGTH_SHORT).show();
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

    //print operation text setup
    public void printLayout(int sys_id,String product_code,String product_name) {

        // check bluetooth connection
        if (new BluetoothUtil().is_bluetooth_enable()) {
            // printer function
            Log.e("print::",product_name+"-"+sys_id+"-"+product_code);
            new PrinterFunction().printQR(product_name,sys_id,product_code);
        }
        else {
            enable_bluetooth();
        }
    }

    private void enable_bluetooth() {
        Intent bluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//        bluetoothIntent.addFlags(BLUETOOTH_ENABLE_REQUEST);
        mContext.startActivity(bluetoothIntent);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_product_code,tv_product_name,tv_product_serial_number,tv_product_batch_number;
        private CardView cv_lay;
        private ImageView iv_deliver;

        public MyViewHolder(View view) {
            super(view);

            cv_lay = itemView.findViewById(R.id.cv_lay);
            tv_product_code = itemView.findViewById(R.id.tv_product_code);
            iv_deliver = itemView.findViewById(R.id.btn_print_qr_code);
            tv_product_name = itemView.findViewById(R.id.tv_product_name);
            tv_product_serial_number = itemView.findViewById(R.id.tv_product_serial_number);
            tv_product_batch_number = itemView.findViewById(R.id.tv_product_batch_number);
        }
    }
}