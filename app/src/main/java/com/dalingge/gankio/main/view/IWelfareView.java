package com.dalingge.gankio.main.view;

import com.dalingge.gankio.base.BaseBean;
import com.dalingge.gankio.base.IBaseSwipeRefreshView;

import java.util.List;

/**
 * FileName:IWelfareView.java
 * Description:
 * Author:dingboyang
 * Email:445850053@qq.com
 * Date:16/4/2
 */
public interface IWelfareView<T extends BaseBean> extends IBaseSwipeRefreshView {


    void addData(List<T> data);

    void showLoadFailMsg();
}
