package com.tids.clikonservice.Utils.RetrofitUtils;

import com.tids.clikonservice.Utils.Constant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static String BASE_URL= Constant.BASE_URL;
    private static Retrofit retrofit=null;

    private static OkHttpClient okHttpClient=new OkHttpClient.Builder()
            .connectTimeout(190, TimeUnit.SECONDS)
            .writeTimeout(190, TimeUnit.SECONDS)
            .readTimeout(190, TimeUnit.SECONDS)
            .build();

    public static Retrofit getClient(){
        if (retrofit==null){
            retrofit=new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        }
        return retrofit;
    }

}
