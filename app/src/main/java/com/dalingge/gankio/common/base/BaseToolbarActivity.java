package com.dalingge.gankio.common.base;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.dalingge.gankio.R;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;

/**
 * Created by dingboyang on 2016/11/9.
 */

public abstract class BaseToolbarActivity<P extends BasePresenter> extends BaseActivity<P> {

    @BindView(R.id.toolbar) Toolbar toolbar;


    protected boolean isBack() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initToolBar(); //初始化ToolBar
    }
    /**
     * 初始化toolbar
     */
    private void initToolBar() {
        if (toolbar == null) {
            throw new IllegalStateException(
                    "The subclass of ToolbarActivity must contain a toolbar.");
        }
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);//让原始的toolbar的title不显示
            if (isBack()) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

//        if (Build.VERSION.SDK_INT >= 21) {
//            toolbar.setElevation(10.6f);
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }
}
