package org.jeonfeel.pilotproject1.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.jeonfeel.pilotproject1.data.remote.model.StarbucksMenuDTO
import org.jeonfeel.pilotproject1.databinding.Viewpager2ItemBinding
import org.jeonfeel.pilotproject1.utils.GridLayoutManagerWrap

class ViewPagerAdapter(private val context: Context,private val itemCount: Int) :
    RecyclerView.Adapter<ViewPagerAdapter.CustomViewHolder>() {

    private val recyclerviewMainAdapter = RecyclerviewMainAdapter(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = Viewpager2ItemBinding.inflate(view, parent, false)

        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerAdapter.CustomViewHolder, position: Int) {
        holder.itemInit()
    }

    override fun getItemCount(): Int {
        return itemCount
    }

    fun search(str: String) {
        recyclerviewMainAdapter.filter.filter(str)
    }

    fun setMainItem(array: ArrayList<StarbucksMenuDTO>) {
        recyclerviewMainAdapter.setRecyclerViewMainItem(array)
    }

    fun updateFavoriteImage(hash: HashMap<String, Int>) {
        recyclerviewMainAdapter.updateFavoriteImage(hash)
    }

    fun filterCaffeine(isCaffeine: Int) {
        recyclerviewMainAdapter.filterCaffeine(isCaffeine)
    }

    inner class CustomViewHolder(private val binding: Viewpager2ItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun itemInit() {
                val gridLayoutManager = GridLayoutManagerWrap(context, 2)
                binding.RecyclerviewMain.layoutManager = gridLayoutManager
                binding.RecyclerviewMain.adapter = recyclerviewMainAdapter
            }
    }
}