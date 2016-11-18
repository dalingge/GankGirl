package com.dalingge.gankio.module.web;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.dalingge.gankio.R;
import com.dalingge.gankio.common.base.BaseToolbarActivity;

import butterknife.BindView;


public class WebActivity extends BaseToolbarActivity {

    private static final String EXTRA_URL = "extra_url";
    private static final String EXTRA_TITLE = "extra_title";

    @BindView(R.id.video_fullView)
    FrameLayout videoFullView;
    @BindView(R.id.web_View)
    WebView webView;

    private View xCustomView;
    private WebChromeClient.CustomViewCallback xCustomViewCallback;

    private String mUrl, mTitle;

    public static Intent newIntent(Context context, String extraURL, String extraTitle) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(EXTRA_URL, extraURL);
        intent.putExtra(EXTRA_TITLE, extraTitle);
        return intent;
    }

    @Override
    public boolean isBack() {
        return true;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_web;
    }

    @Override
    protected void initView() {
        mUrl = getIntent().getStringExtra(EXTRA_URL);
        mTitle = getIntent().getStringExtra(EXTRA_TITLE);

        WebSettings ws =webView.getSettings();

        ws.setJavaScriptEnabled(true);
        ws.setLoadWithOverviewMode(true);
        ws.setAppCacheEnabled(true);
        ws.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        ws.setSupportZoom(true);
        webView.setWebChromeClient(new webChromeClient());
        webView.setWebViewClient(new webViewClient());
        webView.loadUrl(mUrl);
        setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_like){

            return true;
        }else if(id==R.id.action_share){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class webViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return false;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            // waitdialog.dismiss();
        }
    }

    public class webChromeClient extends WebChromeClient {
        private View xprogressvideo;

        // 播放网络视频时全屏会被调用的方法
        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            getToolbar().setVisibility(View.GONE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            webView.setVisibility(View.INVISIBLE);
            // 如果一个视图已经存在，那么立刻终止并新建一个
            if (xCustomView != null) {
                callback.onCustomViewHidden();
                return;
            }
            videoFullView.addView(view);
            xCustomView = view;
            xCustomViewCallback = callback;
            videoFullView.setVisibility(View.VISIBLE);
        }

        // 视频播放退出全屏会被调用的
        @Override
        public void onHideCustomView() {
            if (xCustomView == null)// 不是全屏播放状态
                return;
            getToolbar().setVisibility(View.VISIBLE);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            xCustomView.setVisibility(View.GONE);
            videoFullView.removeView(xCustomView);
            xCustomView = null;
            videoFullView.setVisibility(View.GONE);
            xCustomViewCallback.onCustomViewHidden();
            webView.setVisibility(View.VISIBLE);
        }

        // 视频加载时进程loading
        @Override
        public View getVideoLoadingProgressView() {
            if (xprogressvideo == null) {
                LayoutInflater inflater = LayoutInflater
                        .from(WebActivity.this);
                xprogressvideo = inflater.inflate(
                        R.layout.video_loading_progress, null);
            }
            return xprogressvideo;
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            setTitle(title);
        }

    }

    /**
     * 判断是否是全屏
     *
     * @return
     */
    public boolean inCustomView() {
        return (xCustomView != null);
    }

    /**
     * 全屏时按返加键执行退出全屏方法
     */
    public void hideCustomView() {
        new webChromeClient().onHideCustomView();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.onResume();
        webView.resumeTimers();

        /**
         * 设置为横屏
         */
        if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
        webView.pauseTimers();
    }

    @Override
    protected void onDestroy() {
        videoFullView.removeAllViews();
        webView.loadUrl("about:blank");
        webView.stopLoading();
        webView.setWebChromeClient(null);
        webView.setWebViewClient(null);
        webView.destroy();
        super.onDestroy();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (inCustomView()) {
                    hideCustomView();
                    return true;
                } else {
                    if (webView.canGoBack()) {
                        webView.goBack();
                    }else {
                        finish();
                    }
                    return true;
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
