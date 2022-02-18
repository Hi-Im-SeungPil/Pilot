package org.jeonfeel.pilotproject1.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.jeonfeel.pilotproject1.data.remote.model.StarbucksMenuDTO
import org.jeonfeel.pilotproject1.repository.MainRepository


class MainActivityViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "MainActivityViewModel"
    private val mainRepository = MainRepository(application.applicationContext)

    private val recyclerViewMainList = mainRepository.getStarbucksMenuList()
    private val categoryList = mainRepository.getCategoryList()
    private val recyclerViewMainStarbucksResource = mainRepository.getStarbucksMenuResource()
    private val favorites = mainRepository.getFavorites()

    private val _starbucksMenuLiveData = MutableLiveData(recyclerViewMainList)
    private val _favoriteLiveData = MutableLiveData(favorites)
    private val starbucksMenuLiveData: LiveData<ArrayList<StarbucksMenuDTO>> = _starbucksMenuLiveData
    private val favoriteLiveData: LiveData<HashMap<String, Int>> = _favoriteLiveData

    fun getStarbucksMenuLiveData() = starbucksMenuLiveData
    fun getCategoryList() = categoryList
    fun getFavoriteLiveData() = favoriteLiveData
    fun getStarbucksMenuResource() = recyclerViewMainStarbucksResource

    fun updateStarbucksMenu(position: Int) {
        _starbucksMenuLiveData.value?.clear()
        _starbucksMenuLiveData.value = mainRepository.updateStarbucksMenu(position)
    }

    fun insertFavorite(productCD: String, favoriteHashMap: HashMap<String, Int>) {
        mainRepository.insertFavorite(productCD)
        _favoriteLiveData.value = favoriteHashMap
    }

    fun deleteFavorite(productCD: String, favoriteHashMap: HashMap<String, Int>) {
        mainRepository.deleteFavorite(productCD)
        _favoriteLiveData.value = favoriteHashMap
    }
}