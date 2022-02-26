package org.jeonfeel.pilotproject1.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.jeonfeel.pilotproject1.data.remote.model.StarbucksMenuDTO
import org.jeonfeel.pilotproject1.databinding.Viewpager2ItemBinding
import org.jeonfeel.pilotproject1.utils.GridLayoutManagerWrap

class ViewPagerAdapter(private val context: Context, private val itemSize: Int) :
    RecyclerView.Adapter<ViewPagerAdapter.CustomViewHolder>() {

    val recyclerviewMainAdapter = RecyclerviewMainAdapter(context)
    private val allCoffeeList = arrayListOf<ArrayList<StarbucksMenuDTO>>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewPagerAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = Viewpager2ItemBinding.inflate(view, parent, false)

        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerAdapter.CustomViewHolder, position: Int) {
        holder.itemInit()
    }

    override fun getItemCount(): Int {
        return itemSize + 1
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    fun setMainItem(array: ArrayList<StarbucksMenuDTO>) {
        recyclerviewMainAdapter.setRecyclerViewMainItem(array)
    }

    fun updateFavoriteImage(hash: HashMap<String, Int>) {
        recyclerviewMainAdapter.updateFavoriteImage(hash)
    }

    fun search(str: String) {
        recyclerviewMainAdapter.filter.filter(str)
    }

    inner class CustomViewHolder(private val binding: Viewpager2ItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun itemInit() {
            val gridLayoutManager = GridLayoutManagerWrap(context, 2)
//            binding.RecyclerviewMain.setHasFixedSize(true)
            binding.RecyclerviewMain.itemAnimator = null
            binding.RecyclerviewMain.apply {
                layoutManager = gridLayoutManager
                adapter = recyclerviewMainAdapter
            }
        }
    }
}