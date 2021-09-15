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

    // tables
    public static final String TECHNICIAN_USER = "ADM_USER";
    public static final String SERVICE_PRODUCTS = "SERVICE_MODULE_VIEW";
    public static final String SERVICE_PRODUCT_INFO = "OT_SERVICE_MODULE";

}

