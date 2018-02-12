package com.ppyy.photoselector.loader;

import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.ppyy.photoselector.MimeType;
import com.ppyy.photoselector.bean.FileBean;

import java.util.ArrayList;

/**
 * Created by NeuroAndroid on 2017/5/25.
 */

public class PhotoLoader {
    @NonNull
    public static ArrayList<FileBean> getAllPhotos(@NonNull Context context, boolean showGif) {
        StringBuilder selection = new StringBuilder();
        selection.append(MediaStore.Images.Media.MIME_TYPE).append("=? or ")
                .append(MediaStore.Images.Media.MIME_TYPE).append("=? or ")
                .append(MediaStore.Images.Media.MIME_TYPE).append("=? or ")
                .append(MediaStore.Images.Media.MIME_TYPE).append("=? or ")
                .append(MediaStore.Images.Media.MIME_TYPE);
        String[] selectionValues;
        if (showGif) {
            selection.append("=? or ").append(MediaStore.Images.Media.MIME_TYPE).append("=?");
            selectionValues = new String[]{
                    "image/jpeg", "image/png", "image/jpg", "image/gif",
                    "image/x-ms-bmp", "image/webp"};
        } else {
            selection.append("=?");
            selectionValues = new String[]{
                    "image/jpeg", "image/png", "image/jpg",
                    "image/x-ms-bmp", "image/webp"};
        }
        Cursor cursor = makePhotoCursor(context, selection.toString(), selectionValues, MediaStore.Images.Media.DEFAULT_SORT_ORDER);
        return getPhotos(cursor);
    }

    @NonNull
    private static ArrayList<FileBean> getPhotos(@Nullable final Cursor cursor) {
        ArrayList<FileBean> songs = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                songs.add(getPhotoFromCursorImpl(cursor));
            } while (cursor.moveToNext());
        }

        if (cursor != null)
            cursor.close();
        return songs;
    }

    public static FileBean getPhotoFromCursorImpl(Cursor cursor) {
        FileBean photoBean = new FileBean();
        photoBean.setId(cursor.getInt(cursor.getColumnIndex(BaseColumns._ID)));
        photoBean.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.TITLE)));
        photoBean.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)));
        photoBean.setSize(cursor.getLong(cursor.getColumnIndex(MediaStore.Images.ImageColumns.SIZE)));
        photoBean.setMediaMimeType(MimeType.PHOTO);
        photoBean.setMimeType(cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.MIME_TYPE)));
        photoBean.setWidth(cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.WIDTH)));
        photoBean.setHeight(cursor.getInt(cursor.getColumnIndex(MediaStore.Images.ImageColumns.HEIGHT)));
        photoBean.setDateTaken(cursor.getLong(
                cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN)));
        photoBean.setDateAdded(cursor.getLong(
                cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_ADDED)) * 1000);
        photoBean.setDateModified(cursor.getLong(
                cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_MODIFIED)) * 1000);
        return photoBean;
    }

    @Nullable
    private static Cursor makePhotoCursor(@NonNull final Context context, @Nullable final String selection, final String[] selectionValues, final String sortOrder) {
        try {
            return context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    new String[]{
                            BaseColumns._ID,
                            MediaStore.Images.ImageColumns.TITLE,
                            MediaStore.Images.ImageColumns.DATA,
                            MediaStore.Images.ImageColumns.DATE_TAKEN,
                            MediaStore.Images.ImageColumns.DATE_ADDED,
                            MediaStore.Images.ImageColumns.DATE_MODIFIED,
                            MediaStore.Images.ImageColumns.SIZE,
                            MediaStore.Images.ImageColumns.MIME_TYPE,
                            MediaStore.Images.ImageColumns.WIDTH,
                            MediaStore.Images.ImageColumns.HEIGHT
                    }, selection, selectionValues, sortOrder);
        } catch (SecurityException e) {
            return null;
        }
    }
}
