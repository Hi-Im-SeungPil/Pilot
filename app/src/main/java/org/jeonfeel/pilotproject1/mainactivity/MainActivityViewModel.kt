package org.jeonfeel.pilotproject1.mainactivity

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import org.jeonfeel.pilotproject1.retrofit.RetrofitClient
import org.jeonfeel.pilotproject1.retrofit.RetrofitService
import retrofit2.Call
import kotlin.concurrent.thread

//뷰 모델 추후 설계
class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "MainActivityViewModel"
    private val _recyclerViewMainItem = ArrayList<StarbucksMenuDTO>()
    private val _recyclerViewMainLivedata: MutableLiveData<ArrayList<StarbucksMenuDTO>> by lazy {
        MutableLiveData<ArrayList<StarbucksMenuDTO>>()
    }
    val recyclerViewMainLivedata : MutableLiveData<ArrayList<StarbucksMenuDTO>> = _recyclerViewMainLivedata

//    fun insertListStarbucksMenu(jsonObjectStarbucksMenu: JsonObject) :ArrayList<StarbucksMenuDTO> {
//        val gson = Gson()
//        val starbucksMenuDTOs = ArrayList<StarbucksMenuDTO>()
//        val jsonArrayStarbucksMenu = jsonObjectStarbucksMenu?.getAsJsonArray("list")
//
//        for (i in 0 until jsonArrayStarbucksMenu?.size()!!) {
//            Log.d(TAG,jsonArrayStarbucksMenu[i].toString())
//            val sampleItem = gson.fromJson(jsonArrayStarbucksMenu[i], StarbucksMenuDTO::class.java)
//            starbucksMenuDTOs.add(sampleItem)
//            recyclerViewMainItem.add(sampleItem)
//        }
//        return starbucksMenuDTOs
//    }
}