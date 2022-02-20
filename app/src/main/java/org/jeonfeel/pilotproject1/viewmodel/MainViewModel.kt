package org.jeonfeel.pilotproject1.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.jeonfeel.pilotproject1.data.remote.model.StarbucksMenuDTO
import org.jeonfeel.pilotproject1.repository.MainRepository

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "MainActivityViewModel"
    private val mainRepository = MainRepository(application.applicationContext)

    private var starbucksMenuJsonObject = getStarbucksMenuJsonObj()
    private val categoryList = starbucksMenuJsonObject?.keySet()?.toList()
    private val recyclerViewMainList = getStarbucksMenu()
    private val favorites = getFavorites()

    private val _starbucksMenuLiveData = MutableLiveData(recyclerViewMainList)
    private val _favoriteLiveData = MutableLiveData(favorites)
    val starbucksMenuLiveData: LiveData<ArrayList<StarbucksMenuDTO>>
        get() = _starbucksMenuLiveData
    val favoriteLiveData: LiveData<HashMap<String, Int>>
        get() = _favoriteLiveData

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

    private fun getStarbucksMenuJsonObj(): JsonObject? {
        var starbucksMenuObject: JsonObject? = null
        val thread = Thread {
            try {
                val response = mainRepository.getStarbucksMenuList().execute()
                if (response.isSuccessful) {
                    starbucksMenuObject = response.body()!!
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        thread.start()
        thread.join(3000)

        return starbucksMenuObject
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
        } else if(categoryPosition != -1 && categoryList != null){
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
    private fun getFavorites(): HashMap<String, Int> {
        val favoritesHashMap = HashMap<String, Int>()
        val favoriteList = mainRepository.getFavorites()
        for (elements in favoriteList) {
            favoritesHashMap[elements.productCD] = 0
        }
        return favoritesHashMap
    }

    fun checkFavorite(productCD: String, favoriteIsChecked: Boolean) {
        val favoriteHashMap = favoriteLiveData.value

        if (favoriteIsChecked && favoriteHashMap?.get(productCD) == null) {
            favoriteHashMap?.set(productCD, 0)
            _favoriteLiveData.value = favoriteHashMap
            mainRepository.insertFavorite(productCD)
        } else if (!favoriteIsChecked && favoriteHashMap?.get(productCD) != null) {
            favoriteHashMap.remove(productCD)
            _favoriteLiveData.value = favoriteHashMap
            mainRepository.deleteFavorite(productCD)
        }
    }
}