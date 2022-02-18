package org.jeonfeel.pilotproject1.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.jeonfeel.pilotproject1.data.database.dao.FavoriteDao
import org.jeonfeel.pilotproject1.data.database.entity.Favorite

@Database(entities = [Favorite::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoriteDao(): FavoriteDao

    companion object {
        private var instance: AppDatabase? = null

        fun getDbInstance(context: Context): AppDatabase {
            synchronized(AppDatabase::class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context, AppDatabase::class.java, "AppDatabase"
                    )
                        .build()
                }
            }
            return instance!!
        }
    }
}