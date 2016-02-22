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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.IOException;

/**
 * Created by Oleksii Shliama (https://github.com/shliama).
 */
public class BitmapLoadUtils {

    private static final String TAG = "BitmapLoadUtils";

    @Nullable
    public static Bitmap decode(@NonNull Context context, @Nullable Uri uri,
                                int requiredWidth, int requiredHeight) throws Exception {
        if (uri == null) {
            return null;
        }

        final ParcelFileDescriptor parcelFileDescriptor = context.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor;
        if (parcelFileDescriptor != null) {
            fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        } else {
            return null;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
        options.inSampleSize = calculateInSampleSize(options, requiredWidth, requiredHeight);
        options.inJustDecodeBounds = false;

        Bitmap decodeSampledBitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            close(parcelFileDescriptor);
        }

        ExifInterface exif = getExif(uri);
        if (exif != null) {
            int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            // TODO Should not rotate bitmap but initially apply needed angle to the matrix
            return rotateBitmap(decodeSampledBitmap, exifToDegrees(exifOrientation));
        } else {
            return decodeSampledBitmap;
        }
    }

    public static Bitmap rotateBitmap(@Nullable Bitmap bitmap, int degrees) {
        if (bitmap != null && degrees != 0) {
            Matrix rotateMatrix = new Matrix();
            rotateMatrix.setRotate(degrees, bitmap.getWidth() / (float) 2, bitmap.getHeight() / (float) 2);

            Bitmap converted = Bitmap.createBitmap(bitmap, 0, 0,
                    bitmap.getWidth(), bitmap.getHeight(), rotateMatrix, true);
            if (bitmap != converted) {
                bitmap.recycle();
                bitmap = converted;
            }
        }
        return bitmap;
    }

    public static int calculateInSampleSize(@NonNull BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width lower or equal to the requested height and width.
            while ((height / inSampleSize) > reqHeight || (width / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    @Nullable
    private static ExifInterface getExif(@NonNull Uri imageUri) {
        try {
            return new ExifInterface(imageUri.getPath());
        } catch (IOException e) {
            Log.w(TAG, "getExif: ", e);
        }
        return null;
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    @SuppressWarnings("ConstantConditions")
    public static void close(@Nullable Closeable c) {
        if (c != null && c instanceof Closeable) { // java.lang.IncompatibleClassChangeError: interface not implemented
            try {
                c.close();
            } catch (IOException e) {
                // silence
            }
        }
    }

}
