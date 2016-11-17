package com.dalingge.gankio.common.base.view;

import android.os.Bundle;

/**
 * FileName:ViewPageInfo.java
 * Description:
 * Author:dingboyang
 * Email:445850053@qq.com
 * Date:16/4/2
 */
public class ViewPageInfo {
    public final String tag;
    public final Class<?> clss;
    public final Bundle args;
    public final String title;

    public ViewPageInfo(String _title, String _tag, Class<?> _class, Bundle _args) {
        title = _title;
        tag = _tag;
        clss = _class;
        args = _args;
    }
}
