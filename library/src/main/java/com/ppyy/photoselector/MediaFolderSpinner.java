package com.ppyy.photoselector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.ListPopupWindow;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.ppyy.photoselector.adapter.FolderAdapter;
import com.ppyy.photoselector.bean.FolderBean;
import com.ppyy.photoselector.utils.DataTransferStation;

import java.util.ArrayList;

/**
 * Created by NeuroAndroid on 2017/11/3.
 */

public class MediaFolderSpinner {
    private static final int MAX_SHOWN_COUNT = 6;

    private ListPopupWindow mListPopupWindow;
    private TextView mSelectedTextView;
    private FolderAdapter mFolderAdapter;
    private AdapterView.OnItemClickListener mOnItemClickListener;

    public FolderAdapter getFolderAdapter() {
        return mFolderAdapter;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public MediaFolderSpinner(@NonNull Context context) {
        mListPopupWindow = new ListPopupWindow(context);
        mListPopupWindow.setModal(true);
        float density = context.getResources().getDisplayMetrics().density;
        mListPopupWindow.setContentWidth((int) (216 * density));
        mListPopupWindow.setHorizontalOffset((int) (16 * density));
        mListPopupWindow.setVerticalOffset((int) (-48 * density));

        mListPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                MediaFolderSpinner.this.onItemClick(position);
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(adapterView, view, position, id);
                }
            }
        });
    }

    private void onItemClick(int position) {
        mListPopupWindow.dismiss();
        String folderName = mFolderAdapter.getItem(position).getFolderName();
        mSelectedTextView.setText(folderName);
    }

    public void setAdapter(FolderAdapter folderAdapter) {
        mFolderAdapter = folderAdapter;
        mListPopupWindow.setAdapter(mFolderAdapter);
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setSelectedTextView(TextView tv) {
        mSelectedTextView = tv;
        mSelectedTextView.setVisibility(View.GONE);
        mSelectedTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFolderAdapter.setSelectedSize(DataTransferStation.getInstance().getSelectedItems().size());
                int itemHeight = mSelectedTextView.getResources().getDimensionPixelSize(R.dimen.folder_item_height);
                mListPopupWindow.setHeight(
                        mFolderAdapter.getCount() > MAX_SHOWN_COUNT ? itemHeight * MAX_SHOWN_COUNT
                                : itemHeight * mFolderAdapter.getCount());
                mListPopupWindow.show();
            }
        });
        mSelectedTextView.setOnTouchListener(mListPopupWindow.createDragToOpenListener(mSelectedTextView));
    }

    public void swapFolderList(ArrayList<FolderBean> folderList) {
        mFolderAdapter.setFolderList(folderList);
        String displayName = folderList.get(0).getFolderName();
        mSelectedTextView.setVisibility(View.VISIBLE);
        mSelectedTextView.setText(displayName);
    }

    public void setPopupAnchorView(View view) {
        mListPopupWindow.setAnchorView(view);
    }
}
