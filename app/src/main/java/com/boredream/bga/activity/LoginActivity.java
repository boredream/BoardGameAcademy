package com.boredream.bga.activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;

import com.boredream.bga.R;
import com.boredream.bga.constants.CommonConstants;
import com.boredream.bga.constants.ErrorConstants;
import com.boredream.bga.constants.UserInfoKeeper;
import com.boredream.bga.entity.User;
import com.boredream.bga.net.HttpRequest;
import com.boredream.bga.net.RxComposer;
import com.boredream.bga.net.SimpleDisObserver;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.et_username)
    EditText etUsername;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;

    private User user;

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initExtras();
        initView();
    }

    private void initExtras() {
    }

    private void initView() {
        ivClose.setOnClickListener(v -> finish());
        btnLogin.setOnClickListener(v -> login());
    }

    private void login() {
//        String username = etUsername.getText().toString().trim();
//        String password = etPassword.getText().toString().trim();
//
//        if (StringUtils.isEmpty(username)) {
//            showTip("用户名不能为空");
//            return;
//        }
//
//        if (StringUtils.isEmpty(password)) {
//            showTip("密码不能为空");
//            return;
//        }

        user = new User();
        user.setEmail("48262906@qq.com");
        user.setPassword("123456");
        user.setReturnSecureToken(true);

        HttpRequest.getInstance()
                .getApiService()
                .signInWithPassword(user)
                .compose(RxComposer.commonProgress(this))
                .subscribe(new SimpleDisObserver<User>() {
                    @Override
                    public void onNext(User user) {
                        UserInfoKeeper.getInstance().setCurrentUser(user);
                    }
                });
    }

    @Override
    public boolean handleError(Pair<String, String> throwable) {
        if(ErrorConstants.isTarError(throwable, ErrorConstants.EMAIL_NOT_FOUND)) {
            showSignUpDialog();
            return true;
        }
        return super.handleError(throwable);
    }

    private void showSignUpDialog() {
        new AlertDialog.Builder(this)
                .setMessage("邮箱不存在，请确认是否输入正确。如果是第一次使用，请选择确认注册。邮箱用于找回密码，请注意填写")
                .setPositiveButton("确认注册", (dialog, which) -> register(user))
                .setNegativeButton("重新填写", null)
                .show();
    }

    private void register(User user) {
        HttpRequest.getInstance()
                .getApiService()
                .signUp(user)
                .compose(RxComposer.commonProgress(this))
                .subscribe(new SimpleDisObserver<User>() {
                    @Override
                    public void onNext(User user) {
                        UserInfoKeeper.getInstance().setCurrentUser(user);
                    }
                });
    }

    private void checkFirstLogin(Throwable e, String username, String password) {
//        String errorInfo = ErrorConstants.parseHttpErrorInfo(this, e);
//        if ("Could not find user.".equals(errorInfo)) {
//            // 如果找不到用户代表第一次，则注册之
//            User user = new User();
//            user.setNickname(username);
//            user.setPassword(password);
//
//            HttpRequest.getInstance()
//                    .getApiService()
//                    .register(user)
//                    .compose(RxComposer.commonProgress(this))
//                    .subscribe(new SimpleDisObserver<User>() {
//                        @Override
//                        public void onNext(User user) {
//                            Toast.makeText(LoginActivity.this, "首次登录，已自动注册账号。下次直接用账号密码登录即可", Toast.LENGTH_LONG).show();
//                            loginSuccess(user);
//                        }
//                    });
//        } else {
//            // 其他正常错误直接显示
//            showTip(errorInfo);
//        }
    }

//    private void loginSuccess(User user) {
//        // 保存登录用户数据以及token信息
//        UserInfoKeeper.setCurrentUser(user);
//        finish();
//    }

}
