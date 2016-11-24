package com.dalingge.gankio.module.home.gank;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.dalingge.gankio.R;
import com.dalingge.gankio.common.Constants;
import com.dalingge.gankio.common.base.BaseTadPageFragment;
import com.dalingge.gankio.common.base.view.ViewPageFragmentAdapter;
import com.dalingge.gankio.module.home.submit.SubmitGankActivity;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;


/**
 * A simple {@link Fragment} subclass.
 */
public class GankTadPageFragment extends BaseTadPageFragment implements SearchView.OnQueryTextListener,AdapterView.OnItemClickListener{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
     private SearchView searchView;
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
    String [] testStrings = getResources().getStringArray(R.array.submit_gank_type_arrays);
    @Override
    protected void initView(View view) {
        toolbar.inflateMenu(R.menu.menu_home);
        MenuItem item = toolbar.getMenu().findItem(R.id.search_view);
        searchView = (SearchView) MenuItemCompat.getActionView(item);
        SearchView.SearchAutoComplete completeText = (SearchView.SearchAutoComplete)searchView.findViewById(R.id.search_src_text);
        completeText.setAdapter(new ArrayAdapter<>(getActivity(), R.layout.item_search_auto_complete, testStrings));
        completeText.setOnItemClickListener(this);
        completeText.setThreshold(0);
        searchView.setOnQueryTextListener(this);

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Toast.makeText(getActivity(), query, Toast.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        searchView.setQuery(testStrings[position], true);
    }

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {

        String[] title = getResources().getStringArray(R.array.home_viewpage_arrays);
        Observable.from(title).subscribe(s -> {
            adapter.addTab(s, "", GankFragment.class, getBundle(s));
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
        bundle.putString(Constants.BUNDLE_KEY_TYPE, type);
        return bundle;
    }

    @OnClick({R.id.fab})
    public void onClick(View view) {
        startActivity(SubmitGankActivity.newIntent(view.getContext()));
    }

}
