package com.ppyy.photoselector;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.StyleRes;

import com.ppyy.photoselector.interfaces.ViewHolderCreator;

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
    public boolean showGif;  // 是否显示gif图片
    public boolean showGifFlag;  // 是否显示gif标志
    @DrawableRes
    public int gifFlagResId = R.drawable.ic_gif_flag;  // gif标志resId
    @ColorInt
    public int backgroundColor = Color.WHITE;  // 图片的背景颜色 默认白色
    public boolean showHeaderItem = true;  // 是否展示PhotoSelector第一项(即拍照item)
    public boolean canceledOnTouchOutside = true;  // 是否点击空白区域取消PhotoSelector

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
        showGif = false;
        showGifFlag = false;
        gifFlagResId = R.drawable.ic_gif_flag;
        backgroundColor = Color.WHITE;
        showHeaderItem = true;
        canceledOnTouchOutside = true;
    }

    private static class SelectionOptionsHolder {
        private static final SelectionOptions OPTIONS = new SelectionOptions();
    }
}
