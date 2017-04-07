package com.dalingge.gankio.network;

import com.dalingge.gankio.common.Constants;
import com.dalingge.gankio.common.bean.GankBean;
import com.dalingge.gankio.common.bean.ResultBean;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * FileName: HttpService
 * description:
 * Author: 丁博洋
 * Date: 2016/9/12
 */
public interface HttpService {

    @GET("data/{type}/" + Constants.PAZE_SIZE + "/{page}")
    Observable<ResultBean<List<GankBean>>> getData(
            @Path("type") String type,
            @Path("page") int page);

    @GET("random/data/Android/{count}")
    Observable<ResultBean<List<GankBean>>> getRandomImage(
            @Path("count") int count);

    @FormUrlEncoded
    @POST("add2gank")
    Observable<ResultBean> submit(
            @Field("url") String url,
            @Field("desc") String desc,
            @Field("who") String who,
            @Field("type") String type,
            @Field("debug") boolean debug);

    @GET("search/query/{info}/category/all/count/" + Constants.PAZE_SIZE + "/page/{page} ")
    Observable<ResultBean<List<GankBean>>> getSearchQuery(
            @Path("info") String info,
            @Path("page") int page);
}
