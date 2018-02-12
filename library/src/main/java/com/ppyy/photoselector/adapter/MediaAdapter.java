package com.ppyy.photoselector.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ppyy.photoselector.MimeType;
import com.ppyy.photoselector.R;
import com.ppyy.photoselector.SelectionOptions;
import com.ppyy.photoselector.bean.FileBean;

import java.util.ArrayList;

/**
 * Created by NeuroAndroid on 2017/11/1.
 */

public class MediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_MEDIA_HEADER = 0;
    private static final int VIEW_TYPE_MEDIA_CONTENT = 1;
    private final int mSelectedColor;
    private final int mUnselectedColor;

    private int mMaxSelectable;

    private RecyclerView mRecyclerView;

    private OnItemSelectedListener mOnItemSelectedListener;
    private OnItemClickListener mOnItemClickListener;

    private final Context mContext;
    private ArrayList<FileBean> mMediaList = new ArrayList<>();
    private SelectionOptions mOptions;
    private int mImageResize;

    private final SparseBooleanArray mSelectedStates = new SparseBooleanArray();

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        mOnItemSelectedListener = onItemSelectedListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public MediaAdapter(Context context, RecyclerView recyclerView) {
        mContext = context;
        mRecyclerView = recyclerView;
        clearRvAnim(mRecyclerView);
        mOptions = SelectionOptions.getOptions();
        mMaxSelectable = mOptions.maxSelectable;

        mImageResize = mContext.getResources().getDisplayMetrics().widthPixels / mOptions.gridSize;

        TypedArray ta = mContext.getTheme().obtainStyledAttributes(
                new int[]{R.attr.selected_checkBox_colorFilter, R.attr.unselected_checkBox_colorFilter});
        mSelectedColor = ta.getColor(0, 0);
        mUnselectedColor = ta.getColor(1, 0);
        ta.recycle();
    }

    /**
     * 去除RecyclerView item刷新动画
     */
    public void clearRvAnim(RecyclerView rv) {
        if (rv == null)
            return;
        RecyclerView.ItemAnimator animator = rv.getItemAnimator();
        if (animator instanceof DefaultItemAnimator) {
            ((DefaultItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        rv.getItemAnimator().setChangeDuration(333);
        rv.getItemAnimator().setMoveDuration(333);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_MEDIA_HEADER) {
            final View mediaHeaderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_media_header, parent, false);
            final MediaHeaderViewHolder mediaHeaderViewHolder = new MediaHeaderViewHolder(mediaHeaderView);
            mediaHeaderView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mSelectedStates.size() >= mMaxSelectable) {
                        Toast.makeText(mContext, "最多可以选择" + mMaxSelectable + "个文件", Toast.LENGTH_SHORT).show();
                    } else {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick(mediaHeaderViewHolder, mediaHeaderViewHolder.getLayoutPosition(), null);
                        }
                    }
                }
            });
            return mediaHeaderViewHolder;
        } else {
            /*return new MediaContentViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(R.layout.item_media_content, parent, false));*/
            View mediaContentView = LayoutInflater.from(mContext).inflate(R.layout.item_media_content, parent, false);
            final MediaContentViewHolder mediaContentViewHolder = new MediaContentViewHolder(mediaContentView);
            mediaContentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = mediaContentViewHolder.getLayoutPosition();
                    if (mOptions.previewPhoto) {
                        if (mOnItemClickListener != null) {
                            mOnItemClickListener.onItemClick(mediaContentViewHolder, position, getItem(position));
                        }
                    } else {
                        performClick(mediaContentViewHolder, position);
                    }
                }
            });
            return mediaContentViewHolder;
        }
    }

    private void performClick(RecyclerView.ViewHolder viewHolder, int layoutPosition) {
        final FileBean item = getItem(layoutPosition);
        handle(viewHolder, layoutPosition, item);
    }

    private void handle(RecyclerView.ViewHolder viewHolder, int position, FileBean item) {
        if (item == null)
            return;
        boolean selected = mSelectedStates.get(position, false);
        if (mMaxSelectable == 1) {
            // 单选
            if (selected) {
                mSelectedStates.delete(position);
                item.setSelected(false);

                if (mOnItemSelectedListener != null) {
                    mOnItemSelectedListener.onItemSelected(viewHolder, position, false, item);
                }
            } else {
                if (mSelectedStates.size() == 1) {
                    int prePosition = mSelectedStates.keyAt(0);
                    getItem(prePosition).setSelected(false);
                    mSelectedStates.delete(prePosition);
                    notifyItemChanged(prePosition);

                    if (mOnItemSelectedListener != null) {
                        mOnItemSelectedListener.onItemSelected(viewHolder, prePosition, false, getItem(prePosition));
                    }
                }

                mSelectedStates.put(position, true);
                item.setSelected(true);
            }
        } else {
            // 多选
            if (mSelectedStates.size() >= mMaxSelectable && !selected) {
                Toast.makeText(mContext, "最多可以选择" + mMaxSelectable + "个文件", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selected) {
                mSelectedStates.delete(position);
                item.setSelected(false);
            } else {
                mSelectedStates.put(position, true);
                item.setSelected(true);
            }
        }
        notifyItemChanged(position);

        if (mOnItemSelectedListener != null) {
            mOnItemSelectedListener.onItemSelected(viewHolder, position, !selected, item);
        }
    }

    public void resetSelectedStates(ArrayList<FileBean> selectedItems) {
        mSelectedStates.clear();
        if (selectedItems != null && !selectedItems.isEmpty()) {
            for (FileBean selectedBean : selectedItems) {
                int selectedPosition = indexOfMediaList(selectedBean);
                if (selectedPosition != -1) {
                    FileBean item = getItem(selectedPosition);
                    if (item != null) {
                        item.setSelected(true);
                    }
                    mSelectedStates.put(selectedPosition, true);
                }
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_MEDIA_HEADER) {
            MediaHeaderViewHolder mediaHeaderViewHolder = (MediaHeaderViewHolder) holder;
            if (mOptions.mimeType == MimeType.AUDIO) {
                mediaHeaderViewHolder.ivHeader.setImageResource(R.drawable.ic_recording);
                mediaHeaderViewHolder.tvTitle.setText(R.string.take_recording);
            } else {
                mediaHeaderViewHolder.ivHeader.setImageResource(R.drawable.ic_photo_camera);
                if (mOptions.mimeType == MimeType.ALL) {
                    mediaHeaderViewHolder.tvTitle.setText(R.string.shoot_photo);
                } else if (mOptions.mimeType == MimeType.PHOTO) {
                    mediaHeaderViewHolder.tvTitle.setText(R.string.take_photo);
                } else {
                    mediaHeaderViewHolder.tvTitle.setText(R.string.take_camera);
                }
            }
        } else {
            MediaContentViewHolder mediaContentViewHolder = (MediaContentViewHolder) holder;
            FileBean fileBean = getItem(position);
            if (null != fileBean) {
                /*Sketch.with(mContext)
                        .display(fileBean.getPath(), mediaContentViewHolder.mIvImg)
                        .maxSize((int) UIUtils.getDimen(R.dimen.x720) / 4, (int) UIUtils.getDimen(R.dimen.x720) / 4)
                        .resize((int) UIUtils.getDimen(R.dimen.x720) / 4, (int) UIUtils.getDimen(R.dimen.x720) / 4)
                        .cacheProcessedImageInDisk()
                        .loadingImage(R.drawable.img_loading)
                        .displayer(new TransitionImageDisplayer())
                        .thumbnailMode()
                        .commit();*/
                mediaContentViewHolder.ivCheck.setImageResource(
                        fileBean.isSelected() ? R.drawable.ic_check : R.drawable.ic_uncheck);
                mediaContentViewHolder.ivCheck.setColorFilter(fileBean.isSelected() ? mSelectedColor : mUnselectedColor);
                mediaContentViewHolder.image.setColorFilter(fileBean.isSelected() ?
                        ContextCompat.getColor(mContext, R.color.image_overlay_true) :
                        ContextCompat.getColor(mContext, R.color.image_overlay_false), PorterDuff.Mode.SRC_ATOP);
                int mediaMimeType = fileBean.getMediaMimeType();
                if (mediaMimeType == MimeType.PHOTO) {
                    mediaContentViewHolder.llBottom.setVisibility(View.GONE);
                    if (mOptions.showGif && mOptions.showGifFlag && "image/gif".equals(fileBean.getMimeType())) {
                        mediaContentViewHolder.ivGifFlag.setVisibility(View.VISIBLE);
                        mediaContentViewHolder.ivGifFlag.setImageResource(mOptions.gifFlagResId);
                    } else {
                        mediaContentViewHolder.ivGifFlag.setVisibility(View.GONE);
                    }
                } else {
                    mediaContentViewHolder.llBottom.setVisibility(View.VISIBLE);
                    mediaContentViewHolder.tvDuration.setText(formatVideoDuration(fileBean.getDuration()));
                    if (mediaMimeType == MimeType.VIDEO) {
                        mediaContentViewHolder.ivType.setImageResource(R.drawable.ic_video);
                    } else if (mediaMimeType == MimeType.AUDIO) {
                        mediaContentViewHolder.ivType.setImageResource(R.drawable.ic_audio);
                    }
                }
                mOptions.viewHolderCreator.onBindViewHolder(mediaContentViewHolder.image, fileBean);
            }
        }
    }

    public FileBean getItem(int position) {
        if (mOptions.showHeaderItem) {
            return mMediaList.get(position - 1);
        } else {
            return mMediaList.get(position);
        }
    }

    private int indexOfMediaList(FileBean item) {
        int index = mMediaList.indexOf(item);
        if (index == -1) return index;
        if (mOptions.showHeaderItem) {
            return index + 1;
        } else {
            return index;
        }
    }

    /**
     * 获取预览列表
     */
    public ArrayList<FileBean> getPreviewList() {
        if (mOptions.mimeType == MimeType.AUDIO) {
            // 音频不支持预览
            return null;
        }
        return mMediaList;
    }

    /**
     * 将long类型的时间转换成01:22:12
     */
    @SuppressLint("DefaultLocale")
    private String formatVideoDuration(long duration) {
        int HOUR = 1000 * 60 * 60;
        int MINUTE = 1000 * 60;
        int SECOND = 1000;

        int remainTime;
        int hour = (int) (duration / HOUR);
        remainTime = (int) (duration % HOUR);

        int minute = remainTime / MINUTE;
        remainTime = remainTime % MINUTE;

        int second = remainTime / SECOND;

        if (hour == 0) {
            return String.format("%02d:%02d", minute, second);
        } else {
            return String.format("%02d:%02d:%02d", hour, minute, second);
        }
    }

    @Override
    public int getItemCount() {
        if (mOptions.showHeaderItem) {
            return 1 + mMediaList.size();
        } else {
            return mMediaList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mOptions.showHeaderItem) {
            if (position == 0) {
                return VIEW_TYPE_MEDIA_HEADER;
            } else {
                return VIEW_TYPE_MEDIA_CONTENT;
            }
        } else {
            return VIEW_TYPE_MEDIA_CONTENT;
        }
    }

    public void setMediaList(ArrayList<FileBean> data) {
        this.mMediaList = data;
        notifyDataSetChanged();
    }

    public void putFileBean(int index, FileBean fileBean) {
        if (index >= 0 && fileBean != null) {
            mMediaList.add(index, fileBean);
            notifyItemInserted(index);
        }
    }

    public class MediaHeaderViewHolder extends RecyclerView.ViewHolder {
        private AppCompatImageView ivHeader;
        private AppCompatTextView tvTitle;

        MediaHeaderViewHolder(View itemView) {
            super(itemView);
            ivHeader = itemView.findViewById(R.id.iv_header);
            tvTitle = itemView.findViewById(R.id.tv_title);
        }
    }

    public class MediaContentViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        AppCompatImageView ivCheck;
        AppCompatImageView ivGifFlag;
        LinearLayout llBottom;
        AppCompatImageView ivType;
        TextView tvDuration;

        MediaContentViewHolder(View itemView) {
            super(itemView);
            ViewStub viewStub = itemView.findViewById(R.id.view_stub);
            image = mOptions.viewHolderCreator.inflateViewStub(viewStub);
            ViewGroup.LayoutParams layoutParams = image.getLayoutParams();
            if (layoutParams != null) {
                layoutParams.width = mImageResize;
                layoutParams.height = mImageResize;
            }

            ivCheck = itemView.findViewById(R.id.iv_check);
            ivGifFlag = itemView.findViewById(R.id.iv_gif_flag);
            llBottom = itemView.findViewById(R.id.ll_bottom);
            ivType = itemView.findViewById(R.id.iv_type);
            tvDuration = itemView.findViewById(R.id.tv_duration);
            if (mOptions.previewPhoto) {
                LinearLayout llCheck = itemView.findViewById(R.id.ll_check);
                llCheck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        performClick(MediaContentViewHolder.this, getLayoutPosition());
                    }
                });
            }
        }
    }

    public interface OnItemSelectedListener {
        void onItemSelected(RecyclerView.ViewHolder viewHolder, int position, boolean isSelected, FileBean item);
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerView.ViewHolder viewHolder, int position, FileBean fileBean);
    }
}
