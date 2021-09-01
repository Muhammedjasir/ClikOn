package com.tids.clikonservice.Utils;

public class Constant {

    Constant() {
        //write your action here if need
    }

    private static String domain = "api.techlogica.com";
    //    public static String BASE_URL="https://api.techlogica.com/moneytrftest/moneytrf/";
    public static String BASE_URL = "https://api.techlogica.com/MoneytrfNew/moneytrf/";

    //We will use this to store the user token number into shared preference
    public static final String SHARED_PREF_NAME = "com.tids.clikonservice"; //pcakage name+ id

    public static final String USER_USERNAME = "username";
    public static final String USER_NUMBER = "mobilenumber";  // mobile number
    public static final String USER_EMAIL = "email";
    public static final String USER_PASSWORD = "password";
    public static final String USER_GENDER = "gender";
    public static final String USER_PROFILE = "profile";
    public static final String USER_ONLINE_STATUS = "onlinestatus";  // 0-OFF & 1-ON


}

