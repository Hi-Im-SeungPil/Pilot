package org.jeonfeel.pilotproject1.utils

import androidx.recyclerview.widget.DiffUtil
import org.jeonfeel.pilotproject1.data.remote.model.StarbucksMenuDTO

class RecyclerviewMainDiffUtil(
    private val oldList: ArrayList<StarbucksMenuDTO>,
    private val currentList: ArrayList<StarbucksMenuDTO>
) :
    DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = currentList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].product_CD == currentList[newItemPosition].product_CD
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == currentList[newItemPosition]
    }
}