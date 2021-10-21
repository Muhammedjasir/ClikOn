package com.tids.clikonservice.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.snackbar.Snackbar;
import com.tids.clikonservice.R;
import com.tids.clikonservice.Utils.Constant;
import com.tids.clikonservice.Utils.Helper.PrefManager;
import com.tids.clikonservice.adapter.driver.DriverPickupNotificationAdapter;
import com.tids.clikonservice.model.LocationModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MerchantPickupFragment extends Fragment {

    private View view;
    SharedPreferences sp;
    PrefManager pref;

    private RecyclerView rv_notification;
    private DriverPickupNotificationAdapter pickupNotificationAdapter;
    private ArrayList<LocationModel> locationModelArrayList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_merchant_pickup, container, false);

        pref = new PrefManager(getActivity());
        sp = getActivity().getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        rv_notification = view.findViewById(R.id.recycler_view);
        pickupNotificationAdapter = new DriverPickupNotificationAdapter(getActivity(), locationModelArrayList);
        rv_notification.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL,false));
        rv_notification.setAdapter(pickupNotificationAdapter);
        getPickupNotifications();

        return view;
    }
    private void getPickupNotifications() {
        try {
            String authorization = "Bearer " + sp.getString(Constant.USER_AUTHORIZATION, "");
            String condition = "SELECT CUST_CODE,CUST_NAME,CUST_DEL_ADD_2,CUST_DEL_ADD_3 FROM OM_CUSTOMER WHERE" +
                    " CUST_CODE IN (SELECT CM_CUST_CODE FROM OT_COLLECTION_MODULE WHERE CM_DOC_NO " +
                    "IN (SELECT CTI_CM_DOC_NO FROM OT_CLCTN_ITEMS WHERE CTI_STS_CODE='DVRETR'))" ;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("query",condition);

            AndroidNetworking.post(Constant.BASE_URL + "GetData")
                    .addHeaders("Authorization", authorization)
                    .addJSONObjectBody(jsonObject)
                    .setTag(this)
                    .setPriority(Priority.MEDIUM)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("Response::",response.toString());

                            try {
                                if (response.getBoolean("status")) {
                                    //Get the instance of JSONArray that contains JSONObjects
                                    JSONArray jsonArray = response.getJSONArray("data");
                                    if (jsonArray.length() != 0) {
//                                        tv_pickup_count.setText(String.valueOf(jsonArray.length()));
                                        for (int i = 0; i< jsonArray.length(); i++){
                                            String id = jsonArray.getJSONObject(i).getString("CUST_CODE");
                                            String shop_name = jsonArray.getJSONObject(i).getString("CUST_NAME");
                                            String address1 = jsonArray.getJSONObject(i).getString("CUST_DEL_ADD_2");
                                            String address2 = jsonArray.getJSONObject(i).getString("CUST_DEL_ADD_3");
                                            String address = address1+" "+address2;
                                            String type = "merchant_pickup";

                                            LocationModel locationModel = new LocationModel(id,shop_name,address,type);
                                            locationModelArrayList.add(locationModel);
                                        }
                                        pickupNotificationAdapter.notifyDataSetChanged();
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onError(ANError anError) {
                            showError(anError);
                        }
                    });

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void showError(ANError anError) {
        Toast.makeText(getActivity(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
        Log.e("Error :: ", anError.getErrorBody());
    }

    private void customToast(String message){
        Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).setBackgroundTint(getResources().getColor(R.color.colorPrimary)).show();
    }
}