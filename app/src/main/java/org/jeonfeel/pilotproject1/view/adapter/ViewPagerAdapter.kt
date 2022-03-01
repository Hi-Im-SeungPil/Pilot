package org.jeonfeel.pilotproject1.view.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.jeonfeel.pilotproject1.data.remote.model.StarbucksMenuDTO
import org.jeonfeel.pilotproject1.databinding.Viewpager2ItemBinding
import org.jeonfeel.pilotproject1.utils.GridLayoutManagerWrap
import org.jeonfeel.pilotproject1.view.activity.MainActivity


class ViewPagerAdapter(private val context: Context,private val itemCount: Int) :
    RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>() {

    private val TAG = ViewPagerAdapter::class.java.simpleName
    val recyclerviewMainAdapter = RecyclerviewMainAdapter(context)
//    private var allCoffee = ArrayList<ArrayList<StarbucksMenuDTO>>()
//    private var adapters = ArrayList<RecyclerviewMainAdapter>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = Viewpager2ItemBinding.inflate(view, parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerAdapter.ViewHolder, position: Int) {
        holder.itemInit()
    }

    override fun getItemCount(): Int {
        return itemCount
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun search(str: String) {
        recyclerviewMainAdapter.filter.filter(str)
//        test.search(str)
    }

    fun setMainItem(newMenuDTO: ArrayList<StarbucksMenuDTO>) {
        recyclerviewMainAdapter.setRecyclerViewMainItem(newMenuDTO)
    }

    fun updateFavoriteImage(hash: HashMap<String, Int>) {
        recyclerviewMainAdapter.updateFavoriteImage(hash)
    }

    fun updateSetting() {
        recyclerviewMainAdapter.updateSetting()
    }

    fun scrollToTop() {
//        binding.RecyclerviewMain.scrollToPosition(0)
    }

    inner class ViewHolder(private val binding: Viewpager2ItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun itemInit() {

            val gridLayoutManager = GridLayoutManagerWrap(context, 2)
            binding.RecyclerviewMain.layoutManager = gridLayoutManager
//            recyclerviewMainAdapter.setRecyclerViewMainItem(allCoffee[adapterPosition])
            binding.RecyclerviewMain.adapter = recyclerviewMainAdapter
            Log.d(TAG,adapterPosition.toString())

//            adapters.add(recyclerviewMainAdapter)
        }

//        override fun search(str: String) {
//            adapters[(context as MainActivity).getCurrentPosition()].filter.filter(str)
//        }
    }
}

interface Test {
    fun search(str: String)
}

