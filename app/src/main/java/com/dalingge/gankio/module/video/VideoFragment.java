package com.dalingge.gankio.module.video;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.dalingge.gankio.Constants;
import com.dalingge.gankio.R;
import com.dalingge.gankio.common.base.BaseFragment;
import com.dalingge.gankio.common.factory.RequiresPresenter;
import com.dalingge.gankio.common.widgets.recyclerview.adapter.HeaderAndFooterRecyclerViewAdapter;
import com.dalingge.gankio.common.widgets.recyclerview.anim.adapter.AlphaAnimatorAdapter;
import com.dalingge.gankio.common.widgets.recyclerview.anim.itemanimator.SlideInOutBottomItemAnimator;
import com.dalingge.gankio.common.widgets.recyclerview.refresh.SuperRefreshLayout;
import com.dalingge.gankio.data.model.GankBean;
import com.dalingge.gankio.module.AboutActivity;
import com.dalingge.gankio.module.home.gank.GankAdapter;
import com.dalingge.gankio.module.home.submit.SubmitGankActivity;
import com.dalingge.gankio.module.video.play.VideoPlayActivity;
import com.dalingge.gankio.module.web.WebActivity;
import com.dalingge.gankio.network.HttpExceptionHandle;
import com.dalingge.gankio.network.RequestCommand;
import com.dalingge.gankio.network.RequestContext;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * A simple {@link Fragment} subclass.
 */
@RequiresPresenter(VideoPresenter.class)
public class VideoFragment extends BaseFragment<VideoPresenter> implements Toolbar.OnMenuItemClickListener,GankAdapter.OnItemClickListener, SuperRefreshLayout.OnSuperRefreshLayoutListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_widget)
    SuperRefreshLayout superRefreshLayout;

    private ArrayList<GankBean> mData = new ArrayList<>();
    private GankAdapter mGankAdapter;
    private int page = 1;

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
        toolbar.inflateMenu(R.menu.menu_main);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setTitle("休息视频");
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


        if (mData.isEmpty()) {
            getTipsHelper().showLoading(true);
            requestData();
        }
    }

    protected void requestData() {
        RequestContext requestContext = new RequestContext(RequestCommand.REQUEST_VIDEO_LIST);
        requestContext.setType("休息视频");
        requestContext.setPage(page);
        getPresenter().request(requestContext);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_add){
            startActivity(SubmitGankActivity.newIntent(getContext()));
            return true;
        }else if(id==R.id.action_about){
            startActivity(AboutActivity.newIntent(getContext()));
            return true;
        }
        return false;
    }

    @Override
    public void onRefreshing() {
        mData.clear();
        page = 1;
        requestData();
    }

    @Override
    public void onLoadMore() {
        page = page + 1;
        requestData();
    }

    @Override
    public void onItemClick(View view, int position) {
        GankBean resultsBean = mGankAdapter.getItem(position);
        if(getPresenter().isPlay(resultsBean.url)){
            VideoPlayActivity.start(getContext(),resultsBean.url,resultsBean.desc);
        }else {
            WebActivity.start(getContext(),resultsBean.url,resultsBean.desc);
        }
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
            getTipsHelper().showError(true, responeThrowable.message, v -> {
                getTipsHelper().showLoading(true);
                onRefreshing();
            });
        } else {
            superRefreshLayout.onLoadComplete();
            superRefreshLayout.onFooterError();
        }
    }

    public boolean isFirstPage() {
        return mGankAdapter.getItemCount() <= 0;
    }

}
