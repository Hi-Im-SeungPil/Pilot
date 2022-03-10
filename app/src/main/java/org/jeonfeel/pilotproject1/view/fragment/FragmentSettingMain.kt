package org.jeonfeel.pilotproject1.view.fragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isGone

import androidx.fragment.app.Fragment
import com.google.android.material.slider.LabelFormatter
import com.google.android.material.slider.RangeSlider
import org.jeonfeel.pilotproject1.R
import org.jeonfeel.pilotproject1.data.sharedpreferences.Shared
import org.jeonfeel.pilotproject1.databinding.FragmentSettingMainBinding
import kotlin.math.ceil
import kotlin.math.round

class FragmentSettingMain : Fragment() {

    private val TAG = FragmentSettingMain::class.java.simpleName
    private var _binding: FragmentSettingMainBinding? = null
    private val binding get() = _binding
    private var sortInfo = 0
    private var isCaffeine = false
    private val customListener by lazy {
        requireActivity() as FragmentSettingListener
    }
    private var nutritionalInformation = HashMap<String, Int>()
    private var maxProtein = 0.0f
    private var maxFat = 0.0f
    private var maxSugar = 0.0f

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
        isCaffeine = Shared.getDeCaffeine()
        binding?.switchCaffeine?.isChecked = isCaffeine

