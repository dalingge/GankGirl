package com.dalingge.gankio.common.utils;

import android.content.Context;

/**
 * Created by dingboyang on 2016/11/10.
 */

public class DensityUtils {
    public static float dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return dpValue * scale;
    }

}
