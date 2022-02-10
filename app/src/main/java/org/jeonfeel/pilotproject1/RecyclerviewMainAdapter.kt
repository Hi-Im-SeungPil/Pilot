package org.jeonfeel.pilotproject1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecyclerviewMainAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerviewMainAdapter.ViewHolder>() {

    var recyclerViewMainItem = ArrayList<StarbucksMenuDTO>()
    var recyclerViewMainItemColdBrew = ArrayList<StarbucksMenuDTO>()
    var recyclerViewMainItemBrood = ArrayList<StarbucksMenuDTO>()
    var recyclerViewMainItemEspresso = ArrayList<StarbucksMenuDTO>()
    var recyclerViewMainItemFrappuccino = ArrayList<StarbucksMenuDTO>()
    var recyclerViewMainItemBlended = ArrayList<StarbucksMenuDTO>()
    var recyclerViewMainItemFizzo = ArrayList<StarbucksMenuDTO>()
    var recyclerViewMainItemJuice = ArrayList<StarbucksMenuDTO>()
    var recyclerViewMainItemEtc = ArrayList<StarbucksMenuDTO>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recyclerview_main, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemBinding(recyclerViewMainItem[position])
    }

    override fun getItemCount(): Int = recyclerViewMainItem.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textview_recyclerview_main_item_productName: TextView =
            itemView.findViewById(R.id.textview_recyclerview_main_item_productName)
        private val textview_recyclerview_main_item_kcal: TextView =
            itemView.findViewById(R.id.textview_recyclerview_main_item_kcal)
        private val imageview_recyclerview_main_item: ImageView =
            itemView.findViewById(R.id.imageview_recyclerview_main_item)
        //리사이클러뷰 아이템 바인딩
        fun itemBinding(starbucksMenuDTO: StarbucksMenuDTO) {
            Glide.with(context).load(starbucksMenuDTO.file_PATH).into(imageview_recyclerview_main_item)
            textview_recyclerview_main_item_productName.text = starbucksMenuDTO.product_NM
            textview_recyclerview_main_item_kcal.text = "${starbucksMenuDTO.kcal} Kcal"
        }
    }
}