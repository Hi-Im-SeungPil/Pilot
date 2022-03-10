package org.jeonfeel.pilotproject1.view.adapter

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
    private val allCoffee: ArrayList<ArrayList<StarbucksMenuDTO>>
) :
    RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

    private val TAG = ViewPagerAdapter::class.java.simpleName

    private var adapters = ArrayList<RecyclerviewMainAdapter>()
    private var currentPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = Viewpager2ItemBinding.inflate(view, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerAdapter.ViewHolder, position: Int) {
        holder.itemInit(position)
    }

    override fun getItemCount(): Int {
        return allCoffee.size
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

    fun setFavorite(favorites: HashMap<String, Int>) {
        if(favorites.size != 0){
            Log.d(TAG,favorites.values.groupBy { favorites.keys }.toString())
        }
    }

    fun updateFavoriteImage(hash: HashMap<String, Int>) {
        adapters[currentPosition].updateFavoriteImage(hash)
    }

    inner class ViewHolder(private val binding: Viewpager2ItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun itemInit(position: Int) {
            val recyclerviewMainAdapter = RecyclerviewMainAdapter(context, allCoffee[position])
            val gridLayoutManager = GridLayoutManagerWrap(context, 2)
            binding.rvMain.layoutManager = gridLayoutManager
            binding.rvMain.adapter = recyclerviewMainAdapter
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
}