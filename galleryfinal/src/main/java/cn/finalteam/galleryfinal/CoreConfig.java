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

import android.content.Context;
import android.os.Environment;
import java.io.File;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/27 下午1:41
 */
public class CoreConfig {
    private boolean debug;
    private Context context;
    private ImageLoader imageLoader;
    private File takePhotoFolder;
    private File editPhotoCacheFolder;
    private GalleryTheme galleryTheme;
    private FunctionConfig mFunctionConfig;

    private CoreConfig(Builder builder) {
        this.debug = builder.debug;
        this.context = builder.context;
        this.imageLoader = builder.imageLoader;
        this.takePhotoFolder = builder.takePhotoFolder;
        this.editPhotoCacheFolder = builder.editPhotoCacheFolder;
        this.galleryTheme = builder.galleryTheme;
        this.mFunctionConfig = builder.mFunctionConfig;

        if ( takePhotoFolder == null ) {
            takePhotoFolder = new File(Environment.getExternalStorageDirectory(), "/DCIM/" + "GalleryFinal" + File.separator);
        }

        if ( editPhotoCacheFolder == null ) {
            editPhotoCacheFolder = new File(Environment.getExternalStorageDirectory() + "/GalleryFinal/edittemp/");
        }
    }

    public static class Builder {
        private Context context;
        private GalleryTheme galleryTheme;
        private boolean debug;
        private ImageLoader imageLoader;
        private File takePhotoFolder;//配置拍照缓存目录
        private File editPhotoCacheFolder;//配置编辑图片产生的文件缓存目录
        private FunctionConfig mFunctionConfig;

        public Builder(Context context, ImageLoader imageLoader, GalleryTheme galleryTheme) {
            this.context = context;
            this.imageLoader = imageLoader;
            this.galleryTheme = galleryTheme;
        }

        public Builder setDebug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public Builder setTakePhotoFolder(File takePhotoFolder) {
            this.takePhotoFolder = takePhotoFolder;
            return this;
        }

        public Builder setEditPhotoCacheFolder(File editPhotoCacheFolder) {
            this.editPhotoCacheFolder = editPhotoCacheFolder;
            return this;
        }

        public Builder setFunctionConfig(FunctionConfig FunctionConfig) {
            this.mFunctionConfig = FunctionConfig;
            return this;
        }

        public CoreConfig build() {
            return new CoreConfig(this);
        }
    }

    public boolean isDebug() {
        return debug;
    }

    public Context getContext() {
        return context;
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public File getTakePhotoFolder() {
        return takePhotoFolder;
    }

    public File getEditPhotoCacheFolder() {
        return editPhotoCacheFolder;
    }

    public GalleryTheme getGalleryTheme() {
        return galleryTheme;
    }

    public FunctionConfig getFunctionConfig() {
        return mFunctionConfig;
    }

}
