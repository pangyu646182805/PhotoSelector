package com.ppyy.photoselector.compress;

import com.ppyy.photoselector.bean.FileBean;

import java.util.ArrayList;

/**
 * author：luck
 * project：PictureSelector
 * email：893855882@qq.com
 * data：16/12/31
 */

public interface CompressInterface {
    void compress();

    /**
     * 压缩结果监听器
     */
    interface CompressListener {
        /**
         * 压缩成功
         *
         * @param selectedItems 已经压缩的items
         */
        void onCompressSuccess(ArrayList<FileBean> selectedItems);

        /**
         * 压缩失败
         *
         * @param selectedItems 压缩失败的items
         * @param msg           失败的原因
         */
        void onCompressError(ArrayList<FileBean> selectedItems, String msg);
    }
}
