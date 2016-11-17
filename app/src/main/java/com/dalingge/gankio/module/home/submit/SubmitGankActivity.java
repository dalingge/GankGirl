package com.dalingge.gankio.module.home.submit;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.dalingge.gankio.R;
import com.dalingge.gankio.common.base.BaseToolbarActivity;
import com.dalingge.gankio.common.base.factory.RequiresPresenter;
import com.dalingge.gankio.common.utils.RegexUtils;

import butterknife.BindView;


/**
 * FileName: SubmitGankActivity.java
 * description:
 * Author: dingby(445850053@qq.com)
 * Date: 2016/4/11
 */
@RequiresPresenter(SubmitGankPresenter.class)
public class SubmitGankActivity extends BaseToolbarActivity<SubmitGankPresenter> {

    @BindView(R.id.et_url)
    TextInputEditText etUrl;
    @BindView(R.id.et_desc)
    TextInputEditText etDesc;
    @BindView(R.id.et_who)
    TextInputEditText etWho;
    @BindView(R.id.et_type)
    TextInputEditText etType;
    @BindView(R.id.fab_send)
    FloatingActionButton fabSend;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;

    public static Intent newIntent(Context context) {
        return new Intent(context, SubmitGankActivity.class);
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
        etType.setOnClickListener(view -> {
            String[] types = getResources().getStringArray(R.array.submit_gank_type_arrays);
            new AlertDialog.Builder(view.getContext()).setItems(types, (dialog, which) -> etType.setText(types[which])).show();
        });

        fabSend.setOnClickListener(view -> {
                    if (!validateInput())
                        return;
                    getPresenter().request(etUrl.getText().toString(), etDesc.getText().toString(), etWho.getText().toString(), etType.getText().toString());
                }
        );
    }


    private boolean validateInput() {
        boolean hasErrors = false;

        if (TextUtils.isEmpty(etUrl.getText())) {
            setError(etUrl, R.string.et_error_no_url);
            hasErrors = true;
        } else {
            removeError(etUrl);
            if (!RegexUtils.checkURL(etUrl.getText().toString())) {
                setError(etUrl, R.string.et_error_no_urls);
                hasErrors = true;
            }
        }

        if (TextUtils.isEmpty(etDesc.getText())) {
            setError(etDesc, R.string.et_error_no_desc);
            hasErrors = true;
        } else {
            removeError(etDesc);
        }

        if (TextUtils.isEmpty(etWho.getText())) {
            setError(etWho, R.string.et_error_no_who);
            hasErrors = true;
        } else {
            removeError(etWho);
        }

        if (TextUtils.isEmpty(etType.getText())) {
            setError(etType, R.string.et_error_no_type);
            hasErrors = true;
        } else {
            removeError(etType);
        }


        return !hasErrors;
    }


    private void setError(TextInputEditText editText, @StringRes int errorRes) {
        TextInputLayout layout = (TextInputLayout) editText.getParent();
        layout.setError(getString(errorRes));
    }


    private void removeError(TextInputEditText editText) {
        TextInputLayout layout = (TextInputLayout) editText.getParent();
        layout.setError(null);
    }


    public void onSuccess(String msg) {
        etUrl.setText(null);
        etDesc.setText(null);
        etWho.setText(null);
        etType.setText(null);
        Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_SHORT).show();
    }

    public void onFailure(String msg) {
        Snackbar.make(coordinatorLayout, msg, Snackbar.LENGTH_SHORT).show();
    }
}
