package org.jeonfeel.pilotproject1.mainactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.google.gson.JsonObject
import org.jeonfeel.pilotproject1.databinding.ActivityMainBinding
import org.jeonfeel.pilotproject1.retrofit.RetrofitClient
import org.jeonfeel.pilotproject1.retrofit.RetrofitService
import retrofit2.Call
import java.util.*
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

        initRecyclerViewMain()
        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        Log.d(TAG,mainActivityViewModel.getStarbucksMenuList().value.toString())

        mainActivityViewModel.getStarbucksMenuList().observe(this, Observer {
            recyclerviewMainAdapter.setRecyclerViewMainItem(it)
        })

        binding.tablayoutMain.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> {
                        mainActivityViewModel.updateStarbucksMenu(7)
                        if(binding.edittextSearchMain.length() != 0){
                            val currentString = binding.edittextSearchMain.text.toString()
                            recyclerviewMainAdapter.filter.filter(currentString)
                        }
                        binding.RecyclerviewMain.scrollToPosition(0)
                    }
                    1 -> {
                        mainActivityViewModel.updateStarbucksMenu(0)
                        if(binding.edittextSearchMain.length() != 0){
                            val currentString = binding.edittextSearchMain.text.toString()
                            recyclerviewMainAdapter.filter.filter(currentString)
                        }
                        binding.RecyclerviewMain.scrollToPosition(0)
                    }
                    2 -> {
                        mainActivityViewModel.updateStarbucksMenu(1)
                        if(binding.edittextSearchMain.length() != 0){
                            val currentString = binding.edittextSearchMain.text.toString()
                            recyclerviewMainAdapter.filter.filter(currentString)
                        }
                        binding.RecyclerviewMain.scrollToPosition(0)
                    }
                    3 -> {
                        mainActivityViewModel.updateStarbucksMenu(2)
                        if(binding.edittextSearchMain.length() != 0){
                            val currentString = binding.edittextSearchMain.text.toString()
                            recyclerviewMainAdapter.filter.filter(currentString)
                        }
                        binding.RecyclerviewMain.scrollToPosition(0)
                    }
                    4 -> {
                        mainActivityViewModel.updateStarbucksMenu(3)
                        if(binding.edittextSearchMain.length() != 0){
                            val currentString = binding.edittextSearchMain.text.toString()
                            recyclerviewMainAdapter.filter.filter(currentString)
                        }
                        binding.RecyclerviewMain.scrollToPosition(0)
                    }
                    5 -> {
                        mainActivityViewModel.updateStarbucksMenu(4)
                        if(binding.edittextSearchMain.length() != 0){
                            val currentString = binding.edittextSearchMain.text.toString()
                            recyclerviewMainAdapter.filter.filter(currentString)
                        }
                        binding.RecyclerviewMain.scrollToPosition(0)
                    }
                    6 -> {
                        mainActivityViewModel.updateStarbucksMenu(5)
                        if(binding.edittextSearchMain.length() != 0){
                            val currentString = binding.edittextSearchMain.text.toString()
                            recyclerviewMainAdapter.filter.filter(currentString)
                        }
                        binding.RecyclerviewMain.scrollToPosition(0)
                    }
                    7 -> {
                        mainActivityViewModel.updateStarbucksMenu(6)
                        if(binding.edittextSearchMain.length() != 0){
                            val currentString = binding.edittextSearchMain.text.toString()
                            recyclerviewMainAdapter.filter.filter(currentString)
                        }
                        binding.RecyclerviewMain.scrollToPosition(0)
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        binding.edittextSearchMain.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(str: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(str: CharSequence?, p1: Int, p2: Int, p3: Int) {
                recyclerviewMainAdapter.filter.filter(str.toString())
            }

            override fun afterTextChanged(str: Editable?) {
            }

        })
    }

    private fun initRecyclerViewMain() {
        val gridLayoutManager = GridLayoutManager(this,2)
        binding.RecyclerviewMain.layoutManager = gridLayoutManager
        recyclerviewMainAdapter = RecyclerviewMainAdapter(this)
        binding.RecyclerviewMain.adapter = recyclerviewMainAdapter
    }
}
