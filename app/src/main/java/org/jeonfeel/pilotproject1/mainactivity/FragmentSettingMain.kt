package org.jeonfeel.pilotproject1.mainactivity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.RadioGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import org.jeonfeel.pilotproject1.R
import org.jeonfeel.pilotproject1.databinding.FragmentSettingMainBinding
import org.jeonfeel.pilotproject1.mainactivity.recyclerview.RecyclerviewMainAdapter

class FragmentSettingMain : Fragment() {

    private var _binding: FragmentSettingMainBinding? = null
    private val binding get() = _binding
    private var adapter: RecyclerviewMainAdapter? = null
    private var sortInfo = 0

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
        binding?.radiogroupFragmentSettingMainNormal?.isChecked = true
        initListener()

        return _binding?.root
    }

    private fun initListener() {
        binding?.buttonFragmentSettingMainClose?.setOnClickListener {
            fragmentFinish()
        }

        binding?.buttonAdmitFragmentSettingMain?.setOnClickListener {
            adapter?.updateSetting(sortInfo,0)
            fragmentFinish()
        }

        binding?.radiogroupFragmentSettingMain?.setOnCheckedChangeListener{ radioGroup: RadioGroup, i: Int ->
            val SORT_ROW_KCAL = -1
            val SORT_HIGH_KCAL = 1
            val SORT_BASIC = 0
            when (radioGroup.checkedRadioButtonId) {
                binding?.radiogroupFragmentSettingMainRowkcal?.id -> sortInfo = SORT_ROW_KCAL
                binding?.radiogroupFragmentSettingMainHighkcal?.id -> sortInfo = SORT_HIGH_KCAL
            }
        }
    }

    fun setRecyclerViewMainAdapter(adapter: RecyclerviewMainAdapter) {
        this.adapter = adapter
    }

    fun fragmentFinish() {
        (activity as MainActivity?)!!.frameLayoutGone()
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
        requireActivity().onBackPressedDispatcher.addCallback(this,backPressedCallback)
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
}