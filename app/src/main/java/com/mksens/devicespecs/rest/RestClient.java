package com.mksens.devicespecs.rest;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

import com.mksens.devicespecs.R;
import com.mksens.devicespecs.rest.api.ApiService;

public class RestClient {
    private static final String API_URL = "http://148.6.80.94:8080";
    private static ApiService apiService;

    static {
        setupRestClient();
    }

    private RestClient() {}

    private static void setupRestClient() {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.addConverterFactory(GsonConverterFactory.create());
        builder.baseUrl(API_URL);

        Retrofit retrofit = builder.build();

        apiService = retrofit.create(ApiService.class);
    }

    public static ApiService get() {
        return apiService;
    }
}
