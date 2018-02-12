package com.ppyy.photoselector;

import android.graphics.Color;
import android.view.ViewStub;
import android.widget.ImageView;

import com.ppyy.photoselector.bean.FileBean;
import com.ppyy.photoselector.interfaces.ViewHolderCreator;
import com.ppyy.photoselector.utils.AudioThumbnailUriModel;
import com.ppyy.photoselector.utils.LogUtils;
import com.ppyy.photoselector.utils.VideoThumbnailUriModel;

import me.xiaopan.sketch.Sketch;
import me.xiaopan.sketch.SketchImageView;
import me.xiaopan.sketch.display.ColorTransitionImageDisplayer;

/**
 * Created by NeuroAndroid on 2017/11/3.
 */

public class SketchViewHolderCreator implements ViewHolderCreator {
    @Override
    public ImageView inflateViewStub(ViewStub viewStub) {
        viewStub.setLayoutResource(R.layout.layout_sketch_view);
        viewStub.setInflatedId(R.id.iv_img);
        return (ImageView) viewStub.inflate();
    }

    @Override
    public ImageView inflateGalleryViewStub(ViewStub viewStub) {
        viewStub.setLayoutResource(R.layout.layout_sketch_view);
        viewStub.setInflatedId(R.id.iv_img);
        return (ImageView) viewStub.inflate();
    }

    @Override
    public void onBindViewHolder(ImageView imageView, FileBean item) {
        SketchImageView ivImg = (SketchImageView) imageView;
        ivImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
        String url;
        switch (item.getMediaMimeType()) {
            case MimeType.PHOTO:
            default:
                url = item.getPath();
                break;
            case MimeType.VIDEO:
                url = VideoThumbnailUriModel.makeUri(item.getPath());
                break;
            case MimeType.AUDIO:
                url = AudioThumbnailUriModel.makeUri(item.getPath());
                break;
        }
        LogUtils.e(ivImg.getLayoutParams().width + " : " + ivImg.getLayoutParams().height);
        Sketch.with(ivImg.getContext())
                .display(url, ivImg)
                .maxSize(ivImg.getLayoutParams().width, ivImg.getLayoutParams().height)
                .resize(ivImg.getLayoutParams().width, ivImg.getLayoutParams().height)
                .cacheProcessedImageInDisk()
                .loadingImage(R.drawable.img_loading)
                .errorImage(R.drawable.img_loading)
                .displayer(new ColorTransitionImageDisplayer(Color.parseColor("#3c3f41")))
                .thumbnailMode()
                .commit();
    }
}
