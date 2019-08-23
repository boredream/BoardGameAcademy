package com.boredream.bga.net;



import android.util.Pair;

import com.boredream.bga.BaseView;
import com.boredream.bga.constants.ErrorConstants;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Rx組件。一般情况下直接使用组合类compose，如果有特殊需要自行组装基础compose
 */
public class RxComposer {

    ////////////////////////////// 常用组合compose //////////////////////////////

    /**
     * 常规显示进度框样式
     */
    public static <T> ObservableTransformer<T, T> commonSilence(final BaseView view) {
        return upstream -> upstream.compose(schedulers())
                .compose(lifecycle(view));
    }

    /**
     * 常规显示进度框样式
     */
    public static <T> ObservableTransformer<T, T> commonProgress(final BaseView view) {
        return upstream -> upstream.compose(schedulers())
                .compose(lifecycle(view))
                .compose(defaultFailed(view))
                .compose(handleProgress(view));
    }

    /**
     * 常规refresh刷新列表样式
     */
    public static <T> ObservableTransformer<T, T> commonRefresh(BaseView view, boolean loadMore) {
        return upstream -> upstream.compose(schedulers())
                .compose(lifecycle(view))
                .compose(defaultFailed(view))
                .compose(handleRefresh(view, loadMore));
    }

    ////////////////////////////// 基础compose //////////////////////////////

    /**
     * schedulers线程分发处理
     */
    public static <T> ObservableTransformer<T, T> schedulers() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * lifecycle防止页面销毁后继续回调处理view
     */
    public static <T> ObservableTransformer<T, T> lifecycle(final BaseView view) {
        return upstream -> upstream
                .compose(view.<T>getLifeCycleTransformer());
    }

    /**
     * error统一处理，自动提示Toast
     */
    public static <T> ObservableTransformer<T, T> defaultFailed(final BaseView view) {
        return upstream -> upstream.observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    Pair<String, String> pair = ErrorConstants.parseHttpErrorInfo(throwable);
                    if(!view.handleError(pair)) {
                        view.showTip(pair.second);
                    }
                });
    }

    /**
     * 进度框统一处理，发送请求时自动 showProgress，请求成功/失败时自动 dismissProgress
     */
    public static <T> ObservableTransformer<T, T> handleProgress(final BaseView view) {
        return upstream -> upstream.observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> view.showProgress())
                .doOnError(throwable -> view.dismissProgress())
                .doOnNext((Consumer<T>) t -> view.dismissProgress());
    }

    /**
     * 刷新控件统一处理，发送请求时自动 showRefresh，请求成功/失败时自动 dismissRefresh
     *
     * @param loadMore    true-加载更多, false-下拉刷新
     */
    public static <T> ObservableTransformer<T, T> handleRefresh(final BaseView view, boolean loadMore) {
        return upstream -> upstream.observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> view.showRefresh(loadMore))
                .doOnError(throwable -> view.dismissRefresh(loadMore))
                .doOnNext((Consumer<T>) t -> view.dismissRefresh(loadMore));
    }
}
