package com.dalingge.gankio.Image.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.dalingge.gankio.Image.fragment.ImageViewFragment;
import com.dalingge.gankio.bean.GirlBean;

import java.util.List;

/**
 * FileName:ImagePagerAdapter.java
 * Description:
 * Author:dingboyang
 * Email:445850053@qq.com
 * Date:16/4/4
 */
public class ImagePagerAdapter extends FragmentStatePagerAdapter {

    private ViewPager viewPager;
    private List<GirlBean.ResultsBean> resultsBeanList;
    private int index;
    public ImagePagerAdapter(FragmentManager fm, ViewPager viewPager, List<GirlBean.ResultsBean> resultsBeanList, int index) {
        super(fm);
        this.viewPager=viewPager;
        this.resultsBeanList=resultsBeanList;
        this.index=index;
    }

    @Override
    public int getCount() {
        return resultsBeanList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return ImageViewFragment.newFragment(
                resultsBeanList.get(position).getUrl(),
                position == index);
    }

    public ImageViewFragment getCurrent() {
        return (ImageViewFragment) instantiateItem(viewPager, viewPager.getCurrentItem());
    }
}
