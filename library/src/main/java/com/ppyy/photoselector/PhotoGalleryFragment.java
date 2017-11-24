package com.ppyy.photoselector;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatImageView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;

import com.ppyy.photoselector.bean.FileBean;
import com.ppyy.photoselector.interfaces.DisplayCallBack;
import com.ppyy.photoselector.utils.LogUtils;
import com.ppyy.photoselector.utils.VideoThumbnailUriModel;

import me.xiaopan.sketch.Sketch;
import me.xiaopan.sketch.SketchImageView;
import me.xiaopan.sketch.display.FadeInImageDisplayer;
import me.xiaopan.sketch.drawable.SketchGifDrawable;
import me.xiaopan.sketch.util.SketchUtils;
import me.xiaopan.sketch.viewfun.huge.HugeImageViewer;
import me.xiaopan.sketch.viewfun.zoom.ImageZoomer;

/**
 * Created by NeuroAndroid on 2017/11/6.
 */

public class PhotoGalleryFragment extends Fragment {
    private SketchImageView mIvImg;
    private ViewStub mStub;
    private AppCompatImageView mIvPLay;

    private DisplayCallBack mDisplayCallBack;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DisplayCallBack) {
            mDisplayCallBack = (DisplayCallBack) context;
        }
    }

    private boolean isVisibleToUser() {
        return isResumed() && getUserVisibleHint();
    }

    public static PhotoGalleryFragment newInstance(FileBean fileBean) {
        PhotoGalleryFragment photoGalleryFragment = new PhotoGalleryFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("item", fileBean);
        photoGalleryFragment.setArguments(bundle);
        return photoGalleryFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo_gallery, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FileBean item = getArguments().getParcelable("item");
        if (item == null) return;
        // mIvImg = SelectionOptions.getOptions().viewHolderCreator.inflateGalleryViewStub(mStub);

        mIvImg = view.findViewById(R.id.iv_img);
        mIvPLay = view.findViewById(R.id.iv_play);
        mIvImg.setScaleType(ImageView.ScaleType.FIT_CENTER);
        if (item.getMediaMimeType() == MimeType.PHOTO) {
            mIvPLay.setVisibility(View.GONE);
            mIvImg.setHugeImageEnabled(true);

            mIvImg.setZoomEnabled(true);
            mIvImg.getHugeImageViewer().setPause(!isVisibleToUser());
            if ("image/gif".equals(item.getMimeType()) || item.getPath().endsWith("gif")) {
                Sketch.with(getContext())
                        .display(item.getPath(), mIvImg)
                        .decodeGifImage().commit();
            } else {
                Sketch.with(getContext())
                        .display(item.getPath(), mIvImg)
                        .loadingImage(R.drawable.img_loading)
                        .errorImage(R.drawable.img_loading)
                        .displayer(new FadeInImageDisplayer())
                        .commit();
            }
        } else {
            mIvPLay.setVisibility(View.VISIBLE);
            // 是视频
            Sketch.with(getContext())
                    .display(VideoThumbnailUriModel.makeUri(item.getPath()), mIvImg)
                    .loadingImage(R.drawable.img_loading)
                    .errorImage(R.drawable.img_loading)
                    .displayer(new FadeInImageDisplayer())
                    .commit();
        }

        ImageZoomer imageZoomer = mIvImg.getImageZoomer();
        if (imageZoomer != null) {
            imageZoomer.setOnViewTapListener(new ImageZoomer.OnViewTapListener() {
                @Override
                public void onViewTap(View view, float x, float y) {
                    if (null != mDisplayCallBack) {
                        mDisplayCallBack.controlAppBarLayoutDisplay();
                    }
                }
            });
        } else {
            mIvImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != mDisplayCallBack) {
                        mDisplayCallBack.controlAppBarLayoutDisplay();
                    }
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            onUserVisibleChanged(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            onUserVisibleChanged(false);
        }
    }

    @Override
    public void onDestroyView() {
        if (mIvImg != null) {
            Sketch.cancel(mIvImg);
            mIvImg = null;
        }
        super.onDestroyView();
    }

    private void onUserVisibleChanged(boolean isVisibleToUser) {
        LogUtils.e("isVisibleToUser : " + isVisibleToUser);
        if (mIvImg != null) {
            HugeImageViewer hugeImageViewer = mIvImg.getHugeImageViewer();
            if (null != hugeImageViewer) {
                hugeImageViewer.setPause(!isVisibleToUser);
            }
            Drawable lastDrawable = SketchUtils.getLastDrawable(mIvImg.getDrawable());
            if (null != lastDrawable && lastDrawable instanceof SketchGifDrawable) {
                ((SketchGifDrawable) lastDrawable).followPageVisible(isVisibleToUser, false);
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isResumed()) {
            onUserVisibleChanged(isVisibleToUser);
        }
    }
}
