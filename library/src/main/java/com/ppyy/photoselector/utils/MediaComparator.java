package com.ppyy.photoselector.utils;

import com.ppyy.photoselector.bean.FileBean;

import java.util.Comparator;

/**
 * Created by Administrator on 2017/11/10.
 */

public class MediaComparator implements Comparator<FileBean> {
    @Override
    public int compare(FileBean fileBean, FileBean other) {
        Long time, compareTime;
        time = getTime(fileBean);
        compareTime = getTime(other);
        int flag = compareTime.compareTo(time);
        if (flag == 0) {
            // 如果同出一个时间 按照title排序
            return other.getTitle().compareTo(fileBean.getTitle());
        } else {
            return flag;
        }
    }

    private long getTime(FileBean fileBean) {
        return fileBean.getDateTaken() != null ? fileBean.getDateTaken() : fileBean.getDateModified();
    }
}
