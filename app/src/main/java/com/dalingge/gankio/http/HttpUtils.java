package com.dalingge.gankio.http;

import com.dalingge.gankio.bean.Constants;
import com.dalingge.gankio.bean.GirlBean;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * FileName: HttpUtils.java
 * description:
 * Author: dingby(445850053@qq.com)
 * Date: 2016/4/28
 */
public class HttpUtils {

    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;
    private GankApi gankApi;

    //构造方法私有
    private HttpUtils() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Constants.API_URL)
                .build();

        gankApi = retrofit.create(GankApi.class);
    }

    //在访问HttpMethods时创建单例
    private static class Singleton{
        private static final HttpUtils INSTANCE = new HttpUtils();
    }

    //获取单例
    public static HttpUtils getInstance(){
        return Singleton.INSTANCE;
    }

    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s){
        o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T>   Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            if (httpResult.getCount() == 0) {
                throw new ExceptionApi(100);
            }
            return httpResult.getSubjects();
        }
    }


    public void getDate(Subscriber<GirlBean> subscriber, String type, int pageIndex){

//        movieService.getTopMovie(start, count)
//                .map(new HttpResultFunc<List<Subject>>())
//                .subscribeOn(Schedulers.io())
//                .unsubscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(subscriber);

        Observable observable = gankApi.getTopMovie(type, pageIndex)
                .map(new HttpResultFunc<GirlBean>());

        toSubscribe(observable, subscriber);
    }

}
