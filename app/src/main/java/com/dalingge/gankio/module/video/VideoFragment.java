package com.dalingge.gankio.module.video;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.dalingge.gankio.R;
import com.dalingge.gankio.common.Constants;
import com.dalingge.gankio.common.base.BaseFragment;
import com.dalingge.gankio.module.read.ReadFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends BaseFragment {


    public static VideoFragment newInstance(String param1) {
        VideoFragment fragment = new VideoFragment();
        Bundle args = new Bundle();
        args.putString(Constants.BUNDLE_KEY_TYPE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_video;
    }

    @Override
    protected void initView(View view) {

    }

}
