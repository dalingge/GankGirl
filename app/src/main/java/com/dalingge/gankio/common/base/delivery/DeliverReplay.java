package com.dalingge.gankio.common.base.delivery;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Notification;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.processors.ReplayProcessor;


public class DeliverReplay<View, T> implements FlowableTransformer<T, Delivery<View, T>> {

    private final Flowable<View> view;

    public DeliverReplay(Flowable<View> view) {
        this.view = view;
    }

    @Override
    public Publisher<Delivery<View, T>> apply(Flowable<T> upstream) {
        final ReplayProcessor<Notification<T>> subject = ReplayProcessor.create();

        upstream
                .materialize()
                .filter(new Predicate<Notification<T>>() {
                    @Override
                    public boolean test(Notification<T> notification) throws Exception {
                        return !notification.isOnComplete();
                    }
                })
                .subscribe(subject);

        return view
                .switchMap(new Function<View, Publisher<? extends Delivery<View, T>>>() {
                    @Override
                    public Publisher<? extends Delivery<View, T>> apply(View view) throws Exception {
                        return view == null ? Flowable.never() : subject
                                .map(new Function<Notification<T>, Delivery<View, T>>() {
                                    @Override
                                    public Delivery<View, T> apply(Notification<T> notification) throws Exception {
                                        return new Delivery<>(view, notification);
                                    }
                                });
                    }
                })
                .doOnCancel(new Action() {
                    @Override
                     public void run() throws Exception {
//                         disposable.dispose();
                    }
                });
    }
}
