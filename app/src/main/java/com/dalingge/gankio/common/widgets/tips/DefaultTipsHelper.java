package com.dalingge.gankio.common.widgets.tips;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dalingge.gankio.R;
import com.dalingge.gankio.common.utils.DensityUtils;

/**
 * Created by dingboyang on 2016/11/10.
 */

public class DefaultTipsHelper implements TipsHelper{

    private final ProgressBar mLoadingView;
    private final View mView;

    public DefaultTipsHelper(Context context, View view) {
        this.mView=view;
        mLoadingView = new ProgressBar(context);
        mLoadingView.setPadding(0, (int) DensityUtils.dip2px(context, 10),
                0, (int) DensityUtils.dip2px(context, 10));
        mLoadingView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (int) DensityUtils.dip2px(context, 40)));
    }

    @Override
    public void showEmpty() {
        hideLoading();
        TipsUtils.showTips(mView, TipsType.EMPTY);
    }

    @Override
    public void hideEmpty() {
        TipsUtils.hideTips(mView, TipsType.EMPTY);
    }

    @Override
    public void showLoading(boolean firstPage) {
        hideEmpty();
        hideError();
        if (firstPage) {
            TipsUtils.showTips(mView, TipsType.LOADING);
        }
    }

    @Override
    public void hideLoading() {
        hideEmpty();
        hideError();
        TipsUtils.hideTips(mView, TipsType.LOADING);
    }

    @Override
    public void showError(boolean firstPage, String errorMessage, View.OnClickListener onClickListener) {
        hideLoading();
        if (firstPage) {
            View tipsView = TipsUtils.showTips(mView, TipsType.LOADING_FAILED);
            tipsView.findViewById(R.id.retry_btn).setOnClickListener(onClickListener);
            if (!TextUtils.isEmpty(errorMessage)) {
                ((TextView) tipsView.findViewById(R.id.description)).setText(errorMessage);
            }
            return;
        }

        Toast.makeText(mLoadingView.getContext(), errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void hideError() {
        TipsUtils.hideTips(mView, TipsType.LOADING_FAILED);
    }
}
