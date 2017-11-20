package com.ppyy.photoselector.utils;

import com.ppyy.photoselector.bean.FileBean;

import java.util.ArrayList;

/**
 * Created by NeuroAndroid on 2017/11/6.
 */

public class DataTransferStation {
    private ArrayList<FileBean> items;
    private ArrayList<FileBean> selectedItems;

    private DataTransferStation() {
    }

    public static DataTransferStation getInstance() {
        return DataTransferStationHolder.STATION;
    }

    public void putItems(ArrayList<FileBean> items) {
        this.items = new ArrayList<>(items);
    }

    public ArrayList<FileBean> getItems() {
        if (this.items == null) {
            this.items = new ArrayList<>();
        }
        return this.items;
    }

    public ArrayList<FileBean> getSelectedItems() {
        if (this.selectedItems == null) {
            this.selectedItems = new ArrayList<>();
        }
        return this.selectedItems;
    }

    public void putSelectedItem(FileBean item) {
        if (this.selectedItems == null) {
            this.selectedItems = new ArrayList<>();
        }
        this.selectedItems.add(item);
    }

    public void putSelectedItems(ArrayList<FileBean> items) {
        if (items != null && !items.isEmpty()) {
            if (this.selectedItems == null) {
                this.selectedItems = new ArrayList<>();
            }
            this.selectedItems.addAll(items);
        }
    }

    public void removeFromSelectedItems(FileBean item) {
        if (this.selectedItems != null) {
            this.selectedItems.remove(item);
        }
    }

    public void onDestroy() {
        if (this.items != null) {
            items.clear();
            items = null;
        }
        if (this.selectedItems != null) {
            selectedItems.clear();
            selectedItems = null;
        }
    }

    private static class DataTransferStationHolder {
        private static final DataTransferStation STATION = new DataTransferStation();
    }
}
