package org.jeonfeel.pilotproject1.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.net.UrlQuerySanitizer
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.jeonfeel.pilotproject1.R
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
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val intent = result.data
                val productCD: String = intent!!.getStringExtra("productCD").toString()
                val favoriteIsChecked = intent.getBooleanExtra("favoriteIsChecked", false)

                mainActivityViewModel.checkFavorite(
                    productCD,
                    favoriteIsChecked,
                )
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        initActivity()
    }

    /**
     * ???????????? ?????????
     */
    private fun initActivity() {
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        initObserver()
        initListener()
        backgroundFCM()
    }

    /**
     * ?????????
     */
    private fun initObserver() {
        mainActivityViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        mainActivityViewModel.categoryListLiveData.observe(this) {
            initViewPager()
        }

        mainActivityViewModel.favoriteLiveData.observe(this) {
            viewPagerAdapter.setFavorites(it)
        }

        mainActivityViewModel.starbucksMenuLiveData.observe(this) {
            viewPagerAdapter.setItem(it)
        }
    }

    /**
     * ?????????
     */
    @SuppressLint("ClickableViewAccessibility")
    private fun initListener() {
        binding.tlMain.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (viewPagerAdapter.getSelectedTabPosition() != tab!!.position) {
                    mainActivityViewModel.tempNutritionalInformation.clear()
                }
                viewPagerAdapter.setSelectedTabPosition(tab.position)
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

            Log.d(TAG, mainActivityViewModel.tempNutritionalInformation.toString())

            val fragment = FragmentSettingMain.newInstance()
            fragment.setSliderValue(
                mainActivityViewModel.getProteinMaxValue(binding.tlMain.selectedTabPosition),
                mainActivityViewModel.getFatMaxValue(binding.tlMain.selectedTabPosition),
                mainActivityViewModel.getSugarsMaxValue(binding.tlMain.selectedTabPosition),
                mainActivityViewModel.tempNutritionalInformation
            )
            supportFragmentManager
                .beginTransaction()
                .replace(binding.flSettingMain.id, fragment)
                .commit()

            val bundle = Bundle()
            bundle.putString("event_name", "clickBtnAdjust")
            firebaseAnalytics.logEvent("click_btn_adjust", bundle)
        }

        binding.flSettingMain.setOnTouchListener { _, _ -> true }

        binding.btnMessageBoxMain.setOnClickListener {
            startActivity(Intent(this, FcmBoxActivity::class.java))
        }
    }

    private fun initViewPager() {
        viewPagerAdapter = ViewPagerAdapter(this)
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

    // ??? ??????????????? ??? ???????????? ?????? or FCM ?????? ??????
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
                        loading(true)
                        val sanitizer = UrlQuerySanitizer(deeplink.toString())
                        CoroutineScope(Dispatchers.IO).launch {
                            mainActivityViewModel.loadData().join()
                            val productCD = sanitizer.getValue("product_CD") ?: ""
                            val category = sanitizer.getValue("category") ?: ""
                            val newIntent = mainActivityViewModel.backgroundFCM(productCD, category)
                            startForResult.launch(newIntent)
                            delay(4000)
                            CoroutineScope(Dispatchers.Main).launch {
                                loading(false)
                            }
                        }
                    }
                    intent.extras != null -> {
                        loading(true)
                        CoroutineScope(Dispatchers.IO).launch(Dispatchers.IO) {
                            mainActivityViewModel.loadData().join()
                            val productCD = intent.extras!!.getString("product_CD") ?: ""
                            val category = intent.extras!!.getString("category") ?: ""
                            val newIntent = mainActivityViewModel.backgroundFCM(productCD, category)
                            startForResult.launch(newIntent)
                            CoroutineScope(Dispatchers.Main).launch {
                                loading(false)
                            }
                        }
                        Log.e(TAG, "getDynamicLink: no link found")
                    }
                    else -> {
                        loading(true)
                        CoroutineScope(Dispatchers.IO).launch {
                            mainActivityViewModel.loadData().join()
                            CoroutineScope(Dispatchers.Main).launch {
                                loading(false)
                            }
                        }
                    }
                }
            }
            .addOnFailureListener(this) { e -> Log.w(TAG, "getDynamicLink:onFailure", e) }
    }

    // ?????? foreGround ????????? ??? ???????????? ?????? or FCM ?????? ??????
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

    private fun loading(isLoading: Boolean) {
        if (isLoading) {
            binding.testShimmer.startShimmer()
        } else {
            binding.testShimmer.stopShimmer()
            binding.testShimmer.visibility = View.GONE
            binding.etSearchMain.visibility = View.VISIBLE
            binding.btnAdjustMain.visibility = View.VISIBLE
            binding.viewPager2.visibility = View.VISIBLE
            binding.tlMain.visibility = View.VISIBLE
            binding.btnMessageBoxMain.visibility = View.VISIBLE
            binding.ivSearchMain.visibility = View.VISIBLE
        }
    }

    override fun frameLayoutGone() {
        val animation =
            AnimationUtils.loadAnimation(this, R.anim.anim_slide_down)
        binding.flSettingMain.animation = animation
        binding.flSettingMain.visibility = View.GONE
    }

    override fun updateSettingImmediately(nutritionalInformation: HashMap<String, Int>) {
        mainActivityViewModel.tempNutritionalInformation = nutritionalInformation
        Log.d(TAG, mainActivityViewModel.tempNutritionalInformation.toString())
        mainActivityViewModel.updateSetting(binding.tlMain.selectedTabPosition)
    }

    override fun startForActivityResult(intent: Intent) {
        startForResult.launch(intent)
    }
}