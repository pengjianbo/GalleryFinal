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
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Window;
import android.widget.Toast;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.galleryfinal.utils.MediaScanner;
import cn.finalteam.toolsfinal.ActivityManager;
import cn.finalteam.toolsfinal.DateUtils;
import cn.finalteam.toolsfinal.DeviceUtils;
import cn.finalteam.toolsfinal.FileUtils;
import cn.finalteam.toolsfinal.Logger;
import cn.finalteam.toolsfinal.StringUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/10/10 下午5:46
 */
public abstract class PhotoBaseActivity extends Activity {

    //protected static Map<String, PhotoInfo> mSelectPhotoMap = new HashMap<>();
    protected static String mPhotoTargetFolder;

    private Uri mTakePhotoUri;
    private MediaScanner mMediaScanner;

    protected int mScreenWidth = 720;
    protected int mScreenHeight = 1280;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if ( GalleryFinal.getGalleryConfig() == null ) {
            finish();
            return;
        }
        super.onCreate(savedInstanceState);
        ActivityManager.getActivityManager().addActivity(this);
        mMediaScanner = new MediaScanner(this);
        DisplayMetrics dm = DeviceUtils.getScreenPix(this);
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ( GalleryFinal.getGalleryConfig() == null ) {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        System.gc();
        super.onDestroy();
        mMediaScanner.unScanFile();
        ActivityManager.getActivityManager().finishActivity(this);
    }

    public int getColorByTheme(int attr) {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(attr, typedValue, true);
        int colorTheme = typedValue.data;
        return colorTheme;
    }

    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 拍照
     */
    protected void takePhotoAction() {

        if (!DeviceUtils.existSDCard()) {
            toast("没有SD卡不能拍照呢~");
            return;
        }

        File takePhotoFolder = null;
        if (StringUtils.isEmpty(mPhotoTargetFolder)) {
            takePhotoFolder = GalleryFinal.getGalleryConfig().getTakePhotoFolder();
        } else {
            takePhotoFolder = new File(mPhotoTargetFolder);
        }

        File toFile = new File(takePhotoFolder, "IMG" + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".jpg");
        boolean suc = FileUtils.makeFolders(toFile);
        Logger.d("create folder=" + toFile.getAbsolutePath());
        if (suc) {
            mTakePhotoUri = Uri.fromFile(toFile);
            Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mTakePhotoUri);
            startActivityForResult(captureIntent, GalleryFinal.TAKE_REQUEST_CODE);
        } else {
            Logger.e("create file failure");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( requestCode == GalleryFinal.TAKE_REQUEST_CODE ) {
            if (resultCode == RESULT_OK && mTakePhotoUri != null) {
                final String path = mTakePhotoUri.getPath();
                final PhotoInfo info = new PhotoInfo();
                info.setPhotoId(getRandom(10000, 99999));
                info.setPhotoPath(path);
                updateGallery(path);
                takeResult(info);
            } else {
                toast(getString(R.string.take_photo_fail));
            }
        } else if ( requestCode == GalleryFinal.EDIT_REQUEST_CODE) {
            if ( resultCode == GalleryFinal.EDIT_OK ) {
                ArrayList<PhotoInfo> photoInfoList = (ArrayList<PhotoInfo>) data.getSerializableExtra(GalleryFinal.GALLERY_RESULT_LIST_DATA);
                resultMuti(photoInfoList);
            }
        }
    }

    /**
     * 取某个范围的任意数
     * @param min
     * @param max
     * @return
     */
    private int getRandom(int min, int max){
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }

    /**
     * 更新相册
     */
    private void updateGallery(String filePath) {
        mMediaScanner.scanFile(filePath, "image/jpeg");
    }

    protected void resultMuti(ArrayList<PhotoInfo> resultList) {
        Intent intent = getIntent();
        if (intent == null) {
            intent = new Intent();
        }
        intent.putExtra(GalleryFinal.GALLERY_RESULT_LIST_DATA, resultList);
        setResult(GalleryFinal.GALLERY_RESULT_SUCCESS, intent);
        finish();
    }

    protected abstract void takeResult(PhotoInfo info);

    public StateListDrawable getTitleStateListDrawable() {
        StateListDrawable bg = new StateListDrawable();
        bg.addState(new int[]{android.R.attr.state_pressed}, new ColorDrawable(getColorByTheme(R.attr.colorThemeDark)));
        bg.addState(new int[] {}, new ColorDrawable(getColorByTheme(R.attr.colorTheme)));
        return bg;
    }
}
