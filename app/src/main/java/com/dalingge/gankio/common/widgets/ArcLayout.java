package com.dalingge.gankio.common.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Path;
import android.graphics.Region;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;

import com.dalingge.gankio.R;
import com.dalingge.gankio.common.utils.DensityUtils;


/**
 * Created by dingboyang on 2016/12/5.
 */

public class ArcLayout extends FrameLayout {

    public final static int CROP_INSIDE = 1;

    public final static int CROP_OUTSIDE = 2;

    private int height = 0;

    private int width = 0;

    private Path clipPath;

    private boolean cropInside = true;

    private float arcHeight;

    private float elevation;

    public ArcLayout(Context context) {
        super(context);
        init(context, null);
    }

    public ArcLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {

        TypedArray styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.ArcHeader, 0, 0);
        arcHeight = styledAttributes.getDimension(R.styleable.ArcHeader_arc_height, DensityUtils.dip2px(context, 10));

        final int cropDirection = styledAttributes.getInt(R.styleable.ArcHeader_arc_cropDirection, CROP_INSIDE);
        cropInside = (cropDirection & CROP_INSIDE) == CROP_INSIDE;

        styledAttributes.recycle();

        setElevation(ViewCompat.getElevation(this));

        /**
         *
         * 如果版本 < 18 关闭硬件加速
         *
         */
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    private Path createClipPath() {
        final Path path = new Path();

        float arcHeight = getArcHeight();

        if (isCropInside()) {
            path.moveTo(0, 0);
            path.lineTo(0, height - arcHeight);
            path.quadTo(width / 2, height + arcHeight,
                    width, height - arcHeight);
            path.lineTo(width, 0);
            path.close();
        } else {
            path.moveTo(0, 0);
            path.lineTo(0, height);
            path.quadTo(width / 2, height - 2 * arcHeight,
                    width, height);
            path.lineTo(width, 0);
            path.close();
        }
        return path;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            calculateLayout();
        }
    }

    private void calculateLayout() {

        height = getMeasuredHeight();
        width = getMeasuredWidth();
        if (width > 0 && height > 0) {

            clipPath = createClipPath();
            ViewCompat.setElevation(this, getElevation());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && isCropInside()) {
                ViewCompat.setElevation(this, getElevation());
                setOutlineProvider(new ViewOutlineProvider() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void getOutline(View view, Outline outline) {
                        outline.setConvexPath(clipPath);
                    }
                });
            }
        }
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {

        canvas.save();

        canvas.clipPath(clipPath, Region.Op.REPLACE);
        super.dispatchDraw(canvas);

        canvas.restore();

    }

    public float getElevation() {
        return elevation;
    }

    public void setElevation(float elevation) {
        this.elevation = elevation;
    }

    public boolean isCropInside() {
        return cropInside;
    }

    public float getArcHeight() {
        return arcHeight;
    }
}
