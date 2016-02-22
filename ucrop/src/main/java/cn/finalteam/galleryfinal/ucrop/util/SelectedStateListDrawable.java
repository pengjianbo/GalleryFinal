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

package cn.finalteam.galleryfinal.ucrop.util;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;

/**
 * Hack class to properly support state drawable back to Android 1.6
 */
public class SelectedStateListDrawable extends StateListDrawable {

    private int mSelectionColor;

    public SelectedStateListDrawable(Drawable drawable, int selectionColor) {
        super();
        this.mSelectionColor = selectionColor;
        addState(new int[]{android.R.attr.state_selected}, drawable);
        addState(new int[]{}, drawable);
    }

    @Override
    protected boolean onStateChange(int[] states) {
        boolean isStatePressedInArray = false;
        for (int state : states) {
            if (state == android.R.attr.state_selected) {
                isStatePressedInArray = true;
            }
        }
        if (isStatePressedInArray) {
            super.setColorFilter(mSelectionColor, PorterDuff.Mode.SRC_ATOP);
        } else {
            super.clearColorFilter();
        }
        return super.onStateChange(states);
    }

    @Override
    public boolean isStateful() {
        return true;
    }

}
