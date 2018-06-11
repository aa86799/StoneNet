package com.stone.stonenet.api;

import com.stone.rrog.SRetrofit;
import com.stone.stonenet.MainApplication;

import retrofit2.Retrofit;

/**
 * desc     :
 * author   : stone
 * homepage : http://stone86.top
 * email    : aa86799@163.com
 * time     : 2018/6/11 18 42
 */
public class Api {

    private static final String MOVIE_BASE_URL = "https://api.douban.com/v2/movie/";

    private Retrofit mRetrofit;

    private Api() {
        mRetrofit = SRetrofit.get(MOVIE_BASE_URL, MainApplication.getInstance());
    }

    private static class Builder {
        private static Api instance = new Api();
    }

    private static Api getInstance() {
        return Builder.instance;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public static ApiMovieService getMovieApiService() {
        return getInstance().getRetrofit().create(ApiMovieService.class);
    }
}
