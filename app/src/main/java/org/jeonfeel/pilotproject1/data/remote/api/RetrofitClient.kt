package org.jeonfeel.pilotproject1.data.remote.api

import android.content.Context
import android.util.Log
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    //return retrofit object
    fun getRetrofitClient(): Retrofit {
        val retrofit = Retrofit.Builder().baseUrl("http://dpms.openobject.net:4002/")
            .addConverterFactory(GsonConverterFactory.create()).build()

        return retrofit
    }
}