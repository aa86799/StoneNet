package com.stone.stonenet.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stone.rrog.SRetrofit;
import com.stone.stonenet.MainApplication;

import java.util.List;

import retrofit2.Retrofit;

/**
 * desc     :
 * author   : stone
 * homepage : http://stone86.top
 * email    : aa86799@163.com
 * time     : 2018/6/11 18 42
 */
public class Api {

    public static final String MOVIE_BASE_URL = "https://api.douban.com/v2/movie/";

    private Retrofit mRetrofit;
    private Gson mGson;

    private Api() {
        mRetrofit = SRetrofit.get(MOVIE_BASE_URL, MainApplication.getInstance());
        mGson = new Gson();
    }

    private static class Builder {
        private static Api instance = new Api();
    }

    private static Api getInstance() {
        return Builder.instance;
    }

    public static ApiMovieService getMovieApiService() {
        return getInstance().mRetrofit.create(ApiMovieService.class);
    }

    public static ApiService getApiService() {
        return getInstance().mRetrofit.create(ApiService.class);
    }

    public static <T> T jsonToBean(String json, Class<T> clz) {
        return getInstance().mGson.fromJson(json, clz);
    }

    public static <T> List<T> jsonToListBean(String json, Class<T> clz) {
        return getInstance().mGson.fromJson(json, new TypeToken<List<T>>(){}.getType());
    }
}
