package com.boredream.bga.constants;

import android.content.Context;
import android.os.NetworkOnMainThreadException;

import com.boredream.bga.utils.NetUtils;
import com.boredream.bga.utils.StringUtils;
import com.google.gson.JsonParseException;

import java.io.EOFException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

public class ErrorConstants {

    /**
     * 解析服务器错误信息
     */
    public static String parseHttpErrorInfo(Context context, Throwable throwable) {
        String errorInfo = throwable.getMessage();

        if (!NetUtils.isConnected(context)) {
            errorInfo = "网络未连接";
        } else if (throwable instanceof HttpException) {
            // 如果是Retrofit的Http错误,则转换类型,获取信息
            HttpException exception = (HttpException) throwable;
            ResponseBody responseBody = exception.response().errorBody();
            MediaType type = responseBody.contentType();

            // FIXME: 2019/8/14
            // 如果是application/json类型数据,则解析返回内容
//            String leanError = null;
//            if (type != null && type.type().equals("application") && type.subtype().equals("json")) {
//                try {
//                    // 这里的返回内容是Bmob/AVOS/Parse等RestFul API文档中的错误代码和错误信息对象
//                    ErrorResponse errorResponse = new Gson().fromJson(responseBody.string(), ErrorResponse.class);
//                    String errorStr = errorResponse.getError();
//                    if(StringUtils.isContainChinese(errorStr)) {
//                        leanError = errorStr;
//                    } else {
//                        leanError = errorResponse.getCode() + " : " + errorStr;
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            if (StringUtils.isEmpty(leanError)) {
//                errorInfo = "服务器错误" + exception.code();
//            } else {
//                errorInfo = leanError;
//            }
        } else if (throwable instanceof NetworkOnMainThreadException) {
            errorInfo = "网络请求不能在主线程";
        } else if (throwable instanceof ConnectException) {
            errorInfo = "无法连接服务器";
        } else if (throwable instanceof UnknownHostException) {
            errorInfo = "服务器连接失败";
        } else if (throwable instanceof SocketTimeoutException) {
            errorInfo = "服务器连接超时";
        } else if (throwable.getMessage().equals("The mapper function returned a null value.")
                || throwable instanceof JsonParseException
                || throwable instanceof EOFException) {
            errorInfo = "数据解析错误 " + throwable.getMessage();
        }

        // 缺省处理
        if (StringUtils.isEmpty(errorInfo)) {
            errorInfo = "未知错误 " + throwable.getMessage();
        }

        return errorInfo;
    }
}
