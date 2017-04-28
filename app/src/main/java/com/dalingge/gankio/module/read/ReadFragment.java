package com.dalingge.gankio.module.read;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.dalingge.gankio.Constants;
import com.dalingge.gankio.R;
import com.dalingge.gankio.common.base.BaseLazyFragment;
import com.dalingge.gankio.common.factory.RequiresPresenter;
import com.dalingge.gankio.common.glide.GlideImageLoader;
import com.dalingge.gankio.common.widgets.recyclerview.adapter.HeaderAndFooterRecyclerViewAdapter;
import com.dalingge.gankio.common.widgets.recyclerview.anim.adapter.AlphaAnimatorAdapter;
import com.dalingge.gankio.common.widgets.recyclerview.anim.itemanimator.SlideInOutBottomItemAnimator;
import com.dalingge.gankio.common.widgets.recyclerview.refresh.SuperRefreshLayout;
import com.dalingge.gankio.common.widgets.recyclerview.utils.RecyclerViewUtils;
import com.dalingge.gankio.data.model.ReadChildTypeBean;
import com.dalingge.gankio.data.model.ReadListBean;
import com.dalingge.gankio.network.RequestCommand;
import com.dalingge.gankio.network.RequestContext;
import com.dalingge.gankio.utils.DensityUtils;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static android.view.View.inflate;
import static butterknife.ButterKnife.findById;


/**
 * A simple {@link Fragment} subclass.
 */
@RequiresPresenter(ReadPresenter.class)
public class ReadFragment extends BaseLazyFragment<ReadPresenter> implements SuperRefreshLayout.OnSuperRefreshLayoutListener {

    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_widget)
    SuperRefreshLayout superRefreshLayout;

    private ArrayList<ReadListBean> mData = new ArrayList<>();
    private ReadAdapter mReadAdapter;

    private String type;
    private String url;
    private String url_page;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            type = args.getString(Constants.BUNDLE_KEY_TYPE);
            url = args.getString(Constants.BUNDLE_KEY_URL);
            setUrl_page(url);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_read;
    }

    @Override
    protected void initView(View view) {
        superRefreshLayout.setOnSuperRefreshLayoutListener(this);
        superRefreshLayout.setRecyclerView(getActivity(), recyclerView);
        mReadAdapter = new ReadAdapter(view.getContext(), mData);
        AlphaAnimatorAdapter animatorAdapter = new AlphaAnimatorAdapter(mReadAdapter, recyclerView);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new SlideInOutBottomItemAnimator(recyclerView));
        recyclerView.setAdapter(new HeaderAndFooterRecyclerViewAdapter(animatorAdapter));
        setTipView(superRefreshLayout);
    }

    @Override
    protected void lazyLoad() {
        if ((!isPrepared || !isVisible)) {
            return;
        }

        if (isFirstPage()) {
            getTipsHelper().showLoading(true);
            requestData();
        }
    }

    protected void requestData() {
        RequestContext requestContext = new RequestContext(RequestCommand.REQUEST_READ_LIST);
        requestContext.setType(type);
        requestContext.setUrl(url_page);
        getPresenter().request(requestContext);
    }

    public void onDataChild(List<ReadChildTypeBean> datas) {
        View mHeaderView = inflate(getContext(), R.layout.recycler_header_read, null);
        FlexboxLayout flexboxLayout = findById(mHeaderView, R.id.flexboxLayout);
        if (flexboxLayout.getRootView() == null)
            return;
        flexboxLayout.removeAllViews();

        for (ReadChildTypeBean data : datas) {
            ImageView imageView = new ImageView(getContext());
            FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(DensityUtils.dip2px(getContext(), 30), DensityUtils.dip2px(getContext(), 30));
            imageView.setLayoutParams(layoutParams);
            imageView.setOnClickListener(v -> ReadMoreActivity.start(getContext(),data.getUrl(),data.getTitle(),data.getImg()));
            flexboxLayout.addView(imageView);

            int size = DensityUtils.dip2px(getContext(), 10);
            FlexboxLayout.LayoutParams layoutParam = (FlexboxLayout.LayoutParams) imageView
                    .getLayoutParams();
            layoutParam.setMargins(size, size, size, 0);
            imageView.setLayoutParams(layoutParam);
            GlideImageLoader.loadAdapterCircle(getContext(), data.getImg(), imageView);
        }
        RecyclerViewUtils.setHeaderView(recyclerView, mHeaderView);
    }

    public void onDataList(List<ReadListBean> datas) {
        getTipsHelper().hideLoading();
        superRefreshLayout.onLoadComplete();
        mData.addAll(datas);
        mReadAdapter.notifyDataSetChanged();
        if (isFirstPage()) {
            getTipsHelper().showEmpty();
        }
    }

    public boolean isFirstPage() {
        return mReadAdapter.getItemCount() <= 0;
    }

    @Override
    public void onRefreshing() {
        mData.clear();
        setUrl_page(url);
        requestData();
    }

    @Override
    public void onLoadMore() {
        requestData();
    }

    public void setUrl_page(String url_page) {
        this.url_page = url_page;
    }
}
