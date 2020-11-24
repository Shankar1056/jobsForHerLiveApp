package com.jobsforher.data.model

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

data class HomeBannerData (
    var image : Int ?= null
)

@BindingAdapter("homeBannerImage")
fun homeBannerImage(view: ImageView, imageUrl: Int?) {
    /*Glide.with(view.context)
        .load(imageUrl).apply(RequestOptions().circleCrop())
        .into(view)*/
    if (imageUrl != null) {
        view.setImageResource(imageUrl)
    }
}