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

package cn.finalteam.galleryfinal.ucrop.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import cn.finalteam.galleryfinal.ucrop.R;

public class UCropView extends FrameLayout {

    private final GestureCropImageView mGestureCropImageView;
    private final OverlayView mViewOverlay;

    public UCropView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UCropView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.ucrop_view, this, true);
        mGestureCropImageView = (GestureCropImageView) findViewById(R.id.image_view_crop);
        mViewOverlay = (OverlayView) findViewById(R.id.view_overlay);

        mGestureCropImageView.setCropBoundsChangeListener(new CropImageView.CropBoundsChangeListener() {
            @Override
            public void onCropBoundsChangedRotate(float cropRatio) {
                if (mViewOverlay != null) {
                    mViewOverlay.setTargetAspectRatio(cropRatio);
                    mViewOverlay.postInvalidate();
                }
            }
        });

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ucrop_UCropView);
        mViewOverlay.processStyledAttributes(a);
        mGestureCropImageView.processStyledAttributes(a);
        a.recycle();
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @NonNull
    public GestureCropImageView getCropImageView() {
        return mGestureCropImageView;
    }

    @NonNull
    public OverlayView getOverlayView() {
        return mViewOverlay;
    }

}