package com.ppyy.photoselector.compress;

import android.content.Context;

import com.ppyy.photoselector.bean.FileBean;
import com.ppyy.photoselector.utils.LogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * author：luck
 * project：PictureSelector
 * email：893855882@qq.com
 * data：16/12/31
 */

public class LuBanCompress implements CompressInterface {
    private ArrayList<FileBean> selectedItems;
    private CompressInterface.CompressListener listener;
    private Context context;
    private LuBanOptions options;
    private ArrayList<File> files = new ArrayList<>();

    public LuBanCompress(Context context, CompressConfig config, ArrayList<FileBean> selectedItems,
                         CompressListener listener) {
        options = config.getLubanOptions();
        this.selectedItems = selectedItems;
        this.listener = listener;
        this.context = context;
    }

    @Override
    public void compress() {
        if (selectedItems == null || selectedItems.isEmpty()) {
            listener.onCompressError(selectedItems, " images is null");
            return;
        }
        for (FileBean item : selectedItems) {
            if (item == null) {
                listener.onCompressError(selectedItems, " There are pictures of compress is null.");
                return;
            }
            if (item.isCrop()) {
                files.add(new File(item.getCropPath()));
            } else {
                files.add(new File(item.getPath()));
            }
        }
        if (selectedItems.size() == 1) {
            compressOne();
        } else {
            compressMulti();
        }
    }

    private void compressOne() {
        LogUtils.e("压缩档次 --> " + String.valueOf(options.getGrade()));
        LuBan.compress(context, files.get(0))
                .putGear(options.getGrade())
                .setMaxHeight(options.getMaxHeight())
                .setMaxWidth(options.getMaxWidth())
                .setMaxSize(options.getMaxSize() / 1000)
                .launch(new OnCompressListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(File file) {
                        FileBean item = selectedItems.get(0);
                        item.setCompressPath(file.getPath());
                        item.setCompressed(true);
                        listener.onCompressSuccess(selectedItems);
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onCompressError(selectedItems, e.getMessage() + " is compress failures");
                    }
                });
    }

    private void compressMulti() {
        LogUtils.e("压缩档次 --> " + String.valueOf(options.getGrade()));
        LuBan.compress(context, files)
                .putGear(options.getGrade())
                .setMaxSize(
                        options.getMaxSize() / 1000)  // limit the final image size（unit：Kb）
                .setMaxHeight(options.getMaxHeight())  // limit image height
                .setMaxWidth(options.getMaxWidth())
                .launch(new OnMultiCompressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(List<File> fileList) {
                        handleCompressCallBack(fileList);
                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onCompressError(selectedItems, e.getMessage() + " is compress failures");
                    }
                });
    }

    private void handleCompressCallBack(List<File> files) {
        for (int i = 0, j = selectedItems.size(); i < j; i++) {
            String path = files.get(i).getPath();// 压缩成功后的地址
            FileBean item = selectedItems.get(i);
            // 如果是网络图片则不压缩
            if (path.startsWith("http")) {
                item.setCompressPath("");
            } else {
                item.setCompressed(true);
                item.setCompressPath(path);
            }
        }
        listener.onCompressSuccess(selectedItems);
    }
}
