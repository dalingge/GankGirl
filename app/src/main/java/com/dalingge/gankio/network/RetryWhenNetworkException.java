package com.dalingge.gankio.network;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.Flowable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;

/**
 * FileName: RetryWhenNetworkException
 * description:使用操作符RetryWhen可以用来实现重试机制，默认情况下，最多重试3次，第一次会等3s，第二次会等6s，第三次会等9s。
 * Author: 丁博洋
 * Date: 2016/9/10
 */
public class RetryWhenNetworkException implements Function<Flowable<? extends Throwable>, Flowable<?>> {
    private int count = 3;//retry count
    private long delay = 3000;//delay time

    public RetryWhenNetworkException() {

    }

    public RetryWhenNetworkException(int count) {
        this.count = count;
    }

    public RetryWhenNetworkException(int count, long delay) {
        this.count = count;
        this.delay = delay;
    }

    @Override
    public Flowable<?> apply(Flowable<? extends Throwable> flowable) throws Exception {
        return flowable.zipWith(Flowable.range(1, count + 1), new BiFunction<Throwable, Integer, Wrapper>() {
            @Override
            public Wrapper apply(Throwable throwable, Integer integer) throws Exception {
                return new Wrapper(throwable, integer);
            }
        }).flatMap(wrapper -> {
            if ((wrapper.throwable instanceof ConnectException
                    || wrapper.throwable instanceof SocketTimeoutException
                    || wrapper.throwable instanceof TimeoutException)
                    && wrapper.index < count + 1) {
                return Flowable.timer(delay + (wrapper.index - 1) * delay, TimeUnit.MILLISECONDS);
            }
            return Flowable.error(wrapper.throwable);
        } );
    }

    private class Wrapper {
        private int index;
        private Throwable throwable;

        public Wrapper(Throwable throwable, int index) {
            this.index = index;
            this.throwable = throwable;
        }
    }
}
