package com.ppyy.photoselector.utils;

import android.content.Context;
import android.os.Environment;

import com.ppyy.photoselector.MimeType;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by NeuroAndroid on 2018/1/23.
 */

public class FileUtils {
    private static final String APP_NAME = "PhotoSelector";
    private static final String CAMERA_PATH = "/" + APP_NAME + "/CameraImage/";
    private static final String AUDIO_PATH = "/" + APP_NAME + "/Audio/";

    public static File createCameraFile(Context context, @MimeType.MediaMimeType int type) {
        return type == MimeType.AUDIO ?
                createMediaFile(context, AUDIO_PATH, type) :
                createMediaFile(context, CAMERA_PATH, type);
    }

    private static File createMediaFile(Context context, String parentPath, int type) {
        String state = Environment.getExternalStorageState();
        File rootDir = state.equals(Environment.MEDIA_MOUNTED) ?
                Environment.getExternalStorageDirectory() : context.getCacheDir();

        File folderDir = new File(rootDir.getAbsolutePath() + parentPath);
        if (!folderDir.exists() && folderDir.mkdirs()) {
            LogUtils.i("folderDir mkdirs success!");
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = APP_NAME + "_" + timeStamp + "";
        File tmpFile = null;
        switch (type) {
            case MimeType.PHOTO:
                tmpFile = new File(folderDir, fileName + ".jpeg");
                break;
            case MimeType.VIDEO:
                tmpFile = new File(folderDir, fileName + ".mp4");
                break;
            case MimeType.AUDIO:
                tmpFile = new File(folderDir, fileName + ".mp3");
                break;
        }
        if (tmpFile != null) LogUtils.e("folderDir path : " + folderDir.getAbsolutePath());
        return tmpFile;
    }
}
