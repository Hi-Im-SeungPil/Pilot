package org.jeonfeel.pilotproject1.mainactivity

import java.io.Serializable

//메뉴 DTO
data class StarbucksMenuDTO(
    val content: String,
    val product_CD: String,
    val product_NM: String,
    val file_PATH: String,
    val cate_NAME: String,
    val kcal: String,
    val fat: String,
    val sat_FAT: String,
    val sugars: String,
    val protein: String,
    val sodium: String,
    val caffeine: String
) : Serializable