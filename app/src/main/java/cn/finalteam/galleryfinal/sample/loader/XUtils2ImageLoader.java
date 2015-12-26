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

package cn.finalteam.galleryfinal.sample.loader;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.core.BitmapSize;

import cn.finalteam.galleryfinal.widget.GFImageView;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/2 下午6:44
 */
public class XUtils2ImageLoader implements cn.finalteam.galleryfinal.ImageLoader {

    private BitmapUtils bitmapUtils;

    public XUtils2ImageLoader(Context context) {
        bitmapUtils = new BitmapUtils(context);
    }

    @Override
    public void displayImage(Activity activity, String path, GFImageView imageView, Drawable defaultDrawable, int width, int height) {
        BitmapDisplayConfig config = new BitmapDisplayConfig();
        config.setLoadFailedDrawable(defaultDrawable);
        config.setLoadingDrawable(defaultDrawable);
        config.setBitmapConfig(Bitmap.Config.RGB_565);
        config.setBitmapMaxSize(new BitmapSize(width, height));
        bitmapUtils.display(imageView, "file://" + path, config);
    }

    @Override
    public void clearMemoryCache() {
    }
}
