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

package cn.finalteam.galleryfinal.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Desction:图片信息
 * Author:pengjianbo
 * Date:15/7/30 上午11:23
 */
public class PhotoInfo implements Parcelable {

    private int photoId;
    private String photoPath;
    private String thumbPath;
    private int width;
    private int height;

    public PhotoInfo() {}

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.photoId);
        dest.writeString(this.photoPath);
        dest.writeString(this.thumbPath);
        dest.writeInt(this.width);
        dest.writeInt(this.height);

    }

    protected PhotoInfo(Parcel in) {
        this.photoId = in.readInt();
        this.photoPath = in.readString();
        this.thumbPath = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
    }

    public static final Creator<PhotoInfo> CREATOR = new Creator<PhotoInfo>() {
        public PhotoInfo createFromParcel(Parcel source) {return new PhotoInfo(source);}

        public PhotoInfo[] newArray(int size) {return new PhotoInfo[size];}
    };


}
