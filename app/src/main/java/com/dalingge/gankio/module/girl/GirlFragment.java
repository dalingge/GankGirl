package com.dalingge.gankio.module.girl;


import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dalingge.gankio.R;
import com.dalingge.gankio.common.Constants;
import com.dalingge.gankio.common.base.BaseFragment;
import com.dalingge.gankio.common.base.factory.RequiresPresenter;
import com.dalingge.gankio.common.bean.GankBean;
import com.dalingge.gankio.common.widgets.recyclerview.anim.adapter.AlphaAnimatorAdapter;
import com.dalingge.gankio.common.widgets.recyclerview.anim.itemanimator.SlideInOutBottomItemAnimator;
import com.dalingge.gankio.network.HttpExceptionHandle;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * A simple {@link Fragment} subclass.
 */
@RequiresPresenter(GirlPresenter.class)
public class GirlFragment extends BaseFragment<GirlPresenter> implements SwipeRefreshLayout.OnRefreshListener,GirlAdapter.OnItemClickListener {

    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    @BindView(R.id.swipe_refresh_widget)
    SwipeRefreshLayout swipeRefreshWidget;

    private ArrayList<GankBean> mData = new ArrayList<>();
    private GirlAdapter mGirlAdapter;
    private String mType;

    public static GirlFragment newInstance(String param1) {
        GirlFragment fragment = new GirlFragment();
        Bundle args = new Bundle();
        args.putString(Constants.BUNDLE_KEY_TYPE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mType = args.getString(Constants.BUNDLE_KEY_TYPE);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_girl;
    }

    @Override
    protected void initView(View view) {
        swipeRefreshWidget.setColorSchemeResources(
                R.color.primary, R.color.accent,
                R.color.primary_dark, R.color.primary_light);
        swipeRefreshWidget.setOnRefreshListener(this);
        mGirlAdapter = new GirlAdapter(getActivity(), mData);
        mGirlAdapter.setOnItemClickListener(this);
        AlphaAnimatorAdapter animatorAdapter = new AlphaAnimatorAdapter(mGirlAdapter, recycleView);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity()){
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        recycleView.setLayoutManager(mLinearLayoutManager);
        recycleView.setHasFixedSize(true);
        recycleView.setItemAnimator(new SlideInOutBottomItemAnimator(recycleView));
        recycleView.setAdapter(animatorAdapter);

        if (mData.isEmpty()) {
            showRefresh();
            onRefresh();
        }
    }

    @Override
    public void onRefresh() {
        mData.clear();
        getPresenter().request(mType);
    }


    public void onAddData(List<GankBean> gankBeanList) {
        hideRefresh();
        mData.addAll(gankBeanList);
        mGirlAdapter.notifyDataSetChanged();
    }

    public void onNetworkError(HttpExceptionHandle.ResponeThrowable responeThrowable) {
        hideRefresh();
        Snackbar.make(recycleView.getRootView(), responeThrowable.message, Snackbar.LENGTH_SHORT).show();
    }

    private void showRefresh(){
        swipeRefreshWidget.setRefreshing(true);
    }

    private void hideRefresh(){
        // 防止刷新消失太快，让子弹飞一会儿. do not use lambda!!
        swipeRefreshWidget.postDelayed(()-> {
            if(swipeRefreshWidget != null){
                swipeRefreshWidget.setRefreshing(false);
            }
        },1000);
    }

    @Override
    public void onItemClick(View view, int position) {
        ActivityOptionsCompat options;
        if (Build.VERSION.SDK_INT >= 21) {
            options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    getActivity(), view,  mGirlAdapter.getItem(position)._id);
        } else {
            options = ActivityOptionsCompat.makeScaleUpAnimation(
                    view,
                    view.getWidth()/2, view.getHeight()/2,//拉伸开始的坐标
                    0, 0);//拉伸开始的区域大小，这里用（0，0）表示从无到全屏
        }
        ActivityCompat.startActivity(getActivity(),ImagePagerActivity.newIntent(view.getContext(),position,mGirlAdapter.getData()),options.toBundle());
    }
}
