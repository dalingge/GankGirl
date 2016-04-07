package com.dalingge.gankio;

import android.app.Application;

import com.dalingge.gankio.util.log.L;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

/**
 * FileName:GankApp.java
 * Description:
 * Author:dingboyang
 * Email:445850053@qq.com
 * Date:16/4/2
 */
public class GankApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化日志工具
        L.init(BuildConfig.DEBUG);

        OkHttpUtils.getInstance().debug("OkHttpUtils").setConnectTimeout(100000, TimeUnit.MILLISECONDS);
    }

}
