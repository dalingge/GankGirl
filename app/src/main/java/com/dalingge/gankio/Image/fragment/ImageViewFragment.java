package com.dalingge.gankio.Image.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.dalingge.gankio.Image.activity.ImagePagerActivity;
import com.dalingge.gankio.R;
import com.dalingge.gankio.base.BaseFragment;
import com.dalingge.gankio.widget.TouchImageView;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * FileName:ImageViewFragment.java
 * Description:
 * Author:dingboyang
 * Email:445850053@qq.com
 * Date:16/4/4
 */
public class ImageViewFragment extends BaseFragment implements RequestListener<String, GlideDrawable> {

    @Bind(R.id.touch_image_view)
    TouchImageView touchImageView;

    private ImagePagerActivity activity;

    private String url;
    private boolean initialShown;

    @Override
    protected int getLayout() {
        return R.layout.fragment_image_view;
    }

    public static ImageViewFragment newFragment(String url, boolean initialShown) {
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        bundle.putBoolean("initial_shown", initialShown);

        ImageViewFragment fragment = new ImageViewFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = (ImagePagerActivity) getActivity();

        url = getArguments().getString("url");
        initialShown = getArguments().getBoolean("initial_shown", false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ViewCompat.setTransitionName(touchImageView, url);
    }

    @Override
    public void onResume() {
        super.onResume();
        Glide.with(this)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade(0)
                .listener(this)
                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
    }

    @Override
    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
        maybeStartPostponedEnterTransition();
        return true;
    }

    @Override
    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
        touchImageView.setImageDrawable(resource);
        maybeStartPostponedEnterTransition();
        return true;
    }

    private void maybeStartPostponedEnterTransition() {
        if (initialShown) {
            activity.supportStartPostponedEnterTransition();
        }
    }

    @OnClick(R.id.touch_image_view)
    void toggleToolbar() {
        activity.toggleFade();
    }

    public TouchImageView getSharedElement() {
        return touchImageView;
    }

    public boolean isZoomed() {
        return touchImageView.isZoomed();
    }
}
