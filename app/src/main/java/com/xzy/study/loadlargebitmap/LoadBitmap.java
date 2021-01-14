package com.xzy.study.loadlargebitmap;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

/**
 * load large bitmap
 * @author ：created by xzy.
 * @date ：2021/1/13
 */
class LoadBitmap {
    /**
     * use glide libs to load large bitmap from internet.
     * reference https://blog.csdn.net/weixin_40797204/article/details/79213664
     * @param context   activity
     * @param url       img url
     * @param imageView iv widget
     */
    public static void loadBitmapWithGlide(Activity context, String url, ImageView imageView) {
        Display display = context.getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getRealMetrics(displayMetrics);

        Glide.with(context)
                .asBitmap()
                .load(url)
                .placeholder(R.mipmap.ic_launcher)
                .dontAnimate()
                // 设置宽度为屏幕宽度，高度为图片原始高度
                .into(new SimpleTarget<Bitmap>(displayMetrics.widthPixels, Target.SIZE_ORIGINAL) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        // 设置 ImageView 控件宽度为最大宽度
                        int imageHeight = resource.getHeight();
                        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                        layoutParams.height = imageHeight;
                        imageView.setLayoutParams(layoutParams);
                        // 使用 Glide 加载图片
                        Glide.with(context)
                                .load(url)
                                .placeholder(R.mipmap.ic_launcher)
                                .dontAnimate()
                                .fitCenter()
                                .into(imageView);
                    }
                });
    }

    /**
     * use bitmap factory load large bitmap from local.
     * reference https://www.jianshu.com/p/590f61222637
     * @param res resource
     * @param resId resource id
     * @param imageView iv widget
     */
    public static void loadBitmapWithBitmapFactory(Resources res, int resId, ImageView imageView) {
        // 获取原始图片尺寸
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;
        // 图片类型，可以根据此进行过滤
        String imageType = options.outMimeType;
        // 解码器创建缩小比例的图片
        Bitmap bitmap = decodeSampledBitmapFromResource(res, resId, imageWidth, imageHeight);
        imageView.setImageBitmap(bitmap);
    }


    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
}
