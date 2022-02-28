package org.jeonfeel.pilotproject1.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.jeonfeel.pilotproject1.R
import org.jeonfeel.pilotproject1.databinding.ItemRecyclerviewMainBinding
import org.jeonfeel.pilotproject1.data.remote.model.StarbucksMenuDTO
import org.jeonfeel.pilotproject1.data.sharedpreferences.Shared
import org.jeonfeel.pilotproject1.view.activity.MainActivity
import org.jeonfeel.pilotproject1.view.activity.StarbucksMenuDetailActivity
import kotlin.collections.ArrayList

class RecyclerviewMainAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerviewMainAdapter.ViewHolder>(), Filterable {

    private val TAG = RecyclerviewMainAdapter::class.java.simpleName
    private var recyclerViewMainItem: ArrayList<StarbucksMenuDTO> = ArrayList()
    private var filteredList = recyclerViewMainItem
    private var copyMainItem: ArrayList<StarbucksMenuDTO> = ArrayList()
    private var favoriteHashMap = hashMapOf<String, Int>()
    private var selectedItemPosition: Int? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerviewMainBinding.inflate(view, parent, false)

        return ViewHolder(binding)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemBinding(filteredList[position])
        holder.setItemClickListener(filteredList[position])
        holder.setFavoriteImage(filteredList[position])
        holder.setMarquee()
    }

    override fun getItemCount(): Int = filteredList.size

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    fun setRecyclerViewMainItem(newMenuDTO: ArrayList<StarbucksMenuDTO>) {
        val diffResult = DiffUtil.calculateDiff(
            RecyclerviewMainDiffUtil(recyclerViewMainItem, newMenuDTO),
            false
        )
        recyclerViewMainItem.clear()
        recyclerViewMainItem.addAll(newMenuDTO)
        copyMainItem.clear()
        copyMainItem.addAll(newMenuDTO)
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateSetting() {
        val settingDTO = Shared.getSettingDTO(context)
        val sortInfo = settingDTO.sortInfo
        val isCaffeine = settingDTO.isCaffeine

        if (sortInfo != context.resources.getInteger(R.integer.SORT_BASIC)) {

            if (isCaffeine) {
                filterCaffeine()
            } else {
                filteredList.clear()
                filteredList.addAll(copyMainItem)
            }
            when (sortInfo) {
                context.resources.getInteger(R.integer.SORT_LOW_KCAL) -> filteredList.sortBy { it.kcal.toInt() }
                context.resources.getInteger(R.integer.SORT_HIGH_KCAL) -> filteredList.sortByDescending { it.kcal.toInt() }
            }
            notifyDataSetChanged()
//            viewPagerAdapter.scrollToTop()
        } else {
            filteredList.clear()
            filteredList.addAll(copyMainItem)
            if (isCaffeine) {
                filterCaffeine()
            }
            val currentText = (context as MainActivity).getCurrentText()
            if (currentText.trim().isNotEmpty()) {
                filter.filter(currentText)
            } else {
                notifyDataSetChanged()
//                viewPagerAdapter.scrollToTop()
            }
        }
    }

    fun updateFavoriteImage(newHashMap: HashMap<String, Int>) {
        favoriteHashMap = newHashMap
        if (selectedItemPosition != null) {
            notifyItemChanged(selectedItemPosition!!)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(str: CharSequence?): FilterResults {
                val string = str.toString()
                filteredList = if (string.isEmpty()) {
                    recyclerViewMainItem
                } else {
                    val filteringList = ArrayList<StarbucksMenuDTO>()
                    for (i in 0 until recyclerViewMainItem.size) {
                        if (string in recyclerViewMainItem[i].product_NM) {
                            filteringList.add(recyclerViewMainItem[i])
                        }
                    }
                    filteringList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(str: CharSequence?, filterResults: FilterResults?) {
                filteredList = filterResults?.values as ArrayList<StarbucksMenuDTO>
                Log.d(TAG, filteredList.toString())
                notifyDataSetChanged()
            }
        }
    }

    private fun filterCaffeine() {
        val filteredCaffeineList = ArrayList<StarbucksMenuDTO>()
            for (i in 0 until filteredList.size) {
                if (filteredList[i].caffeine.toInt() == 0) {
                    filteredCaffeineList.add(filteredList[i])
                }
            }
            filteredList.clear()
            filteredList.addAll(filteredCaffeineList)
    }

    inner class ViewHolder(private val binding: ItemRecyclerviewMainBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //리사이클러뷰 아이템 바인딩
        fun itemBinding(starbucksMenuDTO: StarbucksMenuDTO) {
            binding.textviewRecyclerviewMainItemProductName.setHorizontallyScrolling(true)
            with(binding) {
                binding.itemRecyclerviewMain = starbucksMenuDTO
                executePendingBindings()
            }
        }

        fun setFavoriteImage(starbucksMenuDTO: StarbucksMenuDTO) {
            val favorite = favoriteHashMap[starbucksMenuDTO.product_CD]
            Log.d(TAG, favoriteHashMap.toString())
            if (favorite == null) {
                binding.imageviewRecyclerviewMainItemFavorite.setImageResource(R.drawable.img_favorite_unselected_2x)
            } else if (favorite == 0) {
                binding.imageviewRecyclerviewMainItemFavorite.setImageResource(R.drawable.img_favorite_2x)
            }
        }

        fun setItemClickListener(starbucksMenuDTO: StarbucksMenuDTO) {
            binding.cardviewRecyclerviewMainItem.setOnClickListener {
                val intent = Intent(context, StarbucksMenuDetailActivity::class.java)
                intent.putExtra("starbucksMenuDTO", starbucksMenuDTO)
                intent.putExtra("productCD", starbucksMenuDTO.product_CD)

                if (favoriteHashMap[starbucksMenuDTO.product_CD] != null) {
                    intent.putExtra("favoriteIsChecked", true)
                } else {
                    intent.putExtra("favoriteIsChecked", false)
                }
                (context as RecyclerViewMainListener).startForActivityResult(intent)
                selectedItemPosition = adapterPosition
            }
        }

        fun setMarquee() {
            binding.cardviewRecyclerviewMainItem.setOnLongClickListener {
                binding.textviewRecyclerviewMainItemProductName.isSelected = true
                true
            }
        }
    }
}

interface RecyclerViewMainListener {
    fun startForActivityResult(intent: Intent)
}

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