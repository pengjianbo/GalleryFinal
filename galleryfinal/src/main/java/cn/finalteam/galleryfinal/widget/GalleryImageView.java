package cn.finalteam.galleryfinal.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Desction:
 * Author:pengjianbo
 * Date:2015/12/24 0024 20:14
 */
public class GalleryImageView extends ImageView {

    private ImageViewListener mImageViewListener;

    public GalleryImageView(Context context) {
        super(context);
    }

    public GalleryImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GalleryImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init() {
        mImageViewListener = new ImageViewListener() ;
    }

    public class ImageViewListener {
        void onDetach(){}
        void onAttach(){}
        //boolean verifyDrawable(Drawable dr){};

    }

    @Override
    protected void onDetachedFromWindow() {
        mImageViewListener.onDetach();
        super.onDetachedFromWindow();
    }

    @Override
    protected void onAttachedToWindow() {
        mImageViewListener.onAttach();
        super.onAttachedToWindow();
    }

    //@Override
    //protected boolean verifyDrawable(Drawable dr) {
    //    if (mImageViewListener.verifyDrawable(dr)) {
    //        return true;
    //    }
    //    return super.verifyDrawable(dr);
    //}

    @Override
    public void onStartTemporaryDetach() {
        super.onStartTemporaryDetach();
        mImageViewListener.onDetach();
    }

    @Override
    public void onFinishTemporaryDetach() {
        super.onFinishTemporaryDetach();
        mImageViewListener.onAttach();
    }

}
