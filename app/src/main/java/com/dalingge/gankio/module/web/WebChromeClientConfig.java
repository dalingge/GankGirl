package com.dalingge.gankio.module.web;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by dingboyang on 2017/1/5.
 */

public class WebChromeClientConfig extends WebChromeClient {

    private TextView textView;

    private ProgressBar progressBar;

    private Handler handler;


    public WebChromeClientConfig(TextView textView, ProgressBar progressBar, Handler handler) {
        this.textView = textView;
        this.progressBar = progressBar;
        this.handler = handler;
    }

    public WebChromeClientConfig(ProgressBar progressBar, Handler handler) {
        this.progressBar = progressBar;
        this.handler = handler;
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        //获取WebView的标题
        super.onReceivedTitle(view, title);
        //textView.setText(title);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        //加载的进度
        progressBar.setProgress(newProgress);
        if (newProgress == 100) {

            Message msg = new Message();
            msg.what = 5;
            handler.sendMessageDelayed(msg, 300);

        } else {

            if (View.GONE == progressBar.getVisibility())
                progressBar.setVisibility(View.VISIBLE);
        }

        super.onProgressChanged(view, newProgress);
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        return super.onJsAlert(view, url, message, result);
        //Js 弹框
    }

}
