package com.dalingge.gankio.module.home.gank;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dalingge.gankio.R;
import com.dalingge.gankio.common.Constants;
import com.dalingge.gankio.common.base.BaseLazyFragment;
import com.dalingge.gankio.common.base.factory.RequiresPresenter;
import com.dalingge.gankio.common.bean.GankBean;
import com.dalingge.gankio.common.widgets.recyclerview.adapter.HeaderAndFooterRecyclerViewAdapter;
import com.dalingge.gankio.common.widgets.recyclerview.anim.adapter.AlphaAnimatorAdapter;
import com.dalingge.gankio.common.widgets.recyclerview.anim.itemanimator.SlideInOutBottomItemAnimator;
import com.dalingge.gankio.common.widgets.recyclerview.refresh.SuperRefreshLayout;
import com.dalingge.gankio.module.web.WebActivity;
import com.dalingge.gankio.network.HttpExceptionHandle;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * A simple {@link Fragment} subclass.
 */
@RequiresPresenter(GankPresenter.class)
public class GankFragment extends BaseLazyFragment<GankPresenter> implements  GankAdapter.OnItemClickListener , SuperRefreshLayout.OnSuperRefreshLayoutListener{

    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_widget)
    SuperRefreshLayout superRefreshLayout;

    private ArrayList<GankBean> mData = new ArrayList<>();
    private GankAdapter mGankAdapter;
    private String mType;
    private int page = 1;

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

        if (isFirstPage()) {
            getTipsHelper().showLoading(true);
            getPresenter().request(mType,page);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gank;
    }

    @Override
    protected void initView(View view) {

        superRefreshLayout.setOnSuperRefreshLayoutListener(this);
        superRefreshLayout.setRecyclerView(getActivity(), recyclerView);
        mGankAdapter = new GankAdapter(getActivity(), mData);
        mGankAdapter.setOnItemClickListener(this);
        AlphaAnimatorAdapter animatorAdapter = new AlphaAnimatorAdapter(mGankAdapter, recyclerView);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new SlideInOutBottomItemAnimator(recyclerView));
        recyclerView.setAdapter(new HeaderAndFooterRecyclerViewAdapter(animatorAdapter));
        setTipView(superRefreshLayout);

    }

    @Override
    public void onRefreshing() {
        mData.clear();
        page = 1;
        getPresenter().request(mType, page);
    }

    @Override
    public void onLoadMore() {
        page = page + 1;
        getPresenter().request(mType, page);
    }

    @Override
    public void onItemClick(View view, int position) {
        GankBean resultsBean = mGankAdapter.getItem(position);
        getActivity().startActivity(WebActivity.newIntent(getActivity(), resultsBean.url, resultsBean.desc));
    }

    public boolean isFirstPage() {
        return mGankAdapter.getItemCount() <= 0;
    }

    public void onAddData(List<GankBean> gankBeanList) {
        getTipsHelper().hideLoading();
        superRefreshLayout.onLoadComplete();
        mData.addAll(gankBeanList);
        mGankAdapter.notifyDataSetChanged();
        if (isFirstPage()) {
            getTipsHelper().showEmpty();
        }
    }

    public void onNetworkError(HttpExceptionHandle.ResponeThrowable responeThrowable) {
        if (isFirstPage()) {
            getTipsHelper().showError(true, responeThrowable.message, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getTipsHelper().showLoading(true);
                    onRefreshing();
                }
            });
        } else {
            superRefreshLayout.onLoadComplete();
            superRefreshLayout.onFooterError();
        }
    }
}
