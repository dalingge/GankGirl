package com.dalingge.gankio.module.home.gank;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.View;

import com.dalingge.gankio.R;
import com.dalingge.gankio.common.base.BaseTadPageFragment;
import com.dalingge.gankio.common.base.view.ViewPageFragmentAdapter;
import com.dalingge.gankio.module.home.submit.SubmitGankActivity;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;


/**
 * A simple {@link Fragment} subclass.
 */
public class GankTadPageFragment extends BaseTadPageFragment {

    @BindView(R.id.fab)
    FloatingActionButton fab;

    public static GankTadPageFragment newInstance(String param1) {
        GankTadPageFragment fragment = new GankTadPageFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gank_tad_page;
    }

    @Override
    protected void initView(View view) {
    }

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {

        String[] title = getResources().getStringArray(R.array.home_viewpage_arrays);
        Observable.from(title).subscribe(s ->  {
            adapter.addTab(s, "", GankFragment.class,getBundle(s));
        });
        viewPager.setOffscreenPageLimit(title.length);
    }

    /**
     * 基类会根据不同的Type展示相应的数据
     *
     * @param type 要显示的数据类别
     * @return
     */
    private Bundle getBundle(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(GankFragment.BUNDLE_KEY_TYPE, type);
        return bundle;
    }

    @OnClick({R.id.fab})
    public void onClick(View view){
        startActivity(SubmitGankActivity.newIntent(view.getContext()));
    }
}
