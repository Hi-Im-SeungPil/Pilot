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
import retrofit2.Response

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "MainActivityViewModel"
    private val mainRepository = MainRepository(application.applicationContext)

    private var starbucksMenuJsonObject = JsonObject()
    private var recyclerViewMainList = ArrayList<StarbucksMenuDTO>()
    private var categoryList = listOf<String>()
    private var favorites = HashMap<String, Int>()

    private val _starbucksMenuLiveData = MutableLiveData<ArrayList<StarbucksMenuDTO>>()
    private val _favoriteLiveData = MutableLiveData<HashMap<String, Int>>()
    val starbucksMenuLiveData: LiveData<ArrayList<StarbucksMenuDTO>>
        get() = _starbucksMenuLiveData
    val favoriteLiveData: LiveData<HashMap<String, Int>>
        get() = _favoriteLiveData

    init {
        getStarbucksMenuJsonObj()
    }

    /**
     * 카테고리
     * */
    fun getCategoryList() = categoryList

    /**
     * 스타벅스 메뉴
     * */
    fun updateStarbucksMenu(position: Int) {
        _starbucksMenuLiveData.value?.clear()
        _starbucksMenuLiveData.value = getStarbucksMenu(position)
    }

    private fun getStarbucksMenuJsonObj() {
        var jsonObj = JsonObject()
        val job = viewModelScope.async(Dispatchers.IO) {
            val response = mainRepository.getStarbucksMenuList().execute()
            jsonObj = response.body()!!
        }
        if (job.isCompleted) {
            starbucksMenuJsonObject = jsonObj
            categoryList = jsonObj.keySet().toList()
        }
    }

    private fun getStarbucksMenu(categoryPosition: Int = -1): ArrayList<StarbucksMenuDTO> {
        val gson = Gson()
        val starbucksMenuDTOs = ArrayList<StarbucksMenuDTO>()
        if (categoryPosition == -1 && categoryList != null) {
            for (element in categoryList) {
                val categoryJsonObject = starbucksMenuJsonObject?.getAsJsonObject(element)
                val jsonArrayStarbucksMenu = categoryJsonObject?.getAsJsonArray("list")

                for (i in 0 until jsonArrayStarbucksMenu?.size()!!) {
                    Log.d(TAG, jsonArrayStarbucksMenu[i].toString())
                    val sampleItem =
                        gson.fromJson(jsonArrayStarbucksMenu[i], StarbucksMenuDTO::class.java)
                    starbucksMenuDTOs.add(sampleItem)
                }
            }
        } else if (categoryPosition != -1 && categoryList != null) {
            val categoryJsonObject =
                starbucksMenuJsonObject?.getAsJsonObject(categoryList[categoryPosition])
            val jsonArrayStarbucksMenu = categoryJsonObject?.getAsJsonArray("list")

            for (i in 0 until jsonArrayStarbucksMenu?.size()!!) {
                Log.d(TAG, jsonArrayStarbucksMenu[i].toString())
                val sampleItem =
                    gson.fromJson(jsonArrayStarbucksMenu[i], StarbucksMenuDTO::class.java)
                starbucksMenuDTOs.add(sampleItem)
            }
        }
        return starbucksMenuDTOs
    }

    /**
     * 즐겨찾기
     * */
    private fun getFavorites() {
        val favoritesHashMap = HashMap<String, Int>()
        var favoriteList = listOf<Favorite>()

        val job = viewModelScope.async(Dispatchers.IO) {
            favoriteList = mainRepository.getFavoritesInstance().selectAll()
            Log.d(TAG, favoriteList.toString())
            for (elements in favoriteList) {
                favoritesHashMap[elements.productCD] = 0
            }
        }
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