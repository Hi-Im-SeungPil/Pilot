package org.jeonfeel.pilotproject1.data.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import org.jeonfeel.pilotproject1.R


class Shared(val context: Context) {

    companion object {
        var sharedPref: SharedPreferences? = null
        fun getSharedInstance(context: Context): SharedPreferences {
            if (sharedPref == null) {
                sharedPref = context.getSharedPreferences(
                    context.getString(R.string.preference_file_key),
                    Context.MODE_PRIVATE
                )
            }
            return sharedPref!!
        }

        fun setDeCaffeine(context: Context, isCaffeine: Int) {
            getSharedInstance(context).let {
                it.edit()
                    .putInt(context.getString(R.string.saved_de_caffeine_key), isCaffeine).apply()
            }
        }

        fun getCaffeine(context: Context): Int {
            return getSharedInstance(context).getInt(context.getString(R.string.saved_de_caffeine_key), -1)
        }
    }
}