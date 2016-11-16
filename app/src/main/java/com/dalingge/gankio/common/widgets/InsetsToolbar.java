package com.dalingge.gankio.common.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import com.dalingge.gankio.R;

/**
 * FileName: InsetsToolbar.java
 * description:
 * Author: dingby(445850053@qq.com)
 * Date: 2016/4/5
 */
public class InsetsToolbar extends Toolbar {

    public InsetsToolbar(Context context) {
        this(context, null);
    }

    public InsetsToolbar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.toolbarStyle);
    }

    public InsetsToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ViewCompat.setOnApplyWindowInsetsListener(this, new android.support.v4.view.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                final int l = insets.getSystemWindowInsetLeft();
                final int t = insets.getSystemWindowInsetTop();
                final int r = insets.getSystemWindowInsetRight();
                setPadding(l, t, r, 0);
                return insets.consumeSystemWindowInsets();
            }
        });
    }


}