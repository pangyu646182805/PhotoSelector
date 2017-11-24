package com.ppyy.photoselector;

import android.support.annotation.DrawableRes;
import android.support.annotation.StyleRes;

import com.ppyy.photoselector.bean.FileBean;
import com.ppyy.photoselector.compress.LuBan;
import com.ppyy.photoselector.conf.PhotoSelectorConfig;
import com.ppyy.photoselector.interfaces.ViewHolderCreator;

import java.util.ArrayList;

/**
 * Created by NeuroAndroid on 2017/11/1.
 */

public class SelectionOptions {
    @MimeType.MediaMimeType
    public int mimeType = MimeType.PHOTO;  // 媒体库mime类型
    @StyleRes
    public int themeId;
    public int maxSelectable = 1;  // 最多选择数量
    public int gridSize = 3;  // RecyclerView显示网格大小
    public boolean supportDarkStatusBar;  // 是否支持暗色状态栏
    public boolean showGif;  // 是否显示gif图片
    public boolean showGifFlag;  // 是否显示gif标志
    @DrawableRes
    public int gifFlagResId = R.drawable.ic_gif_flag;  // gif标志resId
    public boolean showHeaderItem = true;  // 是否展示PhotoSelector第一项(即拍照item)
    public boolean canceledOnTouchOutside = true;  // 是否点击空白区域取消PhotoSelector
    public ArrayList<FileBean> selectedItems;
    public boolean isCompress;  // 是否压缩图片
    public int compressMode = PhotoSelectorConfig.SYSTEM_COMPRESS_MODE;  // 压缩模式
    public int compressMaxSize = PhotoSelectorConfig.MAX_COMPRESS_SIZE;  // 单位kb
    public int compressGrade = LuBan.CUSTOM_GEAR;
    public int compressHeight;
    public int compressWidth;

    public ViewHolderCreator viewHolderCreator;

    private SelectionOptions() {
    }

    public static SelectionOptions getOptions() {
        return SelectionOptionsHolder.OPTIONS;
    }

    public static SelectionOptions getCleanOptions() {
        SelectionOptions options = SelectionOptionsHolder.OPTIONS;
        options.reset();
        return options;
    }

    private void reset() {
        mimeType = MimeType.PHOTO;
        maxSelectable = 1;
        gridSize = 3;
        supportDarkStatusBar = false;
        showGif = false;
        showGifFlag = false;
        gifFlagResId = R.drawable.ic_gif_flag;
        showHeaderItem = true;
        canceledOnTouchOutside = true;
        if (selectedItems != null) {
            selectedItems.clear();
            selectedItems = null;
        }
        isCompress = false;
        compressMode = PhotoSelectorConfig.SYSTEM_COMPRESS_MODE;
        compressMaxSize = PhotoSelectorConfig.MAX_COMPRESS_SIZE;
        compressGrade = LuBan.CUSTOM_GEAR;
        compressHeight = 0;
        compressWidth = 0;
    }

    private static class SelectionOptionsHolder {
        private static final SelectionOptions OPTIONS = new SelectionOptions();
    }
}
