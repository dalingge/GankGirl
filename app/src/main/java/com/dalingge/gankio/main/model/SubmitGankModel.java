package com.dalingge.gankio.main.model;


import com.dalingge.gankio.bean.Constants;
import com.dalingge.gankio.bean.GirlBean;
import com.dalingge.gankio.util.GsonUtils;
import com.dalingge.gankio.util.log.L;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

/**
 * FileName: SubmitGankModel.java
 * description:
 * Author: dingby(445850053@qq.com)
 * Date: 2016/4/11
 */
public class SubmitGankModel {

    public void submitGank(String tag,String strUrl, String strDesc, String strWho, String strType,final OnSubmitListListener listener) {

        StringCallback callback=new StringCallback() {
            @Override
            public void onResponse(String response) {
                listener.onSuccess(response);
            }
            @Override
            public void onError(Call call, Exception e) {
                listener.onFailure("提交失败！",e);
            }

        };
        OkHttpUtils.post().url(Constants.API_SUBMIT_GANK).tag(tag)
                .addParams("url", strUrl)
                .addParams("desc", strDesc)
                .addParams("who", strWho)
                .addParams("type", strType)
                .addParams("debug", "false")
                .build()
                .execute(callback);
    }

    public interface OnSubmitListListener {
        void onSuccess(String msg);
        void onFailure(String msg, Exception e);
    }

}
