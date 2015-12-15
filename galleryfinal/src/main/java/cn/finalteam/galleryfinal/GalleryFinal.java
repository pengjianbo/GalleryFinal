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

import android.content.Intent;
import android.widget.Toast;
import cn.finalteam.toolsfinal.DeviceUtils;
import cn.finalteam.toolsfinal.FileUtils;
import java.io.File;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/2 上午11:05
 */
public class GalleryFinal {
    public static final int GALLERY_RESULT_SUCCESS = 1000;
    public static final int GALLERY_REQUEST_CODE = 1002;
    public static final String GALLERY_RESULT_LIST_DATA = "gallery_result_list_data";

    static final int TAKE_REQUEST_CODE = 1001;

    static final int EDIT_OK = 1003;
    static final int EDIT_REQUEST_CODE = 1003;

    private static GalleryConfig mGalleryConfig;

    public static GalleryConfig getGalleryConfig() {
        return mGalleryConfig;
    }

    public static void open(GalleryConfig config) {
        if ( config == null ) {
            return;
        }
        if ( config.getImageLoader() == null ) {
            Toast.makeText(config.getActivity(), R.string.open_gallery_fail, Toast.LENGTH_SHORT).show();
            return;
        }

        if (!DeviceUtils.existSDCard()) {
            Toast.makeText(config.getActivity(), R.string.empty_sdcard, Toast.LENGTH_SHORT).show();
            return;
        }

        mGalleryConfig = config;

        Intent intent = new Intent(config.getActivity(), PhotoSelectActivity.class);
        config.getActivity().startActivityForResult(intent, GALLERY_REQUEST_CODE);

        //定时清理文件
        cleanTempFile();

    }

    /**
     * 清楚缓存文件
     */
    public static void clearCacheFile() {
        if (mGalleryConfig != null && mGalleryConfig.getEditPhotoCacheFolder() != null) {
            //清楚裁剪冗余图片
            FileUtils.deleteFile(mGalleryConfig.getEditPhotoCacheFolder());
        }
    }

    private static void cleanTempFile() {
        if (mGalleryConfig == null || mGalleryConfig.getEditPhotoCacheFolder() == null) {
            return;
        }

        File file = mGalleryConfig.getEditPhotoCacheFolder();
        if ( file.exists() ) {
            File []files = file.listFiles();
            if ( files != null && files.length > 0 ) {
                for (File f : files) {
                    long t = f.lastModified();
                    long curTime = System.currentTimeMillis();
                    if (t == 0l && (curTime - t) > 86400000) {
                        try {
                            f.delete();
                        } catch (Exception e){}
                    }
                }
            }
        }
    }

}
