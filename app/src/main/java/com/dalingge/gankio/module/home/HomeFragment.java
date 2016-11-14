package com.dalingge.gankio.module.home;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dalingge.gankio.R;
import com.dalingge.gankio.common.base.BaseLazyFragment;
import com.dalingge.gankio.common.base.factory.RequiresPresenter;
import com.dalingge.gankio.util.animator.recyclerview.adapter.AlphaAnimatorAdapter;

import butterknife.BindView;


/**
 * A simple {@link Fragment} subclass.
 */
@RequiresPresenter(HomePresenter.class)
public class HomeFragment extends BaseLazyFragment<HomePresenter> {

    public static final String BUNDLE_KEY_TYPE="BUNDLE_KEY_TYPE";

    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    @BindView(R.id.swipe_refresh_widget)
    SwipeRefreshLayout swipeRefreshWidget;

    private String mType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mType = args.getString(BUNDLE_KEY_TYPE);
        }
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view) {
        HomeAdapter homeAdapter =new HomeAdapter(getActivity());
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        AlphaAnimatorAdapter animatorAdapter = new AlphaAnimatorAdapter(homeAdapter, recycleView);
        recycleView.setLayoutManager(mLinearLayoutManager);
        recycleView.setHasFixedSize(true);
        recycleView.setAdapter(animatorAdapter);
    }

}
