package com.dalingge.gankio.module.read;

import android.os.Bundle;
import android.util.Log;

import com.dalingge.gankio.Constants;
import com.dalingge.gankio.common.base.BaseRxPresenter;
import com.dalingge.gankio.common.rxjava.Function0;
import com.dalingge.gankio.data.model.ReadTypeBean;
import com.dalingge.gankio.network.HttpRetrofit;
import com.dalingge.gankio.network.RequestCommand;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiConsumer;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictDynamicKey;

/**
 * Created by dingboyang on 2017/4/26.
 */
public class ReadTadPagePresenter extends BaseRxPresenter<ReadTadPageFragment> {

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        restartableFirst(RequestCommand.REQUEST_READ_TYPE,
                new Function0<Observable<List<ReadTypeBean>>>() {
                    @Override
                    public Observable<List<ReadTypeBean>> apply() {
                        return HttpRetrofit.getInstance().providers.getTypeList(observable, new DynamicKey("闲读分类"), new EvictDynamicKey(false))
                                .map(new HttpRetrofit.HttpResultFuncCcche<List<ReadTypeBean>>())
                                .compose(HttpRetrofit.toSubscribe());
                    }
                },
                new BiConsumer<ReadTadPageFragment, List<ReadTypeBean>>() {
                    @Override
                    public void accept(@NonNull ReadTadPageFragment readTadPageFragment, @NonNull List<ReadTypeBean> readTypeBeen) throws Exception {
                        readTadPageFragment.onData(readTypeBeen);
                    }
                });
    }


    private Observable observable = Observable
            .create(new ObservableOnSubscribe<List<ReadTypeBean>>() {
                @Override
                public void subscribe(ObservableEmitter<List<ReadTypeBean>> subscriber) throws Exception {
                    List<ReadTypeBean> datas = new ArrayList<>();
                    try {
                        Document doc = Jsoup.connect(Constants.API_URL_READ).get();
                        Elements tads = doc.select("div#xiandu_cat").select("a");
                        for (Element tad : tads) {
                            ReadTypeBean bean = new ReadTypeBean();
                            bean.setTitle(tad.text());//获取标题
                            bean.setUrl(tad.absUrl("href"));//absUrl可以获取地址的绝对路径
                            datas.add(bean);
                            Log.v("Jsoup","title= "+bean.getTitle()+"   url= "+bean.getUrl());
                        }
                    } catch (IOException e) {
                        subscriber.onError(e);
                    }

                    subscriber.onNext(datas);
                    subscriber.onComplete();
                }
            });
}
