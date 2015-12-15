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

import android.app.Activity;
import android.os.Environment;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/2 上午10:45
 */
public class GalleryConfig {

    private boolean mutiSelect;
    private int maxSize;
    private boolean editPhoto;//编辑
    private boolean crop;//裁剪
    private boolean rotate;//选择
    private boolean showCamera;
    private int cropWidth;
    private int cropHeight;
    private boolean cropSquare;
    private ArrayList<String> selectedList;
    private ArrayList<String> filterList;//过滤器
    private File takePhotoFolder;
    private File editPhotoCacheFolder;
    private Activity activity;
    private ImageLoader imageLoader;

    private GalleryConfig(final Builder builder) {
        this.mutiSelect = builder.mutiSelect;
        this.maxSize = builder.maxSize;
        this.editPhoto = builder.editPhoto;
        this.crop = builder.crop;
        this.rotate = builder.rotate;
        this.showCamera = builder.showCamera;
        this.activity = builder.activity;
        this.imageLoader = builder.imageLoader;
        this.cropWidth = builder.cropWidth;
        this.cropHeight = builder.cropHeight;
        this.cropSquare = builder.cropSquare;
        this.selectedList = builder.selectedList;
        this.filterList = builder.filterList;
        this.takePhotoFolder = builder.takePhotoFolder;
        this.editPhotoCacheFolder = builder.editPhotoCacheFolder;

        if ( takePhotoFolder == null ) {
            takePhotoFolder = new File(Environment.getExternalStorageDirectory(), "/DCIM/" + "GalleryFinal" + File.separator);
        }

        if ( editPhotoCacheFolder == null ) {
            editPhotoCacheFolder = new File(Environment.getExternalStorageDirectory() + "/GalleryFinal/edittemp/");
        }
    }

    public static class Builder {
        private boolean mutiSelect;
        private int maxSize;
        private boolean editPhoto;//编辑
        private boolean crop;//裁剪
        private boolean rotate;//选择
        private boolean showCamera;
        private int cropWidth;
        private int cropHeight;
        private boolean cropSquare;
        private ArrayList<String> selectedList;
        private ArrayList<String> filterList;
        private File takePhotoFolder;
        private File editPhotoCacheFolder;

        private Activity activity;
        private ImageLoader imageLoader;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder mutiSelect() {
            this.mutiSelect = true;
            return this;
        }

        public Builder singleSelect() {
            this.mutiSelect = false;
            return this;
        }

        public Builder mutiSelectMaxSize(int maxSize) {
            this.maxSize = maxSize;
            return this;
        }

        public Builder enableEdit() {
            this.editPhoto = true;
            return this;
        }

        public Builder enableCrop() {
            this.crop = true;
            return this;
        }

        public Builder enableRotate() {
            this.rotate = true;
            return this;
        }

        public Builder showCamera() {
            this.showCamera = true;
            return this;
        }

        public Builder cropWidth(int width) {
            this.cropWidth = width;
            return this;
        }

        public Builder cropHeight(int height) {
            this.cropHeight = height;
            return this;
        }

        public Builder cropSquare() {
            this.cropSquare = true;
            return this;
        }

        public Builder selected(ArrayList<String> selectedList) {
            if (selectedList != null) {
                this.selectedList = (ArrayList<String>) selectedList.clone();
            }
            return this;
        }

        public Builder selected(Collection<PhotoInfo> selectedList) {
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

        public Builder filter(ArrayList<String> filterList) {
            if ( filterList != null ) {
                this.filterList = (ArrayList<String>) filterList.clone();
            }
            return this;
        }

        public Builder filter(Collection<PhotoInfo> filterList) {
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
         * 配置拍照缓存母驴
         * @param file
         * @return
         */
        public Builder setTakePhotoFolter(File file) {
            this.takePhotoFolder = file;
            return this;
        }

        /**
         * 配置编辑图片产生的文件缓存目录
         * @param file
         * @return
         */
        public Builder setEditPhotoCacheFolder(File file) {
            this.editPhotoCacheFolder = file;
            return this;
        }

        public Builder imageloader(ImageLoader imageLoader) {
            this.imageLoader = imageLoader;
            return this;
        }

        public GalleryConfig build() {
            return new GalleryConfig(this);
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

    public boolean isShowCamera() {
        return showCamera;
    }

    public Activity getActivity() {
        return activity;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public int getCropWidth() {
        return cropWidth;
    }

    public int getCropHeight() {
        return cropHeight;
    }

    public List<String> getSelectedList() {
        return selectedList;
    }

    public ArrayList<String> getFilterList() {
        return filterList;
    }

    public boolean isCropSquare() {
        return cropSquare;
    }

    public File getTakePhotoFolder() {
        return takePhotoFolder;
    }

    public File getEditPhotoCacheFolder() {
        return editPhotoCacheFolder;
    }
}
