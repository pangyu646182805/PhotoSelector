package com.ppyy.photoselector.loader;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import com.ppyy.photoselector.MimeType;
import com.ppyy.photoselector.SelectionOptions;
import com.ppyy.photoselector.bean.FileBean;
import com.ppyy.photoselector.bean.FolderBean;
import com.ppyy.photoselector.utils.LogUtils;
import com.ppyy.photoselector.utils.MediaComparator;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by NeuroAndroid on 2017/11/1.
 */

public class MediaLoader implements LoaderManager.LoaderCallbacks<ArrayList<FileBean>> {
    private static final int LOADER_ID = 1;

    private MediaCallBack mMediaCallBack;
    private WeakReference<Context> mContext;
    private LoaderManager mLoaderManager;

    public void onCreate(AppCompatActivity activity, MediaCallBack callBack) {
        mContext = new WeakReference<>(activity.getApplicationContext());
        mLoaderManager = activity.getSupportLoaderManager();
        mMediaCallBack = callBack;
    }

    public void loadMedia() {
        mLoaderManager.initLoader(LOADER_ID, null, this);
    }

    @Override
    public Loader<ArrayList<FileBean>> onCreateLoader(int id, Bundle args) {
        Context context = mContext.get();
        if (null == context) {
            return null;
        }
        return new AsyncMediaLoader(context);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<FileBean>> loader, ArrayList<FileBean> mediaList) {
        if (null == mContext.get()) {
            return;
        }
        if (null == mMediaCallBack) {
            return;
        }
        if (mediaList == null || mediaList.isEmpty()) {
            return;
        }

        long start = System.currentTimeMillis();
        ArrayList<FileBean> fileList;
        Map<String, ArrayList<FileBean>> map = new TreeMap<>();

        ArrayList<FileBean> selectedItems = SelectionOptions.getOptions().selectedItems;
        for (FileBean fileBean : mediaList) {
            if (selectedItems != null && !selectedItems.isEmpty()) {
                for (FileBean selectedItem : selectedItems) {
                    if (selectedItem.equals(fileBean)) {
                        fileBean.setSelected(true);
                        break;
                    }
                }
            }
            String folderName = new File(fileBean.getPath()).getParentFile().getName();
            if (!map.containsKey(folderName)) {
                fileList = new ArrayList<>();
                fileList.add(fileBean);
                map.put(folderName, fileList);
            } else {
                fileList = map.get(folderName);
                fileList.add(fileBean);
            }
        }

        FolderBean folderBean = new FolderBean();
        folderBean.setFolderName("全部");
        folderBean.setFileList(mediaList);

        ArrayList<FolderBean> folderList = new ArrayList<>();
        folderList.add(folderBean);
        for (Map.Entry<String, ArrayList<FileBean>> entry : map.entrySet()) {
            folderBean = new FolderBean();
            folderBean.setFolderName(entry.getKey());
            folderBean.setFileList(entry.getValue());
            folderList.add(folderBean);
        }
        LogUtils.e("folder list size --> " + folderList.size());
        LogUtils.e("加载文件夹耗时 --> " + (System.currentTimeMillis() - start));
        mMediaCallBack.onLoadFinished(mediaList, folderList);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<FileBean>> loader) {
        if (null == mContext.get()) {
            return;
        }
        if (null == mMediaCallBack) {
            return;
        }
        mMediaCallBack.onLoaderReset();
    }

    public void onDestroy() {
        mLoaderManager.destroyLoader(LOADER_ID);
        if (mContext != null)
            mContext.clear();
        mMediaCallBack = null;
    }

    private static class AsyncMediaLoader extends WrappedAsyncTaskLoader<ArrayList<FileBean>> {
        AsyncMediaLoader(Context context) {
            super(context);
        }

        @Override
        public ArrayList<FileBean> loadInBackground() {
            long start = System.currentTimeMillis();
            ArrayList<FileBean> mediaList = new ArrayList<>();
            boolean showGif = SelectionOptions.getOptions().showGif;
            switch (SelectionOptions.getOptions().mimeType) {
                case MimeType.PHOTO:
                    mediaList.addAll(PhotoLoader.getAllPhotos(getContext(), showGif));
                    break;
                case MimeType.VIDEO:
                    mediaList.addAll(VideoLoader.getAllVideos(getContext()));
                    break;
                case MimeType.AUDIO:
                    mediaList.addAll(AudioLoader.getAllAudios(getContext()));
                    break;
                case MimeType.ALL:
                    mediaList.addAll(PhotoLoader.getAllPhotos(getContext(), showGif));
                    mediaList.addAll(VideoLoader.getAllVideos(getContext()));
                    break;
            }
            Collections.sort(mediaList, new MediaComparator());
            LogUtils.e("loadInBackground 耗时 --> " + (System.currentTimeMillis() - start));
            return mediaList;
        }
    }

    public interface MediaCallBack {
        void onLoadFinished(ArrayList<FileBean> mediaList, ArrayList<FolderBean> folderList);

        void onLoaderReset();
    }
}
