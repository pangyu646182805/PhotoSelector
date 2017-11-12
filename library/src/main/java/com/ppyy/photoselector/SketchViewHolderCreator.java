package com.ppyy.photoselector;

import android.view.ViewStub;
import android.widget.ImageView;

import com.ppyy.photoselector.adapter.MediaAdapter;
import com.ppyy.photoselector.bean.FileBean;
import com.ppyy.photoselector.interfaces.ViewHolderCreator;
import com.ppyy.photoselector.utils.AudioThumbnailUriModel;
import com.ppyy.photoselector.utils.VideoThumbnailUriModel;

import me.xiaopan.sketch.Sketch;
import me.xiaopan.sketch.SketchImageView;
import me.xiaopan.sketch.display.TransitionImageDisplayer;
import me.xiaopan.sketch.request.ShapeSize;

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
    public void onBindViewHolder(MediaAdapter.MediaContentViewHolder viewHolder, FileBean item) {
        SketchImageView ivImg = (SketchImageView) viewHolder.image;
        // ivImg.setScaleType(ImageView.ScaleType.FIT_CENTER);
        switch (item.getMediaMimeType()) {
            case MimeType.PHOTO:
                /*Sketch.with(ivImg.getContext())
                        .display(item.getPath(), ivImg)
                        .maxSize(ivImg.getLayoutParams().width, ivImg.getLayoutParams().height)
                        .resize(ivImg.getLayoutParams().width, ivImg.getLayoutParams().height)
                        .cacheProcessedImageInDisk()
                        .loadingImage(R.drawable.img_loading)
                        .errorImage(R.drawable.img_loading)
                        // .shapeSize(ShapeSize.byViewFixedSize())
                        .displayer(new TransitionImageDisplayer())
                        .thumbnailMode()
                        .commit();*/
                Sketch.with(ivImg.getContext())
                        .display(item.getPath(), ivImg)
                        .maxSize(ivImg.getLayoutParams().width, ivImg.getLayoutParams().height)
                        .resize(ivImg.getLayoutParams().width, ivImg.getLayoutParams().height)
                        .cacheProcessedImageInDisk()
                        .loadingImage(R.drawable.img_loading)
                        .displayer(new TransitionImageDisplayer())
                        .thumbnailMode()
                        .commit();
                break;
            case MimeType.VIDEO:
                Sketch.with(ivImg.getContext())
                        .display(VideoThumbnailUriModel.makeUri(item.getPath()), ivImg)
                        .maxSize(ivImg.getLayoutParams().width, ivImg.getLayoutParams().height)
                        .cacheProcessedImageInDisk()
                        .loadingImage(R.drawable.img_loading)
                        .errorImage(R.drawable.img_loading)
                        // .displayer(new ColorTransitionImageDisplayer(Color.parseColor("#33000000")))
                        .displayer(new TransitionImageDisplayer())
                        .shapeSize(ShapeSize.byViewFixedSize())
                        .commit();
                break;
            case MimeType.AUDIO:
                Sketch.with(ivImg.getContext())
                        .display(AudioThumbnailUriModel.makeUri(item.getPath()), ivImg)
                        .maxSize(ivImg.getLayoutParams().width, ivImg.getLayoutParams().height)
                        .cacheProcessedImageInDisk()
                        .loadingImage(R.drawable.img_loading)
                        .errorImage(R.drawable.img_loading)
                        // .displayer(new ColorTransitionImageDisplayer(Color.parseColor("#33000000")))
                        .displayer(new TransitionImageDisplayer())
                        .shapeSize(ShapeSize.byViewFixedSize())
                        .commit();
                break;
        }
    }
}
