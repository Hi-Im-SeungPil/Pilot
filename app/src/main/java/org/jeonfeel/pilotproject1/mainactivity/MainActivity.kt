package org.jeonfeel.pilotproject1.mainactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.JsonObject
import org.jeonfeel.pilotproject1.databinding.ActivityMainBinding
import org.jeonfeel.pilotproject1.retrofit.RetrofitClient
import org.jeonfeel.pilotproject1.retrofit.RetrofitService
import retrofit2.Call
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerviewMainAdapter: RecyclerviewMainAdapter
    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)
        supportActionBar?.hide()

        mainActivityViewModel = MainActivityViewModel(application)
        mainActivityViewModel.recyclerViewMainLivedata.observe(this, )
        initRecyclerViewMain()
        loadStarbucksMenuData()
    }

    private fun initRecyclerViewMain() {
        val gridLayoutManager = GridLayoutManager(this,2)
        binding.RecyclerviewMain.layoutManager = gridLayoutManager
        recyclerviewMainAdapter = RecyclerviewMainAdapter(this)
        binding.RecyclerviewMain.adapter = recyclerviewMainAdapter
    }

    private fun loadStarbucksMenuData() {
        val retrofit = RetrofitClient().getRetrofitClient()
        val service = retrofit.create(RetrofitService::class.java)
        val call: Call<JsonObject> = service.getStarbucksMenu()

        thread(start = true){
            val jsonObjectStarbucksMenu = call.execute().body()
            Log.d(TAG,jsonObjectStarbucksMenu.toString())
            recyclerviewMainAdapter.starbucksMenuJsonObject = jsonObjectStarbucksMenu!!
        }
    }
}
