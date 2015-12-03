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
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Window;
import android.widget.Toast;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.galleryfinal.utils.MediaScanner;
import cn.finalteam.toolsfinal.ActivityManager;
import cn.finalteam.toolsfinal.BitmapUtils;
import cn.finalteam.toolsfinal.DateUtils;
import cn.finalteam.toolsfinal.DeviceUtils;
import cn.finalteam.toolsfinal.FileUtils;
import cn.finalteam.toolsfinal.Logger;
import cn.finalteam.toolsfinal.StringUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/10/10 下午5:46
 */
public abstract class PhotoBaseActivity extends Activity {

    protected static Map<String, PhotoInfo> mSelectPhotoMap = new HashMap<>();
    protected static String mPhotoTargetFolder;

    private Uri mTakePhotoUri;
    private MediaScanner mMediaScanner;

    protected int mScreenWidth = 720;
    protected int mScreenHeight = 1280;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        ActivityManager.getActivityManager().addActivity(this);
        mMediaScanner = new MediaScanner(this);
        DisplayMetrics dm = DeviceUtils.getScreenPix(this);
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
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
            takePhotoFolder = new File(Environment.getExternalStorageDirectory(),
                    "/DCIM/" + Consts.TAKE_PHOTO_FOLDER);
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
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( requestCode == GalleryFinal.TAKE_REQUEST_CODE ) {
            if (resultCode == RESULT_OK && mTakePhotoUri != null) {
                final String path = mTakePhotoUri.getPath();
                final PhotoInfo info = new PhotoInfo();
                info.setPhotoPath(path);
                updateGallery(path);
                takeResult(info);
            } else {
                toast("拍照失败");
            }
        } else if ( requestCode == GalleryFinal.EDIT_REQUEST_CODE) {
            if ( resultCode == GalleryFinal.EDIT_OK ) {
                ArrayList<PhotoInfo> photoInfoList = (ArrayList<PhotoInfo>) data.getSerializableExtra(GalleryFinal.GALLERY_RESULT_LIST_DATA);
                resultMuti(photoInfoList);
            }
        }
    }

    /**
     * 更新相册
     */
    private void updateGallery(String filePath) {
        mMediaScanner.scanFile(filePath, "image/jpeg");
    }


    protected Bitmap rotateBitmap(String path, int degress) {
        try {
            Bitmap bitmap = BitmapUtils.compressBitmap(path, mScreenWidth / 4, mScreenHeight / 4);
            bitmap = BitmapUtils.rotateBitmap(bitmap, degress);
            return bitmap;
        } catch (Exception e) {
            Logger.e(e);
        }

        return null;
    }

    protected void saveRotateBitmap(Bitmap bitmap, String path) {
        //保存
        BitmapUtils.saveBitmap(bitmap, new File(path));
        //修改数据库
        ContentValues cv = new ContentValues();
        cv.put("orientation", 0);
        ContentResolver cr = getContentResolver();
        String where = new String(MediaStore.Images.Media.DATA + "='" + StringUtils.sqliteEscape(path) +"'");
        cr.update(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv, where, null);
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
        bg.addState(new int[]{}, new ColorDrawable(getColorByTheme(R.attr.colorTheme)));
        return bg;
    }
}
