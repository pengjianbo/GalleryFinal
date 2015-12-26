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
import android.widget.AbsListView;
import android.widget.ImageView;

import java.util.List;
import java.util.Map;

import cn.finalteam.galleryfinal.GalleryConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.R;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.galleryfinal.widget.GFImageView;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/10/10 下午4:59
 */
public class PhotoListAdapter extends CommonBaseAdapter<PhotoListAdapter.PhotoViewHolder, PhotoInfo> {

    private Map<String, PhotoInfo> mSelectList;
    private int mScreenWidth;
    private GalleryConfig mGalleryConfig;
    private int mRowWidth;
    public PhotoListAdapter(Activity activity, List<PhotoInfo> list, Map<String, PhotoInfo> selectList, int screenWidth, GalleryConfig galleryConfig) {
        super(activity, list);
        this.mSelectList = selectList;
        this.mScreenWidth = screenWidth;
        this.mGalleryConfig = galleryConfig;
        this.mRowWidth = mScreenWidth/3;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = inflate(R.layout.gf_adapter_photo_list_item, parent);
        setHeight(view);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        PhotoInfo photoInfo = mList.get(position);

        String path = "";
        if (photoInfo != null) {
            path = photoInfo.getPhotoPath();
        }

        holder.mIvThumb.setImageResource(R.drawable.ic_gf_default_photo);
        Drawable defaultDrawable = mActivity.getResources().getDrawable(R.drawable.ic_gf_default_photo);
        mGalleryConfig.getImageLoader().displayImage(mActivity, path, holder.mIvThumb, defaultDrawable, mRowWidth, mRowWidth);

        holder.mIvCheck.setImageResource(GalleryFinal.getGalleryTheme().getIconCheck());
        if ( mGalleryConfig.isMutiSelect() ) {
            holder.mIvCheck.setVisibility(View.VISIBLE);
            if (mSelectList.get(photoInfo.getPhotoPath()) != null) {
                holder.mIvCheck.setBackgroundColor(GalleryFinal.getGalleryTheme().getCheckSelectedColor());
            } else {
                holder.mIvCheck.setBackgroundColor(GalleryFinal.getGalleryTheme().getCheckNornalColor());
            }
        } else {
            holder.mIvCheck.setVisibility(View.GONE);
        }
    }

    private void setHeight(final View convertView) {
        int height = mScreenWidth / 3 - 8;
        convertView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));
    }

    public static class PhotoViewHolder extends CommonBaseAdapter.ViewHolder {

        public GFImageView mIvThumb;
        public ImageView mIvCheck;

        public PhotoViewHolder(View view) {
            super(view);
            mIvThumb = (GFImageView) view.findViewById(R.id.iv_thumb);
            mIvCheck = (ImageView) view.findViewById(R.id.iv_check);
        }
    }
}
