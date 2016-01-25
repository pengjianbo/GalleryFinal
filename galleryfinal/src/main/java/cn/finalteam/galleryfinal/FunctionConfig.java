/*
 * Copyright (C) 2014 pengjianbo(pengjianbosoft@gmail.com), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package cn.finalteam.galleryfinal;

import android.support.annotation.IntRange;

import cn.finalteam.galleryfinal.model.PhotoInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/2 上午10:45
 */
public class FunctionConfig implements Cloneable{

    protected boolean mutiSelect;
    protected int maxSize;
    protected boolean editPhoto;//编辑
    protected boolean crop;//裁剪
    private boolean rotate;//旋转
    private boolean camera;
    private int cropWidth;
    private int cropHeight;
    private boolean cropSquare;
    private boolean rotateReplaceSource;//旋转是否覆盖源文件
    private boolean cropReplaceSource;//裁剪是否覆盖源文件
    private boolean forceCrop;//强制裁剪
    private boolean forceCropEdit;//强制裁剪后是否可对图片编辑，默认不可以
    private boolean preview;//预览
    private ArrayList<String> selectedList;
    private ArrayList<String> filterList;//过滤器

    private FunctionConfig(final Builder builder) {
        this.mutiSelect = builder.mutiSelect;
        this.maxSize = builder.maxSize;
        this.editPhoto = builder.editPhoto;
        this.crop = builder.crop;
        this.rotate = builder.rotate;
        this.camera = builder.camera;
        this.cropWidth = builder.cropWidth;
        this.cropHeight = builder.cropHeight;
        this.cropSquare = builder.cropSquare;
        this.selectedList = builder.selectedList;
        this.filterList = builder.filterList;
        this.rotateReplaceSource = builder.rotateReplaceSource;
        this.cropReplaceSource = builder.cropReplaceSource;
        this.forceCrop = builder.forceCrop;
        this.forceCropEdit = builder.forceCropEdit;
        this.preview = builder.preview;
    }

    public static class Builder {
        private boolean mutiSelect;
        private int maxSize;
        private boolean editPhoto;//编辑
        private boolean crop;//裁剪
        private boolean rotate;//旋转
        private boolean camera;
        private int cropWidth;
        private int cropHeight;
        private boolean cropSquare;
        private boolean rotateReplaceSource;//旋转是否覆盖源文件
        private boolean cropReplaceSource;//裁剪是否覆盖源文件
        private ArrayList<String> selectedList;
        private ArrayList<String> filterList;
        private boolean forceCrop;//强制裁剪
        private boolean forceCropEdit;//强制裁剪后是否可对图片编辑，默认不可以
        private boolean preview;//预览

        protected Builder setMutiSelect(boolean mutiSelect) {
            this.mutiSelect = mutiSelect;
            return this;
        }

        public Builder setMutiSelectMaxSize(@IntRange(from = 1, to = Integer.MAX_VALUE) int maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        public Builder setEnableEdit(boolean enable) {
            this.editPhoto = enable;
            return this;
        }

        public Builder setEnableCrop(boolean enable) {
            this.crop = enable;
            return this;
        }

        public Builder setEnableRotate(boolean enable) {
            this.rotate = enable;
            return this;
        }

        public Builder setEnableCamera(boolean enable) {
            this.camera = enable;
            return this;
        }

        public Builder setCropWidth(@IntRange(from = 1, to = Integer.MAX_VALUE)int width) {
            this.cropWidth = width;
            return this;
        }

        public Builder setCropHeight(@IntRange(from = 1, to = Integer.MAX_VALUE)int height) {
            this.cropHeight = height;
            return this;
        }

        public Builder setCropSquare(boolean enable) {
            this.cropSquare = enable;
            return this;
        }

        public Builder setSelected(ArrayList<String> selectedList) {
            if (selectedList != null) {
                this.selectedList = (ArrayList<String>) selectedList.clone();
            }
            return this;
        }

        public Builder setSelected(Collection<PhotoInfo> selectedList) {
            if ( selectedList != null ) {
                ArrayList<String> list = new ArrayList<>();
                for(PhotoInfo info:selectedList) {
                    if (info != null) {
                        list.add(info.getPhotoPath());
                    }
                }

                this.selectedList = list;
            }
            return this;
        }

        public Builder setFilter(ArrayList<String> filterList) {
            if ( filterList != null ) {
                this.filterList = (ArrayList<String>) filterList.clone();
            }
            return this;
        }

        public Builder setFilter(Collection<PhotoInfo> filterList) {
            if ( filterList != null ) {
                ArrayList<String> list = new ArrayList<>();
                for(PhotoInfo info:filterList) {
                    if (info != null) {
                        list.add(info.getPhotoPath());
                    }
                }

                this.filterList = list;
            }
            return this;
        }

        /**
         * 设置旋转后是否替换原图
         * @param rotateReplaceSource
         * @return
         */
        public Builder setRotateReplaceSource(boolean rotateReplaceSource) {
            this.rotateReplaceSource = rotateReplaceSource;
            return this;
        }

        /**
         * 设置裁剪后是否替换原图
         * @param cropReplaceSource
         * @return
         */
        public Builder setCropReplaceSource(boolean cropReplaceSource) {
            this.cropReplaceSource = cropReplaceSource;
            return this;
        }

        /**
         * 强制裁剪
         * @param forceCrop
         * @return
         */
        public Builder setForceCrop(boolean forceCrop) {
            this.forceCrop = forceCrop;
            return this;
        }

        /**
         * 强制裁剪后是否可以对图片编辑，默认不可编辑
         * @param forceCropEdit
         * @return
         */
        public Builder setForceCropEdit(boolean forceCropEdit) {
            this.forceCropEdit = forceCropEdit;
            return this;
        }

        /**
         * 是否开启预览功能
         * @param preview
         * @return
         */
        public Builder setEnablePreview(boolean preview) {
            this.preview = preview;
            return this;
        }

        public FunctionConfig build() {
            return new FunctionConfig(this);
        }
    }

    public boolean isMutiSelect() {
        return mutiSelect;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public boolean isEditPhoto() {
        return editPhoto;
    }

    public boolean isCrop() {
        return crop;
    }

    public boolean isRotate() {
        return rotate;
    }

    public boolean isCamera() {
        return camera;
    }

    public int getCropWidth() {
        return cropWidth;
    }

    public int getCropHeight() {
        return cropHeight;
    }

    public boolean isCropSquare() {
        return cropSquare;
    }

    public boolean isRotateReplaceSource() {
        return rotateReplaceSource;
    }

    public boolean isCropReplaceSource() {
        return cropReplaceSource;
    }

    public boolean isForceCrop() {
        return forceCrop;
    }

    public boolean isForceCropEdit() {
        return forceCropEdit;
    }

    public ArrayList<String> getSelectedList() {
        return selectedList;
    }

    public ArrayList<String> getFilterList() {
        return filterList;
    }

    public boolean isEnablePreview(){
        return preview;
    }

    @Override
    public FunctionConfig clone() {
        FunctionConfig o = null;
        try {
            o = (FunctionConfig) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }
}
