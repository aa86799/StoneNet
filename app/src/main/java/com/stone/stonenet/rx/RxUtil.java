package com.stone.stonenet.rx;


import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * desc     :
 * author   : stone
 * homepage : http://stone86.top
 * email    : aa86799@163.com
 * time     : 04/01/2018 17 38
 */
public class RxUtil {

    /**
     * 切换线程  供 compose 使用；将之前操作压入异步线程
     * 适用 上游是 Observable
     */
    public static <T> ObservableTransformer<T, T> net() {
//       return new ObservableTransformer<T, T>() {
//            @Override
//            public ObservableSource<T> apply(Observable<T> upstream) {
//                return upstream;
//            }
//        };
        return upstream -> upstream.subscribeOn(Schedulers.newThread());
    }

    /**
     * 切换线程  供 compose 使用；将之前操作压入异步线程
     * 适用 上游是 Flowable
     */
    public static <T> FlowableTransformer<T, T> netFb() {
        return upstream -> upstream.subscribeOn(Schedulers.newThread());
    }

    /**
     * 切换线程  供 compose 使用； 将之前操作压入异步线程，后续操作压入UI线程
     * 适用 上游是 Observable
     */
    public static <T> ObservableTransformer<T, T> netUI() {
        return upstream -> upstream.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 切换线程  供 compose 使用； 将之前操作压入异步线程，后续操作压入UI线程
     * 适用 上游是 Flowable
     */
    public static <T> FlowableTransformer<T, T> netUIFb() {
        return upstream -> upstream.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private static volatile Map<String, Set<WeakReference<CompositeDisposable>>> sMap;

    private static Map<String, Set<WeakReference<CompositeDisposable>>> getSubMap() {
        if (sMap == null) {
            synchronized (RxUtil.class) {
                if (sMap == null) {
                    sMap = Collections.synchronizedSortedMap(new TreeMap<>());
                }
            }
        }
        return sMap;
    }

    /**
     * 根据clz 缓存对应的 CompositeDisposable - Set
     *
     * @param clz
     * @return
     */
    public static void addSubSet(Class clz) {
        Set<WeakReference<CompositeDisposable>> set = new HashSet<>();
        getSubMap().put(clz.toString(), set);
    }

    /**
     * 缓存CompositeDisposable
     *
     * @param clz
     * @param sub
     */
    public static void addSub(Class clz, CompositeDisposable sub) {
        Set<WeakReference<CompositeDisposable>> set = getSubMap().get(clz.toString());
        if (set != null) {
            set.add(new WeakReference<>(sub));
        }
    }

    /**
     * 取消clz 对应的 CompositeDisposable
     *
     * @param clz
     */
    public static void cancelAllSub(Class clz) {
        final String tag = clz.toString();
        Set<WeakReference<CompositeDisposable>> set = getSubMap().get(tag);
        if (set != null) {
            for (WeakReference<CompositeDisposable> next : set) {
                CompositeDisposable sub = next.get();
                if (sub != null && !sub.isDisposed()) {
                    sub.dispose();
                }
            }
            set.clear();
            getSubMap().remove(tag);
        }
    }

    /**
     * 判断 disposable 是否是订阅状态
     *
     * @param disposable
     * @return true 是
     */
    public static boolean isSubscribing(CompositeDisposable disposable) {
        return disposable != null && !disposable.isDisposed();
    }

    /**
     * 取消指定的订阅
     *
     * @param disposable
     */
    public static void unsubscribe(CompositeDisposable disposable) {
        if (disposable != null && !disposable.isDisposed()) disposable.dispose();
    }

    /**
     * 返回一个Observable (外部调用时，可用函数式调用方式)
     * 如，可将DB的一些操作置入action中
     * <p>
     * tips: 本方法，正常执行后，内部返回true，
     *
     * @param action
     * @return
     */
    public static Observable<Boolean> opDb(Action action) {
        return Observable.create(e -> {
            action.run();
            e.onNext(Boolean.TRUE);
        });
    }

    /**
     * 与上例类似， 区别在于， 可以将 T 变换 为 R
     * <p>
     * (Function类似的 类还有 Function3、Function4...  等， 需要多个T型 形参)
     *
     * @param function
     * @param t
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T, R> Observable<R> opDb(Function<T, R> function, T t) {
        return Observable.create(e -> e.onNext(function.apply(t)));
    }
}
