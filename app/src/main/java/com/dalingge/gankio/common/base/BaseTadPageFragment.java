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

        ViewPageFragmentAdapter viewPageFragmentAdapter=new ViewPageFragmentAdapter(getChildFragmentManager(),getContext());
        onSetupTabAdapter(viewPageFragmentAdapter);
        viewPager.setAdapter(viewPageFragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);//将TabLayout和ViewPager关联起来。

//        try {
//            Class<?> tab = tabLayout.getClass();
//            Field tabStrip = tab.getDeclaredField("mTabStrip");
//            tabStrip.setAccessible(true);
//            LinearLayout ll_tab= (LinearLayout) tabStrip.get(tabLayout);
//            for (int i = 0; i < ll_tab.getChildCount(); i++) {
//                View child = ll_tab.getChildAt(i);
//                child.setPadding(0,0,0,0);
//                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT,1);
//                params.setMarginStart((int) DensityUtils.dip2px(getContext(),20f));
//                params.setMarginEnd((int)DensityUtils.dip2px(getContext(),20f));
//                child.setLayoutParams(params);
//                child.invalidate();
//            }
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }

    }

    protected abstract void onSetupTabAdapter(ViewPageFragmentAdapter adapter);
}
