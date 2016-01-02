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

package cn.finalteam.galleryfinal.adapter;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.R;
import cn.finalteam.galleryfinal.model.PhotoFolderInfo;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.galleryfinal.widget.GFImageView;
import cn.finalteam.toolsfinal.adapter.ViewHolderAdapter;
import java.util.List;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/10/10 下午5:09
 */
public class FolderListAdapter extends ViewHolderAdapter<FolderListAdapter.FolderViewHolder, PhotoFolderInfo> {

    private PhotoFolderInfo mSelectFolder;
    private FunctionConfig mFunctionConfig;

    private Activity mActivity;

    public FolderListAdapter(Activity activity, List<PhotoFolderInfo> list, FunctionConfig FunctionConfig) {
        super(activity, list);
        this.mFunctionConfig = FunctionConfig;
        this.mActivity = activity;
    }

    @Override
    public FolderViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = inflate(R.layout.gf_adapter_folder_list_item, parent);
        return new FolderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FolderViewHolder holder, int position) {
        PhotoFolderInfo photoFolderInfo = getDatas().get(position);

        String path = "";
        PhotoInfo photoInfo = photoFolderInfo.getCoverPhoto();
        if (photoInfo != null) {
            path = photoInfo.getPhotoPath();
        }
        holder.mIvCover.setImageResource(R.drawable.ic_gf_default_photo);
        Drawable defaultDrawable = mActivity.getResources().getDrawable(R.drawable.ic_gf_default_photo);
        GalleryFinal.getCoreConfig().getImageLoader().displayImage(mActivity, path, holder.mIvCover, defaultDrawable, 200, 200);

        holder.mTvFolderName.setText(photoFolderInfo.getFolderName());
        int size = 0;
        if ( photoFolderInfo.getPhotoList() != null ) {
            size = photoFolderInfo.getPhotoList().size();
        }
        holder.mTvPhotoCount.setText(mActivity.getString(R.string.folder_photo_size, size));
        if (GalleryFinal.getCoreConfig().getAnimation() > 0) {
            holder.mView.startAnimation(AnimationUtils.loadAnimation(mActivity, GalleryFinal.getCoreConfig().getAnimation()));
        }
        holder.mIvFolderCheck.setImageResource(GalleryFinal.getGalleryTheme().getIconCheck());
        if (mSelectFolder == photoFolderInfo || (mSelectFolder == null && position == 0)) {
            holder.mIvFolderCheck.setVisibility(View.VISIBLE);
            holder.mIvFolderCheck.setColorFilter(GalleryFinal.getGalleryTheme().getCheckSelectedColor());
        } else {
            holder.mIvFolderCheck.setVisibility(View.GONE);
        }
    }

    public void setSelectFolder(PhotoFolderInfo photoFolderInfo) {
        this.mSelectFolder = photoFolderInfo;
    }

    public PhotoFolderInfo getSelectFolder() {
        return mSelectFolder;
    }

    static class FolderViewHolder extends ViewHolderAdapter.ViewHolder {
        GFImageView mIvCover;
        ImageView mIvFolderCheck;
        TextView mTvFolderName;
        TextView mTvPhotoCount;
        View mView;
        public FolderViewHolder(View view) {
            super(view);
            this.mView = view;
            mIvCover = (GFImageView) view.findViewById(R.id.iv_cover);
            mTvFolderName = (TextView) view.findViewById(R.id.tv_folder_name);
            mTvPhotoCount = (TextView) view.findViewById(R.id.tv_photo_count);
            mIvFolderCheck = (ImageView) view.findViewById(R.id.iv_folder_check);
        }
    }
}
