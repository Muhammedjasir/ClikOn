package com.tids.clikonservice.adapter.technician;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.tids.clikonservice.R;
import com.tids.clikonservice.Utils.Constant;
import com.tids.clikonservice.Utils.Helper.PrefManager;
import com.tids.clikonservice.model.ScannedProductModel;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.List;

public class TechnicianHoldProductsAdapter extends RecyclerView.Adapter<TechnicianHoldProductsAdapter.MyViewHolder> {

    private Context mContext;
    private List<ScannedProductModel> modelList;
    private PrefManager pref;
    private SharedPreferences sp;
    private String authorization ="",technicianID ="";

    public TechnicianHoldProductsAdapter(Context mContext, List<ScannedProductModel> modelList) {
        this.mContext = mContext;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public TechnicianHoldProductsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.raw_hold_products, parent, false);

        return new TechnicianHoldProductsAdapter.MyViewHolder(itemView);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull TechnicianHoldProductsAdapter.MyViewHolder holder, int position) {
        ScannedProductModel model = modelList.get(position);

        sp = mContext.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        pref = new PrefManager(mContext);
        technicianID = sp.getString(Constant.USER_USERID,"");
        authorization = "Bearer " + sp.getString(Constant.USER_AUTHORIZATION, "");

        holder.tv_product_name.setText(model.getProductName());
        holder.tv_product_complaint.setText(model.getProductComplaint());
        holder.tv_product_branch_name.setText(model.getProductBatchNumber());
        holder.tv_product_serial_number.setText(model.getProductSerialNumber());

        if (model.getPageFlag().equalsIgnoreCase("hold")){
            holder.iv_play.setVisibility(View.VISIBLE);
        }else {
            holder.iv_play.setVisibility(View.GONE);
        }

        holder.iv_play.setOnClickListener(v -> {

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("SM_STS_CODE","PENSERV");
                jsonObject.put("SM_SRP_SYS_ID","");

                AndroidNetworking.put(Constant.BASE_URL + Constant.SERVICE_PRODUCT_INFO + "/" +
                        model.getProductDocId())
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

                                        modelList.remove(holder.getAdapterPosition());
                                        notifyDataSetChanged();

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
        });

    }

    public int getImage(String imageName) {
        int drawableResourceId = mContext.getResources().getIdentifier(imageName, "drawable", mContext.getPackageName());
        return drawableResourceId;
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_product_name,tv_product_serial_number,tv_product_branch_name,tv_product_complaint;
        private TableRow iv_play;
        private CardView card_play;

        public MyViewHolder(View view) {
            super(view);

            tv_product_name = itemView.findViewById(R.id.tv_product_name);
            tv_product_serial_number = itemView.findViewById(R.id.tv_product_serial_number);
            tv_product_branch_name = itemView.findViewById(R.id.tv_product_branch_name);
            tv_product_complaint = itemView.findViewById(R.id.tv_product_complaint);
            iv_play = itemView.findViewById(R.id.iv_play);
            card_play = itemView.findViewById(R.id.card_scanned);
        }
    }
}