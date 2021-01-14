package com.xzy.study.loadlargebitmap

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val imgUrl =
        "https://api.nongfucang.cn/uploads/20201104/d094872956079d9936d8239c85c3911b.jpg"
    private lateinit var imageView: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        imageView = findViewById(R.id.imageView)
    }

    fun click(view: View) {
        val display = windowManager.defaultDisplay
        val displayMetrics = DisplayMetrics()
        display.getRealMetrics(displayMetrics)
        Log.d("xzy", "${displayMetrics.widthPixels}--${displayMetrics.heightPixels}")
        LoadBitmap.loadBitmapWithGlide(this, imgUrl, imageView)
    }

    fun click2(view: View) {
        LoadBitmap.loadBitmapWithBitmapFactory(resources, R.mipmap.test, imageView);
    }

}