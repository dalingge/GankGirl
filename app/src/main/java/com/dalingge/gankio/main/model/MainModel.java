package com.dalingge.gankio.main.model;

import android.content.Context;
import android.graphics.Bitmap;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.dalingge.gankio.bean.GirlBean;
import com.dalingge.gankio.http.HttpMethods;
import com.dalingge.gankio.common.utils.ImageUtils;

import java.io.File;
import java.util.List;

import rx.Subscriber;

/**
 * FileName: MainModel.java
 * description:
 * Author: dingby(445850053@qq.com)
 * Date: 2016/4/12
 */
public class MainModel{

    public void getSplashImage(final Context context,  int count) {

        Subscriber subscriber = new Subscriber<List<GirlBean>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(List<GirlBean> girlBean) {
                Glide.with(context)
                        .load(girlBean.get(0).getUrl()).asBitmap()
                        .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {

                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                File imgFile = new File(context.getFilesDir(), "start.jpg");
                                ImageUtils.saveImage(context,imgFile,resource);
                            }
                        });
            }
        };
        HttpMethods.getInstance().getRandomImage(subscriber, count);

    }
}
