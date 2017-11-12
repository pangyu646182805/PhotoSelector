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
 * Created by NeuroAndroid on 2017/11/1.
 */

public class AudioLoader {
    @NonNull
    public static ArrayList<FileBean> getAllAudios(@NonNull Context context) {
        Cursor cursor = makePhotoCursor(context, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        return getAudios(cursor);
    }

    @NonNull
    private static ArrayList<FileBean> getAudios(@Nullable final Cursor cursor) {
        ArrayList<FileBean> songs = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                songs.add(getAudioFromCursorImpl(cursor));
            } while (cursor.moveToNext());
        }

        if (cursor != null)
            cursor.close();
        return songs;
    }

    private static FileBean getAudioFromCursorImpl(Cursor cursor) {
        FileBean audioBean = new FileBean();
        audioBean.setId(cursor.getInt(cursor.getColumnIndex(BaseColumns._ID)));
        audioBean.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.TITLE)));
        audioBean.setPath(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA)));
        audioBean.setSize(cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.SIZE)));
        audioBean.setMimeType(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.MIME_TYPE)));
        audioBean.setDuration(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION)));
        audioBean.setAlbumId(cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_ID)));

        audioBean.setMediaMimeType(MimeType.AUDIO);
        audioBean.setDateAdded(cursor.getLong(
                cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATE_ADDED)) * 1000);
        audioBean.setDateModified(cursor.getLong(
                cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATE_MODIFIED)) * 1000);
        return audioBean;
    }

    @Nullable
    private static Cursor makePhotoCursor(@NonNull final Context context, @Nullable final String selection, final String[] selectionValues, final String sortOrder) {
        try {
            return context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    new String[]{
                            BaseColumns._ID,
                            MediaStore.Audio.AudioColumns.TITLE,
                            MediaStore.Audio.AudioColumns.DATA,
                            MediaStore.Audio.AudioColumns.DATE_ADDED,
                            MediaStore.Audio.AudioColumns.DATE_MODIFIED,
                            MediaStore.Audio.AudioColumns.SIZE,
                            MediaStore.Audio.AudioColumns.MIME_TYPE,
                            MediaStore.Audio.AudioColumns.DURATION,
                            MediaStore.Audio.AudioColumns.ALBUM_ID
                    }, selection, selectionValues, sortOrder);
        } catch (SecurityException e) {
            return null;
        }
    }
}
