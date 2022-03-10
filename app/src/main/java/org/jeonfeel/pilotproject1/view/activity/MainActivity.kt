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
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import org.jeonfeel.pilotproject1.R
import org.jeonfeel.pilotproject1.data.remote.model.StarbucksMenuDTO
import org.jeonfeel.pilotproject1.databinding.ActivityMainBinding
import org.jeonfeel.pilotproject1.view.adapter.RecyclerViewMainListener
import org.jeonfeel.pilotproject1.view.adapter.ViewPagerAdapter
import org.jeonfeel.pilotproject1.view.fragment.FragmentSettingMain
import org.jeonfeel.pilotproject1.viewmodel.MainViewModel

class MainActivity : FragmentActivity(), FragmentSettingMain.FragmentSettingListener,
    RecyclerViewMainListener {

    private val TAG = MainActivity::class.java.simpleName
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainActivityViewModel: MainViewModel
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val intent = result.data
                val productCD: String = intent!!.getStringExtra("productCD").toString()
                val favoriteIsChecked = intent.getBooleanExtra("favoriteIsChecked", false)

                mainActivityViewModel.checkFavorite(productCD, favoriteIsChecked, binding.tlMain.selectedTabPosition)
            }
        }

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
        backgroundFCM()
    }

    /**
     * 옵저버
     */
    private fun initObserver() {
        mainActivityViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        mainActivityViewModel.favoriteLiveData.observe(this, Observer {
            viewPagerAdapter.setFavorite(it)
        })

        mainActivityViewModel.starbucksMenuLiveData.observe(this, Observer {
            initViewPager(it)
        })
    }

    /**
     * 리스너
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun initListener() {
        binding.tlMain.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPagerAdapter.setCurrentPosition(tab!!.position)
                searchRecyclerviewItem()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })

        binding.etSearchMain.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(str: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(str: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewPagerAdapter.search(str.toString())
            }

            override fun afterTextChanged(str: Editable?) {
            }
        })

        binding.btnAdjustMain.setOnClickListener {
            val animation =
                AnimationUtils.loadAnimation(this, R.anim.anim_slide_up)
            binding.flSettingMain.animation = animation
            binding.flSettingMain.visibility = View.VISIBLE

            val fragment = FragmentSettingMain.newInstance()
            fragment.setSliderValue(
                mainActivityViewModel.maxProtein,
                mainActivityViewModel.maxFat,
                mainActivityViewModel.maxSugar,
                mainActivityViewModel.tempNutritionalInformation
            )
            supportFragmentManager
                .beginTransaction()
                .replace(binding.flSettingMain.id, fragment)
                .commit()
        }

        binding.flSettingMain.setOnTouchListener { _, _ -> true }

        binding.btnMessageBoxMain.setOnClickListener {
            startActivity(Intent(this, FcmBoxActivity::class.java))
        }
    }

    private fun initViewPager(allCoffeeList: ArrayList<ArrayList<StarbucksMenuDTO>>) {
        viewPagerAdapter = ViewPagerAdapter(this, allCoffeeList)
        binding.viewPager2.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tlMain, binding.viewPager2) { tab, position ->
            addTabLayoutCategory(position - 1, tab)
        }.attach()
    }

    private fun searchRecyclerviewItem() {
        if (binding.etSearchMain.length() != 0) {
            val currentString = binding.etSearchMain.text.toString()
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

    // 앱 종료상태일 떄 다이나믹 링크 or FCM동작 처리
    private fun backgroundFCM() {
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
                        CoroutineScope(Dispatchers.IO).launch {
                            val job1 = mainActivityViewModel.loadData()
                            job1.join()
                            val productCD = sanitizer.getValue("product_CD") ?: ""
                            val category = sanitizer.getValue("category") ?: ""
                            val newIntent = mainActivityViewModel.backgroundFCM(productCD, category)
                            startForResult.launch(newIntent)
                        }
                    }
                    intent.extras != null -> {
                        GlobalScope.launch(Dispatchers.IO) {
                            mainActivityViewModel.loadData().join()
                            val productCD = intent.extras!!.getString("product_CD") ?: ""
                            val category = intent.extras!!.getString("category") ?: ""
                            val newIntent = mainActivityViewModel.backgroundFCM(productCD, category)
                            startForResult.launch(newIntent)
                        }
                        Log.e(TAG, "getDynamicLink: no link found")
                    }
                    else -> {
                        CoroutineScope(Dispatchers.IO).launch {
                            mainActivityViewModel.loadData().start()
                        }
                    }
                }
            }
            .addOnFailureListener(this) { e -> Log.w(TAG, "getDynamicLink:onFailure", e) }
    }

    // 앱이 foreGround 상태일 떄 다이나믹 링크 or FCM동작 처리
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Firebase.dynamicLinks.getDynamicLink(intent).addOnSuccessListener { pendingDynamicLink ->
            var deeplink: Uri? = null
            if (pendingDynamicLink != null) {
                deeplink = pendingDynamicLink.link
            }
            when {
                deeplink != null -> {
                    Log.e(TAG, deeplink.toString())
                    val sanitizer = UrlQuerySanitizer(deeplink.toString())
                    val productCD = sanitizer.getValue("product_CD") ?: ""
                    val category = sanitizer.getValue("category") ?: ""
                    if (category != "" && productCD != "") {
                        val newIntent = mainActivityViewModel.foreGroundFCM(
                            productCD,
                            category
                        )
                        startForResult.launch(newIntent)
                    }
                }
                intent?.extras != null -> {
                    val category = intent.getStringExtra("category") ?: ""
                    val productCD = intent.getStringExtra("product_CD") ?: ""
                    if (category != "" && productCD != "") {
                        val newIntent = mainActivityViewModel.foreGroundFCM(
                            productCD,
                            category
                        )
                        startForResult.launch(newIntent)
                        Log.e(TAG, "getDynamicLink: no link found")
                    }
                }
            }
        }
    }

    override fun frameLayoutGone() {
        val animation =
            AnimationUtils.loadAnimation(this, R.anim.anim_slide_down)
        binding.flSettingMain.animation = animation
        binding.flSettingMain.visibility = View.GONE
    }

    override fun updateSettingImmediately(nutritionalInformation: HashMap<String, Int>) {
        mainActivityViewModel.updateSettingImmediately(this, nutritionalInformation)
        mainActivityViewModel.tempNutritionalInformation = nutritionalInformation
    }

    override fun startForActivityResult(intent: Intent) {
        startForResult.launch(intent)
    }
}