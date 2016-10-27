package com.dalingge.gankio.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * FileName:BaseFragment.java
 * Description:
 * Author:dingboyang
 * Email:445850053@qq.com
 * Date:16/4/2
 */
public abstract class BaseFragment<P extends BasePresenter> extends Fragment {

    protected View rootView;

    protected abstract int getLayout();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(getLayout(), container, false);
            ButterKnife.bind(this, rootView);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        ButterKnife.bind(this, rootView);
        return rootView;
    }

    protected String getName(){
        return BaseFragment.class.getName();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //取消请求
       // ButterKnife.unbind(getName());
    }
}
