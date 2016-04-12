package com.dalingge.gankio.main.model;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.dalingge.gankio.bean.GirlBean;
import com.dalingge.gankio.util.GsonUtils;
import com.dalingge.gankio.util.ImageUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;

import okhttp3.Call;

/**
 * FileName: MainModel.java
 * description:
 * Author: dingby(445850053@qq.com)
 * Date: 2016/4/12
 */
public class MainModel {

    public void getSplashImage(final Context context, final String url) {
        StringCallback callback=new StringCallback() {
            @Override
            public void onResponse(String response) {
                GirlBean girlBean= (GirlBean) GsonUtils.fromJson(response,GirlBean.class);
                Glide.with(context)
                        .load(girlBean.getResults().get(0).getUrl()).asBitmap()
                        .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {

                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                File imgFile = new File(context.getFilesDir(), "start.jpg");
                                ImageUtils.saveImage(imgFile,resource);
                            }
                        });
            }
            @Override
            public void onError(Call call, Exception e) {
            }

        };
        OkHttpUtils.get().url(url).build().execute(callback);
    }
}
