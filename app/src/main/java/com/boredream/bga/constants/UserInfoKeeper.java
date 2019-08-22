package com.boredream.bga.constants;


import android.content.Context;
import android.content.SharedPreferences;

import com.boredream.bga.entity.User;
import com.boredream.bga.utils.StringUtils;
import com.google.gson.Gson;

/**
 * 用户信息保存工具
 */
public class UserInfoKeeper {

    private static volatile UserInfoKeeper instance = null;

    public static UserInfoKeeper getInstance() {
        if (instance == null) {
            synchronized (UserInfoKeeper.class) {
                if (instance == null) {
                    instance = new UserInfoKeeper();
                }
            }
        }
        return instance;
    }

    private UserInfoKeeper() {
        sp = AppKeeper.getApp().getSharedPreferences(CommonConstants.SP_NAME, Context.MODE_PRIVATE);
    }

    private SharedPreferences sp;
    private User currentUser;

    /**
     * 获取当前登录用户
     */
    public User getCurrentUser() {
        if (currentUser == null) {
            String json = sp.getString("user", null);
            if (!StringUtils.isEmpty(json)) {
                try {
                    currentUser = new Gson().fromJson(json, User.class);
                } catch (Exception e) {
                    //
                }
            }
        }
        return currentUser;
    }

    /**
     * 保存设置当前登录用户
     */
    public void setCurrentUser(User user) {
        // 密码信息不保存
        user.setPassword(null);

        currentUser = user;
        SharedPreferences sp = AppKeeper.getApp().getSharedPreferences(CommonConstants.SP_NAME, Context.MODE_PRIVATE);
        sp.edit().putString("user", new Gson().toJson(currentUser)).apply();
    }

    /**
     * 清空当前登录用户
     */
    public void clearCurrentUser() {
        currentUser = null;
        SharedPreferences sp = AppKeeper.getApp().getSharedPreferences(CommonConstants.SP_NAME, Context.MODE_PRIVATE);
        sp.edit().remove("user").apply();
    }

    public String getToken() {
        // 统一Header配置时用的token,没有的话要用空字符串,不能为null
        String token = "";
        User user = getCurrentUser();
//        if (user != null && user.getSessionToken() != null) {
//            token = user.getSessionToken();
//        }
        return token;
    }

    public boolean isLogin() {
        return getCurrentUser() != null;
    }

    /**
     * 检测登录状态
     *
     * @return true-已登录 false-未登录,会自动跳转至登录页
     */
//    public boolean checkLogin(Context context) {
//        if (!isLogin()) {
//            LoginActivity.start(context);
//            return false;
//        }
//        return true;
//    }
}
