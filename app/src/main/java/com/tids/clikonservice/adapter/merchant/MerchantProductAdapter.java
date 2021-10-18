package com.tids.clikonservice.adapter.merchant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tids.clikonservice.R;
import com.tids.clikonservice.activity.merchant.ServiceStatusMainActivity;
import com.tids.clikonservice.model.ProductModel;
import java.util.List;


public class MerchantProductAdapter extends RecyclerView.Adapter<MerchantProductAdapter.MyViewHolder> {

    private Context mContext;
    private List<ProductModel> modelList;

    public MerchantProductAdapter(Context mContext, List<ProductModel> modelList) {
        this.mContext = mContext;
        this.modelList = modelList;

    }

    @NonNull
    @Override
    public MerchantProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_product_list, parent, false);

        return new MerchantProductAdapter.MyViewHolder(itemView);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull MerchantProductAdapter.MyViewHolder holder, int position) {
        ProductModel model = modelList.get(position);

        holder.tv_product_code.setText(model.getProduct_code());
        holder.tv_product_date.setText(model.getProduct_date());
        holder.tv_product_name.setText(model.getProduct_name());
        holder.tv_product_serial_number.setText("Serial Number: "+model.getProduct_serial_number());
        holder.tv_product_batch_number.setText("Batch Number: "+model.getProduct_batch_number());

        String product_code = model.getProduct_status();
        if (product_code.equalsIgnoreCase("DVRPCP")){
            holder.tv_product_status.setText("Pickup");
        }else if (product_code.equalsIgnoreCase("DVRETR")){
            holder.tv_product_status.setText("Entered");
        }else if (product_code.equalsIgnoreCase("CLTD")){
            holder.tv_product_status.setText("Collected");
        }else if (product_code.equalsIgnoreCase("PENSERV")){
            holder.tv_product_status.setText("Pending in service");
        }else if (product_code.equalsIgnoreCase("SERVFIN")){
            holder.tv_product_status.setText("Service finished");
        }else if (product_code.equalsIgnoreCase("PENDLV")){
            holder.tv_product_status.setText("Pending in delivery");
        }else if (product_code.equalsIgnoreCase("DLV")){
            holder.tv_product_status.setText("Delivered");
        }else if (product_code.equalsIgnoreCase("SERVSRT")){
            holder.tv_product_status.setText("Service started");
        }else if (product_code.equalsIgnoreCase("SERVPUS")){
            holder.tv_product_status.setText("Service paused");
        }

        holder.cv_lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ServiceStatusMainActivity.class);
                intent.putExtra("product_id",model.getId()+"");
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_product_code,tv_product_status,tv_product_date,tv_product_name,
                tv_product_serial_number,tv_product_batch_number;
        private CardView cv_lay;

        public MyViewHolder(View view) {
            super(view);

            cv_lay = itemView.findViewById(R.id.cv_lay);
            tv_product_code = itemView.findViewById(R.id.tv_product_code);
            tv_product_status = itemView.findViewById(R.id.tv_product_status);
            tv_product_date = itemView.findViewById(R.id.tv_product_date);
            tv_product_name = itemView.findViewById(R.id.tv_product_name);
            tv_product_serial_number = itemView.findViewById(R.id.tv_product_serial_number);
            tv_product_batch_number = itemView.findViewById(R.id.tv_product_batch_number);
        }
    }
}