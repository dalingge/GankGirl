package com.dalingge.gankio.module.web;

import android.graphics.Bitmap;
import android.os.Build;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.dalingge.gankio.common.utils.DensityUtils;


/**
 *
 * Created by dingboyang on 2017/1/5.
 */
public class WebViewClientConfig extends WebViewClient {

    public WebViewClientConfig() {
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        //页面开始加载时
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        //需要设置在当前WebView中显示网页，才不会跳到默认的浏览器进行显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.loadUrl(request.getUrl().toString());
        } else {
            view.loadUrl(request.toString());
        }
        return true;
    }
    
    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
        super.onReceivedError(view, request, error);
        //加载出错了
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        //  html加载完成之后，调用js的方法
        imgReset(view);
    }

    //自适应图片宽度
    private void imgReset(WebView webView) {
        int maxWidth = DensityUtils.getScreenWidth(webView.getContext());
        webView.loadUrl("javascript:(function(){"
                + "var objs = document.getElementsByTagName('img');"
                + "for(var i=0;i<objs.length;i++){"
                + "    var img = objs[i];"
                + "    if(img.width>"+maxWidth+"){"
                + "       img.style.width = '100%';"
                + "       img.style.height = 'auto';"
                + "    }"
                + "}" + "})()");
    }
}
