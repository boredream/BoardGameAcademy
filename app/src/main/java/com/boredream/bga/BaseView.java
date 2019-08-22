package com.boredream.bga;

import android.util.Pair;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.trello.rxlifecycle3.LifecycleTransformer;

public interface BaseView {

    void showTip(String msg);

    void showProgress();

    void dismissProgress();

    <T> LifecycleTransformer<T> getLifeCycleTransformer();

    void setupRefreshLayout(SmartRefreshLayout refreshLayout);

    void showRefresh(boolean loadMore);

    void dismissRefresh(boolean loadMore);

    boolean handleError(Pair<String, String> throwable);

}
