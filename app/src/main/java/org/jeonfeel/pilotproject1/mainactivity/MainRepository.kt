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

    fun getStarbucksMenuList() : MutableLiveData<ArrayList<StarbucksMenuDTO>> {
        var starbucksMenuDTOs = ArrayList<StarbucksMenuDTO>()
        if (starbucksMenuJsonObject == null) {
            val thread = Thread {
                try {
                    starbucksMenuJsonObject = call.execute().body()!!
                    Log.d(TAG, starbucksMenuJsonObject.toString())
                    starbucksMenuDTOs = getStarbucksMenuResource()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            thread.start()
            try{
                thread.join()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
        return MutableLiveData(starbucksMenuDTOs)
    }

    fun getStarbucksMenuResource(categoryPosition: Int = 7) : ArrayList<StarbucksMenuDTO> {
        val gson = Gson()
        val starbucksMenuDTOs = ArrayList<StarbucksMenuDTO>()
        val category = arrayOf("cold_brew","brood","espresso","frappuccino","blended","juice","etc")
        if (categoryPosition == 7) {
            for (element in category) {
                val categoryJsonObject = starbucksMenuJsonObject?.getAsJsonObject(element)
                val jsonArrayStarbucksMenu = categoryJsonObject?.getAsJsonArray("list")

                for (i in 0 until jsonArrayStarbucksMenu?.size()!!) {
                    Log.d(TAG,jsonArrayStarbucksMenu[i].toString())
                    val sampleItem = gson.fromJson(jsonArrayStarbucksMenu[i], StarbucksMenuDTO::class.java)
                    starbucksMenuDTOs.add(sampleItem)
                }
            }
        }else {
            val categoryJsonObject = starbucksMenuJsonObject?.getAsJsonObject(category[categoryPosition])
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
}