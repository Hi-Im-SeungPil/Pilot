package org.jeonfeel.pilotproject1.retrofit

import com.google.gson.JsonObject
import org.json.JSONArray
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    //get json data
    @GET("temp/TEST_STARBUCKS_MENU.json")
    fun getStarbucksMenu(): Call<JsonObject>

}