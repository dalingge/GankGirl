package com.dalingge.gankio;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import com.dalingge.gankio.util.PreferencesUtils;
import com.dalingge.gankio.util.log.L;

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

        PreferencesUtils preferencesUtils = new PreferencesUtils(this);
        if(preferencesUtils.getBoolean(R.string.action_day_night, false)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            //设置该app的主题根据时间不同显示
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);

        }

        //初始化日志工具
        L.init(BuildConfig.DEBUG);

    }

}
