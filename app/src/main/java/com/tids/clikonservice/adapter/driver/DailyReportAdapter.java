package com.tids.clikonservice.adapter.driver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tids.clikonservice.R;
import com.tids.clikonservice.model.ProductModel;


import java.util.List;

public class DailyReportAdapter extends RecyclerView.Adapter<DailyReportAdapter.MyViewHolder> {

    private Context mContext;
    private List<ProductModel> modelList;

    public DailyReportAdapter(Context mContext, List<ProductModel> modelList) {
        this.mContext = mContext;
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public DailyReportAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_daily_report_adapter, parent, false);

        return new DailyReportAdapter.MyViewHolder(itemView);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull DailyReportAdapter.MyViewHolder holder, int position) {
        ProductModel model = modelList.get(position);

        holder.tv_product_code.setText(model.getProduct_code());
        holder.tv_product_name.setText(model.getProduct_name());
        holder.tv_product_serial_number.setText(model.getProduct_serial_number());
        holder.tv_product_batch_number.setText(model.getProduct_batch_number());

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_product_code,tv_product_name,tv_product_serial_number,tv_product_batch_number;

        public MyViewHolder(View view) {
            super(view);
            
            tv_product_code = itemView.findViewById(R.id.tv_product_code);
            tv_product_name = itemView.findViewById(R.id.tv_product_name);
            tv_product_serial_number = itemView.findViewById(R.id.tv_product_serial_number);
            tv_product_batch_number = itemView.findViewById(R.id.tv_product_batch_number);
        }
    }
}