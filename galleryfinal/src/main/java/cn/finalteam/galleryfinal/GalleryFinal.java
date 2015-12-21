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
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.galleryfinal.utils.Utils;
import cn.finalteam.toolsfinal.DeviceUtils;
import cn.finalteam.toolsfinal.FileUtils;
import cn.finalteam.toolsfinal.StringUtils;
import java.util.HashMap;

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
    private static GalleryTheme mGalleryTheme;

    public static GalleryConfig getGalleryConfig() {
        return mGalleryConfig;
    }

    public static void init(GalleryTheme galleryTheme) {
        mGalleryTheme = galleryTheme;
    }

    public static GalleryTheme getGalleryTheme() {
        if (mGalleryTheme== null) {
            //使用默认配置
            mGalleryTheme = GalleryTheme.DEFAULT;
        }
        return mGalleryTheme;
    }

    /**
     * 打开Gallery
     * @param config
     */
    public static void openGallery(GalleryConfig config) {
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
    }

    /**
     * 打开相机
     * @param config
     */
    public static void openCamera(GalleryConfig config) {
        if ( config == null ) {
            return;
        }
        config.mutiSelect = false;//拍照为单选
        mGalleryConfig = config;
        Intent intent = new Intent(config.getActivity(), PhotoEditActivity.class);
        intent.putExtra(PhotoEditActivity.TAKE_PHOTO_ACTION, true);
        config.getActivity().startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    /**
     * 打开裁剪
     * @param config
     * @param photoPath
     */
    public static void openCrop(GalleryConfig config, String photoPath) {
        if ( config == null || StringUtils.isEmpty(photoPath)) {
            return;
        }
        //必须设置这个三个选项
        config.mutiSelect = false;//拍照为单选
        config.editPhoto = true;
        config.crop = true;

        mGalleryConfig = config;
        HashMap<String, PhotoInfo> map = new HashMap<>();
        PhotoInfo photoInfo = new PhotoInfo();
        photoInfo.setPhotoPath(photoPath);
        photoInfo.setPhotoId(Utils.getRandom(10000, 99999));
        map.put(photoPath, photoInfo);
        Intent intent = new Intent(config.getActivity(), PhotoEditActivity.class);
        intent.putExtra(PhotoEditActivity.CROP_PHOTO_ACTION, true);
        intent.putExtra(PhotoEditActivity.SELECT_MAP, map);
        config.getActivity().startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    /**
     * 打开编辑
     * @param config
     * @param photoPath
     */
    public static void openEdit(GalleryConfig config, String photoPath) {
        if ( config == null || StringUtils.isEmpty(photoPath)) {
            return;
        }
        config.mutiSelect = false;//拍照为单选

        mGalleryConfig = config;
        HashMap<String, PhotoInfo> map = new HashMap<>();
        PhotoInfo photoInfo = new PhotoInfo();
        photoInfo.setPhotoPath(photoPath);
        photoInfo.setPhotoId(Utils.getRandom(10000, 99999));
        map.put(photoPath, photoInfo);
        Intent intent = new Intent(config.getActivity(), PhotoEditActivity.class);
        intent.putExtra(PhotoEditActivity.EDIT_PHOTO_ACTION, true);
        intent.putExtra(PhotoEditActivity.SELECT_MAP, map);
        config.getActivity().startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    /**
     * 清楚缓存文件
     */
    public static void cleanCacheFile() {
        if (mGalleryConfig != null && mGalleryConfig.getEditPhotoCacheFolder() != null) {
            //清楚裁剪冗余图片
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    FileUtils.deleteFile(mGalleryConfig.getEditPhotoCacheFolder());
                }
            }.start();
        }
    }
}
