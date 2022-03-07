package org.jeonfeel.pilotproject1.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.net.UrlQuerySanitizer
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
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import org.jeonfeel.pilotproject1.R
import org.jeonfeel.pilotproject1.databinding.ActivityMainBinding
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

    Firebase.dynamicLinks
        .getDynamicLink(intent)
        .addOnSuccessListener(this) { pendingDynamicLinkData ->
            var deeplink: Uri? = null
            if (pendingDynamicLinkData != null) {
                deeplink = pendingDynamicLinkData.link
            }
            when {
                deeplink != null -> {
                    Log.e(TAG, deeplink.toString())
                    val sanitizer = UrlQuerySanitizer(deeplink.toString())
                    GlobalScope.launch(Dispatchers.IO) {
                        mainActivityViewModel.loadData().join()
                        backgroundFCM(
                            sanitizer.getValue("product_CD") ?: "",
                            sanitizer.getValue("category") ?: ""
                        )
                    }
                }
                intent.extras != null -> {
                    GlobalScope.launch(Dispatchers.IO) {
                        mainActivityViewModel.loadData().join()
                        backgroundFCM(
                            intent.extras!!.getString("product_CD") ?: "" ,
                            intent.extras!!.getString("category") ?: ""
                        )
                    }
                    Log.e(TAG, "getDynamicLink: no link found")
                }
                else -> {
                    mainActivityViewModel.loadData()
                }
            }
        }
        .addOnFailureListener(this) { e -> Log.w(TAG, "getDynamicLink:onFailure", e) }
}

/**
 * 옵저버
 */
private fun initObserver() {
    mainActivityViewModel = ViewModelProvider(this)[MainViewModel::class.java]

    mainActivityViewModel.categoryLiveData.observe(this, Observer {
        initViewPager()
        TabLayoutMediator(binding.tablayoutMain, binding.viewPager2) { tab, position ->
            addTabLayoutCategory(position - 1, tab)
        }.attach()
    })

    mainActivityViewModel.starbucksMenuLiveData.observe(this, Observer {
        viewPagerAdapter.setMainItem(it)
        searchRecyclerviewItem()
    })

    mainActivityViewModel.favoriteLiveData.observe(this, Observer {
        viewPagerAdapter.updateFavoriteImage(it)
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
            searchRecyclerviewItem()
            mainActivityViewModel.tempNutritionalInformation.clear()
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
        fragment.setSliderValue(
            mainActivityViewModel.maxProtein,
            mainActivityViewModel.maxFat,
            mainActivityViewModel.maxSugar,
            mainActivityViewModel.tempNutritionalInformation
        )
        supportFragmentManager
            .beginTransaction()
            .replace(binding.framelayoutSettingMain.id, fragment)
            .commit()
    }

    binding.framelayoutSettingMain.setOnTouchListener { _, _ -> true }

    binding.btnMessageBoxMain.setOnClickListener {
        startActivity(Intent(this, FcmBoxActivity::class.java))
    }
}

private fun initViewPager() {
    viewPagerAdapter = ViewPagerAdapter(
        this,
        mainActivityViewModel.categoryLiveData.value?.size?.plus(1) ?: 9
    )
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
    if (position == -1) {
        tab.text = "All"
    } else {
        tab.text = category!![position]
    }
}

override fun onBackPressed() {
    super.onBackPressed()
}

fun backgroundFCM(productCD: String?, category: String?) {
    val newIntent = mainActivityViewModel.backgroundFCM(productCD!!, category!!)
    startForResult.launch(newIntent)
}

override fun onNewIntent(intent: Intent?) {
    super.onNewIntent(intent)
    val category = intent?.getStringExtra("category") ?: ""
    val productCD = intent?.getStringExtra("product_CD") ?: ""

    if (category != "" && productCD != "") {
        val newIntent = mainActivityViewModel.foreGroundFCM(
            this,
            productCD,
            category
        )
        startForResult.launch(newIntent)
    }
}

private val startForResult =
    registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
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

override fun updateSettingImmediately(nutritionalInformation: HashMap<String, Int>) {
    mainActivityViewModel.updateSettingImmediately(this, nutritionalInformation)
    mainActivityViewModel.tempNutritionalInformation = nutritionalInformation
}

override fun startForActivityResult(intent: Intent) {
    startForResult.launch(intent)
}
}