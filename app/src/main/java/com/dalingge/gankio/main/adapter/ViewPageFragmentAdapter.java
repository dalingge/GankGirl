package com.dalingge.gankio.main.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * FileName:ViewPageFragmentAdapter.java
 * Description:
 * Author:dingboyang
 * Email:445850053@qq.com
 * Date:16/4/2
 */
public class ViewPageFragmentAdapter extends FragmentStatePagerAdapter {

    private final Context mContext;
    protected TabLayout mViewTabs;
    private final ViewPager mViewPager;
    private final List<ViewPageInfo> mTabs = new ArrayList<>();

    public ViewPageFragmentAdapter(FragmentManager fm, TabLayout tabs, ViewPager pager) {
        super(fm);
        mContext = tabs.getContext();
        mViewTabs = tabs;
        mViewPager = pager;
    }

    /**
     *
     * @param title
     * @param tag
     * @param clss
     * @param args
     */
    public void addTab(String title, String tag, Class<?> clss, Bundle args) {
        ViewPageInfo viewPageInfo = new ViewPageInfo(title, tag, clss, args);
        mTabs.add(viewPageInfo);
    }

    @Override
    public Fragment getItem(int position) {
        ViewPageInfo info = mTabs.get(position);
        return Fragment.instantiate(mContext, info.clss.getName(), info.args);
    }

    @Override
    public int getCount() {
        return mTabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabs.get(position).title;
    }
}
