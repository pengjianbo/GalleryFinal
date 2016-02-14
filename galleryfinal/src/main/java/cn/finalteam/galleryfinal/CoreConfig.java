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
import android.widget.AbsListView;

import java.io.File;
import java.io.Serializable;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/27 下午1:41
 */
public class CoreConfig {
    private Context context;
    private ImageLoader imageLoader;
    private File takePhotoFolder;
    private File editPhotoCacheFolder;
    private ThemeConfig themeConfig;
    private FunctionConfig functionConfig;
    private int animRes;
    private AbsListView.OnScrollListener onScrollListener;

    private CoreConfig(Builder builder) {
        this.context = builder.context;
        this.imageLoader = builder.imageLoader;
        this.takePhotoFolder = builder.takePhotoFolder;
        this.editPhotoCacheFolder = builder.editPhotoCacheFolder;
        this.themeConfig = builder.themeConfig;
        this.functionConfig = builder.functionConfig;
        if(builder.noAnimcation) {
            this.animRes = -1;
        } else {
            this.animRes = builder.animRes;
        }
        this.onScrollListener = builder.onScrollListener;

        if ( takePhotoFolder == null ) {
            takePhotoFolder = new File(Environment.getExternalStorageDirectory(), "/DCIM/" + "GalleryFinal/");
        }
        if(!takePhotoFolder.exists()) {
            takePhotoFolder.mkdirs();
        }

        if ( editPhotoCacheFolder == null ) {
            editPhotoCacheFolder = new File(Environment.getExternalStorageDirectory() + "/GalleryFinal/edittemp/");
        }
        if (!editPhotoCacheFolder.exists()) {
            editPhotoCacheFolder.mkdirs();
        }
    }

    public static class Builder {
        private Context context;
        private ThemeConfig themeConfig;
        private ImageLoader imageLoader;
        private File takePhotoFolder;//配置拍照缓存目录
        private File editPhotoCacheFolder;//配置编辑图片产生的文件缓存目录
        private FunctionConfig functionConfig;
        private int animRes;
        private boolean noAnimcation;
        private AbsListView.OnScrollListener onScrollListener;

        public Builder(Context context, ImageLoader imageLoader, ThemeConfig themeConfig) {
            this.context = context;
            this.imageLoader = imageLoader;
            this.themeConfig = themeConfig;
            this.animRes = R.anim.gf_flip_horizontal_in;
        }

        public Builder setTakePhotoFolder(File takePhotoFolder) {
            this.takePhotoFolder = takePhotoFolder;
            return this;
        }

        public Builder setEditPhotoCacheFolder(File editPhotoCacheFolder) {
            this.editPhotoCacheFolder = editPhotoCacheFolder;
            return this;
        }

        public Builder setFunctionConfig(FunctionConfig functionConfig) {
            this.functionConfig = functionConfig;
            return this;
        }

        public Builder setAnimation(int animRes) {
            this.animRes = animRes;
            return this;
        }

        /**
         *  禁止动画
         * @return
         */
        public Builder setNoAnimcation(boolean noAnimcation) {
            this.noAnimcation = noAnimcation;
            return this;
        }

        /**
         * 添加滑动事件用于优化图片加载，只有停止滑动了才去加载图片
         * @param listener
         * @return
         */
        public Builder setPauseOnScrollListener(AbsListView.OnScrollListener listener) {
            this.onScrollListener = listener;
            return this;
        }

        public CoreConfig build() {
            return new CoreConfig(this);
        }
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

    public int getAnimation() {
        return animRes;
    }

    public ThemeConfig getThemeConfig() {
        return themeConfig;
    }

    public FunctionConfig getFunctionConfig() {
        return functionConfig;
    }

    AbsListView.OnScrollListener getPauseOnScrollListener() {
        return onScrollListener;
    }
}
