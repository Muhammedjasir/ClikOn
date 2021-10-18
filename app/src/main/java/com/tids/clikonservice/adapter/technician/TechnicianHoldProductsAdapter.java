package com.tids.clikonservice.adapter.technician;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tids.clikonservice.R;
import com.tids.clikonservice.Utils.Constant;
import com.tids.clikonservice.Utils.Helper.PrefManager;
import com.tids.clikonservice.activity.technician.StartServiceActivity;
import com.tids.clikonservice.model.ScannedProductModel;

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
        holder.tv_product_code.setText(model.getProductCode());
        holder.tv_doc_id.setText("#"+model.getProductDocId());
        holder.tv_ref_number.setText(model.getProductReferId());
        holder.tv_customer_code.setText(model.getCustomerCode());
        holder.tv_customer_name.setText(model.getCustomerName());

        if (model.getPageFlag().equalsIgnoreCase("hold")){
            holder.iv_play.setVisibility(View.VISIBLE);
        }else {
            holder.iv_play.setVisibility(View.GONE);
        }

        holder.card_play.setOnClickListener(v -> {

            if (model.getPageFlag().equalsIgnoreCase("hold") ||
                    model.getPageFlag().equalsIgnoreCase("over")){

                Intent intent = new Intent(mContext, StartServiceActivity.class);
                intent.putExtra("product_doc_id",model.getProductDocId());
                mContext.startActivity(intent);
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

        private TextView tv_product_name,tv_product_serial_number,tv_product_branch_name,
                tv_product_complaint,tv_ref_number,tv_product_code,tv_doc_id,tv_customer_name,
                tv_customer_code,v_product_complaint;
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
            tv_ref_number = itemView.findViewById(R.id.tv_ref_number);
            tv_product_code = itemView.findViewById(R.id.tv_product_code);
            tv_doc_id = itemView.findViewById(R.id.tv_doc_id);
            tv_customer_name = itemView.findViewById(R.id.tv_customer_name);
            tv_customer_code = itemView.findViewById(R.id.tv_customer_code);
        }
    }
}