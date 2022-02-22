package org.jeonfeel.pilotproject1.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.RadioGroup
import androidx.activity.OnBackPressedCallback

import androidx.fragment.app.Fragment
import com.google.android.material.slider.RangeSlider
import org.jeonfeel.pilotproject1.data.sharedpreferences.Shared
import org.jeonfeel.pilotproject1.databinding.FragmentSettingMainBinding
import org.jeonfeel.pilotproject1.view.adapter.RecyclerviewMainAdapter

class FragmentSettingMain : Fragment() {

    private var _binding: FragmentSettingMainBinding? = null
    private val binding get() = _binding
    private var adapter: RecyclerviewMainAdapter? = null
    private var sortInfo = 0
    private var deCaffeine = 0
    private lateinit var shared: Shared

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingMainBinding.inflate(inflater, container, false)

        shared = Shared(requireActivity())
        initListener()

        return _binding?.root
    }

    /**
     * 리스너
     * */
    private fun initListener() {
        binding?.buttonFragmentSettingMainClose?.setOnClickListener {
            fragmentFinish()
        }

        binding?.buttonAdmitFragmentSettingMain?.setOnClickListener {
//            GlobalScope.launch {
//                shared.storeSetting(-192)
//            }
//            shared.getSetting()
            adapter?.updateSetting(sortInfo, deCaffeine)
            fragmentFinish()
        }

        binding?.radiogroupFragmentSettingMain?.setOnCheckedChangeListener { radioGroup: RadioGroup, i: Int ->
            val SORT_ROW_KCAL = -1
            val SORT_HIGH_KCAL = 1
            val SORT_BASIC = 0
            when (radioGroup.checkedRadioButtonId) {
                binding?.radiogroupFragmentSettingMainLowkcal?.id -> sortInfo = SORT_ROW_KCAL
                binding?.radiogroupFragmentSettingMainHighkcal?.id -> sortInfo = SORT_HIGH_KCAL
            }
        }

        binding?.sliderProtein?.stepSize = 10f
        binding?.sliderProtein?.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener{
            @SuppressLint("RestrictedApi")
            override fun onStartTrackingTouch(slider: RangeSlider) {

            }

            @SuppressLint("RestrictedApi")
            override fun onStopTrackingTouch(slider: RangeSlider) {
                val minValue = slider.values[0].toString()
                val maxValue = slider.values[1].toString()
                binding?.tvSliderProteinLow?.text = minValue.toString()
                binding?.tvSliderProteinHigh?.text = maxValue.toString()
            }
        })

        binding?.toggleCaffeine?.setOnCheckedChangeListener { button, boolean ->
            deCaffeine = if (button.isChecked) {
                1
            } else {
                0
            }
        }
    }

    fun setRecyclerViewMainAdapter(adapter: RecyclerviewMainAdapter) {
        this.adapter = adapter
    }

    fun fragmentFinish() {
        val test = activity as FragmentSettingListener
        test.frameLayoutGone()
        activity?.supportFragmentManager
            ?.beginTransaction()
            ?.remove(this)
            ?.commit()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                fragmentFinish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        adapter = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            FragmentSettingMain().apply {
                arguments = Bundle().apply {
                }
            }
    }

    interface FragmentSettingListener {
        fun frameLayoutGone()
    }
}

