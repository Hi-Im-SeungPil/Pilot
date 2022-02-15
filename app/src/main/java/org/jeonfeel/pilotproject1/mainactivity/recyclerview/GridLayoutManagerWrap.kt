package org.jeonfeel.pilotproject1.mainactivity.recyclerview

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager

class GridLayoutManagerWrap(context: Context?, spanCount: Int) :
    GridLayoutManager(context, spanCount) {

    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }
}