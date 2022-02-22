package org.jeonfeel.pilotproject1.data.sharedpreferences

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class Shared(val context: Context) {

//    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "dataStore")
//
//    private val TAG = Shared::class.java.simpleName
//    private val orderBy = intPreferencesKey("orderBy")
//    private val proteinMin = intPreferencesKey("proteinMin")
//    private val proteinMax = intPreferencesKey("proteinMax")
//    private val fatMin = intPreferencesKey("fatMin")
//    private val fatMax = intPreferencesKey("fatMax")
//    private val sugarMin = intPreferencesKey("sugarMin")
//    private val sugarMax = intPreferencesKey("sugarMax")
//    private val deCaffeine = intPreferencesKey("deCaffeine")
//
//    suspend fun storeSetting(
//        orderBy: Int,
//        proteinMin: Int,
//        proteinMax: Int,
//        fatMin: Int,
//        fatMax: Int,
//        sugarMin: Int,
//        sugarMax: Int,
//        deCaffeine: Int
//    ) {
//        Log.d("qqqqqqqq","실행")
//        context.dataStore.edit { preferences ->
//            preferences[this.orderBy] = orderBy
//        }
//        context.dataStore.edit { preferences ->
//            preferences[this.proteinMin] = proteinMin
//        }
//        context.dataStore.edit { preferences ->
//            preferences[this.proteinMax] = proteinMax
//        }
//        context.dataStore.edit { preferences ->
//            preferences[this.fatMin] = fatMin
//        }
//        context.dataStore.edit { preferences ->
//            preferences[this.fatMax] = fatMax
//        }
//        context.dataStore.edit { preferences ->
//            preferences[this.sugarMin] = sugarMin
//        }
//        context.dataStore.edit { preferences ->
//            preferences[this.sugarMax] = sugarMax
//        }
//        context.dataStore.edit { preferences ->
//            preferences[this.deCaffeine] = deCaffeine
//        }
//    }

//    fun getSetting() {
//        val orderBy: Flow<Int> = context.dataStore.data.map {
//            preferences -> preferences[this.orderBy] ?: -5
//        }
//        val proteinMin: Flow<Int> = context.dataStore.data.map {
//                preferences -> preferences[this.proteinMin]!!
//        }
//        val proteinMax: Flow<Int> = context.dataStore.data.map {
//                preferences -> preferences[this.proteinMax]!!
//        }
//        val fatMin: Flow<Int> = context.dataStore.data.map {
//                preferences -> preferences[this.fatMin]!!
//        }
//        val fatMax: Flow<Int> = context.dataStore.data.map {
//                preferences -> preferences[this.fatMax]!!
//        }
//        val sugarMin: Flow<Int> = context.dataStore.data.map {
//                preferences -> preferences[this.sugarMin]!!
//        }
//        val sugarMax: Flow<Int> = context.dataStore.data.map {
//                preferences -> preferences[this.sugarMax]!!
//        }
//        val deCaffeine: Flow<Int> = context.dataStore.data.map {
//                preferences -> preferences[this.deCaffeine]!!
//        }
//
//        CoroutineScope(Dispatchers.IO).launch{
//            Log.d("qqqqqqqq","실행2")
//            Log.d("qqqqqqqq",orderBy.toString())
//            var text = orderBy.collect { value -> "$value"}
//            Log.d("qqqqqq",text)
//        }
//        Log.d("qqqqqqqq",proteinMin.toString())
//        Log.d("qqqqqqqq",proteinMax.toString())
//        Log.d("qqqqqqqq",fatMin.toString())
//        Log.d("qqqqqqqq",fatMax.toString())
//        Log.d("qqqqqqqq",sugarMin.toString())
//        Log.d("qqqqqqqq",sugarMax.toString())
//        Log.d("qqqqqqqq",deCaffeine.toString())
//    }

//    fun setUserSetting(
//        context: Context,
//        orderBy: Int,
//        proteinMin: Int,
//        proteinMax: Int,
//        fatMin: Int,
//        fatMax: Int,
//        sugarMin: Int,
//        sugarMax: Int,
//        deCaffeine: Int
//    ) {
//        val prefs = getPreferences(context)
//        val prefsEditor = prefs?.edit()
//        prefsEditor.apply {
//            this?.putInt("orderBy", orderBy)
//            this?.putInt("proteinMin", proteinMin)
//            this?.putInt("proteinMax", proteinMax)
//            this?.putInt("fatMin", fatMin)
//            this?.putInt("fatMax", fatMax)
//            this?.putInt("sugarMin", sugarMin)
//            this?.putInt("sugarMax", sugarMax)
//            this?.putInt("deCaffeine", deCaffeine)
//        }?.apply()
//    }
}