package com.dalingge.gankio.module.main;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.dalingge.gankio.common.base.BaseRxPresenter;
import com.dalingge.gankio.common.rxjava.Function0;
import com.dalingge.gankio.data.model.GankBean;
import com.dalingge.gankio.network.HttpExceptionHandle;
import com.dalingge.gankio.network.HttpRetrofit;
import com.dalingge.gankio.network.RequestCommand;
import com.dalingge.gankio.network.RetryWhenNetworkException;
import com.dalingge.gankio.utils.ImageUtils;

import java.io.File;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiConsumer;

/**
 * FileName:MainPresenter.java
 * Description:主页
 * Author:dingboyang
 * Email:445850053@qq.com
 * Date:16/4/2
 */
public class MainPresenter extends BaseRxPresenter<MainActivity> {

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        restartableFirst(RequestCommand.REQUEST_RANDOM_GIRL,
                new Function0<Observable<List<GankBean>>>() {
                    @Override
                    public Observable<List<GankBean>> apply() {
                        return HttpRetrofit.getInstance().apiService.getRandomImage(1)
                                .compose(HttpRetrofit.toTransformer())
                                .retryWhen(new RetryWhenNetworkException());
                    }
                },
                new BiConsumer<MainActivity, List<GankBean>>() {
                    @Override
                    public void accept(@NonNull MainActivity mainActivity, @NonNull List<GankBean> gankBeen) throws Exception {
                        Glide.with(mainActivity)
                                .load(gankBeen.get(0).url)
                                .asBitmap()
                                .into(new SimpleTarget<Bitmap>(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL) {
                                    @Override
                                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                        File imgFile = new File(mainActivity.getFilesDir(), "start.jpg");
                                        ImageUtils.saveImage(imgFile, resource);
                                    }
                                });
                    }
                }, new BiConsumer<MainActivity, HttpExceptionHandle.ResponeThrowable>() {
                    @Override
                    public void accept(@NonNull MainActivity mainActivity, @NonNull HttpExceptionHandle.ResponeThrowable responeThrowable) throws Exception {
                        Toast.makeText(mainActivity,responeThrowable.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }
}
