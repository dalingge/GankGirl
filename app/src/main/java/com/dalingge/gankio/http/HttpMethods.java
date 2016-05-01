package com.dalingge.gankio.http;

import com.dalingge.gankio.bean.Constants;
import com.dalingge.gankio.bean.GirlBean;
import com.dalingge.gankio.bean.ResultBean;

import java.util.List;
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
public class HttpMethods {

    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;
    private GankApi gankApi;

    //构造方法私有
    private HttpMethods() {
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
        private static final HttpMethods INSTANCE = new HttpMethods();
    }

    //获取单例
    public static HttpMethods getInstance(){
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
    private class HttpResultFunc<T> implements Func1<ResultBean<T>, T> {

        @Override
        public T call(ResultBean<T> httpResult) {
            if (httpResult.isError()) {
                throw new ExceptionApi(httpResult.getMsg());
            }
            return httpResult.getResults();
        }
    }


    /**
     *
     * @param subscriber
     * @param type
     * @param pageIndex
     */
    public void getData(Subscriber<List<GirlBean>> subscriber, String type, int pageIndex){

        Observable observable = gankApi.getData(type, pageIndex)
                .map(new HttpResultFunc<List<GirlBean>>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 随机图片
     * @param subscriber
     * @param count
     */
    public void getRandomImage(Subscriber<List<GirlBean>> subscriber,int count){

        Observable observable = gankApi.getRandomImage(count)
                .map(new HttpResultFunc<List<GirlBean>>());

        toSubscribe(observable, subscriber);
    }

    /**
     * 提交干货
     * @param subscriber
     * @param strUrl
     * @param strDesc
     * @param strWho
     * @param strType
     */
    public void submitGank(Subscriber<String> subscriber,String strUrl, String strDesc, String strWho, String strType){
        Observable observable = gankApi.submit(strUrl,strDesc,strWho,strType,false)
                .map(new Func1<ResultBean,String>() {
                    @Override
                    public String call(ResultBean resultBean) {
                        if (resultBean.isError()) {
                            throw new ExceptionApi(resultBean.getMsg());
                        }
                        return resultBean.getMsg();
                    }
                });

        toSubscribe(observable, subscriber);
    }

}
