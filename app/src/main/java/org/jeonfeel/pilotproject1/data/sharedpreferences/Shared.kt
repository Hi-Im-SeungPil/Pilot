package org.jeonfeel.pilotproject1.data.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import org.jeonfeel.pilotproject1.R


class Shared(val context: Context) {

    companion object {
        private var sharedPref: SharedPreferences? = null
        private fun getSharedInstance(context: Context): SharedPreferences {
            if (sharedPref == null) {
                sharedPref = context.getSharedPreferences(
                    context.getString(R.string.preference_file_key),
                    Context.MODE_PRIVATE
                )
            }
            return sharedPref!!
        }

        fun setDeCaffeine(context: Context, isCaffeine: Boolean) {
            getSharedInstance(context).let {
                it.edit()
                    .putBoolean(context.getString(R.string.saved_de_caffeine_key), isCaffeine)
                    .apply()
            }
        }

        fun getDeCaffeine(context: Context): Boolean {
            return getSharedInstance(context).getBoolean(
                context.getString(R.string.saved_de_caffeine_key),
                false
            )
        }

        fun setSort(context: Context, sortKind: Int) {
            getSharedInstance(context).let {
                it.edit()
                    .putInt(context.getString(R.string.saved_sort_key), sortKind).apply()
            }
        }

        fun getSort(context: Context): Int {
            return getSharedInstance(context).getInt(context.getString(R.string.saved_sort_key), -1)
        }

        fun sharedClear(context: Context) {
            val instance = getSharedInstance(context)
            instance.let {
                it.edit().putInt(
                    context.getString(R.string.saved_sort_key),
                    context.resources.getInteger(R.integer.SORT_BASIC)
                ).apply()
            }
            instance.let {
                it.edit().putBoolean(
                    context.getString(R.string.saved_de_caffeine_key),
                    context.resources.getBoolean(R.bool.IS_NOT_CAFFEINE)
                ).apply()
            }
        }

        fun getSettingDTO(context: Context): SettingDTO {
            val instance = getSharedInstance(context)
            val sortInfo = instance.getInt(context.getString(R.string.saved_sort_key), -1)
            val isCaffeine =
                instance.getBoolean(context.getString(R.string.saved_de_caffeine_key), false)

            return SettingDTO(sortInfo, isCaffeine)
        }
    }
}