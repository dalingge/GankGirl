package com.dalingge.gankio.module.girl.imagepager;

import android.net.Uri;
import android.support.v4.content.FileProvider;

/**
 * FileName:ImageFileProvider.java
 * Description:
 * Author:dingboyang
 * Email:445850053@qq.com
 * Date:16/4/7
 */
public class ImageFileProvider extends FileProvider {

    @Override
    public String getType(Uri uri) {
        return "image/jpeg";
    }

}

