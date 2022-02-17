package org.jeonfeel.pilotproject1.utils

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager

class GridLayoutManagerWrap(context: Context?, spanCount: Int) :
    GridLayoutManager(context, spanCount) {

    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }
}