package com.dalingge.gankio.common.config;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;

import java.io.InputStream;

import okhttp3.OkHttpClient;

/**
 * FileName: GlideConfig.java
 * description:
 * Author: dingby(445850053@qq.com)
 * Date: 2016/5/5
 */
public class GlideConfig implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // Bitmap格式转换到ARGB_8888
//        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
//        // 配置图片将缓存到SD卡
//        ExternalCacheDiskCacheFactory  externalCacheDiskCacheFactory = new ExternalCacheDiskCacheFactory(context);
//        builder.setDiskCache(externalCacheDiskCacheFactory);

    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        // 配置使用OKHttp3来请求网络
        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(new OkHttpClient()));
    }

}
