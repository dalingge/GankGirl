package com.dalingge.gankio.module.home.gank;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dalingge.gankio.R;
import com.dalingge.gankio.common.Constants;
import com.dalingge.gankio.common.base.BaseLazyFragment;
import com.dalingge.gankio.common.base.factory.RequiresPresenter;
import com.dalingge.gankio.common.bean.GankBean;
import com.dalingge.gankio.common.widgets.recyclerview.anim.adapter.AlphaAnimatorAdapter;
import com.dalingge.gankio.common.widgets.recyclerview.anim.itemanimator.SlideInOutBottomItemAnimator;
import com.dalingge.gankio.common.widgets.tips.DefaultTipsHelper;
import com.dalingge.gankio.module.web.WebActivity;
import com.dalingge.gankio.network.HttpExceptionHandle;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * A simple {@link Fragment} subclass.
 */
@RequiresPresenter(GankPresenter.class)
public class GankFragment extends BaseLazyFragment<GankPresenter> implements SwipeRefreshLayout.OnRefreshListener, GankAdapter.OnItemClickListener {

    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    @BindView(R.id.swipe_refresh_widget)
    SwipeRefreshLayout swipeRefreshWidget;

    private ArrayList<GankBean> mData = new ArrayList<>();
    private GankAdapter mGankAdapter;
    private String mType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mType = args.getString(Constants.BUNDLE_KEY_TYPE);
        }
    }

    @Override
    protected void lazyLoad() {
        if ((!isPrepared || !isVisible)) {
            return;
        }

        if (mData.isEmpty()) {
            showRefresh();
            onRefresh();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gank;
    }

    @Override
    protected void initView(View view) {
        swipeRefreshWidget.setColorSchemeResources(
                R.color.primary, R.color.accent,
                R.color.primary_dark, R.color.primary_light);
        swipeRefreshWidget.setOnRefreshListener(this);
        mGankAdapter = new GankAdapter(getActivity(), mData);
        mGankAdapter.setOnItemClickListener(this);
        AlphaAnimatorAdapter animatorAdapter = new AlphaAnimatorAdapter(mGankAdapter, recycleView);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        recycleView.setLayoutManager(mLinearLayoutManager);
        recycleView.setHasFixedSize(true);
        recycleView.setItemAnimator(new SlideInOutBottomItemAnimator(recycleView));
        recycleView.setAdapter(animatorAdapter);
        defaultTipsHelper = new DefaultTipsHelper(getContext(), swipeRefreshWidget);
    }

    @Override
    public void onRefresh() {
        mData.clear();
        getPresenter().request(mType);
    }

    public void onAddData(List<GankBean> gankBeanList) {
        hideRefresh();
        mData.addAll(gankBeanList);
        mGankAdapter.notifyDataSetChanged();
        if (mGankAdapter.getItemCount() == 0) {
            defaultTipsHelper.showEmpty();
        }
    }

    public void onNetworkError(HttpExceptionHandle.ResponeThrowable responeThrowable) {
        hideRefresh();
        if (mGankAdapter.getItemCount() == 0) {
            defaultTipsHelper.showError(true, responeThrowable.message, view -> {
                defaultTipsHelper.showLoading(true);
                getPresenter().request(mType);
            });
        }
        Snackbar.make(swipeRefreshWidget, responeThrowable.message, Snackbar.LENGTH_SHORT).show();
    }

    private void showRefresh() {
        swipeRefreshWidget.setRefreshing(true);
    }

    private void hideRefresh() {
        defaultTipsHelper.hideLoading();
        // 防止刷新消失太快，让子弹飞一会儿. do not use lambda!!
        swipeRefreshWidget.postDelayed(() -> {
            if (swipeRefreshWidget != null) {
                swipeRefreshWidget.setRefreshing(false);
            }
        }, 1000);
    }

    @Override
    public void onItemClick(View view, int position) {
        GankBean resultsBean = mGankAdapter.getItem(position);
        getActivity().startActivity(WebActivity.newIntent(getActivity(), resultsBean.url, resultsBean.desc));
    }
}
