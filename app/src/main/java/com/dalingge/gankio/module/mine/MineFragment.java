package com.dalingge.gankio.module.mine;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.dalingge.gankio.R;
import com.dalingge.gankio.common.base.BaseFragment;
import com.dalingge.gankio.common.widgets.ArcCollapsingToolbarLayout;

import butterknife.BindView;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MineFragment extends BaseFragment implements AppBarLayout.OnOffsetChangedListener {
    // 控制ToolBar的变量
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 1.0f;

    @BindView(R.id.collapsing_toolbar)
    ArcCollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.iv_avatar)
    ImageView ivAvatar;

    private boolean mIsTheTitleVisible = false;

    public MineFragment() {
        // Required empty public constructor
    }

    public static MineFragment newInstance(String param1) {
        MineFragment fragment = new MineFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView(View view) {
        appBar.addOnOffsetChangedListener(this);
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
        handleToolbarTitleVisibility(percentage);
    }

    // 处理ToolBar的显示
    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {
            if (!mIsTheTitleVisible) {
                collapsingToolbar.setPaintColor(Color.TRANSPARENT);
                ivAvatar.setVisibility(View.GONE);
                mIsTheTitleVisible = true;
            }
        } else {
            if (mIsTheTitleVisible) {
                collapsingToolbar.setPaintColor(0xFFEBEBEB);
                ivAvatar.setVisibility(View.VISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }
}
