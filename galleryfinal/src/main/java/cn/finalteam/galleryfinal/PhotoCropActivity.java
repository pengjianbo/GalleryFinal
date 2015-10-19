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
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.galleryfinal.widget.FloatingActionButton;
import cn.finalteam.toolsfinal.ActivityManager;
import cn.finalteam.toolsfinal.BitmapUtils;
import cn.finalteam.toolsfinal.DateUtils;
import cn.finalteam.toolsfinal.FileUtils;
import cn.finalteam.toolsfinal.Logger;
import cn.finalteam.toolsfinal.StringUtils;
import com.isseiaoki.simplecropview.CropImageView;
import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Desction:图片裁剪
 * Author:pengjianbo
 * Date:15/10/10 下午5:40
 */
public class PhotoCropActivity extends PhotoBaseActivity implements View.OnClickListener{
    public static final String PHOTO_INFO = "photo_info";

    private TextView mTvTitle;
    private ImageView mIvBack;
    private ImageView mIvTakePhoto;
    private CropImageView mIvPhoto;
    private FloatingActionButton mFabOk;
    private TextView mTvEmptyView;

    private PhotoInfo mPhotoInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gf_activity_photo_crop);
        ActivityManager.getActivityManager().addActivity(this);

        mPhotoInfo = getIntent().getParcelableExtra(PHOTO_INFO);

        findViews();
        setListener();

        mTvTitle.setText("图片裁剪");
        mIvPhoto.setBackgroundColor(0xFFFFFFFB);
        mIvPhoto.setOverlayColor(0xAA1C1C1C);
        int colorTheme = getColorByTheme(R.attr.colorTheme);        
        mIvPhoto.setFrameColor(colorTheme);
        mIvPhoto.setHandleColor(colorTheme);
        mIvPhoto.setGuideColor(colorTheme);

        mIvBack.setBackgroundDrawable(getTitleStateListDrawable());
        mIvTakePhoto.setBackgroundDrawable(getTitleStateListDrawable());

        loadImage();
    }

    private void findViews() {
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mIvTakePhoto = (ImageView) findViewById(R.id.iv_take_photo);
        mIvPhoto = (CropImageView) findViewById(R.id.iv_photo);
        mFabOk = (FloatingActionButton) findViewById(R.id.fab_ok);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvEmptyView = (TextView) findViewById(R.id.tv_empty_view);
    }

    private void setListener() {
        mIvTakePhoto.setOnClickListener(this);
        mFabOk.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
    }

    @Override
    protected void takeResult(PhotoInfo info) {
        mPhotoInfo = info;
        PhotoChooseActivity activity = (PhotoChooseActivity) ActivityManager.getActivityManager().getActivity(PhotoChooseActivity.class.getName());
        if ( activity != null ) {
            activity.takeRefreshGallery(info);
        }
        loadImage();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if ( id == R.id.fab_ok ) {
            if ( mPhotoInfo == null ) {
                return;
            }

            try {
                new File(GalleryHelper.PHOTO_DIR, ".nomedia").createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.gc();

            try {
                File toFile = new File(GalleryHelper.PHOTO_DIR, "IMG" + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".jpg");
                FileUtils.makeFolders(toFile);
                Bitmap bitmap = mIvPhoto.getCroppedBitmap();
                if ( bitmap != null ) {
                    BitmapUtils.saveBitmap(bitmap, toFile);
                    mPhotoInfo.setPhotoPath(toFile.getAbsolutePath());
                    bitmap.recycle();
                }
            } catch (Exception e) {
                Logger.e(e);
            }

            Intent intent = getIntent();
            if (intent == null) {
                intent = new Intent();
            }
            intent.putExtra(GalleryHelper.RESULT_DATA, mPhotoInfo);
            setResult(GalleryHelper.CROP_SUCCESS, intent);
            finish();
        } else if ( id == R.id.iv_take_photo ) {
            takePhotoAction();
        } else if ( id == R.id.iv_back ) {
            finish();
        }
    }

    private void loadImage() {
        String coverPath = "";
        if (mPhotoInfo != null) {
            coverPath = mPhotoInfo.getThumbPath();
            if (StringUtils.isEmpty(coverPath)) {
                coverPath = mPhotoInfo.getPhotoPath();
            }
        }

        final int degress = BitmapUtils.getDegress(coverPath);
        final String path = coverPath;
        //if ( degress != 0 ) {
            new AsyncTask<Void, Void, Bitmap>() {

                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    mTvEmptyView.setVisibility(View.VISIBLE);
                }

                @Override
                protected Bitmap doInBackground(Void... params) {
                    Bitmap bitmap = rotateBitmap(path, degress);
                    return bitmap;
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    super.onPostExecute(bitmap);
                    if ( bitmap != null ) {
                        mIvPhoto.setImageBitmap(bitmap);
                        mTvEmptyView.setVisibility(View.GONE);
                    } else {
                        mTvEmptyView.setText("图片");
                    }
                }
            }.execute();

        //} else {
        //    mTvEmptyView.setVisibility(View.GONE);
        //    GalleryHelper.mImageLoader.displayImage(mIvPhoto, "file:/" + path);
        //}
    }
}
