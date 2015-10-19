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

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.finalteam.galleryfinal.adapter.FolderListAdapter;
import cn.finalteam.galleryfinal.adapter.PhotoListAdapter;
import cn.finalteam.galleryfinal.model.PhotoFolderInfo;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.galleryfinal.utils.PhotoTools;
import cn.finalteam.galleryfinal.widget.FloatingActionButton;
import cn.finalteam.toolsfinal.ActivityManager;
import cn.finalteam.toolsfinal.DeviceUtils;
import cn.finalteam.toolsfinal.StringUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Desction:图片选择器
 * Author:pengjianbo
 * Date:15/10/10 下午3:54
 */
public class PhotoChooseActivity extends PhotoBaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    private GridView mGvPhotoList;
    private ListView mLvFolderList;
    private LinearLayout mLllBottomBar;
    private LinearLayout mLlFolderPanel;
    private ImageView mIvTakePhoto;
    private ImageView mIvBack;
    private TextView mTvChooseCount;
    private TextView mTvTitle;
    private TextView mTvChooseFolderName;
    private FloatingActionButton mFabOk;
    private TextView mTvEmptyView;

    private List<PhotoFolderInfo> mAllPhotoFolderList;
    private FolderListAdapter mFolderListAdapter;

    private List<PhotoInfo> mCurPhotoList;
    private PhotoListAdapter mPhotoListAdapter;

    private int mLimit;
    private int mPickMode = GalleryHelper.SINGLE_IMAGE;
    private boolean mCrop;

    //是否需要刷新相册
    private boolean mHasRefreshGallery = false;

    private Handler mHanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if ( msg.what == 1000 ) {
                PhotoInfo photoInfo = (PhotoInfo) msg.obj;
                takeRefreshGallery(photoInfo);
            } else {
                mPhotoListAdapter.notifyDataSetChanged();
                mFolderListAdapter.notifyDataSetChanged();
                if (mAllPhotoFolderList.get(0).getPhotoList() == null ||
                        mAllPhotoFolderList.get(0).getPhotoList().size() == 0) {
                    mTvEmptyView.setText("暂无图片");
                }

                mGvPhotoList.setEnabled(true);
                mTvChooseFolderName.setEnabled(true);
                mIvTakePhoto.setEnabled(true);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gf_activity_photo_choose);

        mLimit = getIntent().getIntExtra(GalleryHelper.LIMIT, 1);
        mPickMode = getIntent().getIntExtra(GalleryHelper.PICK_MODE, GalleryHelper.SINGLE_IMAGE);
        mCrop = getIntent().getBooleanExtra(GalleryHelper.CROP_PHOTO, false);

        mPhotoTargetFolder = null;

        ActivityManager.getActivityManager().addActivity(this);

        findViews();
        setListener();
        mTvTitle.setText("手机相册");

        mAllPhotoFolderList = new ArrayList<>();
        mFolderListAdapter = new FolderListAdapter(this, mAllPhotoFolderList);
        mLvFolderList.setAdapter(mFolderListAdapter);

        mCurPhotoList = new ArrayList<>();
        mPhotoListAdapter = new PhotoListAdapter(this, mCurPhotoList, mSelectPhotoMap, mScreenWidth, mPickMode);
        mGvPhotoList.setAdapter(mPhotoListAdapter);

        if ( mPickMode == GalleryHelper.MULTIPLE_IMAGE ) {
            mTvChooseCount.setVisibility(View.VISIBLE);
            mFabOk.setVisibility(View.VISIBLE);
        }

        mIvBack.setBackgroundDrawable(getTitleStateListDrawable());
        mIvTakePhoto.setBackgroundDrawable(getTitleStateListDrawable());
        mGvPhotoList.setEmptyView(mTvEmptyView);

        refreshSelectCount();
        getPhotos();
    }

    private void findViews() {
        mGvPhotoList = (GridView) findViewById(R.id.gv_photo_list);
        mLvFolderList = (ListView) findViewById(R.id.lv_folder_list);
        mLllBottomBar = (LinearLayout) findViewById(R.id.ll_bottom_bar);
        mLlFolderPanel = (LinearLayout) findViewById(R.id.ll_folder_panel);
        mIvTakePhoto = (ImageView) findViewById(R.id.iv_take_photo);
        mTvChooseCount = (TextView) findViewById(R.id.tv_choose_count);
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mIvBack = (ImageView) findViewById(R.id.iv_back);
        mTvChooseFolderName = (TextView) findViewById(R.id.tv_choose_folder_name);
        mFabOk = (FloatingActionButton) findViewById(R.id.fab_ok);
        mTvEmptyView = (TextView) findViewById(R.id.tv_empty_view);
    }

    private void setListener() {
        mLllBottomBar.setOnClickListener(this);
        mIvTakePhoto.setOnClickListener(this);
        mIvBack.setOnClickListener(this);

        mLvFolderList.setOnItemClickListener(this);
        mGvPhotoList.setOnItemClickListener(this);
        mFabOk.setOnClickListener(this);
    }

    /**
     * 解决在5.0手机上刷新Gallery问题，从startActivityForResult回到Activity把数据添加到集合中然后理解跳转到下一个页面，
     * adapter的getCount与list.size不一致，所以我这里用了延迟刷新数据
     * @param photoInfo
     */
    protected void takeRefreshGallery(PhotoInfo photoInfo) {
        mCurPhotoList.add(photoInfo);
        mPhotoListAdapter.notifyDataSetChanged();

        //添加到集合中
        List<PhotoInfo> photoInfoList = mAllPhotoFolderList.get(0).getPhotoList();
        if (photoInfoList == null) {
            photoInfoList = new ArrayList<>();
        }
        photoInfoList.add(photoInfo);
        mAllPhotoFolderList.get(0).setPhotoList(photoInfoList);

        if ( mFolderListAdapter.getSelectFolder() != null ) {
            PhotoFolderInfo photoFolderInfo = mFolderListAdapter.getSelectFolder();
            List<PhotoInfo> list = photoFolderInfo.getPhotoList();
            if ( list == null ) {
                list = new ArrayList<>();
            }
            list.add(photoInfo);
            if ( list.size() == 1 ) {
                photoFolderInfo.setCoverPhoto(photoInfo);
            }
            mFolderListAdapter.getSelectFolder().setPhotoList(list);
        } else {
            String folderA = new File(photoInfo.getPhotoPath()).getParent();
            for (int i = 1; i < mAllPhotoFolderList.size(); i++) {
                PhotoFolderInfo folderInfo = mAllPhotoFolderList.get(i);
                String folderB = null;
                if (!StringUtils.isEmpty(photoInfo.getPhotoPath())) {
                    folderB = new File(photoInfo.getPhotoPath()).getParent();
                }
                if (TextUtils.equals(folderA, folderB)) {
                    List<PhotoInfo> list = folderInfo.getPhotoList();
                    if (list == null) {
                        list = new ArrayList<>();
                    }
                    list.add(photoInfo);
                    folderInfo.setPhotoList(list);
                    if ( list.size() == 1 ) {
                        folderInfo.setCoverPhoto(photoInfo);
                    }
                }
            }
        }

        mFolderListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void takeResult(PhotoInfo photoInfo) {

        Message message = mHanlder.obtainMessage();
        message.obj = photoInfo;
        message.what = 1000;

        if ( mPickMode == GalleryHelper.SINGLE_IMAGE ) { //单选
            if ( mCrop ) {//裁剪
                mHasRefreshGallery = true;
                toPhotoCrop(photoInfo);
            } else {
                resultSingle(photoInfo);
            }
            mHanlder.sendMessageDelayed(message, 100);
        } else {//多选
            mSelectPhotoMap.put(photoInfo.getPhotoPath(), photoInfo);
            refreshSelectCount();
            mHanlder.sendMessageDelayed(message, 1);
        }

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if ( id == R.id.ll_bottom_bar ) {
            if ( mLlFolderPanel.getVisibility() == View.VISIBLE ) {
                mLlFolderPanel.setVisibility(View.GONE);
            } else {
                mLlFolderPanel.setVisibility(View.VISIBLE);
            }
        } else if ( id == R.id.iv_take_photo ) {
            //判断是否达到多选最大数量
            if (mPickMode == GalleryHelper.MULTIPLE_IMAGE && mSelectPhotoMap.size() == mLimit) {
                toast("已达到最大选择数量,不可以拍照了哦");
                return;
            }

            if (!DeviceUtils.existSDCard()) {
                toast("没有SD卡");
                return;
            }

            takePhotoAction();
        } else if ( id == R.id.iv_back ) {
            finish();
        } else if ( id == R.id.fab_ok ) {
            if (mSelectPhotoMap.size() == 0) {
                toast("请先选择图片");
                return;
            }
            ArrayList<PhotoInfo> photoList = new ArrayList<>(mSelectPhotoMap.values());
            resultMuti(photoList);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int parentId = parent.getId();
        if ( parentId == R.id.lv_folder_list ) {
            folderItemClick(position);
        } else {
            photoItemClick(view, position);
        }
    }
    private void folderItemClick(int position) {
        mLlFolderPanel.setVisibility(View.GONE);
        mCurPhotoList.clear();
        PhotoFolderInfo photoFolderInfo = mAllPhotoFolderList.get(position);
        if ( photoFolderInfo.getPhotoList() != null ) {
            mCurPhotoList.addAll(photoFolderInfo.getPhotoList());
        }
        mPhotoListAdapter.notifyDataSetChanged();

        if (position == 0) {
            mPhotoTargetFolder = null;
        } else {
            PhotoInfo photoInfo = photoFolderInfo.getCoverPhoto();
            if (photoInfo != null && !StringUtils.isEmpty(photoInfo.getPhotoPath())) {
                mPhotoTargetFolder = new File(photoInfo.getPhotoPath()).getParent();
            } else {
                mPhotoTargetFolder = null;
            }
        }
        mTvChooseFolderName.setText(photoFolderInfo.getFolderName());
        mFolderListAdapter.setSelectFolder(photoFolderInfo);
        mFolderListAdapter.notifyDataSetChanged();
    }

    private void photoItemClick(View view, int position) {
        PhotoInfo info = mCurPhotoList.get(position);
        if (mPickMode == GalleryHelper.SINGLE_IMAGE) {
            if (mCrop) {
                mHasRefreshGallery = true;
                toPhotoCrop(info);
            } else {
                resultSingle(info);
            }
            return;
        }
        boolean checked = false;
        if (mSelectPhotoMap.get(info.getPhotoPath()) == null) {
            if (mPickMode == GalleryHelper.MULTIPLE_IMAGE && mSelectPhotoMap.size() == mLimit) {
                toast("已达到最大选择数量");
                return;
            } else {
                mSelectPhotoMap.put(info.getPhotoPath(), info);
                checked = true;
            }
        } else {
            mSelectPhotoMap.remove(info.getPhotoPath());
            checked = false;
        }
        refreshSelectCount();

        PhotoListAdapter.PhotoViewHolder holder = (PhotoListAdapter.PhotoViewHolder) view.getTag();
        if (holder != null) {
            if (checked) {

                holder.mIvCheck.setBackgroundColor(getColorByTheme(R.attr.colorTheme));
            } else {
                holder.mIvCheck.setBackgroundColor(getResources().getColor(R.color.gf_gray));
            }
        } else {
            mPhotoListAdapter.notifyDataSetChanged();
        }
    }

    public void refreshSelectCount() {
        mTvChooseCount.setText("已选(" + mSelectPhotoMap.size() + "/" + mLimit + ")");
    }

    /**
     * 获取所有图片
     */
    private void getPhotos() {
        mTvEmptyView.setText("请稍后…");
        mGvPhotoList.setEnabled(false);
        mTvChooseFolderName.setEnabled(false);
        mIvTakePhoto.setEnabled(false);
        new Thread() {
            @Override
            public void run() {
                super.run();

                mAllPhotoFolderList.clear();
                List<PhotoFolderInfo> allFolderList = PhotoTools.getAllPhotoFolder(PhotoChooseActivity.this);
                mAllPhotoFolderList.addAll(allFolderList);

                mCurPhotoList.clear();
                if ( allFolderList.size() > 0 ) {
                    if ( allFolderList.get(0).getPhotoList() != null ) {
                        mCurPhotoList.addAll(allFolderList.get(0).getPhotoList());
                    }
                }

                mHanlder.sendEmptyMessage(1);
            }
        }.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if ( GalleryHelper.mImageLoader == null ) {
            toast("选择图片失败");
            finish();
        }

        if ( mHasRefreshGallery ) {
            mHasRefreshGallery = false;
            getPhotos();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPhotoTargetFolder = null;
        mSelectPhotoMap.clear();
        mLimit = 1;
        mPickMode = GalleryHelper.SINGLE_IMAGE;
        mCrop = false;
        System.gc();
    }
}
