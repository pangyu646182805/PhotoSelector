package com.ppyy.photoselector.compress;

import android.content.Context;
import android.text.TextUtils;

import com.ppyy.photoselector.bean.FileBean;

import java.io.File;
import java.util.ArrayList;

/**
 * author：luck
 * project：PictureSelector
 * email：893855882@qq.com
 * data：16/12/31
 */
public class CompressImageOptions implements CompressInterface {
    private CompressImageUtil compressImageUtil;
    private ArrayList<FileBean> selectedItems;
    private CompressInterface.CompressListener listener;

    public static CompressInterface compress(Context context, CompressConfig config, ArrayList<FileBean> selectedItems, CompressInterface.CompressListener listener) {
        if (config.getLubanOptions() != null) {
            return new LuBanCompress(context, config, selectedItems, listener);
        } else {
            return new CompressImageOptions(context, config, selectedItems, listener);
        }
    }

    private CompressImageOptions(Context context, CompressConfig config, ArrayList<FileBean> selectedItems, CompressInterface.CompressListener listener) {
        compressImageUtil = new CompressImageUtil(context, config);
        this.selectedItems = selectedItems;
        this.listener = listener;
    }

    @Override
    public void compress() {
        if (selectedItems == null || selectedItems.isEmpty())
            listener.onCompressError(selectedItems, " selectedItems is null");
        for (FileBean item : selectedItems) {
            if (item == null) {
                listener.onCompressError(selectedItems, " There are pictures of compress is null.");
                return;
            }
        }
        compress(selectedItems.get(0));
    }

    private void compress(final FileBean item) {
        String path;
        if (item.isCrop()) {
            path = item.getCropPath();
        } else {
            path = item.getPath();
        }
        if (TextUtils.isEmpty(path)) {
            continueCompress(item, false);
            return;
        }

        File file = new File(path);
        if (!file.exists() || !file.isFile()) {
            continueCompress(item, false);
            return;
        }

        compressImageUtil.compress(path, new CompressImageUtil.CompressListener() {
            @Override
            public void onCompressSuccess(String imgPath) {
                item.setCompressPath(imgPath);
                continueCompress(item, true);
            }

            @Override
            public void onCompressFailed(String imgPath, String msg) {
                continueCompress(item, false, msg);
            }
        });
    }

    private void continueCompress(FileBean item, boolean preSuccess, String... message) {
        item.setCompressed(preSuccess);
        int index = selectedItems.indexOf(item);
        boolean isLast = index == selectedItems.size() - 1;
        if (isLast) {
            handleCompressCallBack(message);
        } else {
            compress(selectedItems.get(index + 1));
        }
    }

    private void handleCompressCallBack(String... message) {
        if (message.length > 0) {
            listener.onCompressError(selectedItems, message[0]);
            return;
        }

        for (FileBean item : selectedItems) {
            if (!item.isCompressed()) {
                listener.onCompressError(selectedItems, item.getCompressPath() + " is compress failures");
                return;
            }
        }
        listener.onCompressSuccess(selectedItems);
    }
}
