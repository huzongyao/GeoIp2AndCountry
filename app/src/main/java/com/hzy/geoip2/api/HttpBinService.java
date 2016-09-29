package com.hzy.geoip2.api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by huzongrao on 16-9-22.
 */
public class HttpBinService {

    public static final String BASE_HTTP_BIN_URL = "http://httpbin.org/";

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_HTTP_BIN_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build();

    private HttpBinService() {
    }

    public static HttpBinApi create() {
        return retrofit.create(HttpBinApi.class);
    }
}
