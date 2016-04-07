package com.dalingge.gankio.main.model;

import com.dalingge.gankio.bean.GirlBean;
import com.dalingge.gankio.util.GsonUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

/**
 * FileName:WelfareModel.java
 * Description:
 * Author:dingboyang
 * Email:445850053@qq.com
 * Date:16/4/2
 */
public class WelfareModel {


    public void loadNews(String tag,String url, final OnLoadDataListListener listener) {
        StringCallback callback=new StringCallback() {
            @Override
            public void onResponse(String response) {
                GirlBean girlBean= (GirlBean) GsonUtils.fromJson(response,GirlBean.class);
                listener.onSuccess(girlBean.getResults());
            }
            @Override
            public void onError(Call call, Exception e) {
                listener.onFailure("load news list failure.", e);
            }

        };
        OkHttpUtils.get().url(url).tag(tag).build().execute(callback);
    }

    public interface OnLoadDataListListener {
        void onSuccess(List<GirlBean.ResultsBean> list);
        void onFailure(String msg, Exception e);
    }
}
