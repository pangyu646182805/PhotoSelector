package com.ppyy.photoselector.interfaces;

import android.view.ViewStub;
import android.widget.ImageView;

import com.ppyy.photoselector.adapter.MediaAdapter;
import com.ppyy.photoselector.bean.FileBean;

/**
 * Created by NeuroAndroid on 2017/11/3.
 */

public interface ViewHolderCreator {
    ImageView inflateViewStub(ViewStub viewStub);

    void onBindViewHolder(MediaAdapter.MediaContentViewHolder viewHolder, FileBean item);
}
