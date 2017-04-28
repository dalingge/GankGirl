package com.dalingge.gankio.module.read;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.dalingge.gankio.Constants;
import com.dalingge.gankio.R;
import com.dalingge.gankio.common.base.BaseToolbarActivity;
import com.dalingge.gankio.common.factory.RequiresPresenter;
import com.dalingge.gankio.common.widgets.recyclerview.adapter.HeaderAndFooterRecyclerViewAdapter;
import com.dalingge.gankio.common.widgets.recyclerview.anim.adapter.AlphaAnimatorAdapter;
import com.dalingge.gankio.common.widgets.recyclerview.anim.itemanimator.SlideInOutBottomItemAnimator;
import com.dalingge.gankio.common.widgets.recyclerview.refresh.SuperRefreshLayout;
import com.dalingge.gankio.data.model.ReadListBean;
import com.dalingge.gankio.network.RequestCommand;
import com.dalingge.gankio.network.RequestContext;
import com.dalingge.gankio.utils.ImageUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

@RequiresPresenter(ReadMorePresenter.class)
public class ReadMoreActivity extends BaseToolbarActivity<ReadMorePresenter> implements SuperRefreshLayout.OnSuperRefreshLayoutListener{

    @BindView(R.id.recycle_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_widget)
    SuperRefreshLayout superRefreshLayout;

    private String mUrl, mTitle, mLogo;

    private ArrayList<ReadListBean> mData = new ArrayList<>();
    private ReadAdapter mReadAdapter;
    private String url_page;

    public static void start(Context context, String extraURL, String extraTitle, String extraLogo) {
        Intent intent = new Intent(context, ReadMoreActivity.class);
        intent.putExtra(Constants.EXTRA_URL, extraURL);
        intent.putExtra(Constants.EXTRA_TITLE, extraTitle);
        intent.putExtra(Constants.EXTRA_LOGO, extraLogo);
        context.startActivity(intent);
    }

    @Override
    public boolean isBack() {
        return true;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_read_more;
    }

    @Override
    protected void initView() {
        mUrl = getIntent().getStringExtra(Constants.EXTRA_URL);
        mTitle = getIntent().getStringExtra(Constants.EXTRA_TITLE);
        mLogo = getIntent().getStringExtra(Constants.EXTRA_LOGO);
        getToolbar().setTitle("   "+mTitle);
        Glide.with(this)
                .load(mLogo)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(new SimpleTarget<Bitmap>(80,80) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        getToolbar().setLogo(new BitmapDrawable(getResources(), ImageUtils.getRoundedCornerBitmap(resource)));
                    }
                });

        superRefreshLayout.setOnSuperRefreshLayoutListener(this);
        superRefreshLayout.setRecyclerView(this, recyclerView);
        mReadAdapter = new ReadAdapter(this, mData);
        AlphaAnimatorAdapter animatorAdapter = new AlphaAnimatorAdapter(mReadAdapter, recyclerView);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new SlideInOutBottomItemAnimator(recyclerView));
        recyclerView.setAdapter(new HeaderAndFooterRecyclerViewAdapter(animatorAdapter));
        setTipView(superRefreshLayout);

        setUrl_page(mUrl);
        if (mData.isEmpty()) {
            getTipsHelper().showLoading(true);
            requestData();
        }
    }

    protected void requestData() {
        RequestContext requestContext = new RequestContext(RequestCommand.REQUEST_READ_CHILD_LIST);
        requestContext.setType(mTitle);
        requestContext.setUrl(url_page);
        getPresenter().request(requestContext);
    }

    @Override
    public void onRefreshing() {
        mData.clear();
        setUrl_page(mUrl);
        requestData();
    }

    @Override
    public void onLoadMore() {
        requestData();
    }

    public void onDataList(List<ReadListBean> datas) {
        getTipsHelper().hideLoading();
        superRefreshLayout.onLoadComplete();
        mData.addAll(datas);
        mReadAdapter.notifyDataSetChanged();
    }

    public void setUrl_page(String url_page) {
        this.url_page = url_page;
    }
}
