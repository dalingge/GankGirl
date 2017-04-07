package com.dalingge.gankio.common.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * FileName: RxUtils.java
 * description:
 * Author: dingby(445850053@qq.com)
 * Date: 2016/5/7
 */
public class RxUtils {

    public static Observable<File> saveImageAndGetPathObservable(final Context context, final String url, final String title) {
        return Observable.create(new ObservableOnSubscribe<Bitmap>() {
            @Override
            public void subscribe(ObservableEmitter<Bitmap> subscriber) throws Exception {
                Bitmap bitmap = null;
                try {
                    bitmap = Glide.with(context).load(url).asBitmap()
                            .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();
                } catch (InterruptedException | ExecutionException  e) {
                    subscriber.onError(e);
                }
                if (bitmap == null) {
                    subscriber.onError(new Exception("无法下载到图片"));
                }
                subscriber.onNext(bitmap);
                subscriber.onComplete();
            }
        }).flatMap(new Function<Bitmap, ObservableSource<File>>() {
            @Override
            public ObservableSource<File> apply(Bitmap bitmap) throws Exception {
                File file = new File(Environment.getExternalStorageDirectory(), "gank");
                if (!file.exists()) {
                    file.mkdir();
                }
                String fileName = title + ".jpg";
                File pictureFile = new File(file, fileName);
                try {
                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    assert bitmap != null;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return Observable.just(pictureFile);
            }
        }).doOnNext(new Consumer<File>() {
            @Override
            public void accept(File file) throws Exception {
                MediaScannerConnection.scanFile(context.getApplicationContext(), new String[]{file.getPath()}, null, null);
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 验证码倒计时
     *
     * @param millisInFuture
     * @param btn
     */
    public static void getTimeCount(long millisInFuture, Button btn) {
        final long count = millisInFuture / 1000;
        Observable.interval(0, 1, TimeUnit.SECONDS)//设置0延迟，每隔一秒发送一条数据
                .take((int) (count + 1)) //设置总共发送的次数
                .map(aLong -> count - aLong) //long 值是从小到大，倒计时需要将值倒置
                .subscribeOn(Schedulers.computation())
                // doOnSubscribe 执行线程由下游逻辑最近的 subscribeOn() 控制，下游没有 subscribeOn() 则跟Subscriber 在同一线程执行
                //执行计时任务前先将 button 设置为不可点击
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        btn.setEnabled(false);
                    }
                })//在发送数据的时候设置为不能点击
                .observeOn(AndroidSchedulers.mainThread())//操作UI主要在UI线程
                .subscribe(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(Long value) {
                        btn.setText(String.valueOf(value));
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        btn.setText("重新获取");
                        btn.setEnabled(true);
                    }
                });
    }
}
