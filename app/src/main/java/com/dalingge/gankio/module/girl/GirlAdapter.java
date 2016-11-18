package com.dalingge.gankio.module.girl;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dalingge.gankio.R;
import com.dalingge.gankio.common.bean.GankBean;
import com.dalingge.gankio.common.widgets.RatioImageView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



/**
 * Created by dingboyang on 2016/11/18.
 */

public class GirlAdapter extends RecyclerView.Adapter<GirlAdapter.ViewHolder> {

    private static final int LAST_POSITION = -1;

    private Context mContext;
    private ArrayList<GankBean> mData;
    private OnItemClickListener mOnItemClickListener;
    private RequestManager glideRequest;

    public GirlAdapter(Context context, ArrayList<GankBean> data) {
        this.mContext = context;
        glideRequest = Glide.with(mContext);
        this.mData = data;
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
                .inflate(R.layout.item_girl, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GankBean gankBean = mData.get(position);
        DrawableRequestBuilder<String> requestBuilder = Glide.with(mContext)
                .load(gankBean.url)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.color.accent)
                .crossFade();
        requestBuilder.into(holder.ivGirlImg);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_girl_img)
        RatioImageView ivGirlImg;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.card_view)
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
