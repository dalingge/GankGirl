package com.dalingge.gankio.module.video.play;

import android.content.Context;
import android.content.Intent;

import com.dalingge.gankio.Constants;
import com.dalingge.gankio.R;
import com.dalingge.gankio.common.base.BaseToolbarActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.hustunique.parsingplayer.player.view.ParsingVideoView;

import butterknife.BindView;


public class VideoPlayActivity extends BaseToolbarActivity {

    @BindView(R.id.videoView)
    ParsingVideoView videoView;
    @BindView(R.id.ad_view)
    AdView adView;

    private String mUrl, mTitle;

    public static void start(Context context, String extraURL, String extraTitle) {
        Intent intent = new Intent(context, VideoPlayActivity.class);
        intent.putExtra(Constants.EXTRA_URL, extraURL);
        intent.putExtra(Constants.EXTRA_TITLE, extraTitle);
        context.startActivity(intent);
    }

    @Override
    public boolean isBack() {
        return true;
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_video_play;
    }

    @Override
    protected void initView() {
        mUrl = getIntent().getStringExtra(Constants.EXTRA_URL);
        mTitle = getIntent().getStringExtra(Constants.EXTRA_TITLE);
        getToolbar().setTitle(mTitle);

        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoView.onResume();
        videoView.play(mUrl);
    }


    @Override
    protected void onPause() {
        super.onPause();
        videoView.onPause();
    }


    @Override
    protected void onDestroy() {
        videoView.onDestroy();
        super.onDestroy();
    }
}
