package com.tids.clikonservicenew.adapter.technician;

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

import com.tids.clikonservicenew.R;
import com.tids.clikonservicenew.model.SparePartsBinModel;

import java.util.ArrayList;
import java.util.List;

public class SparepartsbinAdapter extends ArrayAdapter<SparePartsBinModel> {

    Context context;
    int resource, textViewResourceId;
    List<SparePartsBinModel> items, tempItems, suggestions;
    Filter nameFilter = new Filter() {


        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((SparePartsBinModel) resultValue).getTextField();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            Log.d("Constrain", constraint.toString().toLowerCase());
            if (constraint != null) {
                suggestions.clear();
                for (SparePartsBinModel categoriesData : tempItems) {
                    if (categoriesData.getValueField().toLowerCase().contains(constraint.toString())) {
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
            List<SparePartsBinModel> filterList = (ArrayList<SparePartsBinModel>) results.values;
//            items = (ArrayList<GroceryTypesListModel>) results.values;
//            notifyDataSetChanged();
            if (results != null && results.count > 0) {
                clear();
                for (SparePartsBinModel filterLocations : filterList) {
                    add(filterLocations);
                    notifyDataSetChanged();
                }
            } else {
                notifyDataSetInvalidated();
            }
        }
    };

    public SparepartsbinAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<SparePartsBinModel> items) {
        super(context, resource, textViewResourceId, items);

        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;

        tempItems = new ArrayList<SparePartsBinModel>(items);
        suggestions = new ArrayList<SparePartsBinModel>();


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
            SparePartsBinModel categories = items.get(position);
            if (categories != null) {
                TextView categoryName = view.findViewById(R.id.text1);
                categoryName.setText(categories.getTextField());
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
