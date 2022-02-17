package org.jeonfeel.pilotproject1.repository

import android.content.Context
import org.jeonfeel.pilotproject1.data.database.AppDatabase
import org.jeonfeel.pilotproject1.data.database.entity.Favorite

class DetailActivityRepository(context: Context) {

    private val db = AppDatabase.getDbInstance(context)
    private val favoriteHashMap: HashMap<String, Int> = hashMapOf()



//    fun insertFavorite(favorite: Favorite) {
//        db.favoriteDao().insert(favorite.productCD)
//    }
//
//    fun deleteFavorite(favorite: Favorite) {
//        db.favoriteDao().delete(favorite.productCD)
//    }
}