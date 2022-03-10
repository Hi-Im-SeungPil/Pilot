package org.jeonfeel.pilotproject1.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import okhttp3.Dispatcher
import org.jeonfeel.pilotproject1.R
import org.jeonfeel.pilotproject1.data.database.entity.Favorite
import org.jeonfeel.pilotproject1.data.remote.model.StarbucksMenuDTO
import org.jeonfeel.pilotproject1.data.sharedpreferences.Shared
import org.jeonfeel.pilotproject1.repository.MainRepository
import org.jeonfeel.pilotproject1.view.activity.MainActivity
import org.jeonfeel.pilotproject1.view.activity.StarbucksMenuDetailActivity
import kotlin.math.ceil


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "MainActivityViewModel"
    private val mainRepository = MainRepository(application.applicationContext)

    private var starbucksMenuJsonObject: JsonObject? = null
    private var categoryList: List<String>? = null
    private var favorites: HashMap<String, Int>? = null

    private val _starbucksMenuLiveData: MutableLiveData<ArrayList<ArrayList<StarbucksMenuDTO>>> =
        MutableLiveData<ArrayList<ArrayList<StarbucksMenuDTO>>>()
    val starbucksMenuLiveData: LiveData<ArrayList<ArrayList<StarbucksMenuDTO>>> get() = _starbucksMenuLiveData

    private val _favoriteLiveData: MutableLiveData<HashMap<String, Int>> =
        MutableLiveData<HashMap<String, Int>>()
    val favoriteLiveData: LiveData<HashMap<String, Int>> get() = _favoriteLiveData

    var maxProtein = 0.0f
    var maxFat = 0.0f
    var maxSugar = 0.0f
    var tempNutritionalInformation = HashMap<String, Int>()

    fun loadData(): Job {
        return viewModelScope.launch(start = CoroutineStart.LAZY, context = Dispatchers.IO) {
            getStarbucksMenuJsonObj()
            getCategoryList()
            getAllStarbucksMenu()
            getFavorites()
        }
    }

    /**
     * 카테고리
     * */
    fun getCategory() = categoryList

    private suspend fun getCategoryList() {
        categoryList = starbucksMenuJsonObject?.keySet()?.toList()
    }

    /**
     * 스타벅스 메뉴
     * */

    private suspend fun getStarbucksMenuJsonObj() {
        val response = mainRepository.getStarbucksMenuListCall().execute()
        if (response.isSuccessful) {
            starbucksMenuJsonObject = response.body()!!
        } else {
            // TODO:
        }
    }

    private fun getAllStarbucksMenu() {
        val gson = Gson()
        val allMenuList: ArrayList<ArrayList<StarbucksMenuDTO>> = arrayListOf()
        val allStarbucksMenuDTO = ArrayList<StarbucksMenuDTO>()

        for (element in categoryList!!) {
            val starbucksMenuDTOs = ArrayList<StarbucksMenuDTO>()
            val categoryJsonObject = starbucksMenuJsonObject?.getAsJsonObject(element)
            val starbucksMenuJsonArray = categoryJsonObject?.getAsJsonArray("list")

            for (i in 0 until starbucksMenuJsonArray?.size()!!) {
                val sampleItem =
                    gson.fromJson(starbucksMenuJsonArray[i], StarbucksMenuDTO::class.java)
                starbucksMenuDTOs.add(sampleItem)
                allStarbucksMenuDTO.add(sampleItem)
            }
            allMenuList.add(starbucksMenuDTOs)
        }
        allMenuList.add(0,allStarbucksMenuDTO)

        _starbucksMenuLiveData.postValue(allMenuList)
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
        Log.e(TAG,"favorites => ${favorites.toString()}")
    }

    fun checkFavorite(productCD: String, favoriteIsChecked: Boolean, selectedTabPosition: Int) {
        val favoriteHashMap = favoriteLiveData.value

        if (favoriteIsChecked && favoriteHashMap?.get(productCD) == null) {
            favoriteHashMap?.set(productCD, selectedTabPosition)
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

    /**
     * 세팅
     * */
    private fun updateSetting(
        starbucksMenuDTOs: ArrayList<StarbucksMenuDTO>,
    ): ArrayList<StarbucksMenuDTO> {
        val sortInfo = Shared.getSort()
        val isCaffeine = Shared.getDeCaffeine()

        val resultList: ArrayList<StarbucksMenuDTO> = ArrayList()
        var tempList = sortList(starbucksMenuDTOs, sortInfo)
        tempList = filterCaffeine(tempList, isCaffeine)

        resultList.addAll(tempList)

        return resultList
    }

    // Fragment에서 즉시 update
    fun updateSettingImmediately(context: Context, nutritionalInformation: HashMap<String, Int>) {
//        val sortInfo = Shared.getSort()
//        val isCaffeine = Shared.getDeCaffeine()
//
//        val resultList: ArrayList<StarbucksMenuDTO> = ArrayList()
//        var tempList = sortList(, sortInfo)
//        tempList = filterCaffeine(tempList, isCaffeine)
//        tempList = filterNutritionalInformation(context, tempList, nutritionalInformation)
//
//        resultList.addAll(tempList)
//
//        _starbucksMenuLiveData.value = resultList
    }

    // sorting List
    private fun sortList(
        starbucksMenuDTOs: ArrayList<StarbucksMenuDTO>,
        sortInfo: Int
    ): List<StarbucksMenuDTO> {
//        val resultList = when (sortInfo) {
//            Shared.SORT_LOW_KCAL -> starbucksMenuDTOs.sortedBy { it.kcal.toInt() }
//            Shared.SORT_HIGH_KCAL -> starbucksMenuDTOs.sortedByDescending { it.kcal.toInt() }
//            else -> {
//                originalList
//            }
//        }
        return emptyList()
    }

    // caffeine filter
    private fun filterCaffeine(
        resultList: List<StarbucksMenuDTO>,
        onCaffeineFilter: Boolean
    ): List<StarbucksMenuDTO> {
        if (onCaffeineFilter) {
            return resultList.filter { it.caffeine.toInt() == 0 }
        }
        return resultList
    }

    // 단백질, 지방, 설탕 filter
    private fun filterNutritionalInformation(
        context: Context,
        resultList: List<StarbucksMenuDTO>,
        nutritionalInformation: HashMap<String, Int>
    ): List<StarbucksMenuDTO> {
        if (nutritionalInformation.size != 0) {
            return resultList.filter {
                ceil(it.protein.toFloat()).toInt() in nutritionalInformation[context.getString(R.string.nutritionalInformation_lowProtein_key)]!!..nutritionalInformation[context.getString(
                    R.string.nutritionalInformation_highProtein_key
                )]!!
                        && ceil(it.fat.toFloat()).toInt() in nutritionalInformation[context.getString(
                    R.string.nutritionalInformation_lowFat_key
                )]!!..nutritionalInformation[context.getString(R.string.nutritionalInformation_highFat_key)]!!
                        && ceil(it.sugars.toFloat()).toInt() in nutritionalInformation[context.getString(
                    R.string.nutritionalInformation_lowSugar_key
                )]!!..nutritionalInformation[context.getString(R.string.nutritionalInformation_highSugar_key)]!!
            }
        }
        return resultList
    }

    /**
     * FCM
     * */
    fun foreGroundFCM(productCD: String, category: String): Intent? {
        val newIntent = Intent(getApplication(), StarbucksMenuDetailActivity::class.java)
        val gson = Gson()
        val obj = starbucksMenuJsonObject?.getAsJsonObject(category)
        val lst = obj?.getAsJsonArray("list")

        val menuDTOJsonElement =
            lst?.filter { gson.fromJson(it, StarbucksMenuDTO::class.java).product_CD == productCD }
        val menuDTO = gson.fromJson(menuDTOJsonElement?.get(0), StarbucksMenuDTO::class.java)

        newIntent.putExtra("starbucksMenuDTO", menuDTO)
        newIntent.putExtra("productCD", productCD)
        if (favoriteLiveData.value?.get(productCD) != null) {
            newIntent.putExtra("favoriteIsChecked", true)
        } else {
            newIntent.putExtra("favoriteIsChecked", false)
        }

        return newIntent
    }

    fun backgroundFCM(productCD: String, category: String): Intent {
        val newIntent = Intent(getApplication(), StarbucksMenuDetailActivity::class.java)
        val gson = Gson()
        val obj = starbucksMenuJsonObject?.getAsJsonObject(category)
        val lst = obj?.getAsJsonArray("list")

        val menuDTOJsonElement =
            lst?.filter { gson.fromJson(it, StarbucksMenuDTO::class.java).product_CD == productCD }
        val menuDTO = gson.fromJson(menuDTOJsonElement?.get(0), StarbucksMenuDTO::class.java)

        newIntent.putExtra("starbucksMenuDTO", menuDTO)
        newIntent.putExtra("productCD", productCD)
        if (favorites?.get(productCD) != null) {
            newIntent.putExtra("favoriteIsChecked", true)
        } else {
            newIntent.putExtra("favoriteIsChecked", false)
        }

        return newIntent
    }
}