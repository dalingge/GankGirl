package com.dalingge.gankio.common.base.delivery;

import android.support.annotation.Nullable;

import com.dalingge.gankio.network.HttpExceptionHandle;

import io.reactivex.Notification;
import io.reactivex.functions.BiConsumer;


public final class Delivery<View, T> {

    private final View view;
    private final Notification<T> notification;

    public Delivery(View view, Notification<T> notification) {
        this.view = view;
        this.notification = notification;
    }

    public void split(BiConsumer<View, T> onNext, @Nullable BiConsumer<View, HttpExceptionHandle.ResponeThrowable> onError) throws Exception {
        if (notification.isOnNext()) {
            onNext.accept(view, notification.getValue());
        }else if(notification.isOnError()) {
            assert onError != null;
            onError.accept(view,(HttpExceptionHandle.ResponeThrowable)notification.getError());
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Delivery<?, ?> delivery = (Delivery<?, ?>)o;

        if (view != null ? !view.equals(delivery.view) : delivery.view != null) return false;
        return !(notification != null ? !notification.equals(delivery.notification) : delivery.notification != null);
    }

    @Override
    public int hashCode() {
        int result = view != null ? view.hashCode() : 0;
        result = 31 * result + (notification != null ? notification.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Delivery{" +
            "view=" + view +
            ", notification=" + notification +
            '}';
    }
}
