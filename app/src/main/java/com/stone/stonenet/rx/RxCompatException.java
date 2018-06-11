package com.stone.stonenet.rx;

import com.stone.stonenet.util.ShowUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.functions.Consumer;

/**
 * desc     :
 * author   : stone
 * homepage : http://stone86.top
 * email    : aa86799@163.com
 * time     : 04/01/2018 19 57
 */
public class RxCompatException<T extends Throwable> implements Consumer<T> {

    private static final String TAG = "RxException";

    private static final String SOCKETTIMEOUTEXCEPTION = "网络连接超时，请检查您的网络状态，稍后重试";
    private static final String CONNECTEXCEPTION = "网络连接异常，请检查您的网络状态";
    private static final String UNKNOWNHOSTEXCEPTION = "网络异常，请检查您的网络状态";

    private Consumer<? super Throwable> onError;

    public RxCompatException(Consumer<? super Throwable> onError) {
        this.onError = onError;
    }

    @Override
    public void accept(T t) throws Exception {
        try {
            if (!NetInfoUtil.isNetworkAvailable()) {
                ShowUtils.toast("无网络连接");
            }

            if (t instanceof SocketTimeoutException) {
                //Log.e(TAG, "onError: SocketTimeoutException----" + SOCKETTIMEOUTEXCEPTION);
                onError.accept(new Throwable(SOCKETTIMEOUTEXCEPTION));
            } else if (t instanceof ConnectException) {
                //Log.e(TAG, "onError: ConnectException-----" + CONNECTEXCEPTION);
                onError.accept(new Throwable(CONNECTEXCEPTION));
            } else if (t instanceof UnknownHostException) {
                //Log.e(TAG, "onError: UnknownHostException-----" + UNKNOWNHOSTEXCEPTION);
                onError.accept(new Throwable(UNKNOWNHOSTEXCEPTION));
            } else {
                //Log.e(TAG, "onError:----" + t.getMessage());
                onError.accept(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
