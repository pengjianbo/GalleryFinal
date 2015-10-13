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
import android.content.Intent;
import android.os.Environment;
import android.widget.Toast;
import cn.finalteam.toolsfinal.DeviceUtils;
import java.io.File;

/**
 * Desction:相册助手类
 * Author:pengjianbo
 * Date:15/10/10 下午5:24
 */
public class GalleryHelper {
    public static final int SINGLE_IMAGE = 1;
    public static final int MULTIPLE_IMAGE = 2;

    public static final int GALLERY_RESULT_SUCCESS = 1000;
    public static final int GALLERY_REQUEST_CODE = 1002;

    public static final int TAKE_REQUEST_CODE = 1001;

    public static final int CROP_SUCCESS = 1003;
    public static final int CROP_REQUEST_CODE = 1003;

    public static final String LIMIT = "limit";
    public static final String PICK_MODE = "pick_mode";
    public static final String RESULT_DATA = "result_data";
    public static final String RESULT_LIST_DATA = "result_list_data";
    public static final String CROP_PHOTO = "crop_photo";

    public static final String TAKE_PHOTO_FOLDER = "GalleryFinal" + File.separator;
    public static final String PHOTO_DIR = Environment.getExternalStorageDirectory() + "/GalleryFinal/photo/";

    public static ImageLoader mImageLoader;

    public static void openGallerySingle(Activity activity, ImageLoader imageLoader) {
        openGallerySingle(activity, false, imageLoader);
    }

    public static void openGallerySingle(Activity activity, boolean crop, ImageLoader imageLoader) {
        openGallery(activity, SINGLE_IMAGE, crop, 1, imageLoader);
    }

    public static void openGalleryMuti(Activity activity, int limit, ImageLoader imageLoader) {
        openGallery(activity, MULTIPLE_IMAGE, false, limit, imageLoader);
    }

    private static void openGallery(Activity activity, int picMode, boolean crop, int limit, ImageLoader imageLoader) {
        mImageLoader = imageLoader;

        if (!DeviceUtils.existSDCard()) {
            Toast.makeText(activity, "没有SD卡", Toast.LENGTH_SHORT).show();
        }

        if ( activity != null ) {
            Intent intent = new Intent(activity, PhotoChooseActivity.class);
            intent.putExtra(PICK_MODE, picMode);
            intent.putExtra(CROP_PHOTO, crop);
            intent.putExtra(LIMIT, limit);
            activity.startActivityForResult(intent, GALLERY_REQUEST_CODE);
        }
    }
}
