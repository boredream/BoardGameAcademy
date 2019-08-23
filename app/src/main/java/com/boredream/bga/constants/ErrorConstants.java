package com.boredream.bga.constants;

import android.os.NetworkOnMainThreadException;
import android.util.Pair;

import com.boredream.bga.utils.NetUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.io.EOFException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class ErrorConstants {

    public static final String CODE_CUSTOMER = "CODE_CUSTOMER";

    public static final String INVALID_EMAIL = "INVALID_EMAIL";
    public static final String INVALID_PASSWORD = "INVALID_PASSWORD";
    public static final String ERROR_EMAIL_EXISTS = "EMAIL_EXISTS";
    public static final String EMAIL_NOT_FOUND = "EMAIL_NOT_FOUND";

    /**
     * 解析服务器错误信息
     */
    public static Pair<String, String> parseHttpErrorInfo(Throwable throwable) {
        Pair<String, String> pair = null;

        if (!NetUtils.isConnected(AppKeeper.getApp())) {
            pair = new Pair<>(CODE_CUSTOMER, "网络未连接");
        } else if (throwable instanceof HttpException) {
            try {
                // 如果是Retrofit的Http错误,则转换类型,获取信息
                HttpException exception = (HttpException) throwable;
                ResponseBody responseBody = exception.response().errorBody();
                MediaType type = responseBody.contentType();

                // 如果是application/json类型数据,则解析返回内容
                if (type != null && type.type().equals("application") && type.subtype().equals("json")) {
                    // 业务类错误
                    JsonElement errorJe = new JsonParser().parse(responseBody.string());
                    JsonObject errorJo = errorJe.getAsJsonObject().get("error").getAsJsonObject();

                    String message = errorJo.get("message").getAsString();
                    if (message != null) {
                        pair = new Pair<>(message, getFireBaseErrorChn(message));
                    }
                }
            } catch (Exception e) {
                //
            }
            if (pair == null) {
                pair = new Pair<>(CODE_CUSTOMER, "服务器错误 " + throwable.toString());
            }
        } else if (throwable instanceof NetworkOnMainThreadException) {
            pair = new Pair<>(CODE_CUSTOMER, "网络请求不能在主线程");
        } else if (throwable instanceof ConnectException) {
            pair = new Pair<>(CODE_CUSTOMER, "无法连接服务器");
        } else if (throwable instanceof UnknownHostException) {
            pair = new Pair<>(CODE_CUSTOMER, "服务器连接失败");
        } else if (throwable instanceof SocketTimeoutException) {
            pair = new Pair<>(CODE_CUSTOMER, "服务器连接超时");
        } else if (throwable.getMessage().equals("The mapper function returned a null value.")
                || throwable instanceof JsonParseException
                || throwable instanceof EOFException) {
            pair = new Pair<>(CODE_CUSTOMER, "数据解析错误 " + throwable.getMessage());
        }

        // 缺省处理
        if (pair == null || pair.first == null || pair.second == null) {
            pair = new Pair<>(CODE_CUSTOMER, "未知错误 " + throwable.toString());
        }

        return pair;
    }

    private static String getFireBaseErrorChn(String message) {
        String chn = message;
        switch (message) {
            case INVALID_EMAIL:
                chn = "邮箱地址格式错误";
                break;
            case INVALID_PASSWORD:
                chn = "密码错误";
                break;
        }
        return chn;
    }

    public static boolean isTarError(Pair<String, String> pair, String errorMessage) {
        if (pair == null || pair.first == null) return false;
        return pair.first.equals(errorMessage);
    }
}
