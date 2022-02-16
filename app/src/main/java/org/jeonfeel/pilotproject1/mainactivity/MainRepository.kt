package org.jeonfeel.pilotproject1.mainactivity

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.jeonfeel.pilotproject1.retrofit.RetrofitClient
import org.jeonfeel.pilotproject1.retrofit.RetrofitService
import retrofit2.Call

class MainRepository {

    private val retrofit = RetrofitClient().getRetrofitClient()
    private val service = retrofit.create(RetrofitService::class.java)
    private val call: Call<JsonObject> = service.getStarbucksMenu()
    private var starbucksMenuJsonObject: JsonObject? = null
    private lateinit var categoryList: List<String>

    fun getStarbucksMenuList(): ArrayList<StarbucksMenuDTO> {
        var starbucksMenuDTOs = ArrayList<StarbucksMenuDTO>()
        if (starbucksMenuJsonObject == null) {
            val thread = Thread {
                try {
                    starbucksMenuJsonObject = call.execute().body()!!
                    Log.d(TAG, starbucksMenuJsonObject.toString())
                    categoryList = starbucksMenuJsonObject?.keySet()?.toList()!!
                    starbucksMenuDTOs = getStarbucksMenuResource()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            thread.start()
            try {
                thread.join()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return starbucksMenuDTOs
    }

    fun getStarbucksMenuResource(categoryPosition: Int = -1): ArrayList<StarbucksMenuDTO> {
        val gson = Gson()
        val starbucksMenuDTOs = ArrayList<StarbucksMenuDTO>()
        if (categoryPosition == -1) {
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
        } else {
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

    fun getCategoryList(): List<String> {

        return categoryList
    }
}