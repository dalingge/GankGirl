package com.dalingge.gankio.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * FileName: PreferencesUtils.java
 * description:
 * Author: dingby(445850053@qq.com)
 * Date: 2016/4/8
 */
public class PreferencesUtils {

    private SharedPreferences mSharedPreferences;
    private Context mContext;


    public PreferencesUtils(Context context) {
        mContext = context;
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public void saveBoolean(int keyResId, Boolean value) {
        String key = mContext.getString(keyResId);
        saveBoolean(key, value);
    }


    public void saveBoolean(String key, Boolean value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }


    public Boolean getBoolean(String key) {
        return mSharedPreferences.getBoolean(key, false);
    }


    public Boolean getBoolean(String key, boolean def) {
        return mSharedPreferences.getBoolean(key, def);
    }


    public Boolean getBoolean(int keyResId, boolean def) {
        String key = mContext.getString(keyResId);
        return mSharedPreferences.getBoolean(key, def);
    }


    public int getInt(String key) {
        return mSharedPreferences.getInt(key, 0);
    }


    public void saveInt(String key, int value) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }
}
