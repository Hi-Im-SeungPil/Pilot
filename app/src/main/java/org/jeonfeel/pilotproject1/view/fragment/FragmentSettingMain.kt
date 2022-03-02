package org.jeonfeel.pilotproject1.view.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.activity.OnBackPressedCallback

import androidx.fragment.app.Fragment
import com.google.android.material.slider.RangeSlider
import org.jeonfeel.pilotproject1.R
import org.jeonfeel.pilotproject1.data.sharedpreferences.Shared
import org.jeonfeel.pilotproject1.databinding.FragmentSettingMainBinding

class FragmentSettingMain : Fragment() {

    private val TAG = FragmentSettingMain::class.java.simpleName
    private var _binding: FragmentSettingMainBinding? = null
    private val binding get() = _binding
    private var sortInfo = 0
    private var isCaffeine = false
    private val customListener by lazy {
        requireActivity() as FragmentSettingListener
    }

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

        initSetting()
        initListener()

        return _binding?.root
    }

    private fun initSetting() {
        isCaffeine = Shared.getDeCaffeine(requireActivity())
        binding?.toggleCaffeine?.isChecked = isCaffeine

        sortInfo = Shared.getSort(requireActivity())
        when (sortInfo) {
            resources.getInteger(R.integer.SORT_BASIC) -> {
                binding?.radiogroupFragmentSettingMainNormal?.isChecked = true
            }
            resources.getInteger(R.integer.SORT_HIGH_KCAL) -> {
                binding?.radiogroupFragmentSettingMainHighkcal?.isChecked = true
            }
            resources.getInteger(R.integer.SORT_LOW_KCAL) -> {
                binding?.radiogroupFragmentSettingMainLowkcal?.isChecked = true
            }
        }
    }

    /**
     * 리스너
     * */
    private fun initListener() {
        binding?.buttonFragmentSettingMainClose?.setOnClickListener {
            fragmentFinish()
        }

        binding?.buttonAdmitFragmentSettingMain?.setOnClickListener {
            Shared.setDeCaffeine(requireActivity(), isCaffeine)
            Shared.setSort(requireActivity(), sortInfo)
            customListener.updateSettingImmediately()
            fragmentFinish()
        }

        binding?.radiogroupFragmentSettingMain?.setOnCheckedChangeListener { radioGroup: RadioGroup, i: Int ->
            when (radioGroup.checkedRadioButtonId) {
                binding?.radiogroupFragmentSettingMainLowkcal?.id -> sortInfo =
                    resources.getInteger(R.integer.SORT_LOW_KCAL)
                binding?.radiogroupFragmentSettingMainHighkcal?.id -> sortInfo =
                    resources.getInteger(R.integer.SORT_HIGH_KCAL)
                binding?.radiogroupFragmentSettingMainNormal?.id -> sortInfo =
                    resources.getInteger(R.integer.SORT_BASIC)
            }
        }

        binding?.sliderProtein?.addOnSliderTouchListener(object :
            RangeSlider.OnSliderTouchListener {
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

        binding?.toggleCaffeine?.setOnCheckedChangeListener { button, _ ->
            isCaffeine = if (button.isChecked) {
                resources.getBoolean(R.bool.IS_CAFFEINE)
            } else {
                resources.getBoolean(R.bool.IS_NOT_CAFFEINE)
            }
        }

        binding?.buttonResetFragmentSettingMain?.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(requireActivity())
            dialogBuilder.setTitle("주의!")
                .setMessage("초기화 하시겠습니까?")
                .setPositiveButton("확인") { _, _ -> Shared.sharedClear(requireActivity())
                    initSetting()
                    customListener.updateSettingImmediately()
                    customListener.frameLayoutGone()
                }
                .setNegativeButton("취소") { _, _ -> }

            val dialog = dialogBuilder.create()
            dialog.show()
        }
    }

    fun fragmentFinish() {
        customListener.frameLayoutGone()
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
        fun updateSettingImmediately()
    }
}

