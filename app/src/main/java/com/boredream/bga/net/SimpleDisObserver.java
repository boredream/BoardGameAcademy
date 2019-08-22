package com.boredream.bga.net;


import io.reactivex.annotations.NonNull;
import io.reactivex.observers.DisposableObserver;

/**
 * 通用订阅者,用于统一处理回调
 */
public class SimpleDisObserver<T> extends DisposableObserver<T> {
    @Override
    public void onNext(@NonNull T t) {

    }

    @Override
    public void onError(@NonNull Throwable e) {

    }

    @Override
    public void onComplete() {

    }

}
