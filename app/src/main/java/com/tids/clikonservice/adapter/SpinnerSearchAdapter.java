package com.tids.clikonservice.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tids.clikonservice.R;
import com.tids.clikonservice.model.ResponseModel;

public class SpinnerSearchAdapter extends ArrayAdapter<ResponseModel> {

    Context context;
    int resource, textViewResourceId;
    List<ResponseModel> items, tempItems, suggestions;


    public SpinnerSearchAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List<ResponseModel> items) {
        super(context, resource, textViewResourceId, items);

        this.context = context;
        this.resource = resource;
        this.textViewResourceId = textViewResourceId;
        this.items = items;

        // tempItems = new ArrayList<ResponseModel>(items);
        //  suggestions = new ArrayList<ResponseModel>();
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
            ResponseModel locationsModel = items.get(position);
            if (locationsModel != null) {
                TextView lblName = view.findViewById(R.id.text1);
                lblName.setText(locationsModel.getCode()+" - "+locationsModel.getName());
            }
        }


        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    Filter nameFilter = new Filter() {


        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((ResponseModel) resultValue).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

//            Log.d("Constrain", constraint.toString().toLowerCase());
            if (constraint != null) {
/*
                suggestions.clear();
                for (LocationsModel locations : tempItems) {
                    if (locations.getCityName().toLowerCase().contains(constraint.toString())) {
                        suggestions.add(locations);
                    }
                }
*/

                FilterResults filterResults = new FilterResults();
                filterResults.values = items; //suggestion was here
                filterResults.count = items.size();  //suggestion was here
                Log.d("filterResultsCount", String.valueOf(filterResults.count));

                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            Log.d("Resultcount", String.valueOf(results.count));
//            List<LocationsModel> filterList = (ArrayList<LocationsModel>) results.values;
            items = (ArrayList<ResponseModel>) results.values;
            notifyDataSetChanged();
/*
            if (results != null && results.count > 0) {
                clear();
                for (LocationsModel filterLocations : filterList) {
                    add(filterLocations);
                    notifyDataSetChanged();
                }
            } else {
                notifyDataSetInvalidated();
            }
*/
        }
    };
}