package com.ppyy.photoselector.bean;

import java.util.ArrayList;

/**
 * Created by NeuroAndroid on 2017/11/2.
 */

public class FolderBean {
    private String folderName;
    private ArrayList<FileBean> fileList;

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public ArrayList<FileBean> getFileList() {
        return fileList;
    }

    public void setFileList(ArrayList<FileBean> fileList) {
        this.fileList = fileList;
    }

    @Override
    public boolean equals(Object obj) {
        FolderBean folderBean = (FolderBean) obj;
        return this.folderName.equals(folderBean.getFolderName()) || super.equals(obj);
    }
}
