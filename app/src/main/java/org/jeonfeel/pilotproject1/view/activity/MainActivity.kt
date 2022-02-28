package org.jeonfeel.pilotproject1.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import org.jeonfeel.pilotproject1.R
import org.jeonfeel.pilotproject1.data.sharedpreferences.Shared
import org.jeonfeel.pilotproject1.databinding.ActivityMainBinding
import org.jeonfeel.pilotproject1.utils.MyFirebaseMessagingService
import org.jeonfeel.pilotproject1.view.adapter.RecyclerViewMainListener
import org.jeonfeel.pilotproject1.view.adapter.ViewPagerAdapter
import org.jeonfeel.pilotproject1.view.fragment.FragmentSettingMain
import org.jeonfeel.pilotproject1.viewmodel.MainViewModel

class MainActivity : AppCompatActivity(), FragmentSettingMain.FragmentSettingListener,
    RecyclerViewMainListener {

    private val TAG = MainActivity::class.java.simpleName
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainActivityViewModel: MainViewModel
    private lateinit var viewPagerAdapter: ViewPagerAdapter

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
        initObserver()
        initListener()
        MyFirebaseMessagingService()

        mainActivityViewModel.loadData()
    }

    /**
     * 옵저버
     */
    private fun initObserver() {
        mainActivityViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        mainActivityViewModel.starbucksMenuLiveData.observe(this, Observer {

        })

        mainActivityViewModel.favoriteLiveData.observe(this, Observer {
            viewPagerAdapter.updateFavoriteImage(it)
        })

        mainActivityViewModel.categoryLiveData.observe(this, Observer {
            initViewPager(it.size)
            TabLayoutMediator(binding.tablayoutMain,binding.viewPager2) { tab, position ->
                if(position == 0) {
                    tab.text = "All"
                } else {
                    addTabLayoutCategory(position-1,tab)
                }
            }.attach()

        })
    }

    /**
     * 리스너
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun initListener() {
        binding.tablayoutMain.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
//                mainActivityViewModel.updateStarbucksMenu(tab!!.position - 1)
//                searchRecyclerviewItem()
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
                viewPagerAdapter.search(str.toString())
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
            supportFragmentManager
                .beginTransaction()
                .replace(binding.framelayoutSettingMain.id, fragment)
                .commit()
        }

        binding.framelayoutSettingMain.setOnTouchListener { _, _ -> true }
    }

    private fun initViewPager(itemCount: Int) {
        viewPagerAdapter = ViewPagerAdapter(this,itemCount)
        viewPagerAdapter.setMainItem(mainActivityViewModel.getStarbuk())
        binding.viewPager2.adapter = viewPagerAdapter
    }

    private fun searchRecyclerviewItem() {
        if (binding.edittextSearchMain.length() != 0) {
            val currentString = binding.edittextSearchMain.text.toString()
            viewPagerAdapter.search(currentString)
        }
    }

    private fun addTabLayoutCategory(position: Int, tab: TabLayout.Tab) {
        val category = mainActivityViewModel.getCategory()
        tab.text = category!![position]
    }

    fun getCurrentText(): String {
        return binding.edittextSearchMain.text.toString()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            Log.d(TAG, result.resultCode.toString())
            if (result.resultCode == RESULT_OK) {
                val intent = result.data
                val productCD: String = intent!!.getStringExtra("productCD").toString()
                val favoriteIsChecked = intent.getBooleanExtra("favoriteIsChecked", false)

                mainActivityViewModel.checkFavorite(productCD, favoriteIsChecked)
            }
        }

    override fun frameLayoutGone() {
        val animation =
            AnimationUtils.loadAnimation(this, R.anim.anim_slide_down)
        binding.framelayoutSettingMain.animation = animation

        binding.framelayoutSettingMain.visibility = View.GONE
    }

    override fun updateSetting() {
        viewPagerAdapter.updateSetting()
    }

    override fun startForActivityResult(intent: Intent) {
        startForResult.launch(intent)
    }
}