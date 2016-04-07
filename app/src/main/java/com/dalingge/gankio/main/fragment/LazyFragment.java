package com.dalingge.gankio.main.fragment;

import com.dalingge.gankio.base.BaseSwipeRefreshFragment;
import com.dalingge.gankio.main.presenter.WelfarePresenter;

/**
 * FileName: LazyFragment.java
 * description:
 * Author: dingby(445850053@qq.com)
 * Date: 2016/4/6
 */
public abstract class LazyFragment extends BaseSwipeRefreshFragment<WelfarePresenter> {

    protected boolean isVisible;
    /**
     * 在这里实现Fragment数据的缓加载.
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible(){
        lazyLoad();
    }

    protected abstract void lazyLoad();

    protected void onInvisible(){}
}
