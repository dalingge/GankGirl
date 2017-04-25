package com.dalingge.gankio.module.read;


import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.dalingge.gankio.R;
import com.dalingge.gankio.common.Constants;
import com.dalingge.gankio.common.base.BaseFragment;
import com.dalingge.gankio.module.web.WebChromeClientConfig;
import com.dalingge.gankio.module.web.WebViewClientConfig;

import butterknife.BindView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReadFragment extends BaseFragment {

    @BindView(R.id.web_view)
    LinearLayout webViewLayout;
    @BindView(R.id.progressbar)
    ProgressBar progressbar;
    private WebView webView;

    public static ReadFragment newInstance(String param1) {
        ReadFragment fragment = new ReadFragment();
        Bundle args = new Bundle();
        args.putString(Constants.BUNDLE_KEY_TYPE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_read;
    }

    @Override
    protected void initView(View view) {

        webView = new WebView(getActivity());
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webView.setLayoutParams(params);
        webViewLayout.addView(webView);

        WebSettings webSettings = webView.getSettings();//获得WebView的设置
        webSettings.setUseWideViewPort(false);// 设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);//适配
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//把所有内容放大webview等宽的一列中
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);  //设置 缓存模式
        webSettings.setDomStorageEnabled(true);// 开启 DOM storage API 功能
        webSettings.setDatabaseEnabled(true);//开启 database storage API 功能
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//HTTPS，注意这个是在LOLLIPOP以上才调用的
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webSettings.setAppCacheEnabled(true);//开启 Application Caches 功能
        webSettings.setBlockNetworkImage(true);//关闭加载网络图片，在一开始加载的时候可以设置为true
        webSettings.setBlockNetworkImage(false);//当加载完网页的时候再设置为false
        webSettings.setSupportZoom(true);
        webView.setWebChromeClient(new WebChromeClientConfig(progressbar, handler));
        webView.setWebViewClient(new WebViewClientConfig());
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null); //启动硬件加速
        webView.requestFocus();
        webView.loadUrl("http://gank.io/xiandu");
    }


    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (what == 5) {
                if (progressbar != null)
                    progressbar.setVisibility(View.GONE);
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (webView != null) {
            webView.onResume();
            webView.resumeTimers();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (webView != null) {
            webView.onPause();
            webView.pauseTimers();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.clearCache(true); //清空缓存
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                if (webViewLayout != null) {
                    webViewLayout.removeView(webView);
                }
                webView.removeAllViews();
                webView.destroy();
            } else {
                webView.removeAllViews();
                webView.destroy();
                if (webViewLayout != null) {
                    webViewLayout.removeView(webView);
                }
            }
            webView = null;
        }
    }
}
