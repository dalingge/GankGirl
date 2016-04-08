package com.dalingge.gankio.main.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.widget.TextView;

import com.dalingge.gankio.BuildConfig;
import com.dalingge.gankio.R;
import com.dalingge.gankio.base.BaseActivity;

import butterknife.Bind;

/**
 * FileName:AboutActivity.java
 * Description:
 * Author:dingboyang
 * Email:445850053@qq.com
 * Date:16/4/8
 */
public class AboutActivity extends BaseActivity {


    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.tv_version)
    TextView tvVersion;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        return intent;
    }

    @Override
    public boolean isBack() {
        return true;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_about;
    }

    @Override
    protected void initView() {
        collapsingToolbar.setTitle("关于");

        tvVersion.setText("Version " + BuildConfig.VERSION_NAME);
    }

}
