package org.jeonfeel.pilotproject1.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Favorite(
    @PrimaryKey
    @ColumnInfo(name = "product_CD")
    val productCD: String
)
