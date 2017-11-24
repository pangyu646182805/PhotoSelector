package com.ppyy.photoselector.interfaces;

import android.view.ViewStub;
import android.widget.ImageView;

import com.ppyy.photoselector.bean.FileBean;

/**
 * Created by NeuroAndroid on 2017/11/3.
 */

public interface ViewHolderCreator {
    // 加载MediaAdapter中item中的ViewStub
    ImageView inflateViewStub(ViewStub viewStub);

    // 加载PhotoGalleryFragment中的ViewStub
    ImageView inflateGalleryViewStub(ViewStub viewStub);

    void onBindViewHolder(ImageView imageView, FileBean item);
}
