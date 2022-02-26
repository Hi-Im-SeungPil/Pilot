package org.jeonfeel.pilotproject1.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.activity.OnBackPressedCallback

import androidx.fragment.app.Fragment
import com.google.android.material.slider.RangeSlider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jeonfeel.pilotproject1.data.sharedpreferences.Shared
import org.jeonfeel.pilotproject1.databinding.FragmentSettingMainBinding
import org.jeonfeel.pilotproject1.view.adapter.RecyclerviewMainAdapter

class FragmentSettingMain : Fragment() {

    private val TAG = FragmentSettingMain::class.java.simpleName
    private var _binding: FragmentSettingMainBinding? = null
    private val binding get() = _binding
    private var adapter: RecyclerviewMainAdapter? = null
    private var sortInfo = 0
    private var isCaffeine = 0

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
            Shared.setDeCaffeine(requireActivity(), isCaffeine)
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

        binding?.toggleCaffeine?.setOnCheckedChangeListener { button, boolean ->
            isCaffeine = if (button.isChecked) {
                1
            } else {
                0
            }
        }

        binding?.buttonResetFragmentSettingMain?.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {

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

