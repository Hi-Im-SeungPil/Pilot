package org.jeonfeel.pilotproject1.mainactivity.recyclerview

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import org.jeonfeel.pilotproject1.databinding.ItemRecyclerviewMainBinding
import org.jeonfeel.pilotproject1.mainactivity.MainActivity
import org.jeonfeel.pilotproject1.mainactivity.StarbucksMenuDTO
import kotlin.collections.ArrayList

class RecyclerviewMainAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerviewMainAdapter.ViewHolder>(), Filterable {
    private var recyclerViewMainItem: ArrayList<StarbucksMenuDTO> = ArrayList()
    private var filteredList = recyclerViewMainItem
    private var copyMainItem: ArrayList<StarbucksMenuDTO> = ArrayList()
    private var tempMainItem: ArrayList<StarbucksMenuDTO> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerviewMainBinding.inflate(view, parent, false)

        return ViewHolder(binding)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemBinding(filteredList[position])
        holder.setMarquee()
    }

    override fun getItemCount(): Int = filteredList.size

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    fun setRecyclerViewMainItem(newMenuDTO: ArrayList<StarbucksMenuDTO>) {
        val diffResult = DiffUtil.calculateDiff(RecyclerviewMainDiffUtil(recyclerViewMainItem, newMenuDTO), false)
        recyclerViewMainItem.clear()
        recyclerViewMainItem.addAll(newMenuDTO)
        copyMainItem.clear()
        copyMainItem.addAll(newMenuDTO)
        diffResult.dispatchUpdatesTo(this)
        Log.d("Recyclerview",recyclerViewMainItem.size.toString())
    }

    fun updateSetting(sortInfo: Int) {
        if (sortInfo != 0) {
            when (sortInfo) {
                -1 -> filteredList.sortBy{ it.kcal.toInt() }
                1 -> filteredList.sortByDescending{ it.kcal.toInt() }
            }
            notifyItemRangeChanged(0, filteredList.size)
        } else {
            recyclerViewMainItem.clear()
            recyclerViewMainItem.addAll(copyMainItem)
            val editTextIsEmpty = (context as MainActivity?)!!.getFilterInRecyclerviewAdapter()
            if (editTextIsEmpty) {
                notifyItemRangeChanged(0, itemCount)
            } else {
                noti
            }
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
            override fun publishResults(str: CharSequence?, filterResults: FilterResults?) {
                filteredList = filterResults?.values as ArrayList<StarbucksMenuDTO>
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(private val binding: ItemRecyclerviewMainBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //리사이클러뷰 아이템 바인딩
        fun itemBinding(starbucksMenuDTO: StarbucksMenuDTO) {
            with(binding){
                binding.itemRecyclerviewMain = starbucksMenuDTO
                executePendingBindings()
            }
        }

        fun setMarquee(){
            binding.textviewRecyclerviewMainItemProductName.setHorizontallyScrolling(true)
            binding.textviewRecyclerviewMainItemProductName.isSelected = true
        }
    }
}