package org.jeonfeel.pilotproject1.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jeonfeel.pilotproject1.data.remote.model.StarbucksMenuDTO
import org.jeonfeel.pilotproject1.databinding.Viewpager2ItemBinding

class ViewPagerAdapter(
    private val context: Context,
) :
    RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

    private val TAG = ViewPagerAdapter::class.java.simpleName
    private var adapters = ArrayList<RecyclerviewMainAdapter>()
    private val allCoffee= ArrayList<ArrayList<StarbucksMenuDTO>>()
    private var favorites = HashMap<String, Int>()
    private var currentPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = Viewpager2ItemBinding.inflate(view, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerAdapter.ViewHolder, position: Int) {
        holder.itemInit()
    }

    override fun getItemCount(): Int {
        return allCoffee.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItem(allCoffee: ArrayList<ArrayList<StarbucksMenuDTO>>) {
        this.allCoffee.addAll(allCoffee)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    fun setCurrentPosition(currentPosition: Int) {
        this.currentPosition = currentPosition
        Log.e("TAG", this.currentPosition.toString())
    }

    fun search(str: String) {
        adapters[currentPosition].filter.filter(str)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setFavorites(favoriteHash: HashMap<String, Int>) {
        favorites = favoriteHash
        notifyDataSetChanged()
    }

    fun getFavorites(): HashMap<String, Int> {
        return this.favorites
    }

//    fun updateCurrentView(arr: ArrayList<StarbucksMenuDTO>) {
//        allCoffee[currentPosition] = arr
//    }

    inner class ViewHolder(private val binding: Viewpager2ItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun itemInit() {
            val recyclerviewMainAdapter = RecyclerviewMainAdapter(context,this@ViewPagerAdapter)
            val gridLayoutManager = GridLayoutManagerWrap(context, 2)
            binding.rvMain.layoutManager = gridLayoutManager
            binding.rvMain.adapter = recyclerviewMainAdapter
            recyclerviewMainAdapter.setItem(allCoffee[adapterPosition] as ArrayList<StarbucksMenuDTO>)

            if (adapters.size == allCoffee.size) {
                adapters[adapterPosition] = recyclerviewMainAdapter
            } else {
                adapters.add(recyclerviewMainAdapter)
            }

            Log.e(TAG, "adapters size=> ${adapters.size.toString()}")
            Log.e(TAG, "adapterPosition => ${adapterPosition.toString()}")
        }
    }
}

class GridLayoutManagerWrap(context: Context?, spanCount: Int) :
    GridLayoutManager(context, spanCount) {

    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }
}dd