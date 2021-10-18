package com.tids.clikonservice.adapter.merchant;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.tids.clikonservice.R;
import com.tids.clikonservice.Utils.RoomDb.ClikonModel;

//we will refactor our RecyclerView.Adapter to a ListAdapter ,so now list adapter will handle all operation
public class AddProductAdapter extends ListAdapter<ClikonModel,AddProductAdapter.TodoListHolder> {
    private ItemClickListener mListener;
    //Constructor
    public AddProductAdapter() {
        super(DIFF_CALLBACK);
    }
    // AsyncListDiffer to calculate the differences between the old data
    // set and the new one we get passed in the LiveData’s onChanged method
    private static final DiffUtil.ItemCallback<ClikonModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<ClikonModel>() {
        //to check weather to items have same id or not
        @Override
        public boolean areItemsTheSame(ClikonModel oldItem, ClikonModel newItem) {
            return oldItem.getId() == newItem.getId();
        }
        //to check weather to items have same contects or not
        @Override
        public boolean areContentsTheSame(ClikonModel oldItem, ClikonModel newItem) {
            return oldItem.getProduct_code().equals(newItem.getProduct_code()) &&
                    oldItem.getProduct_name().equals(newItem.getProduct_name()) &&
                    oldItem.getSerial_no().equals(newItem.getSerial_no()) &&
                    oldItem.getBatch_no().equals(newItem.getBatch_no()) &&
                    oldItem.getComplaint().equals(newItem.getComplaint());
        }
    };
    @NonNull
    @Override
    public TodoListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_add_product, parent, false);
        return new TodoListHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull TodoListHolder holder, int position) {
        ClikonModel currentTodo = getItem(position);
        holder.tv_product_name.setText(currentTodo.getProduct_name());
    }
    /**
     * Sets click listener.
     * @param itemClickListener the item click listener
     */
    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mListener = itemClickListener;
    }
    /**
     * The interface Item click listener.
     */
    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
//        void onDeleteItem(ClikonModel todoModel);
        void onEditItem(ClikonModel todoModel);
//        void onCheckItem(ClikonModel todoModel);
    }
    public class TodoListHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tv_product_name;
        private LinearLayout add_product_lay;

        public TodoListHolder(@NonNull View itemView) {
            super(itemView);
            tv_product_name = itemView.findViewById(R.id.tv_product_name);
            add_product_lay = itemView.findViewById(R.id.add_product_lay);
            add_product_lay.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            switch (view.getId())
            {
                case R.id.add_product_lay:
                    if (mListener != null)
                        mListener.onEditItem(getItem(position));
                    break;
                default:
                    break;
            }
        }
    }
}