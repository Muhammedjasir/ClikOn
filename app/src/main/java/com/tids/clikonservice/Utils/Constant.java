package com.tids.clikonservice.Utils;

public class Constant {

    Constant() {
        //write your action here if need
    }

    //url
    public static String BASE_URL = "http://clikonworld.dyndns.org:8082/clkservice/api/";

    //We will use this to store the user token number into shared preference
    public static final String SHARED_PREF_NAME = "com.tids.clikonservice"; //pcakage name+ id

    public static final String USER_USERID = "userid";
    public static final String USER_USERNAME = "username";
    public static final String USER_NUMBER = "mobilenumber";
    public static final String USER_EMAIL = "email";
    public static final String USER_PASSWORD = "password";
    public static final String USER_PROFILE = "profile";
    public static final String USER_TYPE = "type";
    public static final String USER_ONLINE_STATUS = "onlinestatus";  // 0-OFF & 1-ON
    public static final String USER_AUTHORIZATION = "Authorization";

    // table names & ids
    public static final String OT_COLLECTION_MODULE = "6";
    public static final String OT_CLCTN_ITEMS = "7";
    public static final String OT_SERV_PARTS = "10";
    public static final String ADM_USER = "17";
    public static final String OT_SERVICE_MODULE = "27";
    public static final String OT_PARTNT_AVLABLE = "32";
    public static final String OT_DVR_REQ_ALLCTN = "44";
    public static final String OT_DVR_CLCTN = "45";

    public static final String[] PRODUCT_HOLD_REASONS = new String[]{
            "Parts not available",
            "Can't repairable"
    };

    public static final String[] PRODUCT_STATUS = new String[]{
            "Entered",
            "Pickup",
            "Collected",
            "Pending in service",
            "Service finished",
            "Pending in delivery",
            "Delivered",
            "Service Started",
            "Service Paused"
    };

}

