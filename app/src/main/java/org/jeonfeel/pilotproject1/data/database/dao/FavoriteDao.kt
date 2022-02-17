package org.jeonfeel.pilotproject1.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import org.jeonfeel.pilotproject1.data.database.entity.Favorite

@Dao
interface FavoriteDao {
    @Insert
    fun insert(favorite: Favorite)

    @Delete
    fun delete(favorite: Favorite)

    @Query("select * from Favorite")
    fun selectAll(): List<Favorite>
}