package org.jeonfeel.pilotproject1.mainactivity

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

class DataBindingAdapter {
}
@BindingAdapter("coffeeImage")
fun loadCoffeeImage(view: ImageView, imageUrl: String){
    Glide.with(view.context)
        .load(imageUrl)
        .into(view)
}