package com.dalingge.gankio.common.glide;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


/**
 * FileName: GlideImageLoader
 * description: 图片加载类,统一适配(方便换库,方便管理)
 * Author: 丁博洋
 * Date: 2016/8/22
 */
public class GlideImageLoader {

    public static void load(Context context, @DrawableRes int imageRes, ImageView view) {
        Glide.with(context).load(imageRes).crossFade().into(view);
    }

    public static void load(Context context, String imageRes, ImageView view) {
        Glide.with(context)
                .load(imageRes)
                .centerCrop()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);
    }

    /**
     * 下载图片转换圆角
     * @param context
     * @param imageRes
     * @param view
     * @param radius 圆角度
     */
    public static void loadAdapterRadius(Context context, String imageRes, ImageView view,int radius){
        DrawableRequestBuilder requestBuilder = Glide.with(context)
                .load(imageRes)
                .centerCrop()
                .crossFade()
                .transform(new GlideRoundTransform(context, radius))
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        requestBuilder.into(view);
    }

    /**
     * 下载图片转换圆角
     * @param context
     * @param imageRes
     * @param view
     * @param radius
     */
    public static void loadAdapterRadius(Context context, @DrawableRes int imageRes, ImageView view, int radius){
        DrawableRequestBuilder requestBuilder = Glide.with(context)
                .load(imageRes)
                .centerCrop()
                .crossFade()
                .transform(new GlideRoundTransform(context, radius))
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        requestBuilder.into(view);
    }

    /**
     * 下载头像转换圆形
     * @param context
     * @param imageRes
     * @param view
     */
    public static void loadAdapterCircle(Context context, String imageRes, ImageView view){
        RequestManager glideRequest = Glide.with(context);
        glideRequest.load(imageRes)
                .centerCrop()
                .crossFade()//动画
               // .placeholder(R.color.accent)//占位图片
                .transform(new GlideCircleTransform(context))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);

    }

    public static void clear(Context context) {
        Glide.get(context).clearMemory();
    }
}
