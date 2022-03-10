package org.jeonfeel.pilotproject1.data.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import org.jeonfeel.pilotproject1.R

object Shared {
    private const val CAFFEINE_KEY = "isCaffeine"
    private const val SORT_KEY = "sortKind"

    const val SORT_LOW_KCAL = 0
    const val SORT_HIGH_KCAL = 1
    const val SORT_BASIC = 2
    const val CONTAINS_CAFFEINE = true
    const val NOT_CONTAINS_CAFFEINE = false

    private lateinit var sharedPreferencesInstance: SharedPreferences

    fun create(context: Context) {
        sharedPreferencesInstance = context.getSharedPreferences(
            context.getString(R.string.preference_file_key),
            Context.MODE_PRIVATE
        )
    }

    fun setDeCaffeine(isCaffeine: Boolean) {
        sharedPreferencesInstance.edit()
            .putBoolean(CAFFEINE_KEY, isCaffeine)
            .apply()
    }

    fun getDeCaffeine(): Boolean {
        return sharedPreferencesInstance.getBoolean(CAFFEINE_KEY, false)
    }

    fun setSort(sortKind: Int) {
        sharedPreferencesInstance.edit().putInt(SORT_KEY, sortKind).apply()
    }

    fun getSort(): Int {
        return sharedPreferencesInstance.getInt(SORT_KEY, -1)
    }

    fun sharedClear() {
        sharedPreferencesInstance.edit().putInt(SORT_KEY, SORT_BASIC).apply()
        sharedPreferencesInstance.edit().putBoolean(CAFFEINE_KEY, NOT_CONTAINS_CAFFEINE).apply()
    }
}

