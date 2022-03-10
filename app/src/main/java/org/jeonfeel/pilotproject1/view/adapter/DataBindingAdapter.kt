package org.jeonfeel.pilotproject1.view.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("loadCoffeeImage")
fun loadCoffeeImage(view: ImageView, imageUrl: String) {
    Glide.with(view.context)
        .load(imageUrl)
        .into(view)
}

@BindingAdapter("loadSrc")
fun loadSrc(button: androidx.appcompat.widget.AppCompatButton, resId: Int) {
    button.setBackgroundResource(resId)
}