        sortInfo = Shared.getSort()
        when (sortInfo) {
            Shared.SORT_BASIC -> {
                binding?.rbtnFragmentSettingMainNormal?.isChecked = true
            }
            Shared.SORT_HIGH_KCAL -> {
                binding?.rbtnFragmentSettingMainHighkcal?.isChecked = true
            }
            Shared.SORT_LOW_KCAL -> {
                binding?.rbtnFragmentSettingMainLowkcal?.isChecked = true
            }
        }
        initSlider()
    }

    /**
     * 리스너
     * */
    private fun initListener() {
        binding?.btnFragmentSettingMainClose?.setOnClickListener {
            fragmentFinish()
        }

        binding?.btnAdmitFragmentSettingMain?.setOnClickListener {
            Shared.setDeCaffeine(isCaffeine)
            Shared.setSort(sortInfo)
            val proteinValues = binding?.sliderProtein?.values
            val fatValues = binding?.sliderFat?.values
            val sugarValues = binding?.sliderSugar?.values
            saveCurrentSetting(proteinValues!!,fatValues!!,sugarValues!!)
            customListener.updateSettingImmediately(nutritionalInformation)
            fragmentFinish()
        }

        binding?.rgFragmentSettingMain?.setOnCheckedChangeListener { radioGroup: RadioGroup, i: Int ->
            when (radioGroup.checkedRadioButtonId) {
                binding?.rbtnFragmentSettingMainLowkcal?.id -> sortInfo =
                    Shared.SORT_LOW_KCAL
                binding?.rbtnFragmentSettingMainHighkcal?.id -> sortInfo =
                    Shared.SORT_HIGH_KCAL
                binding?.rbtnFragmentSettingMainNormal?.id -> sortInfo =
                    Shared.SORT_BASIC
            }
        }

        binding?.sliderProtein?.addOnChangeListener(RangeSlider.OnChangeListener { slider, _, _ ->
            val sliderValue = slider.values
            val minValue = sliderValue[0]
            val maxValue = sliderValue[1]
            binding?.tvSliderProteinLow?.text = minValue.toInt().toString()
            binding?.tvSliderProteinHigh?.text = maxValue.toInt().toString()
        })

        binding?.sliderFat?.addOnChangeListener(RangeSlider.OnChangeListener { slider, _, _ ->
            val sliderValue = slider.values
            val minValue = sliderValue[0]
            val maxValue = sliderValue[1]
            binding?.tvSliderFatLow?.text = minValue.toInt().toString()
            binding?.tvSliderFatHigh?.text = maxValue.toInt().toString()
        })

        binding?.sliderSugar?.addOnChangeListener(RangeSlider.OnChangeListener { slider, _, _ ->
            val sliderValue = slider.values
            val minValue = sliderValue[0]
            val maxValue = sliderValue[1]
            binding?.tvSugarLow?.text = minValue.toInt().toString()
            binding?.tvSugarHigh?.text = maxValue.toInt().toString()
        })

        binding?.switchCaffeine?.setOnCheckedChangeListener { button, _ ->
            isCaffeine = if (button.isChecked) {
                Shared.CONTAINS_CAFFEINE
            } else {
                Shared.NOT_CONTAINS_CAFFEINE
            }
        }

        binding?.btnResetFragmentSettingMain?.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(requireActivity())
            dialogBuilder.setTitle("초기화")
                .setMessage("ㄹㅇ?")
                .setPositiveButton("ㅇㅇ") { _, _ ->
                    Shared.sharedClear()
                    nutritionalInformation.clear()
                    initSetting()
                    customListener.updateSettingImmediately(nutritionalInformation)
                    fragmentFinish()
                    customListener.frameLayoutGone()
                }
                .setNegativeButton("ㄴㄴ") { _, _ -> }

            val dialog = dialogBuilder.create()
            dialog.show()
        }
    }

    private fun initSlider() {
        when {
            maxProtein == 0.0f -> {
                binding?.sliderProtein?.visibility = View.GONE
                binding?.tvSliderProteinLow?.text = "0"
                binding?.tvSliderProteinHigh?.text = ceil(maxProtein).toInt().toString()
            }
            nutritionalInformation.size != 0 -> {
                binding?.sliderProtein?.apply {
                    labelBehavior = LabelFormatter.LABEL_GONE
                    valueTo = maxProtein
                    values = listOf(
                        nutritionalInformation[requireActivity().getString(R.string.nutritionalInformation_lowProtein_key)]!!.toFloat(),
                        nutritionalInformation[requireActivity().getString(R.string.nutritionalInformation_highProtein_key)]!!.toFloat()
                    )
                }
                binding?.tvSliderProteinLow?.text =
                    nutritionalInformation[requireActivity().getString(R.string.nutritionalInformation_lowProtein_key)]!!.toString()
                binding?.tvSliderProteinHigh?.text =
                    nutritionalInformation[requireActivity().getString(R.string.nutritionalInformation_highProtein_key)]!!.toString()
            }
            else -> {
                binding?.sliderProtein?.apply {
                    labelBehavior = LabelFormatter.LABEL_GONE
                    valueTo = maxProtein
                    values = listOf(0.0f, maxProtein)
                }
                binding?.tvSliderProteinLow?.text = "0"
                binding?.tvSliderProteinHigh?.text = ceil(maxProtein).toInt().toString()
            }
        }

        when {
            maxFat == 0.0f -> {
                binding?.sliderFat?.visibility = View.GONE
                binding?.tvSliderFatLow?.text = "0"
                binding?.tvSliderFatHigh?.text = ceil(maxFat).toInt().toString()
            }
            nutritionalInformation.size != 0 -> {
                binding?.sliderFat?.apply {
                    labelBehavior = LabelFormatter.LABEL_GONE
                    valueTo = maxFat
                    values = listOf(
                        nutritionalInformation[requireActivity().getString(R.string.nutritionalInformation_lowFat_key)]!!.toFloat(),
                        nutritionalInformation[requireActivity().getString(R.string.nutritionalInformation_highFat_key)]!!.toFloat()
                    )
                }
                binding?.tvSliderFatLow?.text =
                    nutritionalInformation[requireActivity().getString(R.string.nutritionalInformation_lowFat_key)].toString()
                binding?.tvSliderFatHigh?.text =
                    nutritionalInformation[requireActivity().getString(R.string.nutritionalInformation_highFat_key)].toString()
            }
            else -> {
                binding?.sliderFat?.apply {
                    labelBehavior = LabelFormatter.LABEL_GONE
                    valueTo = maxFat
                    values = listOf(0.0f, maxFat)
                }
                binding?.tvSliderFatLow?.text = "0"
                binding?.tvSliderFatHigh?.text = ceil(maxFat).toInt().toString()
            }
        }

        when {
            maxSugar == 0.0f -> {
                binding?.sliderSugar?.visibility = View.GONE
                binding?.tvSugarLow?.text = "0"
                binding?.tvSugarHigh?.text = ceil(maxSugar).toInt().toString()
            }
            nutritionalInformation.size != 0 -> {
                binding?.sliderSugar?.apply {
                    labelBehavior = LabelFormatter.LABEL_GONE
                    valueTo = maxSugar
                    values = listOf(
                        nutritionalInformation[requireActivity().getString(R.string.nutritionalInformation_lowSugar_key)]!!.toFloat(),
                        nutritionalInformation[requireActivity().getString(R.string.nutritionalInformation_highSugar_key)]!!.toFloat()
                    )
                }
                binding?.tvSugarLow?.text =
                    nutritionalInformation[requireActivity().getString(R.string.nutritionalInformation_lowSugar_key)].toString()
                binding?.tvSugarHigh?.text =
                    nutritionalInformation[requireActivity().getString(R.string.nutritionalInformation_highSugar_key)].toString()
            }
            else -> {
                binding?.sliderSugar?.apply {
                    labelBehavior = LabelFormatter.LABEL_GONE
                    valueTo = maxSugar
                    values = listOf(0.0f, maxSugar)
                }
                binding?.tvSugarLow?.text = "0"
                binding?.tvSugarHigh?.text = ceil(maxSugar).toInt().toString()
            }
        }
    }

    private fun saveCurrentSetting(
        proteinValues: List<Float>,
        fatValues: List<Float>,
        sugarValues: List<Float>
    ) {
        if (proteinValues.size == 2) {
            nutritionalInformation[requireActivity().getString(R.string.nutritionalInformation_lowProtein_key)] =
                proteinValues[0].toInt()
            nutritionalInformation[requireActivity().getString(R.string.nutritionalInformation_highProtein_key)] =
                proteinValues[1].toInt()
        } else {
            nutritionalInformation[requireActivity().getString(R.string.nutritionalInformation_lowProtein_key)] = 0
            nutritionalInformation[requireActivity().getString(R.string.nutritionalInformation_highProtein_key)] = 0
        }
        if (fatValues.size == 2) {
            nutritionalInformation[requireActivity().getString(R.string.nutritionalInformation_lowFat_key)] =
                fatValues[0].toInt()
            nutritionalInformation[requireActivity().getString(R.string.nutritionalInformation_highFat_key)] =
                fatValues[1].toInt()
        } else {
            nutritionalInformation[requireActivity().getString(R.string.nutritionalInformation_lowFat_key)] = 0
            nutritionalInformation[requireActivity().getString(R.string.nutritionalInformation_highFat_key)] = 0
        }
        if (sugarValues.size == 2) {
            nutritionalInformation[requireActivity().getString(R.string.nutritionalInformation_lowSugar_key)] =
                sugarValues[0].toInt()
            nutritionalInformation[requireActivity().getString(R.string.nutritionalInformation_highSugar_key)] =
                sugarValues[1].toInt()
        } else {
            nutritionalInformation[requireActivity().getString(R.string.nutritionalInformation_lowSugar_key)] = 0
            nutritionalInformation[requireActivity().getString(R.string.nutritionalInformation_highSugar_key)] = 0
        }
    }

    fun setSliderValue(
        maxProtein: Float,
        maxFat: Float,
        maxSugar: Float,
        nutritionalInformation: HashMap<String, Int>
    ) {
        this.maxProtein = maxProtein
        this.maxFat = maxFat
        this.maxSugar = maxSugar
        this.nutritionalInformation = nutritionalInformation
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
        fun updateSettingImmediately(nutritionalInformation: HashMap<String, Int>)
    }
}

