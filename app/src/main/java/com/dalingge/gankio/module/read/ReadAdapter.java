package com.dalingge.gankio.module.read;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dalingge.gankio.R;
import com.dalingge.gankio.common.glide.GlideImageLoader;
import com.dalingge.gankio.data.model.ReadListBean;
import com.dalingge.gankio.module.web.WebActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dingboyang on 2017/4/27.
 */

public class ReadAdapter extends RecyclerView.Adapter<ReadAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<ReadListBean> mData;


    public ReadAdapter(Context context, ArrayList<ReadListBean> data) {
        this.mContext = context;
        this.mData = data;
        for (int i = 0; i < mData.size(); i++) {
            addItem(i, mData.get(i));
        }
    }


    void addItem(int position, ReadListBean resultsBean) {
        mData.add(position, resultsBean);
        notifyItemInserted(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_read_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ReadListBean data = mData.get(position);
        GlideImageLoader.loadAdapterCircle(mContext, data.getLogo(), holder.ivReadLogo);
        holder.tvReadTitle.setText(data.getTitle());
        holder.tvReadSource.setText(data.getSource());
        holder.tvReadTime.setText(data.getTime());
        holder.cardView.setOnClickListener(v -> WebActivity.start(v.getContext(), data.getLink(), data.getTitle()));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_read_title)
        TextView tvReadTitle;
        @BindView(R.id.iv_read_logo)
        ImageView ivReadLogo;
        @BindView(R.id.tv_read_source)
        TextView tvReadSource;
        @BindView(R.id.tv_read_time)
        TextView tvReadTime;
        @BindView(R.id.card_view)
        CardView cardView;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
