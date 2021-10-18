package com.tids.clikonservice.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tids.clikonservice.R;
import com.tids.clikonservice.model.ResponseModel;

import java.util.ArrayList;
import java.util.List;

public class SpinnerAdapter  extends ArrayAdapter<ResponseModel> {

    Context context;
    int resource, textViewResourceId;
    List<ResponseModel> items, tempItems, suggestions;
    Filter nameFilter = new Filter() {


        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((ResponseModel) resultValue).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            Log.d("Constrain", constraint.toString().toLowerCase());
            if (constraint != null) {
                suggestions.clear();
                for (ResponseModel categoriesData : tempItems) {
                    if (categoriesData.getName().toLowerCase().contains(constraint.toString())) {
                        suggestions.add(categoriesData);
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions; //suggestion was here
                filterResults.count = suggestions.size();  //suggestion was here
                Log.d("filterResultsCount", String.valueOf(filterResults.count));

                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            Log.d("Resultcount", String.valueOf(results.count));
            List<ResponseModel> filterList = (ArrayList<ResponseModel>) results.values;
//            items = (ArrayList<GroceryTypesListModel>) results.values;
//            notifyDataSetChanged();
            if (results != null && results.count > 0) {
                clear();
                for (ResponseModel filterLocations : filterList) {
                    add(filterLocations);
                    notifyDataSetChanged();
                }
            } else {
                notifyDataSetInvalidated();
            }
        }
    };

    public SpinnerAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<ResponseModel> items) {
        super(context, resource, textViewResourceId, items);

        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;

        tempItems = new ArrayList<ResponseModel>(items);
        suggestions = new ArrayList<ResponseModel>();


    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // return super.getView(position, convertView, parent);
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.row_spinner_adapter, parent, false);
        }

        if (items != null) {
            ResponseModel categories = items.get(position);
            if (categories != null) {
                TextView categoryName = view.findViewById(R.id.text1);
                categoryName.setText(categories.getCode()+" - "+categories.getName());
//                categoryName.setText(categories.getName());

            }
        }
        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return nameFilter;
    }

}