package com.dalingge.gankio.http;

import com.dalingge.gankio.bean.Constants;
import com.dalingge.gankio.bean.GirlBean;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * FileName: GankApi.java
 * description:
 * Author: dingby(445850053@qq.com)
 * Date: 2016/4/28
 */
public interface GankApi {

    @GET("data/{type}/"+ Constants.PAZE_SIZE+"/{page}")
    Observable<HttpResult<GirlBean>> getTopMovie(
            @Path("type") String type,
            @Path("page") int page);
}
