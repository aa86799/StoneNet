package com.stone.rrog;

import android.content.Context;
import android.net.NetworkInfo;
import android.support.v4.util.ArrayMap;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * desc     :
 * author   : stone
 * homepage : http://stone86.top
 * email    : aa86799@163.com
 * time     : 2018/6/11 17 57
 */
public class SRetrofit {

    private OkHttpClient mClient;
    private ArrayMap<String, Retrofit> mRetrofitMap = new ArrayMap<>();

    private OkHttpClient getClient(Context context) {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.i("sokhttp-> ", message);
            }
        });
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        //缓存文件夹
        File cacheFile = new File(context.getApplicationContext().getExternalCacheDir(),"cache");
        //缓存大小为10M
        int cacheSize = 10 * 1024 * 1024;
        //创建缓存对象
        Cache cache = new Cache(cacheFile,cacheSize);

        mClient = new OkHttpClient.Builder()
                .readTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(logInterceptor)
//                .writeTimeout()
                .cache(cache) //缓存，需要设置缓存文件
//                .cookieJar() //设置 响应和请求中的 cookie
//                .connectionPool() //连接池
//                .sslSocketFactory() //访问 http， ssl-socket 相关规则
//                .authenticator() //token 认证
                .build();
        return mClient;
    }

    private static class Builder {
        private static final SRetrofit instance = new SRetrofit();
    }

    private SRetrofit() {

    }

    private static SRetrofit getInstance() {
        return Builder.instance;
    }


    public static Retrofit get(String baseUrl, Context context) {
        if (getInstance().mRetrofitMap == null) getInstance().mRetrofitMap = new ArrayMap<>();
        Retrofit retrofit = getInstance().mRetrofitMap.get(baseUrl);
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getInstance().getClient(context))
                    .build();
            getInstance().mRetrofitMap.put(baseUrl, retrofit);
        }
        return retrofit;
    }


}
