package com.dalingge.gankio.common.widgets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

/**
 * FileName:PullBackLayout.java
 * Description:
 * Author:dingboyang
 * Email:445850053@qq.com
 * Date:16/4/4
 */
public class PullBackLayout extends FrameLayout {

    private final ViewDragHelper dragger;
    private final int minimumFlingVelocity;
    @Nullable
    private Callback callback;

    public PullBackLayout(Context context) {
        this(context, (AttributeSet)null);
    }

    public PullBackLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullBackLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.dragger = ViewDragHelper.create(this, 0.125F, new ViewDragCallback());
        this.minimumFlingVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
    }

    public void setCallback(@Nullable Callback callback) {
        this.callback = callback;
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return this.dragger.shouldInterceptTouchEvent(ev);
    }

    public boolean onTouchEvent(@NonNull MotionEvent event) {
        this.dragger.processTouchEvent(event);
        return true;
    }

    public void computeScroll() {
        if(this.dragger.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }

    }

    private class ViewDragCallback extends ViewDragHelper.Callback {
        private ViewDragCallback() {
        }

        public boolean tryCaptureView(View child, int pointerId) {
            return true;
        }

        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return 0;
        }

        public int clampViewPositionVertical(View child, int top, int dy) {
            return Math.max(0, top);
        }

        public int getViewHorizontalDragRange(View child) {
            return 0;
        }

        public int getViewVerticalDragRange(View child) {
            return PullBackLayout.this.getHeight();
        }

        public void onViewCaptured(View capturedChild, int activePointerId) {
            if(PullBackLayout.this.callback != null) {
                PullBackLayout.this.callback.onPullStart();
            }

        }

        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            if(PullBackLayout.this.callback != null) {
                PullBackLayout.this.callback.onPull((float)top / (float)PullBackLayout.this.getHeight());
            }

        }

        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            int slop = yvel > (float)PullBackLayout.this.minimumFlingVelocity?PullBackLayout.this.getHeight() / 6:PullBackLayout.this.getHeight() / 3;
            if(releasedChild.getTop() > slop) {
                if(PullBackLayout.this.callback != null) {
                    PullBackLayout.this.callback.onPullComplete();
                }
            } else {
                if(PullBackLayout.this.callback != null) {
                    PullBackLayout.this.callback.onPullCancel();
                }

                PullBackLayout.this.dragger.settleCapturedViewAt(0, 0);
                PullBackLayout.this.invalidate();
            }

        }
    }

    public interface Callback {
        void onPullStart();

        void onPull(float var1);

        void onPullCancel();

        void onPullComplete();
    }

}

