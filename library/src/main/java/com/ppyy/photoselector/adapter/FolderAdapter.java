package com.ppyy.photoselector.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ppyy.photoselector.R;
import com.ppyy.photoselector.bean.FileBean;
import com.ppyy.photoselector.bean.FolderBean;
import com.ppyy.photoselector.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import me.xiaopan.sketch.SketchImageView;

/**
 * Created by NeuroAndroid on 2017/11/3.
 */

public class FolderAdapter extends BaseAdapter {
    private List<FolderBean> mFolderList = new ArrayList<>();
    private final Context mContext;

    private int mSelectedSize;

    public void setSelectedSize(int selectedSize) {
        mSelectedSize = selectedSize;
    }

    public FolderAdapter(@NonNull Context context, List<FolderBean> folderList) {
        mContext = context;
        if (folderList != null) {
            mFolderList = folderList;
        }
    }

    public void setFolderList(ArrayList<FolderBean> folderList) {
        this.mFolderList = folderList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mFolderList.size();
    }

    @Override
    public FolderBean getItem(int position) {
        return mFolderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        ViewHolder holder;
        if (contentView == null) {
            holder = new ViewHolder();
            contentView = LayoutInflater.from(mContext).inflate(R.layout.item_folder, parent, false);
            holder.ivCover = contentView.findViewById(R.id.iv_cover);
            holder.flSelectedHint = contentView.findViewById(R.id.fl_selected_hint);
            holder.tvSelectedSize = contentView.findViewById(R.id.tv_selected_size);
            holder.tvFolderName = contentView.findViewById(R.id.tv_folder_name);
            holder.tvFolderCount = contentView.findViewById(R.id.tv_folder_count);
            contentView.setTag(holder);
        } else {
            holder = (ViewHolder) contentView.getTag();
        }
        FolderBean folderBean = mFolderList.get(position);
        holder.ivCover.displayImage(folderBean.getFileList().get(0).getPath());
        holder.ivCover.setScaleType(ImageView.ScaleType.CENTER_CROP);
        holder.tvFolderName.setText(folderBean.getFolderName());
        holder.tvFolderCount.setText(String.valueOf(folderBean.getFileList().size()));

        if (position == 0) {
            holder.flSelectedHint.setVisibility(mSelectedSize == 0 ? View.GONE : View.VISIBLE);
            holder.tvSelectedSize.setText(String.valueOf(mSelectedSize));
        } else {
            long start = System.currentTimeMillis();
            int selectedSize = 0;
            for (FileBean fileBean : folderBean.getFileList()) {
                if (fileBean.isSelected())
                    selectedSize++;
            }
            if (selectedSize > 0) {
                holder.flSelectedHint.setVisibility(View.VISIBLE);
                holder.tvSelectedSize.setText(String.valueOf(selectedSize));
            } else {
                holder.flSelectedHint.setVisibility(View.GONE);
            }
            LogUtils.e("time : " + (System.currentTimeMillis() - start));
        }
        return contentView;
    }

    class ViewHolder {
        SketchImageView ivCover;
        FrameLayout flSelectedHint;
        TextView tvSelectedSize;
        TextView tvFolderName, tvFolderCount;
    }
}
