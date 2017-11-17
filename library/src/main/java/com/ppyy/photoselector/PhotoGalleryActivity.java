package com.ppyy.photoselector;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import com.ppyy.photoselector.adapter.PreviewPagerAdapter;
import com.ppyy.photoselector.bean.FileBean;
import com.ppyy.photoselector.conf.PhotoSelectorConfig;
import com.ppyy.photoselector.interfaces.DisplayCallBack;
import com.ppyy.photoselector.utils.DataTransferStation;
import com.ppyy.photoselector.utils.StatusBarUtils;
import com.ppyy.photoselector.widget.PreviewViewPager;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/11/5.
 */

public class PhotoGalleryActivity extends AppCompatActivity
        implements ViewPager.OnPageChangeListener, DisplayCallBack {
    private AppBarLayout mAppBarLayout;
    private View mStatusBar;
    private Toolbar mToolbar;
    private PreviewViewPager mVpPreview;
    private ArrayList<FileBean> mImages;
    private FileBean mCurrentImage;
    private int mCurrentPosition;

    private SelectionOptions mOptions;
    private DataTransferStation mDataTransferStation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mOptions = SelectionOptions.getOptions();
        setTheme(mOptions.themeId);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gallery);
        StatusBarUtils.immersiveStatusBar(this);
        if (mOptions.supportDarkStatusBar) {
            StatusBarUtils.setStatusBarDarkMode(this, true);
        }
        mVpPreview = findViewById(R.id.vp_preview);
        mAppBarLayout = findViewById(R.id.app_bar);
        mStatusBar = findViewById(R.id.status_bar);
        mToolbar = findViewById(R.id.tool_bar);

        mStatusBar.getLayoutParams().height = StatusBarUtils.getStatusBarHeight(this);

        setSupportActionBar(mToolbar);
        setDisplayHomeAsUpEnabled();
        setToolbarTitle("");

        mDataTransferStation = DataTransferStation.getInstance();

        initViewPager();
    }

    private void initViewPager() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int position = extras.getInt("position");

            if (position == -1) {
                mImages = mDataTransferStation.getSelectedItems();
                position = 0;
            } else {
                mImages = mDataTransferStation.getItems();
            }

            PreviewPagerAdapter previewPagerAdapter = new PreviewPagerAdapter(getSupportFragmentManager(), mImages);
            mVpPreview.setAdapter(previewPagerAdapter);
            mVpPreview.addOnPageChangeListener(this);
            mVpPreview.setCurrentItem(position);
            onPageSelected(position);
        }
    }

    private void setDisplayHomeAsUpEnabled() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setToolbarTitle(CharSequence title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem checkBoxItem = menu.findItem(R.id.action_check_box);
        MenuItem doneItem = menu.findItem(R.id.action_done);

        checkBoxItem.setIcon(mCurrentImage.isSelected() ?
                R.drawable.ic_check : R.drawable.ic_uncheck);

        int selectedSize = mDataTransferStation.getSelectedItems().size();
        if (selectedSize == 0) {
            doneItem.setEnabled(false);
            doneItem.setTitle("完成(0/" + mOptions.maxSelectable + ")");
        } else {
            doneItem.setEnabled(true);
            doneItem.setTitle("完成(" + selectedSize + "/" + mOptions.maxSelectable + ")");
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photo_gallery, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
        } else if (itemId == R.id.action_check_box) {
            FileBean fileBean = mImages.get(mCurrentPosition);
            if (fileBean.isSelected()) {
                fileBean.setSelected(false);
                mDataTransferStation.removeFromSelectedItems(fileBean);
            } else {
                ArrayList<FileBean> selectedImages = mDataTransferStation.getSelectedItems();
                if (selectedImages.size() >= mOptions.maxSelectable) {
                    Toast.makeText(this, "最多可以选择" + mOptions.maxSelectable + "个文件", Toast.LENGTH_SHORT).show();
                } else {
                    fileBean.setSelected(true);
                    mDataTransferStation.putSelectedItem(fileBean);
                }
            }
            invalidateOptionsMenu();
        } else if (itemId == R.id.action_done) {
            sendBackResult(true);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        sendBackResult(false);
        super.onBackPressed();
    }

    private void sendBackResult(boolean apply) {
        Intent intent = new Intent();
        intent.putExtra(PhotoSelectorConfig.EXTRA_RESULT_APPLY, apply);
        setResult(RESULT_OK, intent);
    }

    @Override
    protected void onDestroy() {
        // DataTransferStation.getInstance().onDestroy();
        mDataTransferStation = null;
        super.onDestroy();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurrentPosition = position;
        mCurrentImage = mImages.get(position);
        setToolbarTitle((position + 1) + "/" + mImages.size());
        invalidateOptionsMenu();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void controlAppBarLayoutDisplay() {
        float translationY = mAppBarLayout.getTranslationY();
        if (translationY != 0) {
            showAppBarLayout();
        } else {
            hideAppBarLayout();
        }
    }

    private void showAppBarLayout() {
        ViewCompat.animate(mAppBarLayout)
                .translationY(0)
                .alpha(1f)
                .setDuration(500)
                .setInterpolator(new DecelerateInterpolator())
                .start();
    }

    private void hideAppBarLayout() {
        ViewCompat.animate(mAppBarLayout)
                .translationY(-mAppBarLayout.getHeight())
                .alpha(0f)
                .setDuration(500)
                .setInterpolator(new AccelerateDecelerateInterpolator())
                .start();
    }
}
