package com.dalingge.gankio.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import com.dalingge.gankio.R;
import com.dalingge.gankio.util.log.L;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * FileName: ImageUtils.java
 * description:
 * Author: dingby(445850053@qq.com)
 * Date: 2016/4/7
 */
public class ImageUtils {


    /**
     * 保存图片
     *
     * @param context
     * @param image  图片
     */
    public static void storeImage(Context context,Bitmap image) {
        String name=context.getString(R.string.app_name);
        File file = new File(Environment.getExternalStorageDirectory(),name);
        if (!file.exists()) {
            file.mkdir();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date());
        String fileName= name+"-"+ timeStamp + ".jpg";
        File pictureFile = new File(file, fileName);

        if (pictureFile == null) {
            L.d("Error creating media file, check storage permissions: ");
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            L.d("File not found: " + e.getMessage());
        } catch (IOException e) {
            L.d("Error accessing file: " + e.getMessage());
        }

        Uri uri = Uri.fromFile(pictureFile);
        // 通知图库更新
        Intent scannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        context.sendBroadcast(scannerIntent);
    }
}
