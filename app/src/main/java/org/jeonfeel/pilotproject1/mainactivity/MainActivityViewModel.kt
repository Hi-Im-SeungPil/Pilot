package org.jeonfeel.pilotproject1.mainactivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel

//뷰 모델 추후 설계
class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "MainActivityViewModel"
    private val mainRepository = MainRepository()
    private val _recyclerViewMainStarbucksMenu = mainRepository.getStarbucksMenuList()
    private val _recyclerViewMainStarbucksResource = mainRepository.getStarbucksMenuResource()

    fun getStarbucksMenuList() = _recyclerViewMainStarbucksMenu
    fun getStarbucksMenuResource() = _recyclerViewMainStarbucksResource

    fun updateStarbucksMenu(position: Int){
        val recyclerViewMainStarbucksResource = mainRepository.getStarbucksMenuResource(position)
        _recyclerViewMainStarbucksMenu.value?.clear()
        _recyclerViewMainStarbucksMenu.value = recyclerViewMainStarbucksResource
    }

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