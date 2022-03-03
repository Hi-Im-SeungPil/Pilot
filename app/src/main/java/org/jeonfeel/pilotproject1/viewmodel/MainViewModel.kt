package org.jeonfeel.pilotproject1.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.*
import org.jeonfeel.pilotproject1.R
import org.jeonfeel.pilotproject1.data.database.entity.Favorite
import org.jeonfeel.pilotproject1.data.remote.model.StarbucksMenuDTO
import org.jeonfeel.pilotproject1.data.sharedpreferences.Shared
import org.jeonfeel.pilotproject1.repository.MainRepository
import kotlin.math.ceil


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "MainActivityViewModel"
    private val mainRepository = MainRepository(application.applicationContext)

    private var starbucksMenuJsonObject: JsonObject? = null
    private var categoryList: List<String>? = null
    private var favorites: HashMap<String, Int>? = null

    private val _starbucksMenuLiveData: MutableLiveData<ArrayList<StarbucksMenuDTO>> =
        MutableLiveData<ArrayList<StarbucksMenuDTO>>()
    val starbucksMenuLiveData: LiveData<ArrayList<StarbucksMenuDTO>> get() = _starbucksMenuLiveData
    private val originalList = ArrayList<StarbucksMenuDTO>()

    private val _favoriteLiveData: MutableLiveData<HashMap<String, Int>> =
        MutableLiveData<HashMap<String, Int>>()
    val favoriteLiveData: LiveData<HashMap<String, Int>> get() = _favoriteLiveData

    private val _categoryLiveData: MutableLiveData<List<String>> = MutableLiveData<List<String>>()
    val categoryLiveData: LiveData<List<String>> get() = _categoryLiveData

    var maxProtein = 0.0f
    var maxFat = 0.0f
    var maxSugar = 0.0f
    var tempNutritionalInformation = HashMap<String, Int>()

    fun loadData() {
        viewModelScope.launch(Dispatchers.IO) {
            getStarbucksMenuJsonObj()
            getCategoryList()
            getStarbucksMenu(getApplication())
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
        getStarbucksMenu(getApplication(), position)
    }

    private suspend fun getStarbucksMenuJsonObj() {
        val response = mainRepository.getStarbucksMenuListCall().execute()
        if (response.isSuccessful) {
            starbucksMenuJsonObject = response.body()!!
        } else {
            // TODO:
        }
    }

    private fun getStarbucksMenu(context: Context, categoryPosition: Int = -1) {
        val gson = Gson()
        val starbucksMenuDTOs = ArrayList<StarbucksMenuDTO>() //category setting
        if (categoryPosition == -1) {
            for (element in categoryList!!) {
                val categoryJsonObject = starbucksMenuJsonObject?.getAsJsonObject(element)
                val jsonArrayStarbucksMenu = categoryJsonObject?.getAsJsonArray("list")

                for (i in 0 until jsonArrayStarbucksMenu?.size()!!) {
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
        maxFat = starbucksMenuDTOs.maxOf { it.fat.toFloat() }
        maxProtein = starbucksMenuDTOs.maxOf { it.protein.toFloat() }
        maxSugar = starbucksMenuDTOs.maxOf { it.sugars.toFloat() }
        originalList.clear()
        originalList.addAll(starbucksMenuDTOs)
        _starbucksMenuLiveData.postValue(updateSetting(starbucksMenuDTOs, context))
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

    /**
     * 세팅
     * */
    private fun updateSetting(
        starbucksMenuDTOs: ArrayList<StarbucksMenuDTO>,
        context: Context
    ): ArrayList<StarbucksMenuDTO> {
        val settingDTO = Shared.getSettingDTO(context)
        val sortInfo = settingDTO.sortInfo
        val isCaffeine = settingDTO.isCaffeine

        val resultList: ArrayList<StarbucksMenuDTO> = ArrayList()
        var tempList = sortList(starbucksMenuDTOs, context, sortInfo)
        tempList = filterCaffeine(tempList, isCaffeine)

        resultList.addAll(tempList)

        return resultList
    }

    fun updateSettingImmediately(context: Context, nutritionalInformation: HashMap<String, Int>) {
        val settingDTO = Shared.getSettingDTO(context)
        val sortInfo = settingDTO.sortInfo
        val isCaffeine = settingDTO.isCaffeine

        val resultList: ArrayList<StarbucksMenuDTO> = ArrayList()
        var tempList = sortList(originalList, context, sortInfo)
        tempList = filterCaffeine(tempList, isCaffeine)
        tempList = filterNutritionalInformation(context, tempList, nutritionalInformation)

        resultList.addAll(tempList)

        _starbucksMenuLiveData.value = resultList
    }

    private fun sortList(
        starbucksMenuDTOs: ArrayList<StarbucksMenuDTO>,
        context: Context,
        sortInfo: Int
    ): List<StarbucksMenuDTO> {
        val resultList = when (sortInfo) {
            context.resources.getInteger(R.integer.SORT_LOW_KCAL) -> starbucksMenuDTOs.sortedBy { it.kcal.toInt() }
            context.resources.getInteger(R.integer.SORT_HIGH_KCAL) -> starbucksMenuDTOs.sortedByDescending { it.kcal.toInt() }
            else -> {
                originalList
            }
        }
        return resultList
    }

    private fun filterCaffeine(
        resultList: List<StarbucksMenuDTO>,
        onCaffeineFilter: Boolean
    ): List<StarbucksMenuDTO> {
        if (onCaffeineFilter) {
            return resultList.filter { it.caffeine.toInt() == 0 }
        }
        return resultList
    }

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
                        && ceil(it.fat.toFloat()).toInt() in nutritionalInformation[context.getString(R.string.nutritionalInformation_lowFat_key)]!!..nutritionalInformation[context.getString(R.string.nutritionalInformation_highFat_key)]!!
                        && ceil(it.sugars.toFloat()).toInt() in nutritionalInformation[context.getString(R.string.nutritionalInformation_lowSugar_key)]!!..nutritionalInformation[context.getString(R.string.nutritionalInformation_highSugar_key)]!!
            }
        }
        return resultList
    }
}