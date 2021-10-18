package com.tids.clikonservice.adapter.technician;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.tids.clikonservice.R;
import com.tids.clikonservice.Utils.Constant;
import com.tids.clikonservice.model.SparepartsModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SparepartsAdapter extends RecyclerView.Adapter<SparepartsAdapter.MyViewHolder> {


    private Context mContext;
    private List<SparepartsModel> modelList;
    private SharedPreferences sp;

    public SparepartsAdapter(Context mContext, List<SparepartsModel> modelList) {
        this.mContext = mContext;
        this.modelList = modelList;

    }

    @NonNull
    @Override
    public SparepartsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_spareparts_adapter, parent, false);

        return new SparepartsAdapter.MyViewHolder(itemView);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull SparepartsAdapter.MyViewHolder holder, int position) {
        SparepartsModel model = modelList.get(position);

        sp = mContext.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        holder.tv_name.setText(model.getPartsName()+"-"+model.getPartsCode());
        holder.tv_qty.setText("  "+model.getQty());

        holder.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String authorization = "Bearer " + sp.getString(Constant.USER_AUTHORIZATION, "");
                    AndroidNetworking.delete(Constant.BASE_URL+ "OT_SERV_PARTS/"+model.getTb_id() )
                            .addHeaders("Authorization", authorization)
                            .setTag(this)
                            .setPriority(Priority.LOW)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.e("Response2::",response.toString());

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
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_name,tv_qty;
        private ImageView iv_delete;

        public MyViewHolder(View view) {
            super(view);

            tv_name = itemView.findViewById(R.id.tv_text);
            tv_qty = itemView.findViewById(R.id.tv_qty);
            iv_delete = itemView.findViewById(R.id.iv_delete);
        }
    }
}