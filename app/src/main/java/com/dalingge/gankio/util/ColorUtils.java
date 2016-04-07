package com.dalingge.gankio.util;

import java.util.Random;

/**
 * FileName: ColorUtils.java
 * description:颜色工具
 * Author: dingby(445850053@qq.com)
 * Date: 2016/4/6
 */
public class ColorUtils {

    /**
     * 生成随机颜色代码
     *
     * @return
     */
    public static synchronized String getRandomColorCode() {
        // 颜色代码位数
        int colorLength = 6;

        // 颜色代码数组
        char[] codeSequence = { 'A', 'B', 'C', 'D', 'E', 'F', '0', '1', '2',
                '3', '4', '5', '6', '7', '8', '9' };

        StringBuffer sb = new StringBuffer("#");
        //StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for (int i = 0; i < colorLength; i++) {
            sb.append(codeSequence[random.nextInt(16)]);
        }
        return sb.toString();
    }


    /**
     * 获取十六进制的颜色代码.例如 "#6E36B4" , For HTML ,
     *
     * @return String
     */
    public static String getRandColorCode() {
        String r, g, b;
        Random random = new Random();
        r = Integer.toHexString(random.nextInt(256)).toUpperCase();
        g = Integer.toHexString(random.nextInt(256)).toUpperCase();
        b = Integer.toHexString(random.nextInt(256)).toUpperCase();

        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;

        return "#"+r + g + b;
    }

    public static void main(String[] args) {
        System.out.println(getRandomColorCode());

        System.out.println(getRandColorCode());
    }
}
