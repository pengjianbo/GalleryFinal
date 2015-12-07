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

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import cn.finalteam.galleryfinal.GalleryConfig;
import cn.finalteam.galleryfinal.PhotoEditActivity;
import cn.finalteam.galleryfinal.R;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import java.util.List;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/11/30 下午2:26
 */
public class PhotoEditListAdapter extends CommonBaseAdapter<PhotoEditListAdapter.ViewHolder, PhotoInfo> {

    private int mPickMode;
    private PhotoEditActivity mActivity;
    private GalleryConfig mGalleryConfig;
    public PhotoEditListAdapter(PhotoEditActivity activity, List<PhotoInfo> list, GalleryConfig galleryConfig) {
        super(activity, list);
        mGalleryConfig = galleryConfig;
        mActivity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View view = inflate(R.layout.gf_adapter_edit_list, parent);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String path = "";
        PhotoInfo photoInfo = mList.get(position);
        if (photoInfo != null) {
            path = photoInfo.getPhotoPath();
        }
        holder.mIvPhoto.setImageResource(R.drawable.ic_gf_default_photo);
        mGalleryConfig.getImageLoader().displayImage(mActivity, path, holder.mIvPhoto, 100, 100);
        if (!mGalleryConfig.isMutiSelect()) {
            holder.mIvDelete.setVisibility(View.GONE);
        } else {
            holder.mIvDelete.setVisibility(View.VISIBLE);
        }
        holder.mIvDelete.setOnClickListener(new OnDeletePhotoClickListener(position));
    }

    public class ViewHolder extends CommonBaseAdapter.ViewHolder {
        ImageView mIvPhoto;
        ImageView mIvDelete;
        public ViewHolder(View view) {
            super(view);
            mIvPhoto = (ImageView) view.findViewById(R.id.iv_photo);
            mIvDelete = (ImageView) view.findViewById(R.id.iv_delete);
        }
    }

    private class OnDeletePhotoClickListener implements View.OnClickListener {

        private int position;

        public OnDeletePhotoClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            PhotoInfo photoInfo = null;
            try {
                photoInfo = mList.remove(position);
            } catch (Exception e){
                e.printStackTrace();
            }
            notifyDataSetChanged();
            mActivity.deleteIndex(position, photoInfo);
        }
    }
}
