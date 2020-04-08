package com.boredream.bga.net;


import android.util.Log;

import com.boredream.bga.constants.CommonConstants;
import com.boredream.bga.entity.Classroom;
import com.boredream.bga.entity.Course;
import com.boredream.bga.entity.User;
import com.boredream.bga.entity.UserProfile;
import com.boredream.bga.entity.UserLookupResponse;
import com.boredream.bga.utils.DateUtils;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.List;
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

        @POST("/v1/accounts:lookup")
        Observable<UserLookupResponse> lookup(
                @Body User user);

    }

    ////////////////////////////// 业务接口方法 //////////////////////////////

    /**
     * 创建课程。
     *
     * @param time      日期
     * @param classroom 教室
     * @param assistant 助教
     */
    public Observable<Course> createCourse(long time, Classroom classroom, UserProfile assistant) {
        String date = DateUtils.date2str(new Date(time), "yyyy-MM-dd");

        Course course = new Course();
        course.setAssistant(assistant);
        course.setClassroom(classroom);
        course.setDate(date);
        course.setStartTime(time);

        return Observable.create(emitter -> FirebaseFirestore.getInstance()
                .collection("course")
                .add(course)
                .addOnSuccessListener(documentReference -> course.setDocumentId(documentReference.getId()))
                .addOnFailureListener(emitter::onError)
                .addOnCompleteListener(task -> emitter.onComplete()));
    }

    public Observable<List<Course>> getCourseList() {
        return Observable.create(emitter -> FirebaseFirestore.getInstance()
                .collection("course")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> emitter.onNext(queryDocumentSnapshots.toObjects(Course.class)))
                .addOnFailureListener(emitter::onError)
                .addOnCompleteListener(task -> emitter.onComplete()));
    }

}

