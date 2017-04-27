package com.dalingge.gankio.module.test;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dalingge.gankio.R;
import com.dalingge.gankio.common.base.BaseToolbarActivity;
import com.dalingge.gankio.common.factory.RequiresPresenter;
import com.dalingge.gankio.network.HttpExceptionHandle;

@RequiresPresenter(TestPresenter.class)
public class TestActivity extends BaseToolbarActivity<TestPresenter> implements View.OnClickListener {

    Button btnStart;
    Button btnStop;
    TextView tvLog;

    @Override
    protected int getLayout() {
        return R.layout.activity_test;
    }

    @Override
    protected void initView() {
        btnStart = (Button) findViewById(R.id.btn_start);
        btnStop = (Button) findViewById(R.id.btn_stop);
        tvLog = (TextView) findViewById(R.id.tv_log);
        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_start:

                getPresenter().start(TestPresenter.REQUEST_ITEMS);
                break;
            case R.id.btn_stop:
                getPresenter().stop(TestPresenter.REQUEST_ITEMS);
                break;
        }
    }

    public void onData(String s) {
        tvLog.setText(s);
    }

    public void onNetworkError(HttpExceptionHandle.ResponeThrowable responeThrowable) {
        tvLog.setText(responeThrowable.message);
    }

}
