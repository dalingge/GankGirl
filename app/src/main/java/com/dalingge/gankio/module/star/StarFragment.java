package com.dalingge.gankio.module.star;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.dalingge.gankio.R;
import com.dalingge.gankio.common.base.BaseFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class StarFragment extends BaseFragment {


    public StarFragment() {
        // Required empty public constructor
    }


    public static StarFragment newInstance(String param1) {
        StarFragment fragment = new StarFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_star;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        String agrs1 = bundle.getString("agrs1");
        TextView tv = (TextView)view.findViewById(R.id.tv_location);
        tv.setText(agrs1);
    }


}
