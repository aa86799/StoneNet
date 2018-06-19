package com.stone.stonenet.rx;

import com.stone.stonenet.bean.BaseRxBean;
import com.stone.stonenet.bean.TestBean;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;

/**
 * desc     :
 * author   : stone
 * homepage : http://stone86.top
 * email    : aa86799@163.com
 * time     : 2018/6/19 16 44
 */
public class BaseRxBeanUtil {


    /**
     * 使用示例
     */
    private void testExample() {
        /* 例1： 一般情形中，构建异步任务 */
        RxUtil.opDb(() -> {
            //do some things.
        }).compose(RxUtil.net()).subscribe(result -> {});

        /* 例2： 网络接口
         *  下面的  Observable.just(new BaseRxBean<EraserBean>())  以相关api获取 Observable 替换
         *
         */
        Observable
                .just(new TestBean()) //<T>  T must implements Serializable
                .compose(RxUtil.netUI()) //线程切换
                .compose(BaseRxBeanUtil.handleResult()) //结果统一处理
                .subscribe(result -> { /* do some things. */ },
                        throwable -> new RxCompatException<>(e -> {e.printStackTrace(); /* do error things */})
                );
    }

    /**
     * 统一处理接口返回数据
     * 前提：server返回接口遵循统一的类似 定义在BaseRxBean中的格式
     *
     * 适用 上游是 Observable
     * @param <T extends BaseRxBean>
     * @return
     */
    public static <T extends BaseRxBean> ObservableTransformer<T, T> handleResult() {
        return upstream -> {
            return upstream.flatMap(result -> {
                        if (result.isSuccess()) {
                            return createData(result);
                        } else {
                            return Observable.error(new Exception(result.msg));
                        }
                    }
            );
        };
    }

    private static <T extends BaseRxBean> Observable<T> createData(final T t) {
        return Observable.create(subscriber -> {
            try {
                subscriber.onNext(t);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        });
    }

    /**
     * 统一处理接口返回数据
     * 前提：server返回接口遵循统一的类似 定义在BaseRxBean中的格式
     *
     * 适用 上游是 Flowable
     * @param <T extends BaseRxBean>
     * @return
     */
    public static <T extends BaseRxBean> FlowableTransformer<T, T> handleResultFb() {
        return upstream -> {
            return upstream.flatMap(result -> {
                        if (result.isSuccess()) {
                            return createDataFb(result);
                        } else {
                            return Flowable.error(new Exception(result.msg));
                        }
                    }
            );
        };
    }

    private static <T extends BaseRxBean> Flowable<T> createDataFb(final T t) {
        return Flowable.create(subscriber -> {
            try {
                subscriber.onNext(t);
                subscriber.onComplete();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        }, BackpressureStrategy.LATEST);//处理事件的缓冲区策略：如果忙不过来，仅处理最新的数据
    }

}
