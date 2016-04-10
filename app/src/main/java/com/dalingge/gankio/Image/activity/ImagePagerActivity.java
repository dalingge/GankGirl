package com.dalingge.gankio.Image.activity;

import android.annotation.TargetApi;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewPager;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.dalingge.gankio.Image.adapter.ImagePagerAdapter;
import com.dalingge.gankio.R;
import com.dalingge.gankio.base.BaseActivity;
import com.dalingge.gankio.bean.GirlBean;
import com.dalingge.gankio.util.ImageUtils;
import com.dalingge.gankio.util.InOutAnimationUtils;
import com.dalingge.gankio.util.log.L;
import com.dalingge.gankio.widget.PullBackLayout;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * FileName:ImagePagerActivity.java
 * Description:图片查看器
 * Author:dingboyang
 * Email:445850053@qq.com
 * Date:16/4/4
 */
public class ImagePagerActivity extends BaseActivity implements PullBackLayout.Callback{

    private static final String EXTRA_IMAGE_INDEX = "image_index";
    private static final String EXTRA_IMAGE_URLS = "image_urls";

    private static final int SYSTEM_UI_BASE_VISIBILITY = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

    private static final int SYSTEM_UI_IMMERSIVE = View.SYSTEM_UI_FLAG_IMMERSIVE
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN;


    @Bind(R.id.view_pager)
    ViewPager viewPager;
    @Bind(R.id.pull_back_layout)
    PullBackLayout pullBackLayout;

    private int index;
    private ImagePagerAdapter imagePagerAdapter;
    private List<GirlBean.ResultsBean> resultsBeanList;

    public static Intent newIntent(Context context, int index, List<GirlBean.ResultsBean> resultsBeanList) {
        Intent intent = new Intent(context, ImagePagerActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ImagePagerActivity.EXTRA_IMAGE_URLS, (Serializable) resultsBeanList);
        intent.putExtras(bundle);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, index);
        return intent;
    }

    @Override
    protected boolean isBack() {
        return true;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_image_pager;
    }

    @Override
    protected void initView() {
        setTitle(null);
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                supportFinishAfterTransition();
            }
        });

        supportPostponeEnterTransition();

        pullBackLayout.setCallback(this);

        index = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
        resultsBeanList = (List<GirlBean.ResultsBean>) getIntent().getSerializableExtra(EXTRA_IMAGE_URLS);

        imagePagerAdapter = new ImagePagerAdapter(getSupportFragmentManager(),viewPager,resultsBeanList,index);

        viewPager.setAdapter(imagePagerAdapter);
        viewPager.setCurrentItem(index);

        // 避免图片在进行 Shared Element Transition 时盖过 Toolbar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getEnterTransition().addListener(new Transition.TransitionListener() {
                @Override
                public void onTransitionStart(Transition transition) {

                }

                @Override
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                public void onTransitionEnd(Transition transition) {
                    getWindow().getEnterTransition().removeListener(this);
                    fadeIn();
                }

                @Override
                public void onTransitionCancel(Transition transition) {

                }

                @Override
                public void onTransitionPause(Transition transition) {

                }

                @Override
                public void onTransitionResume(Transition transition) {

                }
            });
        } else {
            fadeIn();
        }

        setEnterSharedElementCallback(new SharedElementCallback() {
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                GirlBean.ResultsBean image = resultsBeanList.get(viewPager.getCurrentItem());
                sharedElements.clear();
                sharedElements.put(image.get_id(), imagePagerAdapter.getCurrent().getSharedElement());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        ImageView imageView=imagePagerAdapter.getCurrent().getSharedElement();
        imageView.setDrawingCacheEnabled(true);
        Bitmap bitmap=imageView.getDrawingCache();
        if (id == R.id.action_share) {

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, ImageUtils.storeImage(ImagePagerActivity.this,bitmap));
            shareIntent.setType("image/jpeg");
            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_title)));

            imageView.setDrawingCacheEnabled(false);
            return true;
        }else if(id == R.id.action_save){

            ImageUtils.storeImage(ImagePagerActivity.this,bitmap);
            imageView.setDrawingCacheEnabled(false);

            File file = new File(Environment.getExternalStorageDirectory(), getString(R.string.app_name)+"/image");
            String msg = String.format(getString(R.string.save_success),
                    file.getAbsolutePath());
            Toast.makeText(this,msg,Toast.LENGTH_LONG).show();
            return true;
        }else if(id == R.id.action_set_wallpaper){

            final WallpaperManager wm = WallpaperManager.getInstance(this);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                startActivity(wm.getCropAndSetWallpaperIntent(ImageUtils.storeImage(ImagePagerActivity.this,bitmap)));
            } else {
                try {
                    wm.setStream(getContentResolver().openInputStream(ImageUtils.storeImage(ImagePagerActivity.this,bitmap)));
                    Toast.makeText(this, R.string.set_wallpaper_success,Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    L.e( "Failed to set wallpaper", e);
                    Toast.makeText(this, e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
            imageView.setDrawingCacheEnabled(false);

            Toast.makeText(this,R.string.set_wallpaper_success,Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void toggleFade() {
        if (getToolbar().getVisibility() == View.VISIBLE) {
            fadeOut();
        } else {
            fadeIn();
        }
    }

    void fadeIn() {
        InOutAnimationUtils.animateIn(getToolbar(), R.anim.image_toolbar_fade_in);
        viewPager.setSystemUiVisibility(SYSTEM_UI_BASE_VISIBILITY);
    }

    void fadeOut() {
        InOutAnimationUtils.animateOut(getToolbar(), R.anim.image_toolbar_fade_out);
        viewPager.setSystemUiVisibility(SYSTEM_UI_BASE_VISIBILITY | SYSTEM_UI_IMMERSIVE);
    }

    @Override
    public void onPullStart() {
        fadeOut();
    }

    @Override
    public void onPull(float progress) {
        progress = Math.min(1f, progress * 3f);
        getWindow().getDecorView().getBackground().setAlpha((int) (0xff * (1f - progress)));
    }

    @Override
    public void onPullCancel() {
        fadeIn();
    }

    @Override
    public void onPullComplete() {
        supportFinishAfterTransition();
    }

    @Override
    public void supportFinishAfterTransition() {
        Intent data = new Intent();
        data.putExtra(EXTRA_IMAGE_INDEX, viewPager.getCurrentItem());
        setResult(RESULT_OK, data);

        viewPager.setSystemUiVisibility(SYSTEM_UI_BASE_VISIBILITY | SYSTEM_UI_IMMERSIVE);

        super.supportFinishAfterTransition();
    }
}
