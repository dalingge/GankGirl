package com.dalingge.gankio.network;

import android.content.Context;

import com.dalingge.gankio.GankApp;
import com.dalingge.gankio.common.Constants;
import com.dalingge.gankio.common.bean.ResultBean;
import com.dalingge.gankio.common.utils.FileUtils;
import com.dalingge.gankio.common.utils.NetWorkUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * FileName: HttpRetrofit
 * description:网络请求服务
 * Author: 丁博洋
 * Date: 2016/9/12
 */
public class HttpRetrofit {
    public static final int DEFAULT_TIMEOUT = 6;

    public Retrofit retrofit;
    public HttpService apiService;

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final HttpRetrofit INSTANCE = new HttpRetrofit();
    }

    //获取单例
    public static HttpRetrofit getInstance() {
        return SingletonHolder.INSTANCE;
    }

    //构造方法私有
    private HttpRetrofit() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").serializeNulls().create();
        retrofit = new Retrofit.Builder()
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(gson)) //  添加数据解析ConverterFactory
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //添加RxJava
                .baseUrl(Constants.API_URL)
                .build();
        apiService = retrofit.create(HttpService.class);
    }


    /**
     * 配置OKHTTP
     *
     * @return OkHttpClient
     */
    private OkHttpClient getOkHttpClient() {
        File httpCacheDirectory = new File(FileUtils.getCacheDir(GankApp.context()), "OkHttpCache"); //设置缓存
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();//拦截器
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();  //定制OkHttp
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS); //设置连接超时
        httpClientBuilder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);   //设置写入超时
        httpClientBuilder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);    //设置读取超时
        httpClientBuilder.cache(new Cache(httpCacheDirectory, 10 * 1024 * 1024));//设置缓存目录和10M缓存
//        int[] certificates = {R.raw.srca};
//        httpClientBuilder.socketFactory(getSSLSocketFactory(AppContext._context, certificates));
//        String hosts[] = {Constants.API_URL};
//        httpClientBuilder.hostnameVerifier(getHostnameVerifier(hosts));
        httpClientBuilder.addInterceptor(interceptor); //添加日志拦截器（该方法也可以设置公共参数，头信息）
        httpClientBuilder.addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR);
        //httpClientBuilder.retryOnConnectionFailure(true); //错误重连
        return httpClientBuilder.build();
    }


    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = chain -> {

        CacheControl.Builder cacheBuilder = new CacheControl.Builder();
        cacheBuilder.maxAge(0, TimeUnit.SECONDS);
        cacheBuilder.maxStale(365, TimeUnit.DAYS);
        CacheControl cacheControl = cacheBuilder.build();
        Request request = chain.request();
        if (!NetWorkUtils.isConnectedByState(GankApp.context())) {
            request = request.newBuilder()
                    .cacheControl(cacheControl)
                    .build();
        }
        Response originalResponse = chain.proceed(request);
        if (NetWorkUtils.isConnectedByState(GankApp.context())) {
            int maxAge = 0; // 从缓存中读取
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public ,max-age=" + maxAge)
                    .build();
        } else {
            int maxStale = 60 * 60 * 24 * 28; //缓存4周
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
    };


    /**
     * 绑定证书
     *
     * @param context      上下文
     * @param certificates 证书源
     * @return
     */
    private static SSLSocketFactory getSSLSocketFactory(Context context, int[] certificates) {
        if (context == null) {
            throw new NullPointerException("context == null");
        }

        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);

            for (int i = 0; i < certificates.length; i++) {
                InputStream certificate = context.getResources().openRawResource(certificates[i]);
                keyStore.setCertificateEntry(String.valueOf(i), certificateFactory.generateCertificate(certificate));
                if (certificate != null) {
                    certificate.close();
                }
            }
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (CertificateException | KeyStoreException | IOException | NoSuchAlgorithmException | KeyManagementException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * @param hostUrls
     * @return
     */
    private static HostnameVerifier getHostnameVerifier(final String[] hostUrls) {
        return (hostname, session) -> {
            boolean ret = false;
            for (String host : hostUrls) {
                if (host.equalsIgnoreCase(hostname)) {
                    ret = true;
                }
            }
            return ret;
        };
    }

    /**
     * 线程切换操作
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> toSubscribe() {
        return tObservable -> tObservable.subscribeOn(Schedulers.io())//访问网络切换异步线程
                .unsubscribeOn(Schedulers.io())//销毁访问网络切换异步线程
                .observeOn(AndroidSchedulers.mainThread()); //响应结果处理切换成主线程

    }

    /**
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<ResultBean<T>, T> toTransformer() {
        return tObservable ->
                tObservable.map(new HttpResultFunc<>())
                        .onErrorResumeNext(new HttpResponseFunc<>())
                        .compose(toSubscribe());
    }

    /**
     * @return
     */
    public static ObservableTransformer<ResultBean, String> toStringTransformer() {
        return new ObservableTransformer<ResultBean, String>(){

            @Override
            public ObservableSource<String> apply(Observable<ResultBean> upstream) {
                return upstream.map(new Function<ResultBean, String>() {
                    @Override
                    public String apply(ResultBean resultBean) throws Exception {
                        if (resultBean.isError()) {
                            throw new RuntimeException(resultBean.getMsg());
                        }
                        return resultBean.getMsg();
                    }
                }).onErrorResumeNext(new HttpResponseFunc<>())
                        .compose(toSubscribe());
            }
        };
    }

    /**
     * 异常处理
     *
     * @param <T>
     */
    private static class HttpResponseFunc<T> implements Function<Throwable, Observable<T>> {
        @Override
        public Observable<T> apply(Throwable throwable) throws Exception {
            return Observable.error(HttpExceptionHandle.handleException(throwable));
        }
    }
}
