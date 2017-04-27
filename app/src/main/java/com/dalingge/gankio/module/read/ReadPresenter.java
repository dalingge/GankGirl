package com.dalingge.gankio.module.read;

import android.os.Bundle;

import com.dalingge.gankio.common.base.BaseRxPresenter;
import com.dalingge.gankio.data.model.ReadChildTypeBean;
import com.dalingge.gankio.common.rxjava.Function0;
import com.dalingge.gankio.network.HttpRetrofit;

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

import static com.dalingge.gankio.module.test.TestPresenter.REQUEST_ITEMS;

/**
 * Created by dingboyang on 2017/4/27.
 */

public class ReadPresenter extends BaseRxPresenter<ReadFragment> {

    private String url;
    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        restartableFirst(REQUEST_ITEMS,
                new Function0<Observable<List<ReadChildTypeBean>>>() {
                    @Override
                    public Observable<List<ReadChildTypeBean>> apply() {
                        return HttpRetrofit.getInstance().providers.getStackTypeList(observable, new DynamicKey(url), new EvictDynamicKey(false))
                                .map(new HttpRetrofit.HttpResultFuncCcche<List<ReadChildTypeBean>>())
                                .compose(HttpRetrofit.toSubscribe());
                    }
                },
                new BiConsumer<ReadFragment, List<ReadChildTypeBean>>() {
                    @Override
                    public void accept(@NonNull ReadFragment readFragment, @NonNull List<ReadChildTypeBean> readChildTypeBeen) throws Exception {
                        readFragment.onData(readChildTypeBeen);
                    }
                }
        );
    }


    private Observable observable = Observable
            .create(new ObservableOnSubscribe<List<ReadChildTypeBean>>() {
                @Override
                public void subscribe(ObservableEmitter<List<ReadChildTypeBean>> subscriber) throws Exception {
                    List<ReadChildTypeBean> datas = new ArrayList<>();
                    try {
                        Document doc = Jsoup.connect(url).get();
                        Elements tads = doc.select("div.xiandu_choice").select("a");
                        for (Element tad : tads) {
                            ReadChildTypeBean bean = new ReadChildTypeBean();
                            Elements img= tad.select("img");
                            bean.setTitle(img.attr("title"));
                            bean.setImg(img.attr("src"));
                            bean.setUrl(tad.absUrl("href"));
                            datas.add(bean);
                        }

                    } catch (IOException e) {
                        subscriber.onError(e);
                    }
                    subscriber.onNext(datas);
                    subscriber.onComplete();
                }
            });

    void request(String url) {
        this.url = url;
        start(REQUEST_ITEMS);
    }
}
