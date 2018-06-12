package com.stone.stonenet.api;


import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * desc     :
 * author   : stone
 * homepage : http://stone86.top
 * email    : aa86799@163.com
 * time     : 2018/6/12 00 12
 */
public interface ApiService {

// Retrofit不支持二次泛型， 会报java.lang.IllegalArgumentException:
//      Method return type must not include a type variable or wildcard: io.reactivex.Observable<T>
//    @GET
//    <T> Observable<T> get(@Url String url, @QueryMap Map<String, Object> params);

    /*
     * https://www.jianshu.com/p/8a67302a3377  retrofit的坑
     * 当通用 service 中的泛型为 String 时，会被内部的 json converter 转成 json obj;
     * 再配置 gson-converter 来转换，会报错
     *
     * 需要改成 ResponseBody ，用 string()，再转成 java bean
     */

    @GET
    Observable<ResponseBody> get(@Url String url, @QueryMap Map<String, Object> params);

    @FormUrlEncoded
    @POST
    Observable<ResponseBody> post(@Url String url, @FieldMap Map<String, Object> params);

    @DELETE
    Observable<ResponseBody> delete(@Url String url, @QueryMap Map<String, Object> params);

    @FormUrlEncoded
    @PUT
    Observable<ResponseBody> put(@Url String url, @FieldMap Map<String, Object> params);

    @Streaming //Retrofit 默认会一次性在内存中下载后，再写入成文件； 使用@Streaming  边下载，边写入
    @GET
    Observable<ResponseBody> download(@Url String url, @QueryMap Map<String, Object> params);

    @Multipart
    @POST
    Observable<ResponseBody> upload(@Url String url, @Part MultipartBody.Part part);
}
