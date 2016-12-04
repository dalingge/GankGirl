package com.dalingge.gankio.common.widgets.recyclerview.refresh;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import com.dalingge.gankio.R;
import com.dalingge.gankio.common.widgets.recyclerview.utils.RecyclerViewStateUtils;


/**
 * FileName: SuperRefreshLayout
 * description:上拉加载 下拉刷新
 * Author: 丁博洋
 * Date: 2016/9/22
 */
public class SuperRefreshLayout extends SwipeRefreshLayout implements SwipeRefreshLayout.OnRefreshListener {

    private Activity mActivity;
    private RecyclerView mRecyclerView;
    private OnSuperRefreshLayoutListener superRefreshLayoutListener;

    private boolean loadMore = true;

    public SuperRefreshLayout(Context context) {
        this(context, null);
    }

    public SuperRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setColorSchemeResources(
                R.color.primary, R.color.accent,
                R.color.primary_dark, R.color.primary_light);
        setOnRefreshListener(this);
    }

    @Override
    public boolean canChildScrollUp() {
        if (mRecyclerView != null) {
            return ViewCompat.canScrollVertically(mRecyclerView, -1);
        }
        return super.canChildScrollUp();
    }

    /**
     * 获取RecyclerView并添加Footer
     */
    public void setRecyclerView(Activity activity, RecyclerView recyclerView) {
        if (recyclerView != null) {
            mActivity=activity;
            mRecyclerView = recyclerView;
            mRecyclerView.addOnScrollListener(new RecyclerViewOnScroll(this));
        }
    }

    public void refresh(){
        setRefreshing(true);
        if (superRefreshLayoutListener != null) {
            superRefreshLayoutListener.onRefreshing();
        }
    }

    @Override
    public void onRefresh() {
        if (superRefreshLayoutListener != null) {
            superRefreshLayoutListener.onRefreshing();
        }
    }

    public void loadMore() {
        LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mRecyclerView);
        if(state == LoadingFooter.State.Loading) {
            Log.d("@Cundong", "the state is Loading, just wait..");
            return;
        }

        RecyclerViewStateUtils.setFooterViewState(mActivity, mRecyclerView, 2, LoadingFooter.State.Loading, null);
        if (superRefreshLayoutListener != null) {
            superRefreshLayoutListener.onLoadMore();
        }
    }

    public void onFooterError(){
        RecyclerViewStateUtils.setFooterViewState(mActivity, mRecyclerView, 2, LoadingFooter.State.NetWorkError, v -> loadMore());
    }

    public void onFooterEnd(){
        setLoadMore(false);
        RecyclerViewStateUtils.setFooterViewState(mActivity, mRecyclerView, 2, LoadingFooter.State.TheEnd, v ->{
            setLoadMore(true);
            loadMore();
        });
    }

    /**
     * 加载结束记得调用
     */
    public void onLoadComplete() {
        if(isRefreshing()){
            // 防止刷新消失太快，让子弹飞一会儿.
            postDelayed(() -> setRefreshing(false), 1000);
        }

        RecyclerViewStateUtils.setFooterViewState(mRecyclerView, LoadingFooter.State.Normal);
    }

    public boolean isLoadMore() {
        return loadMore;
    }

    public void setLoadMore(boolean loadMore) {
        this.loadMore = loadMore;
    }

    public void setOnSuperRefreshLayoutListener(OnSuperRefreshLayoutListener superRefreshLayoutListener) {
        this.superRefreshLayoutListener = superRefreshLayoutListener;
    }

    public interface OnSuperRefreshLayoutListener {
        void onRefreshing();

        void onLoadMore();
    }
}
