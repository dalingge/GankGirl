package com.dalingge.gankio.main.presenter;

import android.content.Context;
import android.widget.Toast;

import com.dalingge.gankio.base.BasePresenter;
import com.dalingge.gankio.main.model.MainModel;
import com.dalingge.gankio.main.view.IMainView;
import com.dalingge.gankio.util.NetWorkUtils;

/**
 * FileName:MainPresenter.java
 * Description:
 * Author:dingboyang
 * Email:445850053@qq.com
 * Date:16/4/2
 */
public class MainPresenter extends BasePresenter<IMainView>{

    private Context context;
    private MainModel mainModel;
    public MainPresenter(IMainView view) {
        super(view);
        context = view.getContext();
        mainModel = new MainModel();
    }

    public void getSplashImage() {
        if (NetWorkUtils.isConnectedByState(context)) {
            mainModel.getSplashImage(context,1);
        }else {
            Toast.makeText(context, "没有网络连接!", Toast.LENGTH_LONG).show();
        }
    }
}
