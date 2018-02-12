package com.ppyy.photoselector;

import android.support.annotation.IntDef;

import java.io.File;
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

    public static String getMimeTypeByFile(File file) {
        if (file != null) {
            String name = file.getName();
            if (name.endsWith(".mp4") || name.endsWith(".avi")
                    || name.endsWith(".3gpp") || name.endsWith(".3gp") || name.startsWith(".mov")) {
                return "video/mp4";
            } else if (name.endsWith(".PNG") || name.endsWith(".png") || name.endsWith(".jpeg")
                    || name.endsWith(".gif") || name.endsWith(".GIF") || name.endsWith(".jpg")
                    || name.endsWith(".webp") || name.endsWith(".WEBP") || name.endsWith(".JPEG")) {
                return "image/jpeg";
            } else if (name.endsWith(".mp3") || name.endsWith(".amr")
                    || name.endsWith(".aac") || name.endsWith(".war")
                    || name.endsWith(".flac")) {
                return "audio/mpeg";
            }
        }
        return "image/jpeg";
    }

    @IntDef({ALL, PHOTO, VIDEO, AUDIO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface MediaMimeType {
    }
}
