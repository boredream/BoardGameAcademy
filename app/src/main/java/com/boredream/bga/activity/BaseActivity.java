package com.boredream.bga.activity;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import com.boredream.bga.BaseApplication;
import com.boredream.bga.BaseView;
import com.boredream.bga.R;
import com.boredream.bga.constants.CommonConstants;
import com.boredream.bga.constants.ErrorConstants;
import com.boredream.bga.utils.DialogUtils;
import com.boredream.bga.utils.TintBarUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

public abstract class BaseActivity extends RxAppCompatActivity implements BaseView {

    public String TAG;
    public Dialog progressDialog;
    public BaseApplication application;
    public SharedPreferences sp;

    private SmartRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setScreenOrientation();
        TintBarUtils.setWindowStatusBar(this, R.color.trans, true, true);

        // 如果是退出应用flag,则直接关闭当前页面,不加载UI
        boolean exit = getIntent().getBooleanExtra("exit", false);
        if (exit) {
            finish();
            return;
        }

        TAG = getClass().getSimpleName();
        progressDialog = DialogUtils.createProgressDialog(this);
        application = (BaseApplication) getApplication();
        sp = getSharedPreferences(CommonConstants.SP_NAME, MODE_PRIVATE);
    }

    protected void setScreenOrientation() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 跳转页面,无extra简易型
     *
     * @param tarActivity 目标页面
     */
    public void intent2Activity(Class<? extends Activity> tarActivity) {
        Intent intent = new Intent(this, tarActivity);
        startActivity(intent);
    }

    public void showLog(String msg) {
        Log.i(TAG, msg);
    }

    @Override
    public void showTip(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void dismissProgress() {
        progressDialog.dismiss();
    }

    @Override
    public <T> LifecycleTransformer<T> getLifeCycleTransformer() {
        return bindUntilEvent(ActivityEvent.DESTROY);
    }

    @Override
    public void setupRefreshLayout(SmartRefreshLayout refreshLayout) {
        this.refreshLayout = refreshLayout;
    }

    @Override
    public void showRefresh(boolean loadMore) {
        if (this.refreshLayout == null) {
            throw new RuntimeException("请在Activity中先调用 setupRefreshLayout 方法");
        }
        if (loadMore) refreshLayout.autoLoadmore();
        // else refreshLayout.autoRefresh();
    }

    @Override
    public void dismissRefresh(boolean loadMore) {
        if (this.refreshLayout == null) {
            throw new RuntimeException("请在Activity中先调用 setupRefreshLayout 方法");
        }
        if (loadMore) refreshLayout.finishLoadmore(200);
        else refreshLayout.finishRefresh();
    }

    @Override
    public boolean handleError(Pair<String, String> throwable) {
        return false;
    }

    /**
     * 退出程序
     */
    protected void exit() {
        // 退出程序方法有多种
        // 这里使用clear + new task的方式清空整个任务栈,只保留新打开的Main页面
        // 然后Main页面接收到退出的标志位exit=true,finish自己,这样就关闭了全部页面
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("exit", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
