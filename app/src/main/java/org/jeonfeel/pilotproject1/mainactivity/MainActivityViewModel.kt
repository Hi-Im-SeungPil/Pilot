package org.jeonfeel.pilotproject1.mainactivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel

//뷰 모델 추후 설계
class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "MainActivityViewModel"
    private val mainRepository = MainRepository()
    private val recyclerViewMainStarbucksMenu = mainRepository.getStarbucksMenuList()
    private val recyclerViewMainStarbucksResource = mainRepository.getStarbucksMenuResource()

    fun getStarbucksMenuList() = recyclerViewMainStarbucksMenu
    fun getStarbucksMenuResource() = recyclerViewMainStarbucksResource

    fun updateStarbucksMenu(position: Int){
        val recyclerViewMainStarbucksResource = mainRepository.getStarbucksMenuResource(position)
        recyclerViewMainStarbucksMenu.value?.clear()
        recyclerViewMainStarbucksMenu.value = recyclerViewMainStarbucksResource
    }
}