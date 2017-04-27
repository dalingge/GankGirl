package com.dalingge.gankio.common.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.dalingge.gankio.R;
import com.dalingge.gankio.Constants;
import com.dalingge.gankio.common.base.view.ViewPageFragmentAdapter;

import butterknife.BindView;

/**
 * Created by dingboyang on 2016/11/11.
 */

public abstract class BaseTadPageFragment<P extends BasePresenter> extends BaseFragment<P> {

    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    protected ViewPager viewPager;
    protected ViewPageFragmentAdapter viewPageFragmentAdapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPageFragmentAdapter = new ViewPageFragmentAdapter(getChildFragmentManager(), getContext());
        viewPager.setAdapter(viewPageFragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);//将TabLayout和ViewPager关联起来。
        onSetupTabAdapter(viewPageFragmentAdapter);
    }


    /**
     * 基类会根据不同的Type展示相应的数据
     *
     * @param type 要显示的数据类别
     * @return
     */
    protected Bundle getBundle(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BUNDLE_KEY_TYPE, type);
        return bundle;
    }


    protected abstract void onSetupTabAdapter(ViewPageFragmentAdapter adapter);
}
