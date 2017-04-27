package com.dalingge.gankio.module.read;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import com.dalingge.gankio.R;
import com.dalingge.gankio.Constants;
import com.dalingge.gankio.common.base.BaseTadPageFragment;
import com.dalingge.gankio.common.factory.RequiresPresenter;
import com.dalingge.gankio.common.base.ViewPageFragmentAdapter;
import com.dalingge.gankio.data.model.ReadTypeBean;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
@RequiresPresenter(ReadTadPagePresenter.class)
public class ReadTadPageFragment extends BaseTadPageFragment<ReadTadPagePresenter> {

    public static ReadTadPageFragment newInstance(String param1) {
        ReadTadPageFragment fragment = new ReadTadPageFragment();
        Bundle args = new Bundle();
        args.putString(Constants.BUNDLE_KEY_TYPE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_read_tad_page;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
        getPresenter().start(1);
    }

    public void onData(List<ReadTypeBean> datas) {
        for (ReadTypeBean readTypeBean : datas) {
            viewPageFragmentAdapter.addTab(readTypeBean.getTitle(), "", ReadFragment.class, getBundle(readTypeBean.getUrl()));
        }
        viewPageFragmentAdapter.notifyDataSetChanged();
        viewPager.setOffscreenPageLimit(datas.size());
    }
}
