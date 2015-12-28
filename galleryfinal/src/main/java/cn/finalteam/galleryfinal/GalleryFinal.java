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
import android.util.SparseArray;
import android.widget.Toast;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.galleryfinal.utils.Utils;
import cn.finalteam.toolsfinal.DeviceUtils;
import cn.finalteam.toolsfinal.FileUtils;
import cn.finalteam.toolsfinal.Logger;
import cn.finalteam.toolsfinal.StringUtils;
import java.io.File;
import java.util.HashMap;
import java.util.List;

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

    private static FunctionConfig mGlobalFunctionConfig;
    private static GalleryTheme mGalleryTheme;
    private static CoreConfig mCoreConfig;
    private static SparseArray<OnHanlderResultCallback> mCallbackMap;


    public static void init(CoreConfig coreConfig) {
        mGalleryTheme = coreConfig.getGalleryTheme();
        mCoreConfig = coreConfig;
        mGlobalFunctionConfig = coreConfig.getFunctionConfig();
        Logger.init("galleryfinal", coreConfig.isDebug());
    }

    public static FunctionConfig copyGlobalFuncationConfig() {
        if ( mGlobalFunctionConfig != null ) {
            return mGlobalFunctionConfig.clone();
        }

        return null;
    }

    public static CoreConfig getCoreConfig() {
        return mCoreConfig;
    }

    public static FunctionConfig getFunctionConfig() {
        return mFunctionConfig;
    }

    public static GalleryTheme getGalleryTheme() {
        if (mGalleryTheme== null) {
            //使用默认配置
            mGalleryTheme = GalleryTheme.DEFAULT;
            mCallbackMap = new SparseArray<>();
        }
        return mGalleryTheme;
    }

    /**
     * 打开Gallery-单选
     * @param requestCode
     * @param callback
     */
    public static void openGallerySingle(int requestCode, OnHanlderResultCallback callback) {
        FunctionConfig config = copyGlobalFuncationConfig();
        if (config != null) {
            config.mutiSelect = false;
            openGallerySingle(requestCode, config, callback);
        } else {
            if(callback != null) {
                callback.onHanlderFailure(requestCode, mCoreConfig.getContext().getString(R.string.open_gallery_fail));
            }
            Logger.e("FunctionConfig null");
        }

    }

    /**
     * 打开Gallery-单选
     * @param requestCode
     * @param config
     * @param callback
     */
    public static void openGallerySingle(int requestCode, FunctionConfig config, OnHanlderResultCallback callback) {
        if ( mCoreConfig.getImageLoader() == null ) {
            if(callback != null){
                callback.onHanlderFailure(requestCode, mCoreConfig.getContext().getString(R.string.open_gallery_fail));
            }
            return;
        }

        if ( config == null && mGlobalFunctionConfig == null) {
            if(callback != null){
                callback.onHanlderFailure(requestCode, mCoreConfig.getContext().getString(R.string.open_gallery_fail));
            }
            return;
        }

        if (!DeviceUtils.existSDCard()) {
            Toast.makeText(mCoreConfig.getContext(), R.string.empty_sdcard, Toast.LENGTH_SHORT).show();
            return;
        }
        mCallbackMap.put(requestCode, callback);

        Intent intent = new Intent(mCoreConfig.getContext(), PhotoSelectActivity.class);
        mCoreConfig.getContext().startActivity(intent);
    }

    /**
     * 打开Gallery-单选
     * @param requestCode
     * @param callback
     */
    public static void openGalleryMuti(int requestCode, OnHanlderResultCallback callback) {
        FunctionConfig config = copyGlobalFuncationConfig();
        if (config != null) {
            config.mutiSelect = true;
            openGalleryMuti(requestCode, config, callback);
        } else {
            if(callback != null) {
                callback.onHanlderFailure(requestCode, mCoreConfig.getContext().getString(R.string.open_gallery_fail));
            }
            Logger.e("FunctionConfig null");
        }

    }

    /**
     * 打开Gallery-单选
     * @param requestCode
     * @param config
     * @param callback
     */
    public static void openGalleryMuti(int requestCode, FunctionConfig config, OnHanlderResultCallback callback) {
        if ( mCoreConfig.getImageLoader() == null ) {
            if(callback != null){
                callback.onHanlderFailure(requestCode, mCoreConfig.getContext().getString(R.string.open_gallery_fail));
            }
            return;
        }

        if ( config == null && mGlobalFunctionConfig == null) {
            if(callback != null){
                callback.onHanlderFailure(requestCode, mCoreConfig.getContext().getString(R.string.open_gallery_fail));
            }
            return;
        }

        if (!DeviceUtils.existSDCard()) {
            Toast.makeText(mCoreConfig.getContext(), R.string.empty_sdcard, Toast.LENGTH_SHORT).show();
            return;
        }
        mCallbackMap.put(requestCode, callback);

        Intent intent = new Intent(mCoreConfig.getContext(), PhotoSelectActivity.class);
        mCoreConfig.getContext().startActivity(intent);
    }

    /**
     * 打开相机
     * @param config
     */
    public static void openCamera(FunctionConfig config) {
        if ( config == null ) {
            return;
        }
        config.mutiSelect = false;//拍照为单选
        mFunctionConfig = config;
        Intent intent = new Intent(mCoreConfig.getContext(), PhotoEditActivity.class);
        intent.putExtra(PhotoEditActivity.TAKE_PHOTO_ACTION, true);
        mCoreConfig.getContext().startActivity(intent);
    }

    /**
     * 打开裁剪
     * @param config
     * @param photoPath
     */
    public static void openCrop(FunctionConfig config, String photoPath) {
        if ( config == null || StringUtils.isEmpty(photoPath) || !new File(photoPath).exists()) {
            Logger.d("config为空或文件不存在");
            return;
        }
        //必须设置这个三个选项
        config.mutiSelect = false;//拍照为单选
        config.editPhoto = true;
        config.crop = true;

        mFunctionConfig = config;
        HashMap<String, PhotoInfo> map = new HashMap<>();
        PhotoInfo photoInfo = new PhotoInfo();
        photoInfo.setPhotoPath(photoPath);
        photoInfo.setPhotoId(Utils.getRandom(10000, 99999));
        map.put(photoPath, photoInfo);
        Intent intent = new Intent(mCoreConfig.getContext(), PhotoEditActivity.class);
        intent.putExtra(PhotoEditActivity.CROP_PHOTO_ACTION, true);
        intent.putExtra(PhotoEditActivity.SELECT_MAP, map);
        mCoreConfig.getContext().startActivity(intent);
    }

    /**
     * 打开编辑
     * @param config
     * @param photoPath
     */
    public static void openEdit(FunctionConfig config, String photoPath) {
        if ( config == null || StringUtils.isEmpty(photoPath) || !new File(photoPath).exists()) {
            Logger.d("config为空或文件不存在");
            return;
        }
        config.mutiSelect = false;//拍照为单选

        mFunctionConfig = config;
        HashMap<String, PhotoInfo> map = new HashMap<>();
        PhotoInfo photoInfo = new PhotoInfo();
        photoInfo.setPhotoPath(photoPath);
        photoInfo.setPhotoId(Utils.getRandom(10000, 99999));
        map.put(photoPath, photoInfo);
        Intent intent = new Intent(mCoreConfig.getContext(), PhotoEditActivity.class);
        intent.putExtra(PhotoEditActivity.EDIT_PHOTO_ACTION, true);
        intent.putExtra(PhotoEditActivity.SELECT_MAP, map);
        mCoreConfig.getContext().startActivity(intent);
    }

    /**
     * 清楚缓存文件
     */
    public static void cleanCacheFile() {
        if (mFunctionConfig != null && mCoreConfig.getEditPhotoCacheFolder() != null) {
            //清楚裁剪冗余图片
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    FileUtils.deleteFile(mCoreConfig.getEditPhotoCacheFolder());
                }
            }.start();
        }
    }

    /**
     * 处理结果
     */
    public static interface OnHanlderResultCallback {
        /**
         * 处理成功
         * @param reqeustCode
         * @param resultList
         */
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList);

        /**
         * 处理失败或异常
         * @param requestCode
         * @param errorMsg
         */
        public void onHanlderFailure(int requestCode, String errorMsg);
    }
}
