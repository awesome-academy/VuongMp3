package com.vuongnq14798.vuongmp3.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.vuongnq14798.vuongmp3.R

object ImageUtils {

    fun loadImage(image: ImageView, link: Any?) {
        Glide
            .with(image.context)
            .load(link)
            .error(R.drawable.ic_my_music)
            .into(image)
    }
}
