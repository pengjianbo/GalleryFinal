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

package cn.finalteam.galleryfinal.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

/**
 * Desction:
 * Author:pengjianbo
 * Date:15/12/7 下午7:32
 */
public class Utils {

    public static String getFileName(String pathandname) {
        int start = pathandname.lastIndexOf("/");
        int end = pathandname.lastIndexOf(".");
        if (start != -1 && end != -1) {
            return pathandname.substring(start + 1, end);
        } else {
            return null;
        }
    }

    /**
     * 保存Bitmap到文件
     * @param bitmap
     * @param format
     * @param target
     */
    public static void saveBitmap(Bitmap bitmap, Bitmap.CompressFormat format, File target) {
        if (target.exists()) {
            target.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(target);
            bitmap.compress(format, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap rotateBitmap(String path, int orientation, int screenWidth, int screenHeight) {
        Bitmap bitmap = null;
        final int maxWidth = screenWidth / 2;
        final int maxHeight = screenHeight / 2;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, options);
            int sourceWidth, sourceHeight;
            if (orientation == 90 || orientation == 270) {
                sourceWidth = options.outHeight;
                sourceHeight = options.outWidth;
            } else {
                sourceWidth = options.outWidth;
                sourceHeight = options.outHeight;
            }
            boolean compress = false;
            if (sourceWidth > maxWidth || sourceHeight > maxHeight) {
                float widthRatio = (float) sourceWidth / (float) maxWidth;
                float heightRatio = (float) sourceHeight / (float) maxHeight;

                options.inJustDecodeBounds = false;
                if (new File(path).length() > 512000) {
                    float maxRatio = Math.max(widthRatio, heightRatio);
                    options.inSampleSize = (int) maxRatio;
                    compress = true;
                }
                bitmap = BitmapFactory.decodeFile(path, options);
            } else {
                bitmap = BitmapFactory.decodeFile(path);
            }
            if (orientation > 0) {
                Matrix matrix = new Matrix();
                //matrix.postScale(sourceWidth, sourceHeight);
                matrix.postRotate(orientation);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }
            sourceWidth = bitmap.getWidth();
            sourceHeight = bitmap.getHeight();
            if ((sourceWidth > maxWidth || sourceHeight > maxHeight) && compress) {
                float widthRatio = (float) sourceWidth / (float) maxWidth;
                float heightRatio = (float) sourceHeight / (float) maxHeight;
                float maxRatio = Math.max(widthRatio, heightRatio);
                sourceWidth = (int) ((float) sourceWidth / maxRatio);
                sourceHeight = (int) ((float) sourceHeight / maxRatio);
                Bitmap bm = Bitmap.createScaledBitmap(bitmap, sourceWidth, sourceHeight, true);
                bitmap.recycle();
                return bm;
            }
        } catch (Exception e) {
        }
        return bitmap;
    }

    /**
     * 取某个范围的任意数
     * @param min
     * @param max
     * @return
     */
    public static int getRandom(int min, int max){
        Random random = new Random();
        int s = random.nextInt(max) % (max - min + 1) + min;
        return s;
    }
}
