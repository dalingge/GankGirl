package com.dalingge.gankio.module.video;

import android.os.Bundle;

import com.dalingge.gankio.common.base.BaseRxPresenter;
import com.dalingge.gankio.network.HttpRetrofit;
import com.dalingge.gankio.network.RequestCommand;
import com.dalingge.gankio.network.RetryWhenNetworkException;
import com.hustunique.parsingplayer.parser.extractor.BilibiliExtractor;
import com.hustunique.parsingplayer.parser.extractor.QQExtractor;
import com.hustunique.parsingplayer.parser.extractor.SoHuExtractor;
import com.hustunique.parsingplayer.parser.extractor.YoukuExtractor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dingboyang on 2017/4/28.
 */

public class VideoPresenter extends BaseRxPresenter<VideoFragment> {


    @Override
    protected void onCreate(Bundle savedState) {
        super.onCreate(savedState);

        restartableFirst(RequestCommand.REQUEST_VIDEO_LIST,
                () -> HttpRetrofit.getInstance().apiService
                        .getData(requestContext.getType(), requestContext.getPage())
                        .compose(HttpRetrofit.toTransformer())
                        .retryWhen(new RetryWhenNetworkException()),
                (gankFragment, gankBeanList) -> gankFragment.onAddData(gankBeanList),
                (gankFragment, responeThrowable) -> gankFragment.onNetworkError(responeThrowable));
    }

    public boolean isPlay(String url) {
        return isYouku(url) || isSoHu(url) || isBilibili(url) || isQQ(url);
    }

    private boolean isYouku(String url) {
        Pattern pattern = Pattern.compile(YoukuExtractor.VALID_URL);
        Matcher matcher = pattern.matcher(url);
        return matcher.find();
    }

    private boolean isSoHu(String url) {
        Pattern pattern = Pattern.compile(SoHuExtractor.VALID_URL);
        Matcher matcher = pattern.matcher(url);
        return matcher.find();
    }

    private boolean isBilibili(String url) {
        Pattern pattern = Pattern.compile(BilibiliExtractor.VALID_URL);
        Matcher matcher = pattern.matcher(url);
        return matcher.find();
    }

    private boolean isQQ(String url) {
        Pattern pattern = Pattern.compile(QQExtractor.VALID_URL);
        Matcher matcher = pattern.matcher(url);
        return matcher.find();
    }
}
