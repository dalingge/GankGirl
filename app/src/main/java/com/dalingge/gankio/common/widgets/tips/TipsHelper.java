package com.dalingge.gankio.common.widgets.tips;


import android.view.View;

/**
 * FileName: TipsHelper
 * description:
 * Author: 丁博洋
 * Date: 2016/8/13
 */
public interface TipsHelper {

    void showEmpty();

    void hideEmpty();

    void showLoading(boolean firstPage);

    void hideLoading();

    void showError(boolean firstPage, String errorMessage, View.OnClickListener l);

    void hideError();

}