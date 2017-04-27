package com.dalingge.gankio.module.read;

import android.os.Bundle;

import com.dalingge.gankio.common.base.BaseRxPresenter;
import com.dalingge.gankio.common.rxjava.Function0;
import com.dalingge.gankio.data.model.ReadChildTypeBean;
import com.dalingge.gankio.data.model.ReadListBean;
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
 * Created by dingboyang on 2017/4/27.
 */

public class ReadPresenter extends BaseRxPresenter<ReadFragment> {

    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        restartableFirst(RequestCommand.REQUEST_READ_LIST,
                new Function0<Observable<ReadTypeBean>>() {
                    @Override
                    public Observable<ReadTypeBean> apply() {
                        return HttpRetrofit.getInstance().providers.getStackTypeList(observable, new DynamicKey(requestContext.getType()), new EvictDynamicKey(true))
                                .map(new HttpRetrofit.HttpResultFuncCcche<ReadTypeBean>())
                                .compose(HttpRetrofit.toSubscribe());
                    }
                },
                new BiConsumer<ReadFragment, ReadTypeBean>() {
                    @Override
                    public void accept(@NonNull ReadFragment readFragment, @NonNull ReadTypeBean readTypeBean) throws Exception {

                        if (readTypeBean.getReadChildTypeBeanList()!= null)
                            readFragment.onDataChild(readTypeBean.getReadChildTypeBeanList());
                        if (readTypeBean.getReadListBeanList() != null)
                            readFragment.onDataList(readTypeBean.getReadListBeanList());
                    }
                }
        );
    }


    private Observable observable = Observable
            .create(new ObservableOnSubscribe<ReadTypeBean>() {
                @Override
                public void subscribe(ObservableEmitter<ReadTypeBean> subscriber) throws Exception {
                    ReadTypeBean readTypeBean = new ReadTypeBean();
                    readTypeBean.setTitle(requestContext.getType());
                    readTypeBean.setUrl(requestContext.getUrl());
                    List<ReadChildTypeBean> readChildTypeBeanList = new ArrayList<>();
                    List<ReadListBean> readListBeanList = new ArrayList<>();
                    try {
                        Document doc = Jsoup.connect(requestContext.getUrl()).get();
                        Elements childs = doc.select("div.xiandu_choice").select("a");
                        for (Element child : childs) {
                            ReadChildTypeBean bean = new ReadChildTypeBean();
                            Elements img = child.select("img");
                            bean.setTitle(img.attr("title"));
                            bean.setImg(img.attr("src"));
                            bean.setUrl(child.absUrl("href"));
                            readChildTypeBeanList.add(bean);
                        }
                        Elements items = doc.select("div.xiandu_item");
                        for (Element item : items) {
                            ReadListBean bean = new ReadListBean();
                            Elements aLeft = item.select("div.xiandu_left").select("a");
                            bean.setTitle(aLeft.text());
                            bean.setLink(aLeft.attr("href"));
                            bean.setTime(item.select("small").text());
                            Elements aRight = item.select("div.xiandu_right").select("a");
                            bean.setSource(aRight.attr("title"));
                            bean.setLogo(aRight.select("img").attr("src"));
                            readListBeanList.add(bean);
                        }
                    } catch (IOException e) {
                        subscriber.onError(e);
                    }
                    readTypeBean.setReadListBeanList(readListBeanList);
                    readTypeBean.setReadChildTypeBeanList(readChildTypeBeanList);
                    subscriber.onNext(readTypeBean);
                    subscriber.onComplete();
                }
            });

}
