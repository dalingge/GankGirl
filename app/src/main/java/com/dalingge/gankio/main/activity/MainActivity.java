package com.dalingge.gankio.main.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.dalingge.gankio.R;
import com.dalingge.gankio.base.BaseActivity;
import com.dalingge.gankio.main.adapter.ViewPageFragmentAdapter;
import com.dalingge.gankio.main.fragment.WelfareListFragment;
import com.dalingge.gankio.main.model.GankCategory;
import com.dalingge.gankio.main.presenter.MainPresenter;
import com.dalingge.gankio.main.view.IMainView;

import butterknife.Bind;

/**
 * FileName:MainActivity.java
 * Description:主页
 * Author:dingboyang
 * Email:445850053@qq.com
 * Date:16/4/2
 */
public class MainActivity extends BaseActivity<MainPresenter> implements IMainView {


    @Bind(R.id.tab_layout)
    TabLayout tablayout;
    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    private ViewPageFragmentAdapter tabsAdapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        setTitle(R.string.title_main);

        tabsAdapter = new ViewPageFragmentAdapter(getSupportFragmentManager(), tablayout, viewPager);

        for (GankCategory gankCategory : GankCategory.values()) {
                tabsAdapter.addTab(gankCategory.name().toString(), "", WelfareListFragment.class,
                        getBundle(gankCategory.name()));
        }
        viewPager.setOffscreenPageLimit(GankCategory.values().length);
        viewPager.setAdapter(tabsAdapter);
        tablayout.setupWithViewPager(viewPager);//将TabLayout和ViewPager关联起来。
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    /**
     * 基类会根据不同的orderType展示相应的数据
     *
     * @param type 要显示的数据类别
     * @return
     */
    private Bundle getBundle(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(WelfareListFragment.BUNDLE_KEY_TYPE, type);
        return bundle;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Context getContext() {
        return this;
    }

}
