package org.jeonfeel.pilotproject1.repository

import android.content.Context
import com.google.gson.JsonObject
import org.jeonfeel.pilotproject1.data.database.AppDatabase
import org.jeonfeel.pilotproject1.data.remote.api.RetrofitClient
import org.jeonfeel.pilotproject1.data.remote.api.RetrofitService
import retrofit2.Call

class MainRepository(context: Context) {

    private val TAG = MainRepository::class.java.simpleName
    private val retrofit = RetrofitClient().getRetrofitClient()
    private val service = retrofit.create(RetrofitService::class.java)
    private val db = AppDatabase.getDbInstance(context)

    suspend fun getStarbucksMenuListCall(): Call<JsonObject> {
        return service.getStarbucksMenu()
    }

    suspend fun getFavoritesInstance() = db.favoriteDao()

}