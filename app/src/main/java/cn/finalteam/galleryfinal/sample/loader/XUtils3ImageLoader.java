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
import android.graphics.Bitmap;
import android.widget.ImageView;
import org.xutils.image.ImageOptions;
import org.xutils.x;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/2 下午6:54
 */
public class XUtils3ImageLoader implements cn.finalteam.galleryfinal.ImageLoader {
    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        ImageOptions options = new ImageOptions.Builder()
                .setLoadingDrawableId(cn.finalteam.galleryfinal.R.drawable.ic_gf_default_photo)
                .setFailureDrawableId(cn.finalteam.galleryfinal.R.drawable.ic_gf_default_photo)
                .setConfig(Bitmap.Config.RGB_565)
                .setSize(width, height)
                .setCrop(true)
                .setUseMemCache(false)
                .build();
        x.image().bind(imageView, "file://" + path, options);
    }

    @Override
    public void clearMemoryCache() {
    }
}
