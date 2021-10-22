package com.tids.clikonservice.adapter.driver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tids.clikonservice.R;
import com.tids.clikonservice.Utils.Constant;
import com.tids.clikonservice.Utils.Helper.PrefManager;
import com.tids.clikonservice.activity.driver.StoreActivity;
import com.tids.clikonservice.model.LocationModel;


import java.util.List;

public class DriverPickupNotificationAdapter extends RecyclerView.Adapter<DriverPickupNotificationAdapter.MyViewHolder> {

    private Context mContext;
    private List<LocationModel> modelList;
    private PrefManager pref;
    private SharedPreferences sp;
    private String authorization ="",technicianID ="";

    public DriverPickupNotificationAdapter(Context mContext, List<LocationModel> modelList) {
        this.mContext = mContext;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public DriverPickupNotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_store_notification, parent, false);

        return new DriverPickupNotificationAdapter.MyViewHolder(itemView);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull DriverPickupNotificationAdapter.MyViewHolder holder, int position) {
        LocationModel model = modelList.get(position);

        sp = mContext.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        pref = new PrefManager(mContext);
        technicianID = sp.getString(Constant.USER_USERID,"");
        authorization = "Bearer " + sp.getString(Constant.USER_AUTHORIZATION, "");

        holder.tv_place.setText(model.getShopName());
        holder.tv_address.setText(model.getAddress());

        holder.card_place.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, StoreActivity.class);
            intent.putExtra("type",model.getType());
            intent.putExtra("customerCode",model.getId());
            intent.putExtra("shopName",model.getShopName());
            intent.putExtra("shopAddress",model.getAddress());
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_place,tv_address;
        private CardView card_place;

        public MyViewHolder(View view) {
            super(view);

            tv_place = itemView.findViewById(R.id.tv_place);
            tv_address = itemView.findViewById(R.id.tv_address);
            card_place = itemView.findViewById(R.id.card_place);
        }
    }
}