package com.xzy.study.loadlargebitmap

import android.app.Activity
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.DisplayMetrics
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition

/**
 * load large bitmap
 * @author ：created by xzy.
 * @date ：2021/1/13
 */
internal object LoadBitmap2 {
    /**
     * use glide libs to load large bitmap from internet.
     * reference https://blog.csdn.net/weixin_40797204/article/details/79213664
     * @param context   activity
     * @param url       img url
     * @param imageView iv widget
     */
    fun loadBitmapWithGlide(context: Activity, url: String?, imageView: ImageView) {
        val display = context.windowManager.defaultDisplay
        val displayMetrics = DisplayMetrics()
        display.getRealMetrics(displayMetrics)
        Glide.with(context)
            .asBitmap()
            .load(url)
            .placeholder(R.mipmap.ic_launcher)
            .dontAnimate() // 设置宽度为屏幕宽度，高度为图片原始高度
            .into(object : SimpleTarget<Bitmap?>(displayMetrics.widthPixels, SIZE_ORIGINAL) {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap?>?
                ) {
                    // 设置 ImageView 控件宽度为最大宽度
                    val imageHeight = resource.height
                    val layoutParams = imageView.layoutParams
                    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                    layoutParams.height = imageHeight
                    imageView.layoutParams = layoutParams
                    // 使用 Glide 加载图片
                    Glide.with(context)
                        .load(url)
                        .placeholder(R.mipmap.ic_launcher)
                        .dontAnimate()
                        .fitCenter()
                        .into(imageView)
                }
            })
    }

    /**
     * use bitmap factory load large bitmap from local.
     * reference https://www.jianshu.com/p/590f61222637
     * @param res resource
     * @param resId resource id
     * @param imageView iv widget
     */
    fun loadBitmapWithBitmapFactory(res: Resources, resId: Int, imageView: ImageView) {
        // 获取原始图片尺寸
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(res, resId, options)
        val imageHeight = options.outHeight
        val imageWidth = options.outWidth
        // 图片类型，可以根据此进行过滤
        val imageType = options.outMimeType
        // 解码器创建缩小比例的图片
        val bitmap = decodeSampledBitmapFromResource(res, resId, imageWidth, imageHeight)
        imageView.setImageBitmap(bitmap)
    }

    private fun calculateInSampleSize(
        options: BitmapFactory.Options,
        reqWidth: Int,
        reqHeight: Int
    ): Int {
        // Raw height and width of image
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1
        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    private fun decodeSampledBitmapFromResource(
        res: Resources,
        resId: Int,
        reqWidth: Int,
        reqHeight: Int
    ): Bitmap {
        // First decode with inJustDecodeBounds=true to check dimensions
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeResource(res, resId, options)

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeResource(res, resId, options)
    }
}