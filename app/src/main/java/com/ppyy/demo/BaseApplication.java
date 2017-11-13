package com.ppyy.demo;

import android.app.Application;

import com.ppyy.photoselector.utils.AudioThumbnailUriModel;
import com.ppyy.photoselector.utils.VideoThumbnailUriModel;

import me.xiaopan.sketch.Configuration;
import me.xiaopan.sketch.Sketch;

/**
 * Created by NeuroAndroid on 2017/11/13.
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Configuration configuration = Sketch.with(this).getConfiguration();
        configuration.getUriModelRegistry().add(new VideoThumbnailUriModel());
        configuration.getUriModelRegistry().add(new AudioThumbnailUriModel());
    }
}
