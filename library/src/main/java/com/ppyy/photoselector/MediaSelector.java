package com.ppyy.photoselector;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.lang.ref.WeakReference;

/**
 * Created by NeuroAndroid on 2017/11/1.
 */

public class MediaSelector {
    private final WeakReference<Activity> mContext;
    private final WeakReference<Fragment> mFragment;

    private MediaSelector(Activity activity) {
        this(activity, null);
    }

    private MediaSelector(Fragment fragment) {
        this(fragment.getActivity(), fragment);
    }

    private MediaSelector(Activity activity, Fragment fragment) {
        mContext = new WeakReference<>(activity);
        mFragment = new WeakReference<>(fragment);
    }

    public static MediaSelector from(Activity activity) {
        return new MediaSelector(activity);
    }

    public static MediaSelector from(Fragment fragment) {
        return new MediaSelector(fragment);
    }

    public SelectionCreator choose(@MimeType.MediaMimeType int mimeType) {
        return new SelectionCreator(this, mimeType);
    }

    @Nullable
    Activity getActivity() {
        return mContext.get();
    }

    @Nullable
    Fragment getFragment() {
        return mFragment != null ? mFragment.get() : null;
    }
}
