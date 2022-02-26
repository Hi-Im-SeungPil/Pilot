package org.jeonfeel.pilotproject1.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import org.jeonfeel.pilotproject1.data.database.entity.Favorite
import org.jeonfeel.pilotproject1.data.remote.model.StarbucksMenuDTO
import org.jeonfeel.pilotproject1.repository.MainRepository


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "MainActivityViewModel"
    private val mainRepository = MainRepository(application.applicationContext)

    private var starbucksMenuJsonObject: JsonObject? = null
    private var categoryList: List<String>? = null
    private var recyclerViewMainList: ArrayList<StarbucksMenuDTO>? = null
    private var favorites: HashMap<String, Int>? = null

    private val _starbucksMenuLiveData: MutableLiveData<ArrayList<StarbucksMenuDTO>> =
        MutableLiveData<ArrayList<StarbucksMenuDTO>>()
    val starbucksMenuLiveData: LiveData<ArrayList<StarbucksMenuDTO>> get() = _starbucksMenuLiveData

    private val _favoriteLiveData: MutableLiveData<HashMap<String, Int>> =
        MutableLiveData<HashMap<String, Int>>()
    val favoriteLiveData: LiveData<HashMap<String, Int>> get() = _favoriteLiveData

    private val _categoryLiveData: MutableLiveData<List<String>> = MutableLiveData<List<String>>()
    val categoryLiveData: LiveData<List<String>> get() = _categoryLiveData

    fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            getStarbucksMenuJsonObj()
            getCategoryList()
            getStarbucksMenu()
            getFavorites()
        }
    }

    /**
     * 카테고리
     * */
    fun getCategory() = categoryList

    private suspend fun getCategoryList() {
        categoryList = starbucksMenuJsonObject?.keySet()?.toList()
        _categoryLiveData.postValue(categoryList)
    }

    /**
     * 스타벅스 메뉴
     * */
    fun updateStarbucksMenu(position: Int = -1) {
        _starbucksMenuLiveData.value?.clear()
        getStarbucksMenu(position)
    }

    private suspend fun getStarbucksMenuJsonObj() {
        val response = mainRepository.getStarbucksMenuListCall().execute()
        if (response.isSuccessful) {
            starbucksMenuJsonObject = response.body()!!
        } else {
            // TODO:
        }
    }

    private fun getStarbucksMenu(categoryPosition: Int = -1) {
        val gson = Gson()
        val starbucksMenuDTOs = ArrayList<StarbucksMenuDTO>() //category setting
        if (categoryPosition == -1) {
            for (element in categoryList!!) {
                val categoryJsonObject = starbucksMenuJsonObject?.getAsJsonObject(element)
                val jsonArrayStarbucksMenu = categoryJsonObject?.getAsJsonArray("list")

                for (i in 0 until jsonArrayStarbucksMenu?.size()!!) {
                    Log.d(TAG, jsonArrayStarbucksMenu[i].toString())
                    val sampleItem =
                        gson.fromJson(jsonArrayStarbucksMenu[i], StarbucksMenuDTO::class.java)
                    starbucksMenuDTOs.add(sampleItem)
                }
            }
        } else {
            val categoryJsonObject =
                starbucksMenuJsonObject?.getAsJsonObject(categoryList?.get(categoryPosition))
            val jsonArrayStarbucksMenu = categoryJsonObject?.getAsJsonArray("list")

            for (i in 0 until jsonArrayStarbucksMenu?.size()!!) {
                Log.d(TAG, jsonArrayStarbucksMenu[i].toString())
                val sampleItem =
                    gson.fromJson(jsonArrayStarbucksMenu[i], StarbucksMenuDTO::class.java)
                starbucksMenuDTOs.add(sampleItem)
            }
        }
        recyclerViewMainList = starbucksMenuDTOs
        _starbucksMenuLiveData.postValue(recyclerViewMainList)
    }

    /**
     * 즐겨찾기
     * */
    private suspend fun getFavorites() {
        val favoritesHashMap = HashMap<String, Int>()
        val favoriteList = mainRepository.getFavoritesInstance().selectAll()
        for (elements in favoriteList) {
            favoritesHashMap[elements.productCD] = 0
        }
        favorites = favoritesHashMap
        _favoriteLiveData.postValue(favorites)
    }

    fun checkFavorite(productCD: String, favoriteIsChecked: Boolean) {
        val favoriteHashMap = favoriteLiveData.value

        if (favoriteIsChecked && favoriteHashMap?.get(productCD) == null) {
            favoriteHashMap?.set(productCD, 0)
            _favoriteLiveData.value = favoriteHashMap
            insertFavorite(productCD)
        } else if (!favoriteIsChecked && favoriteHashMap?.get(productCD) != null) {
            favoriteHashMap.remove(productCD)
            _favoriteLiveData.value = favoriteHashMap
            deleteFavorite(productCD)
        }
    }

    private fun insertFavorite(productCD: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val favorite = Favorite(productCD)
            mainRepository.getFavoritesInstance().insert(favorite)
        }
    }

    private fun deleteFavorite(productCD: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val favorite = Favorite(productCD)
            mainRepository.getFavoritesInstance().delete(favorite)
        }
    }
}