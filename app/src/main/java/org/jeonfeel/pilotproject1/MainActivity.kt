package org.jeonfeel.pilotproject1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.jeonfeel.pilotproject1.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private var mBinding: ActivityMainBinding? = null
    private val binding get() = mBinding!!
    lateinit var recyclerviewMainAdapter: RecyclerviewMainAdapter
    val recyclerViewMainItem = ArrayList<StarbucksMenuDTO>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        supportActionBar?.hide()

        initRecyclerViewMain()
        getStarbucksMenuData()
    }
    // 리사이클러뷰 초기 세팅
    private fun initRecyclerViewMain() {
        val gridLayoutManager = GridLayoutManager(this,2)
        binding.RecyclerviewMain.layoutManager = gridLayoutManager
        recyclerviewMainAdapter = RecyclerviewMainAdapter(this)
        binding.RecyclerviewMain.adapter = recyclerviewMainAdapter
    }

    private fun getStarbucksMenuData() {
        val retrofit = RetrofitClient().getRetrofitClient()
        val service = retrofit.create(RetrofitService::class.java)

        service.getStarbucksMenu().enqueue(object: Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val jsonObjectColdBrew = response.body()?.getAsJsonObject("cold_brew")
                    val jsonObjectBrood = response.body()?.getAsJsonObject("brood")
                    val jsonObjectEspresso = response.body()?.getAsJsonObject("espresso")
                    val jsonObjectFrappuccino = response.body()?.getAsJsonObject("frappuccino")
                    val jsonObjectBlended = response.body()?.getAsJsonObject("blended")
                    val jsonObjectFizzo = response.body()?.getAsJsonObject("fizzo")
                    val jsonObjectJuice = response.body()?.getAsJsonObject("juice")
                    val jsonObjectEtc = response.body()?.getAsJsonObject("etc")

                    recyclerviewMainAdapter.recyclerViewMainItemColdBrew = insertListStarbucksMenu(jsonObjectColdBrew!!)
                    recyclerviewMainAdapter.recyclerViewMainItemBrood = insertListStarbucksMenu(jsonObjectBrood!!)
                    recyclerviewMainAdapter.recyclerViewMainItemEspresso = insertListStarbucksMenu(jsonObjectEspresso!!)
                    recyclerviewMainAdapter.recyclerViewMainItemFrappuccino = insertListStarbucksMenu(jsonObjectFrappuccino!!)
                    recyclerviewMainAdapter.recyclerViewMainItemBlended = insertListStarbucksMenu(jsonObjectBlended!!)
                    recyclerviewMainAdapter.recyclerViewMainItemFizzo = insertListStarbucksMenu(jsonObjectFizzo!!)
                    recyclerviewMainAdapter.recyclerViewMainItemJuice = insertListStarbucksMenu(jsonObjectJuice!!)
                    recyclerviewMainAdapter.recyclerViewMainItemEtc = insertListStarbucksMenu(jsonObjectEtc!!)

                    recyclerviewMainAdapter.recyclerViewMainItem = recyclerViewMainItem
                    recyclerviewMainAdapter.notifyDataSetChanged()
                }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.d(TAG,"Connect Fail")
                Log.d(TAG,t.toString())
            }
        })
    }

    fun insertListStarbucksMenu(jsonObjectStarbucksMenu: JsonObject) :ArrayList<StarbucksMenuDTO> {
        val gson = Gson()
        val starbucksMenuDTOs = ArrayList<StarbucksMenuDTO>()
        val jsonArrayStarbucksMenu = jsonObjectStarbucksMenu?.getAsJsonArray("list")

        for (i in 0 until jsonArrayStarbucksMenu?.size()!!) {
            Log.d(TAG,jsonArrayStarbucksMenu[i].toString())
            val sampleItem = gson.fromJson(jsonArrayStarbucksMenu[i], StarbucksMenuDTO::class.java)
            starbucksMenuDTOs.add(sampleItem)
            recyclerViewMainItem.add(sampleItem)
        }
        return starbucksMenuDTOs
    }
}
