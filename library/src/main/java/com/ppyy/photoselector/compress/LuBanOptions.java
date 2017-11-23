package com.ppyy.photoselector.compress;

import java.io.Serializable;

/**
 * Created by NeuroAndroid on 2017/11/23.
 */

public class LuBanOptions implements Serializable {
    /**
     * 压缩到的最大大小，单位B
     */
    private int maxSize;
    private int maxHeight;
    private int maxWidth;
    private int grade;

    private LuBanOptions() {
    }

    public int getGrade() {
        if (grade == 0) {
            return LuBan.CUSTOM_GEAR;
        }
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public static class Builder {
        private LuBanOptions options;

        public Builder() {
            options = new LuBanOptions();
        }

        public Builder setMaxSize(int maxSize) {
            options.setMaxSize(maxSize);
            return this;
        }

        public Builder setGrade(int grade) {
            options.setGrade(grade);
            return this;
        }

        public Builder setMaxHeight(int maxHeight) {
            options.setMaxHeight(maxHeight);
            return this;
        }

        public Builder setMaxWidth(int maxWidth) {
            options.setMaxWidth(maxWidth);
            return this;
        }

        public LuBanOptions create() {
            return options;
        }
    }
}
