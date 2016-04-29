package com.dalingge.gankio.http;

import com.dalingge.gankio.bean.Constants;
import com.dalingge.gankio.bean.GirlBean;
import com.dalingge.gankio.bean.ResultBean;

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import rx.Observable;

/**
 * FileName: GankApi.java
 * description:
 * Author: dingby(445850053@qq.com)
 * Date: 2016/4/28
 */
public interface GankApi {

    @GET("data/{type}/" + Constants.PAZE_SIZE + "/{page}")
    Observable<ResultBean<List<GirlBean>>> getData(
            @Path("type") String type,
            @Path("page") int page);

    @GET("random/data/福利/{count}")
    Observable<ResultBean<List<GirlBean>>> getRandomImage(
            @Path("count") int count);

    @FormUrlEncoded
    @POST("add2gank")
    Observable<ResultBean> submit(
            @Field("url") String url,
            @Field("desc") String desc,
            @Field("who") String who,
            @Field("type") String type,
            @Field("debug") boolean debug);
}
