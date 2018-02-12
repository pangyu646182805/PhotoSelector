package com.ppyy.demo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.ppyy.photoselector.bean.FileBean;

import java.util.ArrayList;

import me.xiaopan.sketch.Sketch;
import me.xiaopan.sketch.SketchImageView;
import me.xiaopan.sketch.display.TransitionImageDisplayer;

/**
 * Created by NeuroAndroid on 2018/1/2.
 */

public class GridImgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_ADD_IMAGE = 0;
    private static final int VIEW_TYPE_IMAGE_CONTENT = 1;

    private final Context mContext;
    private ArrayList<FileBean> mDataList;

    private int mMaxSelectable;

    public void setMaxSelectable(int maxSelectable) {
        mMaxSelectable = maxSelectable;
    }

    public void setDataList(ArrayList<FileBean> dataList) {
        mDataList = dataList;
        notifyDataSetChanged();
    }

    public GridImgAdapter(Context context, ArrayList<FileBean> dataList) {
        mContext = context;
        mDataList = dataList == null ? new ArrayList<>() : dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        switch (viewType) {
            case VIEW_TYPE_ADD_IMAGE:
                viewHolder = new AddImageViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.item_add_image, parent, false));
                break;
            case VIEW_TYPE_IMAGE_CONTENT:
            default:
                viewHolder = new ImageContentViewHolder(LayoutInflater.from(mContext)
                        .inflate(R.layout.item_image_content, parent, false));
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof AddImageViewHolder) {
            AddImageViewHolder addImageViewHolder = (AddImageViewHolder) holder;
            addImageViewHolder.itemView.setOnClickListener(view -> {
                if (mOnAddImageListener != null) {
                    mOnAddImageListener.onAddImage();
                }
            });
        } else {
            ImageContentViewHolder imageContentViewHolder = (ImageContentViewHolder) holder;
            imageContentViewHolder.llDel.setOnClickListener(view -> {
                int layoutPosition = imageContentViewHolder.getLayoutPosition();
                if (layoutPosition != RecyclerView.NO_POSITION) {
                    mDataList.remove(layoutPosition);
                    notifyItemRemoved(layoutPosition);
                    notifyItemRangeChanged(layoutPosition, mDataList.size());
                }
            });
            FileBean fileBean = mDataList.get(position);
            String path;
            if (!TextUtils.isEmpty(fileBean.getCompressPath())) {
                path = fileBean.getCompressPath();
            } else {
                path = fileBean.getPath();
            }
            Sketch.with(imageContentViewHolder.ivImg.getContext())
                    .display(path, imageContentViewHolder.ivImg)
                    .loadingImage(R.drawable.img_loading)
                    .errorImage(R.drawable.img_loading)
                    .displayer(new TransitionImageDisplayer())
                    .shapeSize(300, 300)
                    .cacheProcessedImageInDisk()
                    .commit();
        }
    }

    @Override
    public int getItemCount() {
        int size = mDataList.size();
        if (size < mMaxSelectable) {
            return size + 1;
        } else {
            return size;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mDataList.size()) {
            return VIEW_TYPE_ADD_IMAGE;
        } else {
            return VIEW_TYPE_IMAGE_CONTENT;
        }
    }

    public class AddImageViewHolder extends RecyclerView.ViewHolder {
        public AddImageViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ImageContentViewHolder extends RecyclerView.ViewHolder {
        SketchImageView ivImg;
        LinearLayout llDel;

        public ImageContentViewHolder(View itemView) {
            super(itemView);
            ivImg = itemView.findViewById(R.id.iv_img);
            llDel = itemView.findViewById(R.id.ll_del);
        }
    }

    private OnAddImageListener mOnAddImageListener;

    public void setOnAddImageListener(OnAddImageListener onAddImageListener) {
        mOnAddImageListener = onAddImageListener;
    }

    public interface OnAddImageListener {
        void onAddImage();
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, FileBean fileBean);
    }
}
