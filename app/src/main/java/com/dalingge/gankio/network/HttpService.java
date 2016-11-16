package com.dalingge.gankio.network;

import com.dalingge.gankio.bean.GirlBean;
import com.dalingge.gankio.common.Constants;
import com.dalingge.gankio.common.bean.GankBean;
import com.dalingge.gankio.common.bean.ResultBean;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

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

    @GET("random/data/福利/{count}")
    Observable<ResultBean<List<GirlBean>>> getRandomImage(
            @Path("count") int count);
}
