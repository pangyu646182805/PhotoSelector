package com.ppyy.photoselector;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;

import com.ppyy.photoselector.bean.FileBean;
import com.ppyy.photoselector.interfaces.ViewHolderCreator;

import java.util.ArrayList;

/**
 * Created by NeuroAndroid on 2017/11/1.
 */

public final class SelectionCreator {
    private final MediaSelector mMediaSelector;
    private final SelectionOptions mOptions;

    SelectionCreator(MediaSelector mediaSelector, @MimeType.MediaMimeType int mimeType) {
        mMediaSelector = mediaSelector;
        mOptions = SelectionOptions.getCleanOptions();
        mOptions.mimeType = mimeType;
    }

    public SelectionCreator themeId(@StyleRes int themeId) {
        mOptions.themeId = themeId;
        return this;
    }

    /**
     * 最大可选择数量
     *
     * @param maxSelectable 必须大于1
     */
    public SelectionCreator maxSelectable(int maxSelectable) {
        if (maxSelectable < 1) {
            throw new IllegalArgumentException("maxSelectable must be greater than or equal to one");
        }
        mOptions.maxSelectable = maxSelectable;
        return this;
    }

    /**
     * PhotoSelector显示的网格大小
     *
     * @param gridSize 必须大于2
     */
    public SelectionCreator gridSize(int gridSize) {
        if (gridSize < 2) {
            throw new IllegalArgumentException("gridSize must be greater than or equal to two");
        }
        mOptions.gridSize = gridSize;
        return this;
    }

    /**
     * 是否支持暗色状态栏
     */
    public SelectionCreator supportDarkStatusBar(boolean supportDarkStatusBar) {
        mOptions.supportDarkStatusBar = supportDarkStatusBar;
        return this;
    }

    /**
     * 是否展示gif图片
     */
    public SelectionCreator showGif(boolean showGif) {
        mOptions.showGif = showGif;
        return this;
    }

    /**
     * 是否显示gif标志
     */
    public SelectionCreator showGifFlag(boolean showGifFlag) {
        mOptions.showGifFlag = showGifFlag;
        return this;
    }

    /**
     * 设置gif标志的resId
     */
    public SelectionCreator setGifFlagResId(@DrawableRes int gifFlagResId) {
        mOptions.gifFlagResId = gifFlagResId;
        return this;
    }

    /**
     * 是否展示PhotoSelector第一项(即拍照item)
     */
    public SelectionCreator showHeaderItem(boolean showHeaderItem) {
        mOptions.showHeaderItem = showHeaderItem;
        return this;
    }

    /**
     * 设置是否点击空白区域取消PhotoSelector
     */
    public SelectionCreator setCanceledOnTouchOutside(boolean canceledOnTouchOutside) {
        mOptions.canceledOnTouchOutside = canceledOnTouchOutside;
        return this;
    }

    public SelectionCreator customViewHolder(ViewHolderCreator viewHolderCreator) {
        if (null == viewHolderCreator) {
            throw new IllegalArgumentException("viewHolderCreator must not be null");
        }
        mOptions.viewHolderCreator = viewHolderCreator;
        return this;
    }

    /**
     * 将已经选择的items传进去，默认会勾选这些items
     *
     * @param selectedItems 之前选择的项目
     */
    public SelectionCreator selectedItems(ArrayList<FileBean> selectedItems) {
        if (selectedItems != null && !selectedItems.isEmpty()) {
            mOptions.selectedItems = selectedItems;
        }
        return this;
    }

    public void forResult(int requestCode) {
        Activity activity = mMediaSelector.getActivity();
        if (activity == null) {
            return;
        }

        Intent intent = new Intent(activity, PhotoSelectorActivity.class);

        Fragment fragment = mMediaSelector.getFragment();
        if (fragment != null) {
            fragment.startActivityForResult(intent, requestCode);
        } else {
            activity.startActivityForResult(intent, requestCode);
        }
    }
}
