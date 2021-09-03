//package com.tids.clikonservice.adapter;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.tids.clikonservice.R;
//
//public class TechnicianScannedProductAdapter extends RecyclerView.Adapter<TechnicianScannedProductAdapter.MyViewHolder> {
//
//
//    private List<PayMethod> payMethodData;
//    private Context context;
//    private String currency;
//
//
//    public TechnicianScannedProductAdapter(Context context, List<PayMethod> payMethodData, String currency) {
//        this.context = context;
//        this.payMethodData = payMethodData;
//        this.currency = currency;
//    }
//
//
//    @NonNull
//    @Override
//    public TechnicianScannedProductAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_received_product, parent, false);
//        return new MyViewHolder(view);
//    }
//
//
//    @Override
//    public void onBindViewHolder(@NonNull final TechnicianScannedProductAdapter.MyViewHolder holder, int position) {
//
////        String value = payMethodData.get(position).getValue();
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return payMethodData.size();
//    }
//
//    public class MyViewHolder extends RecyclerView.ViewHolder {
//
//        TextView tv_product_name,tv_product_serial_number,tv_product_branch_name,tv_product_complaint;
//
//        public MyViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            tv_product_name = itemView.findViewById(R.id.tv_product_name);
//            tv_product_serial_number = itemView.findViewById(R.id.tv_product_serial_number);
//            tv_product_branch_name = itemView.findViewById(R.id.tv_product_branch_name);
//            tv_product_complaint = itemView.findViewById(R.id.tv_product_complaint);
//
//
//        }
//
//    }
//
//
//}
