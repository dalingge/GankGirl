package com.dalingge.gankio.module.home;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.View;

import com.dalingge.gankio.R;
import com.dalingge.gankio.common.base.BaseTadPageFragment;
import com.dalingge.gankio.main.activity.SubmitGankActivity;
import com.dalingge.gankio.main.adapter.ViewPageFragmentAdapter;
import com.dalingge.gankio.main.fragment.WelfareListFragment;

import butterknife.BindView;
import rx.Observable;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeTadPageFragment extends BaseTadPageFragment<HomePresenter> {

    @BindView(R.id.fab)
    FloatingActionButton fab;

    public static HomeTadPageFragment newInstance(String param1) {
        HomeTadPageFragment fragment = new HomeTadPageFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home_tad_page;
    }

    @Override
    protected void initView(View view) {
        fab.setOnClickListener(v ->  startActivity(SubmitGankActivity.newIntent(v.getContext())));
    }

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {

        String[] title = getResources().getStringArray(R.array.home_viewpage_arrays);
        Observable.from(title).subscribe(s -> adapter.addTab(s, "", HomeFragment.class,getBundle(s)));

    }

    /**
     * 基类会根据不同的Type展示相应的数据
     *
     * @param type 要显示的数据类别
     * @return
     */
    private Bundle getBundle(String type) {
        Bundle bundle = new Bundle();
        bundle.putString(WelfareListFragment.BUNDLE_KEY_TYPE, type);
        return bundle;
    }
}
