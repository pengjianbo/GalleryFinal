package cn.finalteam.galleryfinal.utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Desction:释放imageview bitmap
 * Author:pengjianbo
 *
 * Date:2015/12/24 0024 18:43
 */
public class RecycleViewBitmapUtils {

    /**
     * 回收继承自ViewGroup的类,如GridView,ListView,LinearLayout,RelativeLayout等
     *
     * @param layout
     */
    public static void recycleViewGroup(ViewGroup layout) {
        if (layout == null) return;
        synchronized (layout) {
            for (int i = 0; i < layout.getChildCount(); i++) {
                View subView = layout.getChildAt(i);
                if (subView instanceof ViewGroup) {
                    recycleViewGroup((ViewGroup) subView);
                } else {
                    if (subView instanceof ImageView) {
                        recycleImageView((ImageView) subView);
                    }
                }
            }
        }
    }


    /**
     * 回收ImageView占用的图像内存;
     *
     * @param view
     */
    public static void recycleImageView(View view) {
        if (view == null) return;
        if (view instanceof ImageView) {
            Drawable drawable = ((ImageView) view).getDrawable();
            if (drawable instanceof BitmapDrawable) {
                Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();
                if (bmp != null && !bmp.isRecycled()) {
                    ((ImageView) view).setImageBitmap(null);
                    bmp.recycle();
                    bmp = null;
                }
            }
        }
    }
}
