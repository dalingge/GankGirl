package com.dalingge.gankio;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.dalingge.gankio.common.utils.PreferencesUtils;

/**
 * FileName:GankApp.java
 * Description:
 * Author:dingboyang
 * Email:445850053@qq.com
 * Date:16/4/2
 */
public class GankApp extends Application {

    private static GankApp instance;
    private static Context _context;

    public static GankApp getInstance() {

        if (instance == null) {
            synchronized (GankApp.class) {
                if (instance == null) {
                    instance = new GankApp();
                }
            }
        }
        return instance;
    }

    public static synchronized GankApp context() {
        return (GankApp) _context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        _context = getApplicationContext();
        instance = this;
        PreferencesUtils preferencesUtils = new PreferencesUtils(this);
        if(preferencesUtils.getBoolean(R.string.action_day_night, false)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            //设置该app的主题根据时间不同显示
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
        }
    }

}
