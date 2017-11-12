package com.ppyy.photoselector.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import me.xiaopan.sketch.uri.AbsBitmapDiskCacheUriModel;
import me.xiaopan.sketch.uri.GetDataSourceException;
import me.xiaopan.sketch.util.SketchUtils;

/**
 * Created by Administrator on 2017/11/4.
 */

public class AudioThumbnailUriModel extends AbsBitmapDiskCacheUriModel {
    private static final String SCHEME = "audio.thumbnail://";

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
        return createAlbumArt(getUriContent(uri));
    }

    /**
     * @param filePath 文件路径，like XXX/XXX/XX.mp3
     * @return 专辑封面bitmap
     * @Description 获取专辑封面
     */
    public Bitmap createAlbumArt(final String filePath) {
        Bitmap bitmap = null;
        //能够获取多媒体文件元数据的类
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath); //设置数据源
            byte[] embedPic = retriever.getEmbeddedPicture(); //得到字节型数据
            bitmap = BitmapFactory.decodeByteArray(embedPic, 0, embedPic.length); //转换为图片
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                retriever.release();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return bitmap;
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
