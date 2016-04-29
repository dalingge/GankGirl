package com.dalingge.gankio.main.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

import com.dalingge.gankio.R;
import com.dalingge.gankio.base.BaseActivity;
import com.dalingge.gankio.main.presenter.SubmitGankPresenter;
import com.dalingge.gankio.main.view.ISubmitGankView;

import butterknife.Bind;

/**
 * FileName: SubmitGankActivity.java
 * description:
 * Author: dingby(445850053@qq.com)
 * Date: 2016/4/11
 */
public class SubmitGankActivity extends BaseActivity<SubmitGankPresenter> implements ISubmitGankView {

    @Bind(R.id.et_url)
    TextInputEditText etUrl;
    @Bind(R.id.et_desc)
    TextInputEditText etDesc;
    @Bind(R.id.et_who)
    TextInputEditText etWho;
    @Bind(R.id.et_type)
    TextInputEditText etType;
    @Bind(R.id.fab_send)
    FloatingActionButton fabSend;

    private SubmitGankPresenter submitGankPresenter;
    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SubmitGankActivity.class);
        return intent;
    }

    @Override
    protected boolean isBack() {
        return true;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_add_gank;
    }

    @Override
    protected void initView() {
        setTitle("提交干货");

        submitGankPresenter =new SubmitGankPresenter(this);
        etType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                final String[] types = {"福利", "iOS", "Android", "前端", "App", "拓展资源", "瞎推荐", "休息视频"};
                builder.setItems(types, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        etType.setText(types[which]);
                    }
                });
                builder.show();
            }
        });

        fabSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitGankPresenter.submitGank(getName(),etUrl,etDesc,etWho,etType);
            }
        });
    }


    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showSuccessMsg(String msg) {
        Toast.makeText(this, msg,Toast.LENGTH_LONG).show();
    }

    @Override
    public void showFailMsg(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
    }
}
