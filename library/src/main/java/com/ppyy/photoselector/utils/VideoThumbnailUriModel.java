package com.ppyy.photoselector.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import me.xiaopan.sketch.uri.AbsBitmapDiskCacheUriModel;
import me.xiaopan.sketch.uri.GetDataSourceException;
import me.xiaopan.sketch.util.SketchUtils;

/**
 * Created by Administrator on 2017/11/4.
 */

public class VideoThumbnailUriModel extends AbsBitmapDiskCacheUriModel {
    private static final String SCHEME = "video.thumbnail://";

    public static String makeUri(String filePath) {
        return SCHEME + filePath;
    }

    @Override
    protected boolean match(@NonNull String uri) {
        return !TextUtils.isEmpty(uri) && uri.startsWith(SCHEME);
    }

    @NonNull
    @Override
    protected Bitmap getContent(@NonNull Context context, @NonNull String uri) throws GetDataSourceException {
        return ThumbnailUtils.createVideoThumbnail(getUriContent(uri), MediaStore.Images.Thumbnails.MINI_KIND);
    }

    /**
     * 获取 uri 所真正包含的内容部分，例如 "video.thumbnail:///sdcard/test.mp4"，就会返回 "/sdcard/test.mp4"
     *
     * @param uri 图片 uri
     * @return uri 所真正包含的内容部分，例如 "video.thumbnail:///sdcard/test.mp4"，就会返回 "/sdcard/test.mp4"
     */
    @NonNull
    @Override
    public String getUriContent(@NonNull String uri) {
        return match(uri) ? uri.substring(SCHEME.length()) : uri;
    }

    @NonNull
    @Override
    public String getDiskCacheKey(@NonNull String uri) {
        return SketchUtils.createFileUriDiskCacheKey(uri, getUriContent(uri));
    }
}
