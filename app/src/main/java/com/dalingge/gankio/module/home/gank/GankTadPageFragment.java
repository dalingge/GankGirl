package com.dalingge.gankio.module.home.gank;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.dalingge.gankio.R;
import com.dalingge.gankio.Constants;
import com.dalingge.gankio.common.base.BaseTadPageFragment;
import com.dalingge.gankio.common.base.view.ViewPageFragmentAdapter;
import com.dalingge.gankio.module.home.submit.SubmitGankActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;


/**
 * A simple {@link Fragment} subclass.
 */
public class GankTadPageFragment extends BaseTadPageFragment implements SearchView.OnQueryTextListener,AdapterView.OnItemClickListener{

//    @BindView(R.id.toolbar)
//    Toolbar toolbar;

    private SearchView searchView;

    public static GankTadPageFragment newInstance(String param1) {
        GankTadPageFragment fragment = new GankTadPageFragment();
        Bundle args = new Bundle();
        args.putString(Constants.BUNDLE_KEY_TYPE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_gank_tad_page;
    }

     private List<String> stringList = new ArrayList<>();
    @Override
    protected void initView(View view) {
//        toolbar.setTitle(R.string.button_navigation_home_text);
//        toolbar.inflateMenu(R.menu.menu_home);
//        MenuItem item = toolbar.getMenu().findItem(R.id.search_view);
//        searchView = (SearchView) MenuItemCompat.getActionView(item);
//        SearchView.SearchAutoComplete completeText = (SearchView.SearchAutoComplete)searchView.findViewById(R.id.search_src_text);
//        ArrayAdapter arrayAdapter= new ArrayAdapter<>(getActivity(), R.layout.item_search_auto_complete,R.id.text1 ,stringList);
//        completeText.setAdapter(arrayAdapter);
//        completeText.setOnItemClickListener(this);
//        completeText.setThreshold(0);
//        searchView.setOnQueryTextListener(this);

//        RxSearchView.queryTextChanges(searchView)
//                .subscribeOn(AndroidSchedulers.mainThread())
//                .debounce(300, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
//                .filter(new Func1<CharSequence, Boolean>() {
//                    @Override public Boolean call(CharSequence charSequence) {
//                        // 清空搜索出来的结构
//                        stringList.clear();
//                        arrayAdapter.notifyDataSetChanged();
//                        //当 EditText 中文字大于0的时候
//                        return charSequence.length() > 0;
//                    }
//                }).switchMap(new Func1<CharSequence, Observable<List<GankBean>>>() {
//                    @Override public Observable<List<GankBean>> call(CharSequence charSequence) {
//                        // 搜索
//                        return HttpRetrofit.getInstance().apiService.getSearchQuery(charSequence.toString(),1).compose(HttpRetrofit.toTransformer());
//                    }
//                })
//                .map(new Func1<List<GankBean>, List<String>>() {
//                    @Override
//                    public  List<String> call(List<GankBean> gankBeanList) {
//                        List<String> stringList = new ArrayList<>();
//                        for (GankBean gankBean :gankBeanList){
//                            stringList.add(gankBean.desc);
//                        }
//                        return stringList;
//                    }
//                })
//                .subscribe(new Action1<List<String>>() {
//                    @Override
//                    public void call(List<String> gankBean) {
//                        stringList.addAll(gankBean);
//                        arrayAdapter.notifyDataSetChanged();
//                       // Toast.makeText(getContext(),gankBean.desc,Toast.LENGTH_SHORT).show();
//                    }
//                }, new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        Toast.makeText(getContext(),throwable.getMessage(),Toast.LENGTH_SHORT).show();
//                    }
//                });
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
        //searchView.setQuery(testStrings[position], true);
    }

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {

        String[] title = getResources().getStringArray(R.array.home_viewpage_arrays);
        Observable.fromArray(title).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                adapter.addTab(s, "", GankFragment.class, getBundle(s));
            }
        });
        viewPager.setOffscreenPageLimit(title.length);
        adapter.notifyDataSetChanged();
    }

    @OnClick({R.id.fab})
    public void onClick(View view) {
        startActivity(SubmitGankActivity.newIntent(view.getContext()));
    }

}
