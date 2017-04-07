package com.dalingge.gankio.module.girl.imagepager;

import android.annotation.TargetApi;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.transition.Transition;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.dalingge.gankio.BuildConfig;
import com.dalingge.gankio.R;
import com.dalingge.gankio.common.base.BaseToolbarActivity;
import com.dalingge.gankio.common.bean.GankBean;
import com.dalingge.gankio.common.rxjava.permission.RxPermissions;
import com.dalingge.gankio.common.utils.AnimationUtils;
import com.dalingge.gankio.common.utils.PermissionUtils;
import com.dalingge.gankio.common.utils.RxUtils;
import com.dalingge.gankio.common.widgets.PullBackLayout;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.internal.disposables.ListCompositeDisposable;


/**
 * FileName:ImagePagerActivity.java
 * Description:图片查看器
 * Author:dingboyang
 * Email:445850053@qq.com
 * Date:16/4/4
 */
public class ImagePagerActivity extends BaseToolbarActivity  implements PullBackLayout.Callback ,PermissionUtils.RequestPermission {

    private static final String EXTRA_IMAGE_INDEX = "image_index";
    private static final String EXTRA_IMAGE_URLS = "image_urls";

    private static final int SYSTEM_UI_BASE_VISIBILITY = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;

    private static final int SYSTEM_UI_IMMERSIVE = View.SYSTEM_UI_FLAG_IMMERSIVE
            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            | View.SYSTEM_UI_FLAG_FULLSCREEN;

    private static final String AUTHORITY_IMAGES = BuildConfig.APPLICATION_ID + ".images";

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.pull_back_layout)
    PullBackLayout pullBackLayout;

    private int index;
    private ImagePagerAdapter imagePagerAdapter;
    private List<GankBean> resultsBeanList;

    public static Intent newIntent(Context context, int index, List<GankBean> resultsBeanList) {
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
        getToolbar().setNavigationOnClickListener(view -> supportFinishAfterTransition());

        supportPostponeEnterTransition();

        pullBackLayout.setCallback(this);

        index = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
        resultsBeanList = (List<GankBean>) getIntent().getSerializableExtra(EXTRA_IMAGE_URLS);

        imagePagerAdapter = new ImagePagerAdapter(getSupportFragmentManager(), viewPager, resultsBeanList, index);

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
                GankBean image = resultsBeanList.get(viewPager.getCurrentItem());
                sharedElements.clear();
                sharedElements.put(image._id, imagePagerAdapter.getCurrent().getSharedElement());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_image, menu);
        return true;
    }

    private ListCompositeDisposable mCompositeSubscription;

    public void addSubscription(Disposable s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new ListCompositeDisposable();
        }

        this.mCompositeSubscription.add(s);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_share) {
            Disposable subscription = RxUtils.saveImageAndGetPathObservable(this, getCurrentImage().url, getCurrentImage()._id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(new Function<File, Uri>() {
                        @Override
                        public Uri apply(File file) throws Exception {
                            return Uri.fromFile(file);
                        }
                    })
                    .retry()
                    .subscribe(new Consumer<Uri>() {
                        @Override
                        public void accept(Uri uri) throws Exception {
                            Intent shareIntent = new Intent();
                            shareIntent.setAction(Intent.ACTION_SEND);
                            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                            shareIntent.setType("image/jpeg");
                            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_title)));
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Toast.makeText(ImagePagerActivity.this, throwable.getMessage() + "\n再试试...", Toast.LENGTH_LONG).show();
                        }
                    });
            addSubscription(subscription);
            return true;
        } else if (id == R.id.action_save) {
            PermissionUtils.externalStorage(this,new RxPermissions(this));

            return true;
        } else if (id == R.id.action_set_wallpaper) {
            Disposable subscription = RxUtils.saveImageAndGetPathObservable(this, getCurrentImage().url, getCurrentImage()._id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .map(new Function<File, Uri>() {
                        @Override
                        public Uri apply(File file) throws Exception {
                            return FileProvider.getUriForFile(ImagePagerActivity.this, AUTHORITY_IMAGES, file);
                        }

                    })
                    .retry()
                    .subscribe(new Consumer<Uri>() {
                        @Override
                        public void accept(Uri uri) throws Exception {
                            final WallpaperManager wm = WallpaperManager.getInstance(ImagePagerActivity.this);
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                                startActivity(wm.getCropAndSetWallpaperIntent(uri));
                            } else {
                                try {
                                    wm.setStream(getContentResolver().openInputStream(uri));
                                    Toast.makeText(ImagePagerActivity.this, R.string.set_wallpaper_success, Toast.LENGTH_LONG).show();
                                } catch (IOException e) {
                                    Toast.makeText(ImagePagerActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Toast.makeText(ImagePagerActivity.this, throwable.getMessage() + "\n再试试...", Toast.LENGTH_LONG).show();
                        }
                    });

            addSubscription(subscription);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.dispose();
        }
    }

    private GankBean getCurrentImage() {
        return resultsBeanList.get(viewPager.getCurrentItem());
    }


    public void toggleFade() {
        if (getToolbar().getVisibility() == View.VISIBLE) {
            fadeOut();
        } else {
            fadeIn();
        }
    }

    void fadeIn() {
        AnimationUtils.animateIn(getToolbar(), R.anim.image_toolbar_fade_in);
        viewPager.setSystemUiVisibility(SYSTEM_UI_BASE_VISIBILITY);
    }

    void fadeOut() {
        AnimationUtils.animateOut(getToolbar(), R.anim.image_toolbar_fade_out);
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

    @Override
    public void onRequestPermissionSuccess() {
        Disposable subscription = RxUtils.saveImageAndGetPathObservable(this, getCurrentImage().url, getCurrentImage()._id)
                .observeOn(AndroidSchedulers.mainThread())
                .retry()
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File file) throws Exception {
                        String msg = String.format(getString(R.string.save_success),
                                file.getPath());
                        Toast.makeText(ImagePagerActivity.this, msg, Toast.LENGTH_LONG).show();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable e) {
                        Toast.makeText(ImagePagerActivity.this, e.getMessage() + "\n再试试...", Toast.LENGTH_LONG).show();
                    }
                });

        addSubscription(subscription);
    }
}
