package com.boredream.bga.net;


import com.boredream.bga.constants.CommonConstants;
import com.boredream.bga.entity.User;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class HttpRequest {

    private static volatile HttpRequest instance = null;

    public static HttpRequest getInstance() {
        if (instance == null) {
            synchronized (HttpRequest.class) {
                if (instance == null) {
                    instance = new HttpRequest();
                }
            }
        }
        return instance;
    }

    private OkHttpClient client;
    private ApiService service;

    private HttpRequest() {
        // header
        Interceptor headerInterceptor = chain -> {
            HttpUrl newUrl = chain.request().url().newBuilder()
                    .addQueryParameter("key", CommonConstants.API_KEY)
                    .build();

            Request request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("X-Firebase-Locale", "zh")
                    .url(newUrl)
                    .build();
            return chain.proceed(request);
        };

        // log
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        int timeout = 20;
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS);
        builder.addInterceptor(headerInterceptor);
        builder.addInterceptor(logInterceptor);

        // Retrofit
        client = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://identitytoolkit.googleapis.com")
                .addConverterFactory(GsonConverterFactory.create()) // gson
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // rxjava
                .client(client)
                .build();

        service = retrofit.create(ApiService.class);
    }

    public ApiService getApiService() {
        return service;
    }

    public interface ApiService {
        ////////////////////////////// 通用接口 //////////////////////////////

        @POST("/v1/accounts:signInWithPassword")
        Observable<User> signInWithPassword(
                @Body User user);

        @POST("/v1/accounts:signUp")
        Observable<User> signUp(
                @Body User user);

    }

    ////////////////////////////// 业务接口方法 //////////////////////////////

}

