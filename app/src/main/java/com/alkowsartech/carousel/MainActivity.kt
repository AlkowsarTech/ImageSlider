package com.alkowsartech.carousel

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alkowsartech.imageslideshow.models.SlideModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.banner1))
        imageList.add(SlideModel(R.drawable.banner2))
        imageList.add(SlideModel(R.drawable.banner3))
        imageList.add(SlideModel(R.drawable.banner4))

        imageView.setImageList(imageList)
    }
}