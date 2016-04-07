package com.dalingge.gankio.main.presenter;

import com.dalingge.gankio.base.BasePresenter;
import com.dalingge.gankio.bean.Constants;
import com.dalingge.gankio.bean.GirlBean;
import com.dalingge.gankio.main.model.GankCategory;
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

    public void loadNews(String tag,String type, int pageIndex) {

        String url = getUrl(type, pageIndex);
        //只有第一页的或者刷新的时候才显示刷新进度条
        if(pageIndex == 1) {
            mView.showRefresh();
        }
        mWelfareModel.loadNews(tag,url, this);
    }
    private String getUrl(String type, int pageIndex) {
        StringBuffer sb = new StringBuffer();
        sb.append(Constants.API_DATE)
                .append(GankCategory.valueOf(type).toString())
                .append(Constants.END_URL)
                .append(pageIndex);
        return sb.toString();
    }

    @Override
    public void onSuccess(List<GirlBean.ResultsBean> list) {
        mView.hideRefresh();
        mView.addData(list);
    }

    @Override
    public void onFailure(String msg, Exception e) {
        mView.hideRefresh();
        mView.showLoadFailMsg();
    }
}
