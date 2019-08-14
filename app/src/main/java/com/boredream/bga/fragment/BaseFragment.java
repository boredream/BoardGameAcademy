package com.boredream.bga.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.boredream.bga.BaseView;
import com.boredream.bga.activity.BaseActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.trello.rxlifecycle3.components.support.RxFragment;

public class BaseFragment extends RxFragment implements BaseView {

    public String TAG;
    public BaseActivity activity;

    private SmartRefreshLayout refreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TAG = getClass().getSimpleName();
        activity = (BaseActivity) getActivity();
    }

    public void showLog(String msg) {
        Log.i(TAG, msg);
    }

    @Override
    public Activity getViewContext() {
        return activity;
    }

    @Override
    public void showTip(String msg) {
        activity.showTip(msg);
    }

    @Override
    public void showProgress() {
        activity.showProgress();
    }

    @Override
    public void dismissProgress() {
        activity.dismissProgress();
    }

    @Override
    public <T> LifecycleTransformer<T> getLifeCycleTransformer() {
        return bindUntilEvent(FragmentEvent.DESTROY_VIEW);
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
}
