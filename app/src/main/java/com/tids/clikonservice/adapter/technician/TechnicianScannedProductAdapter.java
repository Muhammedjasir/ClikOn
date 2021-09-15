package com.tids.clikonservice.adapter.technician;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.tids.clikonservice.R;
import com.tids.clikonservice.Utils.Constant;
import com.tids.clikonservice.Utils.Helper.PrefManager;
import com.tids.clikonservice.activity.StartServiceActivity;
import com.tids.clikonservice.model.ScannedProductModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.List;

public class TechnicianScannedProductAdapter extends RecyclerView.Adapter<TechnicianScannedProductAdapter.MyViewHolder> {

    private Context mContext;
    private List<ScannedProductModel> modelList;
    private PrefManager pref;
    private SharedPreferences sp;
    private int pause_pos = 0;
    private String authorization ="",technicianID ="";

    public TechnicianScannedProductAdapter(Context mContext, List<ScannedProductModel> modelList) {
        this.mContext = mContext;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public TechnicianScannedProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_received_product, parent, false);

        return new TechnicianScannedProductAdapter.MyViewHolder(itemView);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull TechnicianScannedProductAdapter.MyViewHolder holder, int position) {
        ScannedProductModel model = modelList.get(position);

        sp = mContext.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        pref = new PrefManager(mContext);
        technicianID = sp.getString(Constant.USER_USERID,"");
        authorization = "Bearer " + sp.getString(Constant.USER_AUTHORIZATION, "");
        pause_pos = position;

        holder.tv_product_name.setText(model.getProductName());
        holder.tv_product_complaint.setText(model.getProductComplaint());
        holder.tv_product_branch_name.setText(model.getProductBatchNumber());
        holder.tv_product_serial_number.setText(model.getProductSerialNumber());

        Log.e("TechnicianProductId::",pref.getTechnicianProductId());
        if (pref.getTechnicianProductStatus().equalsIgnoreCase("start") &&
                pref.getTechnicianProductId().equalsIgnoreCase(model.getProductScannedId())){
            Glide.with(mContext).load(getImage("ic_pause")).into(holder.iv_play);
//            swapeItem(position,0);
        }else {
            Glide.with(mContext).load(getImage("ic_play")).into(holder.iv_play);
        }

        holder.card_play.setOnClickListener(v -> {
            if (!pref.getTechnicianProductStatus().equalsIgnoreCase("start")){

                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("SM_STS_CODE","SERVSRT");
                    jsonObject.put("SM_SRP_SYS_ID",technicianID);

                    Log.e("url::",Constant.BASE_URL + Constant.SERVICE_PRODUCT_INFO + "/" +
                            model.getProductScannedId());
                    Log.e("Authorization::",authorization);
                    Log.e("body::",jsonObject.toString());

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

                                            Glide.with(mContext).load(getImage("ic_pause")).into(holder.iv_play);
                                            pref.setTechnicianProductStatus("start");
                                            pref.setTechnicianProductId(model.getProductScannedId());
                                            pref.setTechnicianProductName(model.getProductName());
                                            pref.setTechnicianProductDocId(model.getProductDocId());
                                            notifyDataSetChanged();

                                            Intent intent = new Intent(mContext, StartServiceActivity.class);
                                            mContext.startActivity(intent);

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
        });

    }

    public int getImage(String imageName) {
        int drawableResourceId = mContext.getResources().getIdentifier(imageName, "drawable", mContext.getPackageName());
        return drawableResourceId;
    }

    public void swapeItem(int fromPosition,int toPosition){
        Collections.swap(modelList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_product_name,tv_product_serial_number,tv_product_branch_name,tv_product_complaint;
        private ImageView iv_play;
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