package com.dalingge.gankio.common.glide;

import android.content.Context;
import android.os.Environment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;
import com.dalingge.gankio.utils.FileUtils;

import java.io.File;
import java.io.InputStream;

import okhttp3.OkHttpClient;

/**
 * Created by dingby on 2015/12/10.
 */
public class GlideConfiguration implements GlideModule {

    @Override
    public void applyOptions(final Context context, GlideBuilder builder) {
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);// Bitmap格式转换到ARGB_8888
        //内存缓存
        MemorySizeCalculator memorySizeCalculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = memorySizeCalculator.getMemoryCacheSize();
        int defalutBitmapPoolSize = memorySizeCalculator.getBitmapPoolSize();
        builder.setMemoryCache(new LruResourceCache((int) (defalutBitmapPoolSize * 1.2)));//内部
        builder.setBitmapPool(new LruBitmapPool((int) (defalutBitmapPoolSize * 1.2)));
        //磁盘缓存
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, 1024 * 1024 * 10));//内部磁盘缓存
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, 10 * 1024 * 1024));//磁盘缓存到外部存储
        //指定缓存目录1
        String downLoadPath = Environment.getDownloadCacheDirectory().getPath();
        builder.setDiskCache(new DiskLruCacheFactory(downLoadPath, defaultMemoryCacheSize));
        //指定缓存目录2
        builder.setDiskCache(new DiskCache.Factory() {
            @Override
            public DiskCache build() {
                File cacheLocation = new File(FileUtils.getCacheDir(context), "GlideCache");
                return DiskLruCacheWrapper.get(cacheLocation, 1024 * 1024 * 10);
            }
        });

    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        // 配置使用OKHttp3来请求网络
        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(new OkHttpClient()));
    }
}
