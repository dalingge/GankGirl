package com.dalingge.gankio.module.read;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.dalingge.gankio.R;
import com.dalingge.gankio.Constants;
import com.dalingge.gankio.common.base.BaseLazyFragment;
import com.dalingge.gankio.common.base.factory.RequiresPresenter;
import com.dalingge.gankio.data.model.ReadChildTypeBean;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
@RequiresPresenter(ReadPresenter.class)
public class ReadFragment extends BaseLazyFragment<ReadPresenter> {

    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            url = args.getString(Constants.BUNDLE_KEY_TYPE);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_read;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void lazyLoad() {
        if ((!isPrepared || !isVisible)) {
            return;
        }
    }


    public void onData(List<ReadChildTypeBean> datas) {

    }
}
