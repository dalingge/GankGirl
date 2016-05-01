package com.dalingge.gankio.main.presenter;

import com.dalingge.gankio.base.BasePresenter;
import com.dalingge.gankio.bean.GirlBean;
import com.dalingge.gankio.main.model.WelfareModel;
import com.dalingge.gankio.main.view.IWelfareView;

import java.util.List;

/**
 * FileName:WelfarePresenter.java
 * Description:
 * Author:dingboyang
 * Email:445850053@qq.com
 * Date:16/4/2
 */
public class WelfarePresenter extends BasePresenter<IWelfareView> implements WelfareModel.OnLoadDataListListener{

    private WelfareModel mWelfareModel;

    public WelfarePresenter(IWelfareView view) {
        super(view);
        mWelfareModel=new WelfareModel();
    }

    public void loadNews(String type, int pageIndex) {

        //只有第一页的或者刷新的时候才显示刷新进度条
        if(pageIndex == 1) {
            mView.showRefresh();
        }
        mWelfareModel.loadNews(type,pageIndex, this);
    }

    @Override
    public void onSuccess(List<GirlBean> list) {
        mView.hideRefresh();
        mView.addData(list);
    }

    @Override
    public void onFailure(String msg, Throwable e) {

        mView.hideRefresh();
        mView.showLoadFailMsg();
    }
}
