package com.dalingge.gankio.module.test;

import android.os.Bundle;

import com.dalingge.gankio.common.base.BaseRxPresenter;

/**
 * Created by dingboyang on 2016/11/26.
 */

public class TestPresenter extends BaseRxPresenter<TestActivity> {

    public static final int REQUEST_ITEMS = 1;

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

//        restartableReplay(REQUEST_ITEMS,
//                new Func0<Observable<List<GankBean>>>() {
//                    @Override
//                    public Observable<List<GankBean>> call() {
//                        return HttpRetrofit.getInstance().apiService.getRandomImage(1)
//                                .compose(HttpRetrofit.toTransformer())
//                                .repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
//                                    @Override
//                                    public Observable<?> call(Observable<? extends Void> observable) {
//                                        Log.v("tag", "repeatWhen, call");
//                                        /**
//                                         * 这个方法只会被调用一次。
//                                         * 5 表示每次重复的调用（repeated call）会被延迟5s。
//                                         */
//                                        return observable.delay(5, TimeUnit.SECONDS);
//                                    }
//                                })
//                                .takeUntil(new Func1<List<GankBean>, Boolean>() {
//                                    @Override
//                                    public Boolean call(List<GankBean> gankBeen) {
//                                        /** 在这里，我们可以检查服务器返回的数据是否正确，和决定我们是否应该
//                                         *  停止轮询。
//                                         *  当服务器的任务完成时，我们停止轮询。
//                                         *  换句话说，“当任务（job）完成时，我们不拿（take）了”
//                                         */
//                                        Log.v("tag", "takeUntil, call response " + gankBeen.toString());
//                                        return false;
//                                    }
//                                })
//                                .filter(new Func1<List<GankBean>, Boolean>() {
//                                    @Override
//                                    public Boolean call(List<GankBean> gankBeen) {
//                                        /**
//                                         * 如果我们在这里返回“false”的话，那这个结果会被过滤掉（filter）
//                                         * 过滤（Filtering） 表示 onNext() 不会被调用.
//                                         * 但是 onComplete() 仍然会被传递.
//                                         */
//                                        Log.v("tag", "filter, call response " + gankBeen.toString());
//                                        return true;
//                                    }
//                                });
//                    }
//                },
//                new Action2<TestActivity, List<GankBean>>() {
//                    @Override
//                    public void call(TestActivity testActivity, List<GankBean> gankBeen) {
//                        testActivity.onData(gankBeen.get(0).desc);
//                    }
//                },
//                new Action2<TestActivity, HttpExceptionHandle.ResponeThrowable>() {
//                    @Override
//                    public void call(TestActivity testActivity, HttpExceptionHandle.ResponeThrowable responeThrowable) {
//                        testActivity.onNetworkError(responeThrowable);
//                    }
//                });

    }

}
