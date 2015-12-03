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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.finalteam.galleryfinal.adapter.PhotoEditListAdapter;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.galleryfinal.widget.FloatingActionButton;
import cn.finalteam.galleryfinal.widget.HorizontalListView;
import cn.finalteam.galleryfinal.widget.crop.CropImageActivity;
import cn.finalteam.galleryfinal.widget.crop.CropImageView;
import cn.finalteam.toolsfinal.ActivityManager;
import cn.finalteam.toolsfinal.BitmapUtils;
import cn.finalteam.toolsfinal.DateUtils;
import cn.finalteam.toolsfinal.FileUtils;
import cn.finalteam.toolsfinal.Logger;
import cn.finalteam.toolsfinal.StringUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * Desction:图片裁剪
 * Author:pengjianbo
 * Date:15/10/10 下午5:40
 */
public class PhotoEditActivity extends CropImageActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ImageView mIvBack;
    private TextView mTvTitle;
    private ImageView mIvTakePhoto;
    private ImageView mIvCrop;
    private ImageView mIvRotation;
    private CropImageView mIvCropPhoto;
    private ImageView mIvSourcePhoto;
    private TextView mTvEmptyView;
    private FloatingActionButton mFabCrop;
    private HorizontalListView mLvGallery;
    private LinearLayout mLlGallery;
    private ArrayList<PhotoInfo> mPhotoList;
    private PhotoEditListAdapter mPhotoEditListAdapter;
    private int mSelectIndex = 0;
    private boolean mCropState;

    private GalleryConfig mGalleryConfig;

    private android.os.Handler mHanlder = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (mPhotoList.get(mSelectIndex) != null) {
                    PhotoInfo photoInfo = mPhotoList.get(mSelectIndex);
                    String path = (String) msg.obj;
                    photoInfo.setPhotoPath(path);
                    photoInfo.setThumbPath(path);
                    loadImage(photoInfo);
                    mPhotoEditListAdapter.notifyDataSetChanged();
                }

                toast(getString(R.string.crop_suc));
            } else {
                toast(getString(R.string.crop_fail));
            }

            corpPageState(false);
            mCropState = false;
            mTvTitle.setText(R.string.photo_edit);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gf_activity_photo_edit);

        mPhotoList = new ArrayList<>(mSelectPhotoMap.values());
        mGalleryConfig = GalleryFinal.getGalleryConfig();

        if (mPhotoList == null) {
            mPhotoList = new ArrayList<>();
        }

        findViews();
        setListener();

        mIvBack.setBackgroundDrawable(getTitleStateListDrawable());
        //mIvTakePhoto.setBackgroundDrawable(getTitleStateListDrawable());
        //mIvCrop.setBackgroundDrawable(getTitleStateListDrawable());
        //mIvRotation.setBackgroundDrawable(getTitleStateListDrawable());

        mPhotoEditListAdapter = new PhotoEditListAdapter(this, mPhotoList, mGalleryConfig);
        mLvGallery.setAdapter(mPhotoEditListAdapter);

        try {
            new File(Consts.PHOTO_CROP_DIR, ".nomedia").createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //mIvPhoto.setCropEnabled(false);
        //if (mPhotoList.size() > 0) {
        //    loadImage(mPhotoList.get(0));
        //}

        if (mGalleryConfig.isShowCamera()) {
            mIvTakePhoto.setVisibility(View.VISIBLE);
        }

        if ( mGalleryConfig.isCrop() ) {
            mIvCrop.setVisibility(View.VISIBLE);
        }

        if (mGalleryConfig.isRotate()) {
            mIvRotation.setVisibility(View.VISIBLE);
        }

        initCrop(mIvCropPhoto, mGalleryConfig.isCropSquare(), mGalleryConfig.getCropWidth(), mGalleryConfig.getCropHeight());
        if (mPhotoList.size() > 0) {
            loadImage(mPhotoList.get(0));
        }
    }

    private void findViews() {
        mIvTakePhoto = (ImageView) findViewById(R.id.iv_take_photo);
        mIvCropPhoto = (CropImageView) findViewById(R.id.iv_crop_photo);
        mIvSourcePhoto = (ImageView) findViewById(R.id.iv_source_photo);
        mLvGallery = (HorizontalListView) findViewById(R.id.lv_gallery);
        mLlGallery = (LinearLayout) findViewById(R.id.ll_gallery);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvEmptyView = (TextView) findViewById(R.id.tv_empty_view);
        mFabCrop = (FloatingActionButton) findViewById(R.id.fab_crop);
        mIvCrop = (ImageView) findViewById(R.id.iv_crop);
        mIvRotation = (ImageView) findViewById(R.id.iv_rotation);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
    }

    private void setListener() {
        mIvTakePhoto.setOnClickListener(this);
        mIvBack.setOnClickListener(this);
        mLvGallery.setOnItemClickListener(this);
        mFabCrop.setOnClickListener(this);
        mIvCrop.setOnClickListener(this);
        mIvRotation.setOnClickListener(this);
    }

    @Override
    protected void takeResult(PhotoInfo info) {
        if (!mGalleryConfig.isMutiSelect()) {
            mPhotoList.clear();
        }
        mPhotoList.add(info);
        mPhotoEditListAdapter.notifyDataSetChanged();

        PhotoSelectActivity activity = (PhotoSelectActivity) ActivityManager.getActivityManager().getActivity(PhotoSelectActivity.class.getName());
        if (activity != null) {
            activity.takeRefreshGallery(info, true);
        }
        loadImage(info);
    }

    private void loadImage(PhotoInfo photo) {
        mTvEmptyView.setVisibility(View.GONE);
        mIvSourcePhoto.setVisibility(View.VISIBLE);
        mIvCropPhoto.setVisibility(View.GONE);

        String coverPath = "";
        if (photo != null) {
            coverPath = photo.getThumbPath();
            if (StringUtils.isEmpty(coverPath)) {
                coverPath = photo.getPhotoPath();
            }
        }
        String path = coverPath;
        if ( mGalleryConfig.isCrop() ) {
            setSourceUri(Uri.fromFile(new File(path)));
        }

        mGalleryConfig.getImageLoader().displayImage(this, path, mIvSourcePhoto, mScreenWidth, mScreenHeight);
    }

    public void deleteIndex(int position, PhotoInfo dPhoto) {
        if(dPhoto != null) {
            try {
                Iterator<Map.Entry<String, PhotoInfo>> entries = mSelectPhotoMap.entrySet().iterator();
                while (entries.hasNext()) {
                    Map.Entry<String, PhotoInfo> entry = entries.next();
                    if (TextUtils.equals(entry.getKey(), dPhoto.getPhotoPath())) {
                        entries.remove();
                    }
                }
                } catch (Exception e){}
        }

        PhotoSelectActivity activity = (PhotoSelectActivity) ActivityManager.getActivityManager().getActivity(PhotoSelectActivity.class.getName());
        if (activity != null) {
            activity.refreshAdapter();
        }

        if (mPhotoList.size() == 0) {
            mSelectIndex = 0;
            mTvEmptyView.setText(R.string.no_photo);
            mTvEmptyView.setVisibility(View.VISIBLE);
            mIvSourcePhoto.setVisibility(View.GONE);
            mIvCropPhoto.setVisibility(View.GONE);
        } else {
            if ( position == 0 ) {
                mSelectIndex = 0;
            } else if ( position == mPhotoList.size() ) {
                mSelectIndex = position - 1;
            } else {
                mSelectIndex = position;
            }

            PhotoInfo photoInfo = mPhotoList.get(mSelectIndex);
            loadImage(photoInfo);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        mSelectIndex = i;
        PhotoInfo photoInfo = mPhotoList.get(i);
        loadImage(photoInfo);
    }

    @Override
    public void setCropSaveSuccess(final File file) {
        Message message = mHanlder.obtainMessage();
        message.what = 1;
        message.obj = file.getAbsolutePath();
        mHanlder.sendMessage(message);
    }

    @Override
    public void setCropSaveException(Throwable throwable) {
        mHanlder.sendEmptyMessage(0);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.fab_crop) {
            if (mPhotoList.size() == 0) {
                return;
            }
            if (mCropState) {
                System.gc();
                try {
                    File toFile = new File(Consts.PHOTO_CROP_DIR, "IMG" + DateUtils.format(new Date(), "yyyyMMddHHmmss") + ".jpg");
                    FileUtils.makeFolders(toFile);
                    onSaveClicked(toFile);//保存裁剪
                } catch (Exception e) {
                    Logger.e(e);
                }

            } else { //完成选择
                ArrayList<PhotoInfo> photoList = new ArrayList<>(mSelectPhotoMap.values());
                Intent intent = getIntent();
                if (intent == null) {
                    intent = new Intent();
                }
                intent.putExtra(GalleryFinal.GALLERY_RESULT_LIST_DATA, photoList);
                setResult(GalleryFinal.EDIT_OK, intent);
                finish();
            }
        } else if (id == R.id.iv_crop) {
            if (mCropState) {
                setCropEnabled(false);

                corpPageState(false);

                mTvTitle.setText(R.string.photo_edit);
            } else {
                corpPageState(true);
                setCropEnabled(true);

                mTvTitle.setText(R.string.photo_crop);
            }
            mCropState = !mCropState;
        } else if (id == R.id.iv_rotation) {
            rotationPhoto();
        } else if (id == R.id.iv_take_photo) {
            if(mGalleryConfig.isMutiSelect() && mGalleryConfig.getMaxSize() == mSelectPhotoMap.size()) {
                toast(getString(R.string.select_max_tips));
            } else {
                takePhotoAction();
            }
        } else if (id == R.id.iv_back) {
            if ( mCropState ) {
                mIvCrop.performClick();
            } else {
                finish();
            }
        }
    }

    /**
     * 图片旋转
     */
    private void rotationPhoto() {
        if (mPhotoList.size() > 0 && mPhotoList.get(mSelectIndex) != null) {
            PhotoInfo photoInfo = mPhotoList.get(mSelectIndex);
            String coverPath = "";
            if (photoInfo != null) {
                coverPath = photoInfo.getThumbPath();
                if (StringUtils.isEmpty(coverPath)) {
                    coverPath = photoInfo.getPhotoPath();
                }
            }

            final String path = coverPath;
            new AsyncTask<Void, Void, Bitmap>() {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                    mTvEmptyView.setVisibility(View.VISIBLE);
                }

                @Override
                protected Bitmap doInBackground(Void... params) {
                    Bitmap bitmap = rotateBitmap(path, 90);
                    if (bitmap != null) {
                        BitmapUtils.saveBitmap(bitmap, new File(path));
                    }
                    return bitmap;
                }

                @Override
                protected void onPostExecute(Bitmap bitmap) {
                    super.onPostExecute(bitmap);
                    if (bitmap != null) {
                        mIvSourcePhoto.setImageBitmap(bitmap);
                        mTvEmptyView.setVisibility(View.GONE);
                    } else {
                        mTvEmptyView.setText(R.string.no_photo);
                    }
                }
            }.execute();
        }
    }

    private void corpPageState(boolean crop) {
        if (crop) {
            mIvSourcePhoto.setVisibility(View.GONE);
            mIvCropPhoto.setVisibility(View.VISIBLE);
            mLlGallery.setVisibility(View.GONE);
            if(mGalleryConfig.isCrop()) {
                mIvCrop.setVisibility(View.VISIBLE);
            }
            if (mGalleryConfig.isRotate()) {
                mIvRotation.setVisibility(View.GONE);
            }

            if (mGalleryConfig.isShowCamera()) {
                mIvTakePhoto.setVisibility(View.GONE);
            }
        } else {
            mIvSourcePhoto.setVisibility(View.VISIBLE);
            mIvCropPhoto.setVisibility(View.GONE);
            mLlGallery.setVisibility(View.VISIBLE);
            if(mGalleryConfig.isCrop()) {
                mIvCrop.setVisibility(View.VISIBLE);
            }
            if (mGalleryConfig.isRotate()) {
                mIvRotation.setVisibility(View.VISIBLE);
            }

            if (mGalleryConfig.isShowCamera()) {
                mIvTakePhoto.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( keyCode == KeyEvent.KEYCODE_BACK ) {
            if ( mCropState ) {
                mIvCrop.performClick();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
