package com.tids.clikonservice.Utils.Helper;

/**
 * Summary: to manage shared preference data.
 * Created by Eldho on 20/11/17.
 * Modified by Eldho on 12/1/18.
 */

import android.content.Context;
import android.content.SharedPreferences;


public class PrefManager {
    // Shared preferences file name
    private static final String PREF_NAME = "CLIKON";
    // All Shared Preferences Keys
    private static final String KEY_LOGIN = "login";
    private static final String KEY_DEVICE = "device";
    private static final String KEY_DEVICE_KEY = "device_key";

    public static final String LOGIN_FLAG = "login_flag";

    public static final String START_SERVICES = "start_services";
    public static final String HOLD_SERVICES = "hold_services";

    public static final String TECHNICIAN_PRODUCT_STATUS = "technician_product_status";
    public static final String TECHNICIAN_PRODUCT_ID = "technician_product_id";
    public static final String TECHNICIAN_PRODUCT_NAME = "technician_product_name";
    public static final String TECHNICIAN_PRODUCT_DOC_ID = "technician_product_doc_id";
    public static final String HOLD_PRODUCT_COUNT = "hold_product_count";
    public static final String TECHNICIAN_PRODUCT_REF_ID = "technician_product_ref_id";
    public static final String TECHNICIAN_PRODUCT_CODE = "technician_product_code";
    public static final String CUSTOMER_NAME = "customer_name";
    public static final String CUSTOMER_CODE = "customer_code";


    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public String getKeyDevice() {
        return pref.getString(KEY_DEVICE, "");
    }

    public void setKeyDevice(String count) {
        editor.putString(KEY_DEVICE, count);
        editor.commit();
    }

    public String getKeyDeviceId() {
        return pref.getString(KEY_DEVICE_KEY, "");
    }

    public void setKeyDeviceId(String count) {
        editor.putString(KEY_DEVICE_KEY, count);
        editor.commit();
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(KEY_LOGIN, false);
    }

    public void setLogin(boolean isLoggedIn) {
        editor.putBoolean(KEY_LOGIN, isLoggedIn);
        // commit changes
        editor.commit();
    }

    public boolean isLoginFlag() { return pref.getBoolean(LOGIN_FLAG,false);}

    public void setLoginFlag(boolean loginFlag){
        editor.putBoolean(LOGIN_FLAG,loginFlag);
        editor.commit();
    }

    public String getStartServices(){
        return  pref.getString(START_SERVICES,"");
    }

    public void setStartServices(String startServices){
        editor.putString(START_SERVICES,startServices);
        editor.commit();
    }

    public String getHoldServices(){
        return pref.getString(HOLD_SERVICES,"");

    }

    public void setHoldServices(String holdServices){
        editor.putString(HOLD_SERVICES,"");
        editor.commit();
    }

    public String getTechnicianProductId(){
        return pref.getString(TECHNICIAN_PRODUCT_ID,"");
    }

    public void setTechnicianProductId(String productId){
        editor.putString(TECHNICIAN_PRODUCT_ID,productId);
        editor.commit();
    }

    public String getTechnicianProductStatus(){
        return pref.getString(TECHNICIAN_PRODUCT_STATUS,"");
    }

    public void setTechnicianProductStatus(String productStatus){
        editor.putString(TECHNICIAN_PRODUCT_STATUS,productStatus);
        editor.commit();
    }

    public String getTechnicianProductName(){
        return pref.getString(TECHNICIAN_PRODUCT_NAME,"");
    }

    public void setTechnicianProductName(String productName){
        editor.putString(TECHNICIAN_PRODUCT_NAME,productName);
        editor.commit();
    }

    public String getTechnicianProductDocId(){
        return pref.getString(TECHNICIAN_PRODUCT_DOC_ID,"");
    }

    public void setTechnicianProductDocId(String doc_id){
        editor.putString(TECHNICIAN_PRODUCT_DOC_ID,doc_id);
        editor.commit();
    }

    public String getHoldProductCount(){
        return pref.getString(HOLD_PRODUCT_COUNT,"");
    }

    public void setHoldProductCount(String count){
        editor.putString(HOLD_PRODUCT_COUNT,count);
        editor.commit();
    }

    public String getTechnicianProductRefId(){
        return pref.getString(TECHNICIAN_PRODUCT_REF_ID,"");
    }

    public void setTechnicianProductRefId(String refId){
        editor.putString(TECHNICIAN_PRODUCT_REF_ID,refId);
        editor.commit();
    }

    public String getTechnicianProductCode(){
        return pref.getString(TECHNICIAN_PRODUCT_CODE,"");
    }

    public void setTechnicianProductCode(String code){
        editor.putString(TECHNICIAN_PRODUCT_CODE,code);
        editor.commit();
    }

    public String getCustomerName(){
        return pref.getString(CUSTOMER_NAME,"");
    }

    public void setCustomerName(String name){
        editor.putString(CUSTOMER_NAME,name);
        editor.commit();
    }

    public String getCustomerCode(){
        return pref.getString(CUSTOMER_CODE,"");
    }

    public void setCustomerCode(String code){
        editor.putString(CUSTOMER_CODE,code);
        editor.commit();
    }
}
