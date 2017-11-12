package com.ppyy.photoselector.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.ppyy.photoselector.PhotoGalleryActivity;
import com.ppyy.photoselector.conf.PhotoSelectorConfig;

/**
 * Created by Administrator on 2017/11/5.
 */

public class PhotoGallery {
    public static void openPhotoGallery(@NonNull Activity activity, Bundle bundle) {
        Intent intent = new Intent(activity, PhotoGalleryActivity.class);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivityForResult(intent, PhotoSelectorConfig.REQUEST_CODE_PREVIEW);
    }
}
