package com.dalingge.gankio.network;

/**
 * 请求命令类 区分请求信息
 * Created by dingboyang on 2016/12/23.
 */
public class RequestCommand {

    /**
     * 获取首页干货
     */
    public static final int REQUEST_HOME_GANK = 0;
    /**
     * 响应提交干货
     */
    public static final int RESPONSE_SUBMIT_GANK = 1;
    /**
     * 获取妹子福利
     */
    public static final int REQUEST_GIRL_IMAGE = 2;
    /**
     * 获取休闲视频
     */
    public static final int REQUEST_VIDEO_LIST = 3;
    /**
     * 获取闲读分类
     */
    public static final int REQUEST_READ_TYPE = 4;
    /**
     * 获取闲读列表
     */
    public static final int REQUEST_READ_LIST = 5;
    /**
     * 获取闲读子类列表
     */
    public static final int REQUEST_READ_CHILD_LIST = 6;

}
