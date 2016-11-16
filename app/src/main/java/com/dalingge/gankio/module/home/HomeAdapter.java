package com.dalingge.gankio.module.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.dalingge.gankio.R;
import com.dalingge.gankio.common.bean.GankBean;
import com.dalingge.gankio.common.utils.DateUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dingboyang on 2016/11/13.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private static final int LAST_POSITION = -1;

    private Context mContext;
    private RequestManager mRequestManager;
    private ArrayList<GankBean> mData;

    public HomeAdapter(Context context, ArrayList<GankBean> data) {
        this.mContext = context;
        mRequestManager = Glide.with(mContext);
        mData =data;
        for (int i = 0; i < mData.size(); i++) {
            addItem(i, mData.get(i));
        }
    }

    public GankBean getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    public ArrayList<GankBean> getData() {
        return mData;
    }


    public void setDate(ArrayList<GankBean> data) {
        mData = data;
    }

    public void add(GankBean resultsBean, int position) {
        position = position == LAST_POSITION ? getItemCount() : position;
        mData.add(position, resultsBean);
        notifyItemInserted(position);
    }

    void addItem(int position, GankBean resultsBean) {
        mData.add(position, resultsBean);
        notifyItemInserted(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_home, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GankBean gankBean = mData.get(position);
        holder.tvHomeGankTitle.setText(gankBean.desc);
        holder.tvHomeGankAuthor.setText(gankBean.who);
        holder.tvHomeGankDate.setText(DateUtils.formatDateDetailDay(DateUtils.parseStringToDate(gankBean.publishedAt)));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_home_gank_img)
        ImageView ivHomeGankImg;
        @BindView(R.id.tv_home_gank_title)
        TextView tvHomeGankTitle;
        @BindView(R.id.tv_home_gank_author)
        TextView tvHomeGankAuthor;
        @BindView(R.id.tv_home_gank_date)
        TextView tvHomeGankDate;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.card_view)
        public void onClick(View view) {

        }
    }
}
