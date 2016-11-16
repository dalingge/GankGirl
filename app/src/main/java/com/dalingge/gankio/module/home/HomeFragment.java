package com.dalingge.gankio.module.home;


import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.dalingge.gankio.R;
import com.dalingge.gankio.common.base.BaseLazyFragment;
import com.dalingge.gankio.common.base.factory.RequiresPresenter;
import com.dalingge.gankio.common.bean.GankBean;
import com.dalingge.gankio.common.widgets.recyclerview.anim.adapter.AlphaAnimatorAdapter;
import com.dalingge.gankio.common.widgets.recyclerview.anim.itemanimator.SlideInOutBottomItemAnimator;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
@RequiresPresenter(HomePresenter.class)
public class HomeFragment extends BaseLazyFragment<HomePresenter> implements SwipeRefreshLayout.OnRefreshListener {

    public static final String BUNDLE_KEY_ID = "BUNDLE_KEY_ID";
    public static final String BUNDLE_KEY_TYPE = "BUNDLE_KEY_TYPE";

    //@BindView(R.id.recycle_view)
    RecyclerView recycleView;
    ///@BindView(R.id.swipe_refresh_widget)
    SwipeRefreshLayout swipeRefreshWidget;

    private ArrayList<GankBean> mData = new ArrayList<>();
    private HomeAdapter mHomeAdapter;
    private int mId;
    private String mType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mId =args.getInt(BUNDLE_KEY_ID);
            mType = args.getString(BUNDLE_KEY_TYPE);
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
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view) {
        recycleView = (RecyclerView) view.findViewById(R.id.recycle_view);
        swipeRefreshWidget = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_widget);
        swipeRefreshWidget.setColorSchemeResources(
                R.color.primary, R.color.accent,
                R.color.primary_dark, R.color.primary_light);
        swipeRefreshWidget.setOnRefreshListener(this);
        mHomeAdapter = new HomeAdapter(getActivity(), mData);
        AlphaAnimatorAdapter animatorAdapter = new AlphaAnimatorAdapter(mHomeAdapter, recycleView);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        recycleView.setLayoutManager(mLinearLayoutManager);
        recycleView.setHasFixedSize(true);
        recycleView.setItemAnimator(new SlideInOutBottomItemAnimator(recycleView));
        recycleView.setAdapter(animatorAdapter);
    }

    public void addData(List<GankBean> gankBeanList) {
        hideRefresh();
        mData.addAll(gankBeanList);
        mHomeAdapter.notifyDataSetChanged();
    }

    private void showRefresh(){
        swipeRefreshWidget.setRefreshing(true);
    }

    private void hideRefresh(){
        // 防止刷新消失太快，让子弹飞一会儿. do not use lambda!!
        swipeRefreshWidget.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(swipeRefreshWidget != null){
                    swipeRefreshWidget.setRefreshing(false);
                }
            }
        },1000);
    }

    public void showMessage(String message) {
        hideRefresh();
        Snackbar.make(recycleView.getRootView(), message, Snackbar.LENGTH_SHORT).show();
        Toast.makeText(getContext(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        mData.clear();
        getPresenter().request(mId,mType);
    }
}
