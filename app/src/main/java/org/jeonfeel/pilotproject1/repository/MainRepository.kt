package org.jeonfeel.pilotproject1.repository

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.jeonfeel.pilotproject1.data.database.AppDatabase
import org.jeonfeel.pilotproject1.data.database.entity.Favorite
import org.jeonfeel.pilotproject1.data.remote.api.RetrofitClient
import org.jeonfeel.pilotproject1.data.remote.api.RetrofitService
import org.jeonfeel.pilotproject1.data.remote.model.StarbucksMenuDTO
import retrofit2.Call

class MainRepository(context: Context) {

    private val TAG = MainRepository::class.java.simpleName
    private val retrofit = RetrofitClient().getRetrofitClient()
    private val service = retrofit.create(RetrofitService::class.java)
    private val db = AppDatabase.getDbInstance(context)

    suspend fun getStarbucksMenuList(): Call<JsonObject> {
        return service.getStarbucksMenu()
    }

    suspend fun getFavoritesInstance() = db.favoriteDao()

//    fun insertFavorite(productCD: String) {
//        val favorite = Favorite(productCD)
//        db.favoriteDao().insert(favorite)
//    }
//
//    fun deleteFavorite(productCD: String) {
//        val favorite = Favorite(productCD)
//        db.favoriteDao().delete(favorite)
//    }
}