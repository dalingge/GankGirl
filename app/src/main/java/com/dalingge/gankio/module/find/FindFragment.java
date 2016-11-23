package com.dalingge.gankio.module.find;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.dalingge.gankio.R;
import com.dalingge.gankio.common.base.BaseFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class FindFragment extends BaseFragment {


    public FindFragment() {
        // Required empty public constructor
    }


    public static FindFragment newInstance(String param1) {
        FindFragment fragment = new FindFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_find;
    }

    @Override
    protected void initView(View view) {

    }
}
