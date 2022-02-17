package org.jeonfeel.pilotproject1.view.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("coffeeImage")
fun loadCoffeeImage(view: ImageView, imageUrl: String){
    Glide.with(view.context)
        .load(imageUrl)
        .into(view)
}