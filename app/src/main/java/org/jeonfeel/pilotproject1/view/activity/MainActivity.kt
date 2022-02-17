package org.jeonfeel.pilotproject1.view.activity

import android.annotation.SuppressLint
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import org.jeonfeel.pilotproject1.R
import org.jeonfeel.pilotproject1.data.database.AppDatabase
import org.jeonfeel.pilotproject1.databinding.ActivityMainBinding
import org.jeonfeel.pilotproject1.utils.GridLayoutManagerWrap
import org.jeonfeel.pilotproject1.view.adapter.RecyclerviewMainAdapter
import org.jeonfeel.pilotproject1.view.fragment.FragmentSettingMain
import org.jeonfeel.pilotproject1.viewmodel.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerviewMainAdapter: RecyclerviewMainAdapter
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private var favoriteHashMap = HashMap<String, Int>()
    lateinit var favoritesLiveData: MutableLiveData<HashMap<String, Int>>
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        initActivity()
    }

    /**
     * 액티비티 초기화
     */
    private fun initActivity() {
        db = AppDatabase.getDbInstance(this@MainActivity)
        initObserver()
        initRecyclerViewMain()
        initListener()
        addTabLayoutCategory()
    }

    /**
     * 옵저버
     */
    private fun initObserver() {
        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        mainActivityViewModel.getStarbucksMenuLiveData().observe(this, Observer {
            recyclerviewMainAdapter.setRecyclerViewMainItem(it)
        })

        favoritesLiveData = mainActivityViewModel.getFavoriteLiveData()
        favoriteHashMap = favoritesLiveData.value!!

        favoritesLiveData.observe(this, Observer {
            recyclerviewMainAdapter.updateFavoriteImage()
        })
    }

    /**
     * 리스너
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun initListener() {
        binding.tablayoutMain.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                mainActivityViewModel.updateStarbucksMenu(tab!!.position - 1)
                filteringRecyclerviewItem()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        binding.edittextSearchMain.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(str: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(str: CharSequence?, p1: Int, p2: Int, p3: Int) {
                recyclerviewMainAdapter.filter.filter(str.toString())
            }

            override fun afterTextChanged(str: Editable?) {
            }
        })

        binding.buttonAdjust.setOnClickListener {
            val animation =
                AnimationUtils.loadAnimation(this, R.anim.anim_slide_up)
            binding.framelayoutSettingMain.animation = animation
            binding.framelayoutSettingMain.visibility = View.VISIBLE

            val fragment = FragmentSettingMain.newInstance()
            fragment.setRecyclerViewMainAdapter(recyclerviewMainAdapter)
            supportFragmentManager
                .beginTransaction()
                .replace(binding.framelayoutSettingMain.id, fragment)
                .commit()
        }

        binding.framelayoutSettingMain.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                return true
            }
        })
    }

    private fun initRecyclerViewMain() {
        val gridLayoutManager = GridLayoutManagerWrap(this, 2)
        binding.RecyclerviewMain.layoutManager = gridLayoutManager
        recyclerviewMainAdapter = RecyclerviewMainAdapter(this)
        recyclerviewMainAdapter.setFavoriteHashMap(favoriteHashMap)
        binding.RecyclerviewMain.adapter = recyclerviewMainAdapter
    }

    private fun filteringRecyclerviewItem() {
        if (binding.edittextSearchMain.length() != 0) {
            val currentString = binding.edittextSearchMain.text.toString()
            recyclerviewMainAdapter.filter.filter(currentString)
        }
        binding.RecyclerviewMain.scrollToPosition(0)
    }

    private fun addTabLayoutCategory() {
        val categoryList = mainActivityViewModel.getCategoryList()
        for (i in categoryList.indices) {
            val tabItem = binding.tablayoutMain.newTab()
            tabItem.text = categoryList[i]
            binding.tablayoutMain.addTab(tabItem)
        }
    }

    fun getCurrentText(): String {
        return binding.edittextSearchMain.text.toString()
    }

    fun frameLayoutGone() {
        val animation =
            AnimationUtils.loadAnimation(this, R.anim.anim_slide_down)
        binding.framelayoutSettingMain.animation = animation

        binding.framelayoutSettingMain.visibility = View.GONE
    }

    val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            Log.d(TAG, result.resultCode.toString())
            if (result.resultCode == RESULT_OK) {
                val intent = result.data
                val productCD: String = intent!!.getStringExtra("productCD").toString()
                val favoriteIsChecked = intent.getBooleanExtra("favoriteIsChecked", false)

                if (favoriteIsChecked && favoriteHashMap[productCD] == null) {
                    mainActivityViewModel.insertFavorite(productCD)
                    favoriteHashMap[productCD] = 0
                    favoritesLiveData.value = favoriteHashMap
                } else if (!favoriteIsChecked && favoriteHashMap[productCD] != null) {
                    mainActivityViewModel.deleteFavorite(productCD)
                    favoriteHashMap.remove(productCD)
                    favoritesLiveData.value = favoriteHashMap
                }
            }
        }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
