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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import java.util.List;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/7/30 下午10:21
 */
abstract class CommonBaseAdapter<VH extends CommonBaseAdapter.ViewHolder, T> extends BaseAdapter {

    protected Activity mActivity;
    protected List<T> mList;
    public LayoutInflater mInflater;

    public CommonBaseAdapter(Activity activity, List<T> list) {
        this.mActivity = activity;
        this.mList = list;
        this.mInflater = LayoutInflater.from(mActivity);
    }

    @Override
    public int getCount() {
        return this.mList.size();
    }

    @Override
    public T getItem(int position) {
        return this.mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //protected Drawable createCheckIcon(int color, int resId) {
    //    Drawable checkIcon = ContextCompat.getDrawable(mActivity, resId);
    //    checkIcon = DrawableCompat.wrap(checkIcon);
    //    DrawableCompat.setTint(checkIcon, color);
    //    return checkIcon;
    //}

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        VH holder = null;
        if ( view == null ) {
            holder = onCreateViewHolder(viewGroup, i);
            holder.view.setTag(holder);
        } else {
            holder = (VH) view.getTag();
        }

        onBindViewHolder(holder, i);
        return holder.view;
    }

    public View inflate(int resLayout, ViewGroup parent) {
        return mInflater.inflate(resLayout, parent, false);
    }

    public abstract VH onCreateViewHolder(ViewGroup parent, int position);
    public abstract void onBindViewHolder(VH holder, int position);

    public static class ViewHolder {
        View view;
        public ViewHolder(View view) {
            this.view = view;
        }
    }

}
