package com.tids.clikonservice.adapter.technician;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tids.clikonservice.R;
import com.tids.clikonservice.Utils.Helper.PrefManager;
import com.tids.clikonservice.model.ScannedProductModel;

import java.util.List;

public class TechnicianCompletedProductAdapter extends RecyclerView.Adapter<TechnicianCompletedProductAdapter.MyViewHolder> {


    private Context mContext;
    private List<ScannedProductModel> modelList;
    private PrefManager pref;
    private int pause_pos = 0;

    public TechnicianCompletedProductAdapter(Context mContext, List<ScannedProductModel> modelList) {
        this.mContext = mContext;
        this.modelList = modelList;

    }

    @NonNull
    @Override
    public TechnicianCompletedProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_product_details, parent, false);

        return new TechnicianCompletedProductAdapter.MyViewHolder(itemView);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull TechnicianCompletedProductAdapter.MyViewHolder holder, int position) {
        ScannedProductModel model = modelList.get(position);

        pref = new PrefManager(mContext);
        pause_pos = position;

        holder.tv_product_name.setText(model.getProductName());
        holder.tv_product_complaint.setText(model.getProductComplaint());
        holder.tv_product_branch_name.setText(model.getProductBatchNumber());
        holder.tv_product_serial_number.setText(model.getProductSerialNumber());
       
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_product_name,tv_product_serial_number,tv_product_branch_name,tv_product_complaint;

        public MyViewHolder(View view) {
            super(view);

            tv_product_name = itemView.findViewById(R.id.tv_product_name);
            tv_product_serial_number = itemView.findViewById(R.id.tv_product_serial_number);
            tv_product_branch_name = itemView.findViewById(R.id.tv_product_branch_name);
            tv_product_complaint = itemView.findViewById(R.id.tv_product_complaint);
        }
    }
}