package org.jeonfeel.pilotproject1.mainactivity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import org.jeonfeel.pilotproject1.R
import org.jeonfeel.pilotproject1.databinding.ActivityMainBinding
import org.jeonfeel.pilotproject1.mainactivity.recyclerview.GridLayoutManagerWrap
import org.jeonfeel.pilotproject1.mainactivity.recyclerview.RecyclerviewMainAdapter

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerviewMainAdapter: RecyclerviewMainAdapter
    private lateinit var mainActivityViewModel: MainActivityViewModel

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        initRecyclerViewMain()
        binding.framelayoutSettingMain.visibility = View.GONE
        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        Log.d(TAG,mainActivityViewModel.getStarbucksMenuList().value.toString())

        mainActivityViewModel.getStarbucksMenuList().observe(this, Observer {
            recyclerviewMainAdapter.setRecyclerViewMainItem(it)
        })

        binding.tablayoutMain.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0){
                    mainActivityViewModel.updateStarbucksMenu(7)
                } else {
                    mainActivityViewModel.updateStarbucksMenu(tab!!.position - 1)
                }
                filteringRecyclerviewItem()
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
        binding.buttonAdjust.setOnClickListener {
            val animation = AnimationUtils.loadAnimation(this, R.anim.fragment_setting_main_slide_up)
            binding.framelayoutSettingMain.animation = animation
            binding.framelayoutSettingMain.visibility = View.VISIBLE

            val fragment = FragmentSettingMain.newInstance()
            fragment.setRecyclerViewMainAdapter(recyclerviewMainAdapter)
                supportFragmentManager
                    .beginTransaction()
                    .replace(binding.framelayoutSettingMain.id,fragment)
                    .commit()
        }

        binding.framelayoutSettingMain.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                return true
            }
        })
    }

    private fun initRecyclerViewMain() {
        val gridLayoutManager = GridLayoutManagerWrap(this,2)
        binding.RecyclerviewMain.layoutManager = gridLayoutManager
        recyclerviewMainAdapter = RecyclerviewMainAdapter(this)
        binding.RecyclerviewMain.adapter = recyclerviewMainAdapter
    }

    private fun filteringRecyclerviewItem() {
        if(binding.edittextSearchMain.length() != 0){
            val currentString = binding.edittextSearchMain.text.toString()
            recyclerviewMainAdapter.filter.filter(currentString)
            binding.RecyclerviewMain.scrollToPosition(0)
        }
    }

    fun frameLayoutGone() {
        binding.framelayoutSettingMain.visibility = View.GONE
    }

    fun moveRecyclerviewFirst() {
        binding.RecyclerviewMain.scrollToPosition(0)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
