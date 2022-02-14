package org.jeonfeel.pilotproject1.mainactivity

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Spacing(spacing: Int, spanCount: Int, edge: Boolean) : RecyclerView.ItemDecoration() {

    val _spacing = spacing
    val _spanCount = spanCount
    val _edge = edge

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        val column = position % _spanCount
        if (_edge) {
            outRect.left = _spacing - column * _spacing / _spanCount
            outRect.right = (column + 1) * _spacing / _spanCount

            if (position < _spanCount) {
                outRect.top = _spacing
            }
            outRect.bottom = _spacing
        }else {
            outRect.left = column * _spacing / _spanCount
            outRect.right = _spacing - (column + 1) * _spacing / _spanCount
            if (position >= _spanCount){
                outRect.top = _spacing
            }
        }
    }
}

