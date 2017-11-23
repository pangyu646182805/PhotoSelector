package com.ppyy.photoselector.compress;

import android.graphics.Bitmap;

import java.io.File;

/**
 * author：luck
 * project：PictureSelector
 * email：893855882@qq.com
 * data：16/12/31
 */

public class LuBanBuilder {
    int maxSize;

    int maxWidth;

    int maxHeight;

    File cacheDir;

    Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;

    int gear = LuBan.THIRD_GEAR;

    LuBanBuilder(File cacheDir) {
        this.cacheDir = cacheDir;
    }
}
