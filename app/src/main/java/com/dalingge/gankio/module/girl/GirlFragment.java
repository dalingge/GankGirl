package com.dalingge.gankio.module.girl;


import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dalingge.gankio.R;
import com.dalingge.gankio.Constants;
import com.dalingge.gankio.common.base.BaseFragment;
import com.dalingge.gankio.common.factory.RequiresPresenter;
import com.dalingge.gankio.data.model.GankBean;
import com.dalingge.gankio.utils.DensityUtils;
import com.dalingge.gankio.common.widgets.recyclerview.adapter.HeaderAndFooterRecyclerViewAdapter;
import com.dalingge.gankio.common.widgets.recyclerview.refresh.SuperRefreshLayout;
import com.dalingge.gankio.module.girl.imagepager.ImagePagerActivity;
import com.dalingge.gankio.network.HttpExceptionHandle;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
@RequiresPresenter(GirlPresenter.class)
public class GirlFragment extends BaseFragment<GirlPresenter> implements GirlAdapter.OnItemClickListener, SuperRefreshLayout.OnSuperRefreshLayoutListener {

//    @BindView(R.id.appbar)
//    AppBarLayout appbar;
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_widget)
    SuperRefreshLayout superRefreshLayout;

    private ArrayList<GankBean> mData = new ArrayList<>();
    private GirlAdapter mGirlAdapter;
    private String mType;
    private int page = 1;

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
        //toolbar.setTitle(R.string.button_navigation_girl_text);
        superRefreshLayout.setOnSuperRefreshLayoutListener(this);
        superRefreshLayout.setRecyclerView(getActivity(), recyclerView);
        mGirlAdapter = new GirlAdapter(getActivity(), mData);
        mGirlAdapter.setOnItemClickListener(this);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity()) {
            @Override
            protected int getExtraLayoutSpace(RecyclerView.State state) {
                return 300;
            }
        };
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.bottom = -DensityUtils.dip2px(view.getContext(), 10);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstPosition = layoutManager.findFirstVisibleItemPosition();
                int lastPosition = layoutManager.findLastVisibleItemPosition();
                int visibleCount = lastPosition - firstPosition;
                //重置控件的位置及高度
                int elevation = 1;
                for (int i = firstPosition - 1; i <= (firstPosition + visibleCount) + 1; i++) {
                    View view = layoutManager.findViewByPosition(i);
                    if (view != null) {
                        if (view instanceof CardView) {
                            ((CardView) view).setCardElevation(DensityUtils.dip2px(recyclerView.getContext(), elevation));
                            elevation += 5;
                        }
                        float translationY = view.getTranslationY();
                        if (i > firstPosition && translationY != 0) {
                            view.setTranslationY(0);
                        }
                    }
                }

                View firstView = layoutManager.findViewByPosition(firstPosition);
                float firstViewTop = firstView.getTop();
                firstView.setTranslationY(-firstViewTop / 2.0f);
            }
        });
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new HeaderAndFooterRecyclerViewAdapter(mGirlAdapter));

        setTipView(superRefreshLayout);
        if (mData.isEmpty()) {
            getTipsHelper().showLoading(true);
            getPresenter().request(mType, page);
        }
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
        ActivityOptionsCompat options;
        if (Build.VERSION.SDK_INT >= 21) {
            options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    getActivity(), view, mGirlAdapter.getItem(position)._id);
        } else {
            options = ActivityOptionsCompat.makeScaleUpAnimation(
                    view,
                    view.getWidth() / 2, view.getHeight() / 2,//拉伸开始的坐标
                    0, 0);//拉伸开始的区域大小，这里用（0，0）表示从无到全屏
        }
        ActivityCompat.startActivity(getActivity(), ImagePagerActivity.newIntent(view.getContext(), position, mGirlAdapter.getData()), options.toBundle());
    }

    public void onAddData(List<GankBean> gankBeanList) {
        getTipsHelper().hideLoading();
        superRefreshLayout.onLoadComplete();
        mData.addAll(gankBeanList);
        mGirlAdapter.notifyDataSetChanged();
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


    public boolean isFirstPage() {
        return mGirlAdapter.getItemCount() <= 0;
    }

}
