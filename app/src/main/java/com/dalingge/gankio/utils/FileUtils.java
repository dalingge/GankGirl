package com.dalingge.gankio.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.dalingge.gankio.GankApp;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * FileName:FileUtils.java
 * Description:文件工具
 * Author:dingboyang
 * Date:16/4/2
 */
public class FileUtils {

    public static boolean save(File dic, String fileName, String msg) {

        File file = new File(dic, fileName);

        try {
            OutputStream outputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
            outputStreamWriter.write(msg);
            outputStreamWriter.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static String getFileName() {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder("Log_");
        stringBuilder.append(Long.toString(System.currentTimeMillis()+random.nextInt(10000)).substring(4));
        stringBuilder.append(".txt");
        return stringBuilder.toString();
    }

    /**
     * 得到手机的缓存目录
     *
     * @param context
     * @return
     */
    public static File getCacheDir(Context context) {
        Log.i("getCacheDir", "cache sdcard state: " + Environment.getExternalStorageState());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File cacheDir = context.getExternalCacheDir();
            if (cacheDir != null && (cacheDir.exists() || cacheDir.mkdirs())) {
                Log.i("getCacheDir", "cache dir: " + cacheDir.getAbsolutePath());
                return cacheDir;
            }
        }

        File cacheDir = context.getCacheDir();
        Log.i("getCacheDir", "cache dir: " + cacheDir.getAbsolutePath());
        return cacheDir;
    }

    /**
     * @return  创建缓存目录
     */
    public static File getcacheDirectory()
    {
        File file = new File(GankApp.context().getExternalCacheDir(), "RxCache");
        if(!file.exists())
        {
            boolean b = file.mkdirs();
            Log.e("file", "文件不存在  创建文件    "+b);
        }else{
            Log.e("file", "文件存在");
        }
        return file;
    }

    /**
     * 关闭IO
     *
     * @param closeable closeable
     */
    public static void closeIO(Closeable closeable) {
        if (closeable == null) return;
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
