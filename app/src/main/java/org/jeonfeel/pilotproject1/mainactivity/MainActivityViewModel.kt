package org.jeonfeel.pilotproject1.mainactivity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData


class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "MainActivityViewModel"
    private val mainRepository = MainRepository()
    private val recyclerViewMainStarbucksMenu = mainRepository.getStarbucksMenuList()
    private val categoryList = mainRepository.getCategoryList()
    private val recyclerViewMainStarbucksResource = mainRepository.getStarbucksMenuResource()
    private val starbucksMenuLiveData = MutableLiveData(recyclerViewMainStarbucksMenu)

    fun getStarbucksMenuLiveData() = starbucksMenuLiveData
    fun getStarbucksMenuResource() = recyclerViewMainStarbucksResource
    fun getCategoryList() = categoryList

    fun updateStarbucksMenu(position: Int) {
        val recyclerViewMainStarbucksResource = mainRepository.getStarbucksMenuResource(position)
        starbucksMenuLiveData.value?.clear()
        starbucksMenuLiveData.value = recyclerViewMainStarbucksResource
    }
}