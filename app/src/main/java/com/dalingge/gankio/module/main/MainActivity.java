package com.dalingge.gankio.module.main;

import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatDelegate;
import android.view.Menu;
import android.view.MenuItem;

import com.dalingge.gankio.R;
import com.dalingge.gankio.common.base.BaseActivity;
import com.dalingge.gankio.common.factory.RequiresPresenter;
import com.dalingge.gankio.module.AboutActivity;
import com.dalingge.gankio.module.girl.GirlFragment;
import com.dalingge.gankio.module.home.gank.GankTadPageFragment;
import com.dalingge.gankio.module.read.ReadTadPageFragment;
import com.dalingge.gankio.module.video.VideoFragment;
import com.dalingge.gankio.network.RequestCommand;
import com.dalingge.gankio.network.RequestContext;
import com.dalingge.gankio.utils.PreferencesUtils;

import butterknife.BindView;


/**
 * FileName:MainActivity.java
 * Description:主页
 * Author:dingboyang
 * Email:445850053@qq.com
 * Date:16/4/2
 */
@RequiresPresenter(MainPresenter.class)
public class MainActivity extends BaseActivity<MainPresenter> implements BottomNavigationView.OnNavigationItemSelectedListener{

    @BindView(R.id.buttonNavigationView)
    BottomNavigationView buttonNavigationView;

    private GankTadPageFragment mGankTadPageFragment;
    private ReadTadPageFragment mReadTadPageFragment;
    private GirlFragment mGirlFragment;
    private VideoFragment mVideoFragment;

    // 定义FragmentManager对象管理器
    private FragmentManager fragmentManager;

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        getPresenter().request(new RequestContext(RequestCommand.REQUEST_RANDOM_GIRL));

        buttonNavigationView.setOnNavigationItemSelectedListener(this);
        fragmentManager = getSupportFragmentManager();
        setDefaultFragment(0);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_navigation_home:
                setDefaultFragment(0);
                break;
            case R.id.item_navigation_read:
                setDefaultFragment(1);
                break;
            case R.id.item_navigation_girl:
                setDefaultFragment(2);
                break;
            case R.id.item_navigation_video:
                setDefaultFragment(3);
                break;
        }
        return false;
    }

    /**
     * 设置默认的Fragment
     *
     * @param index 选项卡的标号：0, 1, 2, 3
     */
    private void setDefaultFragment(int index) {
        //开启事务
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragments(fragmentTransaction);
        switch (index) {
            case 0:
                if (mGankTadPageFragment == null) {
                    mGankTadPageFragment = GankTadPageFragment.newInstance(getString(R.string.button_navigation_home_text));
                    fragmentTransaction.add(R.id.contentLayout, mGankTadPageFragment);
                } else {
                    // 如果不为空，则直接将它显示出来
                    fragmentTransaction.show(mGankTadPageFragment);
                }
                break;
            case 1:
                if (mReadTadPageFragment == null) {
                    mReadTadPageFragment = ReadTadPageFragment.newInstance(getString(R.string.button_navigation_read_text));
                    fragmentTransaction.add(R.id.contentLayout, mReadTadPageFragment);
                } else {
                    // 如果不为空，则直接将它显示出来
                    fragmentTransaction.show(mReadTadPageFragment);
                }
                break;
            case 2:
                if (mGirlFragment == null) {
                    mGirlFragment = GirlFragment.newInstance(getString(R.string.button_navigation_girl_text));
                    fragmentTransaction.add(R.id.contentLayout, mGirlFragment);
                } else {
                    // 如果不为空，则直接将它显示出来
                    fragmentTransaction.show(mGirlFragment);
                }
                break;
            case 3:
                if (mVideoFragment == null) {
                    mVideoFragment = VideoFragment.newInstance(getString(R.string.button_navigation_video_text));
                    fragmentTransaction.add(R.id.contentLayout, mVideoFragment);
                } else {
                    // 如果不为空，则直接将它显示出来
                    fragmentTransaction.show(mVideoFragment);
                }
                break;
        }
        fragmentTransaction.commit();   // 事务提交
    }

    /**
     * 隐藏Fragment
     *
     * @param fragmentTransaction 事务
     */
    private void hideFragments(FragmentTransaction fragmentTransaction) {
        if (mGankTadPageFragment != null) {
            fragmentTransaction.hide(mGankTadPageFragment);
        }

        if (mReadTadPageFragment != null) {
            fragmentTransaction.hide(mReadTadPageFragment);
        }

        if (mGirlFragment != null) {
            fragmentTransaction.hide(mGirlFragment);
        }

        if (mVideoFragment != null) {
            fragmentTransaction.hide(mVideoFragment);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_day_night);
        initNotifiableItemState(item);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        int uiMode = getResources().getConfiguration().uiMode;
        int dayNightUiMode = uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (dayNightUiMode == Configuration.UI_MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (dayNightUiMode == Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
        }
    }

    private void initNotifiableItemState(MenuItem item) {
        PreferencesUtils preferencesUtils = new PreferencesUtils(this);
        item.setChecked(preferencesUtils.getBoolean(R.string.action_day_night, false));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_day_night:
                PreferencesUtils preferencesUtils = new PreferencesUtils(this);
                if (item.isChecked()) {
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    preferencesUtils.saveBoolean(R.string.action_day_night, false);
                } else {
                    getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    preferencesUtils.saveBoolean(R.string.action_day_night, true);
                }
                getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
                recreate();
                return true;
            case R.id.action_about:
                startActivity(AboutActivity.newIntent(this));
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

}
