package cn.finalteam.galleryfinal.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Desction:
 * Author:pengjianbo
 * Date:2015/12/24 0024 20:14
 */
public class GalleryImageView extends ImageView {
    public GalleryImageView(Context context) {
        super(context);
    }

    public GalleryImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GalleryImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


//    public void displayImage(String url) {
//        setImageUri(url, null);
//    }
//
//    public void displayImage(String url, int defaultResId) {
//        setImageResource(defaultResId);
//        setImageUri(url, null);
//    }
//
//    public void displayImage(String url, Drawable defaultImg) {
//        setImageDrawable(defaultImg);
//        setImageUri(url, null);
//    }
//
//    public void displayImage(String url, Bitmap defaultImg) {
//        setImageUri(url, null);
//        setImageBitmap(defaultImg);
//    }
//
//    public void displayImage(String url, ResizeOptions imageSize) {
//        setImageUri(url, imageSize);
//    }
//
//    public void displayImage(String url, int defaultResId, ResizeOptions imageSize) {
//        setImageResource(defaultResId);
//        setImageUri(url, imageSize);
//    }
//
//    @Override
//    protected void onDetachedFromWindow() {
//        mDraweeHolder.onDetach();
//        super.onDetachedFromWindow();
//    }
//
//    @Override
//    protected void onAttachedToWindow() {
//        init();
//        mDraweeHolder.onAttach();
//        super.onAttachedToWindow();
//    }
//
//    @Override
//    protected boolean verifyDrawable(Drawable dr) {
//        if (dr == mDraweeHolder.getHierarchy().getTopLevelDrawable()) {
//            return true;
//        }
//        return super.verifyDrawable(dr);
//    }
//
//    @Override
//    public void onStartTemporaryDetach() {
//        super.onStartTemporaryDetach();
//        mDraweeHolder.onDetach();
//    }
//
//    @Override
//    public void onFinishTemporaryDetach() {
//        super.onFinishTemporaryDetach();
//        mDraweeHolder.onAttach();
//    }
//
//    /**
//     * 加载远程图片
//     * @param url
//     * @param imageSize
//     */
//    private void setImageUri(String url, ResizeOptions imageSize) {
//        ImageRequest imageRequest = ImageRequestBuilder
//                .newBuilderWithSource(Uri.parse(url))
//                .setResizeOptions(imageSize)//图片目标大小
//                .build();
//        ImagePipeline imagePipeline = Fresco.getImagePipeline();
//        final DataSource<CloseableReference<CloseableImage>> dataSource = imagePipeline.fetchDecodedImage(imageRequest, this);
//        DraweeController controller = Fresco.newDraweeControllerBuilder()
//                .setOldController(mDraweeHolder.getController())
//                .setImageRequest(imageRequest)
//                .setControllerListener(new BaseControllerListener<ImageInfo>() {
//                    @Override
//                    public void onFinalImageSet(String s, ImageInfo imageInfo, Animatable animatable) {
//                        try {
//                            imageReference = dataSource.getResult();
//                            if (imageReference != null) {
//                                CloseableImage image = imageReference.get();
//                                if (image != null && image instanceof CloseableStaticBitmap) {
//                                    CloseableStaticBitmap closeableStaticBitmap = (CloseableStaticBitmap) image;
//                                    Bitmap bitmap = closeableStaticBitmap.getUnderlyingBitmap();
//                                    if (bitmap != null) {
//                                        setImageBitmap(bitmap);
//                                    }
//                                }
//                            }
//                        } finally {
//                            dataSource.close();
//                            CloseableReference.closeSafely(imageReference);
//                        }
//                    }
//                })
//                .setTapToRetryEnabled(true)
//                .build();
//        mDraweeHolder.setController(controller);
//    }

}
