package com.stone.stonenet;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.stone.stonenet.api.Api;
import com.stone.stonenet.bean.BaseRxBean;
import com.stone.stonenet.bean.MovieBean;
import com.stone.stonenet.bean.SubjectsBean;
import com.stone.stonenet.rx.BaseRxBeanUtil;
import com.stone.stonenet.rx.RxCompatException;
import com.stone.stonenet.rx.RxUtil;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * desc     : rxjava2 + retrofit2 + okhttp3 + gson
 * author   : stone
 * homepage : http://stone86.top
 * email    : aa86799@163.com
 * time     : 2018/6/11 18 37
 */
public class RrogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
//        loadMovies1();
//        loadMovies2();
//        loadMovies3();
        loadMovies4();
    }

    private void loadMovies1() {
        Api.getMovieApiService().getTopMovies1(0, 5)
            .compose(RxUtil.netUI())
            .subscribe(movie -> {
                List<SubjectsBean> list = movie.getSubjects();
                for (SubjectsBean sub : list) {
                    Log.d("stone1", "onNext: " + sub.getId() + "," + sub.getYear() + "," + sub.getTitle());
                }
            }, new RxCompatException<>(e -> {
                e.printStackTrace();
            }));
    }

    private void loadMovies2() {
        Api.getMovieApiService().getTopMovies2("top250", 10, 1)
                .compose(RxUtil.netUI())
                .subscribe(movie -> {
                    List<SubjectsBean> list = movie.getSubjects();
                    for (SubjectsBean sub : list) {
                        Log.d("stone2", "onNext: " + sub.getId() + "," + sub.getYear() + "," + sub.getTitle());
                    }
                }, new RxCompatException<>(e -> {
                    e.printStackTrace();
                }));
    }

    private void loadMovies3() {
        Api.getMovieApiService().getTopMovies3("top250",20, 10)
                .compose(RxUtil.netUIFb())
                .subscribe(movie -> {
                    List<SubjectsBean> list = movie.getSubjects();
                    for (SubjectsBean sub : list) {
                        Log.d("stone3", "onNext: " + sub.getId() + "," + sub.getYear() + "," + sub.getTitle());
                    }
                }, new RxCompatException<>(e -> {
                    e.printStackTrace();
                }));
    }

    /**
     * 使用基础 get 接口来执行 api 请求
     */
    private void loadMovies4() {
        Map<String, Object> params = new HashMap<>();
        params.put("start", 30);
        params.put("count", 5);

//        Api.getApiService().get(Api.MOVIE_BASE_URL + "top250", params)
//                .compose(RxUtil.netUI())
//                .subscribe(responseBody -> {
//                    StringReader reader = new StringReader(responseBody.string());
//                    JsonReader jsonReader = new JsonReader(reader);
//                    MovieBean movie = new Gson().fromJson(jsonReader, MovieBean.class);
//                    List<SubjectsBean> list = movie.getSubjects();
//                    for (SubjectsBean sub : list) {
//                        Log.d("stone4", "onNext: " + sub.getId() + "," + sub.getYear() + "," + sub.getTitle());
//                    }
//                }, new RxCompatException<>(e -> {
//                    e.printStackTrace();
//                }));

        get(HttpMethodType.GET, Api.MOVIE_BASE_URL + "top250", params, MovieBean.class, movie -> {
            List<SubjectsBean> list = movie.getSubjects();
            for (SubjectsBean sub : list) {
                Log.d("stone4", "onNext: " + sub.getId() + "," + sub.getYear() + "," + sub.getTitle());
            }
        });
    }

    /*
     * 对通用api方法 结合 rxjava 再次封装，方便使用
     */
    private <T> void get(HttpMethodType type, String url, Map<String, Object> params,
                                            final Class<T> clz, final ILoadSuccess<T> loadSuccess) {
//        Api.getApiService().get(url, params)
        generateObservable(type, url, params)
            .compose(RxUtil.netUI())
            .subscribe(responseBody -> {
                StringReader reader = new StringReader(responseBody.string());
                JsonReader jsonReader = new JsonReader(reader);
                T data = new Gson().fromJson(jsonReader, clz);
                if (loadSuccess != null) {
                    loadSuccess.onSuccessed(data);
                }
            }, new RxCompatException<>(e -> {
                e.printStackTrace();
            }));
    }

    enum HttpMethodType {
        GET,
        POST,
        DELETE,
        PUT,
        DOWNLOAD;
//        UPLOAD; //关于上传方法，因第二参数为 part，所以需要单独写一个，见下面两个 generateXxx 方法
    }

    Observable<ResponseBody> generateObservable(HttpMethodType type, String url, Map<String, Object> params) {
        switch (type) {
            case GET:
                return Api.getApiService().get(url, params);
            case POST:
                return Api.getApiService().post(url, params);
            case DELETE:
                return Api.getApiService().delete(url, params);
            case PUT:
                return Api.getApiService().put(url, params);
            case DOWNLOAD:
                return Api.getApiService().download(url, params);
            default:
                return null;
        }
    }

    Observable<ResponseBody> generateUploadObservable(@Url String url, @Part MultipartBody.Part part) {
        return Api.getApiService().upload(url, part);
    }

    interface ILoadSuccess<T> {
        void onSuccessed(T data);
    }

    /*
    * 如果对 BaseRxBean的子类进行请求；
    */
    private <T extends BaseRxBean> void get2(String url, Map<String, Object> params, final Class<T> clz,
                                            final ILoadSuccess<T> loadSuccess, final ILoadError loadError) {
        Api.getApiService().get(url, params)
                .compose(RxUtil.netUI())
                .subscribe(responseBody -> {
                    StringReader reader = new StringReader(responseBody.string());
                    JsonReader jsonReader = new JsonReader(reader);
                    T data = new Gson().fromJson(jsonReader, clz);
                    if (data.isSuccess()) {
                        if (loadSuccess != null) {
                            loadSuccess.onSuccessed(data);
                        }
                    } else {
                        if (loadError != null) {
                            loadError.onError(data.msg);
                        }
                    }

                }, new RxCompatException<>(e -> {
                    e.printStackTrace();
                }));
    }

    interface ILoadError {
        void onError(String message);
    }

}
