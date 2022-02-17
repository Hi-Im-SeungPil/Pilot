package org.jeonfeel.pilotproject1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.jeonfeel.pilotproject1.data.database.entity.Favorite
import org.jeonfeel.pilotproject1.repository.MainRepository


class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "MainActivityViewModel"
    private val mainRepository = MainRepository(application.applicationContext)

    private val recyclerViewMainStarbucksMenu = mainRepository.getStarbucksMenuList()
    private val categoryList = mainRepository.getCategoryList()
    private val recyclerViewMainStarbucksResource = mainRepository.getStarbucksMenuResource()
    private val favorites = mainRepository.getFavorites()

    private val starbucksMenuLiveData = MutableLiveData(recyclerViewMainStarbucksMenu)
    private val favoriteLiveData = MutableLiveData(favorites)

    fun getStarbucksMenuLiveData() = starbucksMenuLiveData
    fun getStarbucksMenuResource() = recyclerViewMainStarbucksResource
    fun getCategoryList() = categoryList
    fun getFavoriteLiveData() = favoriteLiveData

    fun updateStarbucksMenu(position: Int) {
        starbucksMenuLiveData.value?.clear()
        starbucksMenuLiveData.value = mainRepository.updateStarbucksMenu(position)
    }

    fun insertFavorite(productCD: String) {
        mainRepository.insertFavorite(productCD)
    }

    fun deleteFavorite(productCD: String) {
        mainRepository.deleteFavorite(productCD)
    }
}