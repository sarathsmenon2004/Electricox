package com.example.electricox.COMMON;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient instance = null;
    private ApiService googleApiService;

    private static final String BASE_URL_GOOGLE = "https://maps.googleapis.com/maps/api/";

    private RetrofitClient() {
        googleApiService = getGoogleClient().create(ApiService.class);
    }

    private Retrofit getGoogleClient() {
        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL_GOOGLE)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public ApiService getGoogleApi() {
        return googleApiService;
    }
}
