package com.ppyy.photoselector;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by NeuroAndroid on 2017/10/27.
 */

public class MimeType {
    /**
     * 图片和视频
     * MimeType为All的时候允许同时选择图片和视频
     */
    public static final int ALL = 0;
    public static final int PHOTO = 2;
    public static final int VIDEO = 4;
    public static final int AUDIO = 6;

    @MediaMimeType
    private int mMimeType = PHOTO;

    public void setMimeType(@MediaMimeType int mimeType) {
        this.mMimeType = mimeType;
    }

    @MediaMimeType
    public int getMimeType() {
        return mMimeType;
    }

    @IntDef({ALL, PHOTO, VIDEO, AUDIO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MediaMimeType {
    }
}
