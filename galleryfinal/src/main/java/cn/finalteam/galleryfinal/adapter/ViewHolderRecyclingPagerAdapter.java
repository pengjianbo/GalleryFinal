package cn.finalteam.galleryfinal.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cn.finalteam.toolsfinal.adapter.RecyclingPagerAdapter;

/**
 * Desction:
 * Author:pengjianbo
 * Date:2015/12/29 0029 16:09
 */
public abstract class ViewHolderRecyclingPagerAdapter<VH extends ViewHolderRecyclingPagerAdapter.ViewHolder, T> extends RecyclingPagerAdapter {
    private Context mContext;
    private List<T> mList;
    private LayoutInflater mInflater;

    public ViewHolderRecyclingPagerAdapter(Context context, List<T> list) {
        this.mContext = context;
        this.mList= list;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {
        VH holder;
        if (convertView == null) {
            holder = onCreateViewHolder(container, position);
            holder.view.setTag(holder);
        } else {
            holder = (VH) convertView.getTag();
        }

        onBindViewHolder(holder, position);
        return holder.view;
    }

    @Override
    public int getCount() {
        return this.mList.size();
    }

    public abstract VH onCreateViewHolder(ViewGroup parent, int position);

    public abstract void onBindViewHolder(VH holder, int position);

    public View inflate(int resLayout, ViewGroup parent) {
        return mInflater.inflate(resLayout, parent, false);
    }

    /**
     * 返回列表数据
     *
     * @return
     */
    public List<T> getDatas() {
        return this.mList;
    }

    public Context getContext() {
        return this.mContext;
    }

    public LayoutInflater getLayoutInflater() {
        return this.mInflater;
    }

    public static class ViewHolder {
        View view;

        public ViewHolder(View view) {
            this.view = view;
        }
    }
}
