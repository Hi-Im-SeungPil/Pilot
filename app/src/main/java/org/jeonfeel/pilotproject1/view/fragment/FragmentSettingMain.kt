package org.jeonfeel.pilotproject1.view.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import org.jeonfeel.pilotproject1.databinding.FragmentSettingMainBinding
import org.jeonfeel.pilotproject1.view.activity.MainActivity
import org.jeonfeel.pilotproject1.view.adapter.RecyclerviewMainAdapter

// SharedPreference 사용해서 상태정보 저장.
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
            adapter?.updateSetting(sortInfo, 0)
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
}