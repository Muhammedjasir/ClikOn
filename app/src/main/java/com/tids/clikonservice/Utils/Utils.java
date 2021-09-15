package com.tids.clikonservice.Utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivity = (ConnectivityManager) activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String parseServerDateTime(String input) {
        String output = "";
        if (!input.isEmpty()) {
            try {
                SimpleDateFormat format = new SimpleDateFormat(DateTimeFormats.SERVER_DATE_TIME_FORMAT, Locale.ENGLISH);
                Date newDate = format.parse(input);

                format = new SimpleDateFormat(DateTimeFormats.APP_DATE_TIME_FORMAT, Locale.ENGLISH);
                output = format.format(newDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return output;
    }

}
