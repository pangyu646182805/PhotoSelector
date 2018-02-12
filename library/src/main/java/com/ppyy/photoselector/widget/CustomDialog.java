package com.ppyy.photoselector.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.ppyy.photoselector.R;

/**
 * Created by NeuroAndroid on 2018/1/3.
 */
public class CustomDialog extends Dialog {
    public CustomDialog(@NonNull Context context) {
        super(context, R.style.custom_dialog);
        setCancelable(true);
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_custom_dialog);
    }
}
