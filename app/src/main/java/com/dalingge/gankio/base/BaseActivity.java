package com.dalingge.gankio.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.dalingge.gankio.R;

import butterknife.BindView;
import butterknife.ButterKnife;



/**
 * FileName:BaseActivity.java
 * Description:
 * Author:dingboyang
 * Email:445850053@qq.com
 * Date:16/4/2
 */
public abstract class BaseActivity <P extends BasePresenter> extends AppCompatActivity {

    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    /**
     * set layout of this activity
     * @return the id of layout
     */
    protected abstract int getLayout();
    protected abstract void initView();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ButterKnife.bind(this);
        initToolBar();
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
       // MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
      ///  MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消请求
        //ButterKnife.unbind(getName());
    }

    private void initToolBar() {

        setTitle("");
        if(toolbar!=null){
            setSupportActionBar(toolbar);

            if (isBack()) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
    }

    protected boolean isBack() {
        return false;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        return super.onMenuOpened(featureId, menu);
    }

    protected String getName(){
        return BaseFragment.class.getName();
    }
}
