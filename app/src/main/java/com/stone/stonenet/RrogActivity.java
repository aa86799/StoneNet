package com.stone.stonenet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.stone.stonenet.api.Api;
import com.stone.stonenet.bean.MovieBean;
import com.stone.stonenet.bean.SubjectsBean;
import com.stone.stonenet.rx.RxCompatException;
import com.stone.stonenet.rx.RxUtil;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private void loadMovies4() {
        Map<String, Object> params = new HashMap<>();
        params.put("start", 30);
        params.put("count", 5);
        Api.getApiService().get(Api.MOVIE_BASE_URL + "top250", params)
                .compose(RxUtil.netUI())
                .subscribe(responseBody -> {
                    StringReader reader = new StringReader(responseBody.string());
                    JsonReader jsonReader = new JsonReader(reader);
                    MovieBean movie = new Gson().fromJson(jsonReader, MovieBean.class);
                    List<SubjectsBean> list = movie.getSubjects();
                    for (SubjectsBean sub : list) {
                        Log.d("stone4", "onNext: " + sub.getId() + "," + sub.getYear() + "," + sub.getTitle());
                    }
                }, new RxCompatException<>(e -> {
                    e.printStackTrace();
                }));
    }
}
