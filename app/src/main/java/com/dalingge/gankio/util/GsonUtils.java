package com.dalingge.gankio.util;

import android.text.TextUtils;

import com.dalingge.gankio.util.log.L;
import com.google.gson.Gson;

/**
 * FileName:GsonUtils.java
 * Description:Gson解析
 * Author:dingboyang
 * Email:445850053@qq.com
 * Date:16/4/2
 */
public class GsonUtils {

    public static Object fromJson(String jsonStr,Class clazz){
        Object object = null;
        if (!TextUtils.isEmpty(jsonStr)) {
            try {
                object = new Gson().fromJson(jsonStr, clazz);
            }catch (Exception e){
                e.printStackTrace();
                L.e("Gson fromJson is error!");
            }
        }
        return object;
    }

}
