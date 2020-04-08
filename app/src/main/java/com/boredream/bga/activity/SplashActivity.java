package com.boredream.bga.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.boredream.bga.R;
import com.boredream.bga.constants.AppKeeper;
import com.boredream.bga.constants.UserInfoKeeper;
import com.boredream.bga.entity.User;
import com.boredream.bga.entity.UserLookupResponse;
import com.boredream.bga.net.HttpRequest;
import com.boredream.bga.net.RxComposer;
import com.boredream.bga.net.SimpleDisObserver;
import com.boredream.bga.utils.CollectionUtils;
import com.boredream.bga.utils.DialogUtils;
import com.yanzhenjie.permission.AndPermission;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {

    private static final long DUR_TIME_MIN = 2000;

    @BindView(R.id.iv_splash)
    ImageView ivSplash;

    private long startLoginTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        // 避免从桌面启动程序后，会重新实例化入口类的activity
        if (!this.isTaskRoot()) {
            Intent intent = getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                    finish();
                    return;
                }
            }
        }

        setContentView(R.layout.activity_splash);

        init();
    }

    private void init() {
        ButterKnife.bind(this);
        AppKeeper.isNormalEnter = true;
        checkPermission();
    }

    private void checkPermission() {
        AndPermission.with(SplashActivity.this)
                .runtime()
                .permission(Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .onGranted(permissions -> {
                    try {
                        startLogin();
                    } catch (Exception e) {
                        DialogUtils.showConfirmDialog(this, e.toString(), (dialog, which) -> finish());
                    }
                })
                .onDenied(permissions -> {
                    Toast.makeText(this, "请同意应用的必要权限申请，否则无法正常使用App！", Toast.LENGTH_LONG).show();
                    finish();
                })
                .start();
    }

    private void startLogin() {
        startLoginTime = SystemClock.elapsedRealtime();
        // 自动登陆
        User user = new User();
        user.setIdToken(UserInfoKeeper.getInstance().getToken());

        HttpRequest.getInstance()
                .getApiService()
                .lookup(user)
                .compose(RxComposer.commonSilence(this))
                .subscribe(new SimpleDisObserver<UserLookupResponse>() {
                    @Override
                    public void onNext(UserLookupResponse response) {
                        onLookupSuccess(response);
                    }

                    @Override
                    public void onError(Throwable e) {
                        loginComplete(null);
                    }
                });
    }

    private void onLookupSuccess(UserLookupResponse response) {
        ArrayList<UserLookupResponse.FirebaseUser> users = response.getUsers();

        User user = null;
        if (!CollectionUtils.isEmpty(users)) {
            // TODO: chunyang 2019-08-26 update info
            user = UserInfoKeeper.getInstance().getCurrentUser();
        }
        loginComplete(user);
    }

    private void loginComplete(User user) {
        long delayTime = SystemClock.elapsedRealtime() - startLoginTime;
        if (delayTime > DUR_TIME_MIN) {
            delayTime = 0;
        } else {
            delayTime = DUR_TIME_MIN - delayTime;
        }

        ivSplash.postDelayed(() -> {
            finish();
            if (user != null) {
                UserInfoKeeper.getInstance().setCurrentUser(user);
                MainActivity.start(this);
            } else {
                LoginActivity.start(this);
            }
        }, delayTime);
    }
}
