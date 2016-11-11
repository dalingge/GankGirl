package com.dalingge.gankio.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.dalingge.gankio.R;
import com.dalingge.gankio.main.adapter.ViewPageFragmentAdapter;

import butterknife.BindView;

/**
 * Created by dingboyang on 2016/11/11.
 */

public abstract class BaseTadPageFragment<P extends BasePresenter> extends BaseFragment<P>{

    @BindView(R.id.tab_layout)
    protected TabLayout tabLayout;
    @BindView(R.id.view_pager)
    protected ViewPager viewPager;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPageFragmentAdapter viewPageFragmentAdapter=new ViewPageFragmentAdapter(getChildFragmentManager(),tabLayout,viewPager);
        onSetupTabAdapter(viewPageFragmentAdapter);
        viewPager.setAdapter(viewPageFragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);//将TabLayout和ViewPager关联起来。
    }

    protected abstract void onSetupTabAdapter(ViewPageFragmentAdapter adapter);
}
