package com.dalingge.gankio.main.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.dalingge.gankio.R;
import com.dalingge.gankio.bean.GirlBean;
import com.dalingge.gankio.common.utils.DateUtils;
import com.dalingge.gankio.common.utils.GlideRoundTransform;
import com.dalingge.gankio.common.widgets.RatioImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * FileName:WelfareAdapter.java
 * Description:
 * Author:dingboyang
 * Email:445850053@qq.com
 * Date:16/4/2
 */
public class WelfareAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int LAST_POSITION = -1 ;
    private static final int TYPE_GIRL = 0;
    private static final int TYPE_GAN = 1;
    private static final int TYPE_VIDEO = 2;
    private static final int TYPE_FOOTER = -1;

    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private List<GirlBean> mData;
    private RequestManager glideRequest;
    private String mType;
    private OnItemClickListener mOnItemClickListener;
    private boolean mShowFooter = true;

    public WelfareAdapter(Context context, String type,List<GirlBean> data) {
        this.mContext = context;
        this.mType = type;
        mLayoutInflater = LayoutInflater.from(mContext);
        glideRequest = Glide.with(mContext);

        mData = new ArrayList<>();
        for (int i=0;i<data.size();i++) {
            addItem(i,data.get(i));
        }
    }

    public GirlBean getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    public List<GirlBean> getData() {
        return mData;
    }


    public void setDate(List<GirlBean> data) {
        mData=data;
    }

    public void add(GirlBean resultsBean,int position) {
        position = position == LAST_POSITION ? getItemCount()  : position;
        mData.add(position,resultsBean);
        notifyItemInserted(position);
    }

    void addItem(int position,GirlBean resultsBean) {
        mData.add(position,resultsBean);
        notifyItemInserted(position);
    }

    public void isShowFooter(boolean showFooter) {
        this.mShowFooter = showFooter;
    }

    public boolean isShowFooter() {
        return this.mShowFooter;
    }

    @Override
    public int getItemViewType(int position) {
        switch (mType) {
            case "福利":
                return TYPE_GIRL;
            case "iOS":
            case "Android":
            case "前端":
            case "App":
            case "拓展资源":
            case "瞎推荐":
                return TYPE_GAN;
            case "休息视频":
                return TYPE_VIDEO;
            default:
                return TYPE_FOOTER;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if (viewType == TYPE_GIRL) {
            return new CirlCardViewHolder(mLayoutInflater
                    .inflate(R.layout.item_main_girl, parent, false));
        } else if (viewType == TYPE_VIDEO) {
            return new VideoCardViewHolder(mLayoutInflater
                    .inflate(R.layout.item_main_video, parent, false));
        } else if (viewType == TYPE_GAN) {
            return new GanCardViewHolder(mLayoutInflater
                    .inflate(R.layout.item_main_gan, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        GirlBean resultsBean = mData.get(position);
        if (viewHolder instanceof CirlCardViewHolder) {
            bindGirlItem(position, (CirlCardViewHolder) viewHolder, resultsBean);
        } else if (viewHolder instanceof VideoCardViewHolder) {
            bindVideoItem(position, (VideoCardViewHolder) viewHolder, resultsBean);
        } else if (viewHolder instanceof GanCardViewHolder) {
            bindGanItem(position, (GanCardViewHolder) viewHolder, resultsBean);
        }

    }

    private void bindGirlItem(int position, final CirlCardViewHolder viewHolder, GirlBean resultsBean) {
        glideRequest.load(resultsBean.getUrl())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transform(new GlideRoundTransform(mContext, 8))
                .animate(R.anim.image_zoom_in)
                .into(new BitmapImageViewTarget(viewHolder.imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        viewHolder.imageView.setOriginalSize(resource.getWidth(), resource.getHeight());
                        viewHolder.imageView.setImageBitmap(resource);
                    }
                });
    }

    private void bindVideoItem(int position, VideoCardViewHolder viewHolder, GirlBean resultsBean) {
        viewHolder.tvTitle.setText(resultsBean.getDesc());
        viewHolder.tvAuthor.setText(resultsBean.getWho());
        viewHolder.tvDate.setText(DateUtils.formatDateDetailDay(DateUtils.parseStringToDate(resultsBean.getPublishedAt())));
    }

    private void bindGanItem(int position, GanCardViewHolder viewHolder, GirlBean resultsBean) {
        viewHolder.tvTitle.setText(resultsBean.getDesc());
        viewHolder.tvAuthor.setText(resultsBean.getWho());
        viewHolder.tvDate.setText(DateUtils.formatDateDetailDay(DateUtils.parseStringToDate(resultsBean.getPublishedAt())));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    class CirlCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.radio_iv)
        RatioImageView imageView;

        CirlCardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    class VideoCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_author)
        TextView tvAuthor;
        @BindView(R.id.tv_date)
        TextView tvDate;

         VideoCardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

     class GanCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_author)
        TextView tvAuthor;
        @BindView(R.id.tv_date)
        TextView tvDate;

         GanCardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }
}
