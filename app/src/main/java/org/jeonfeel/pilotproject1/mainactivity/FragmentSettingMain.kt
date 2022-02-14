package org.jeonfeel.pilotproject1.mainactivity

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentTransaction
import org.jeonfeel.pilotproject1.R
import org.jeonfeel.pilotproject1.databinding.FragmentSettingMainBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private var _binding: FragmentSettingMainBinding? = null
private val binding get() = _binding
/**
 * A simple [Fragment] subclass.
 * Use the [FragmentSettingMain.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentSettingMain : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var adapter: RecyclerviewMainAdapter? = null
    private var sortInfo = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingMainBinding.inflate(inflater, container, false)
        binding?.radiogroupFragmentSettingMainNormal?.isChecked = true

        binding?.buttonFragmentSettingMainClose?.setOnClickListener {
            fragmentFinish()
        }

        binding?.buttonAdmitFragmentSettingMain?.setOnClickListener {
            adapter?.updateSetting(sortInfo)
            fragmentFinish()
        }

        binding?.radiogroupFragmentSettingMain?.setOnCheckedChangeListener{ radioGroup: RadioGroup, i: Int ->
            when (radioGroup.checkedRadioButtonId) {
                binding?.radiogroupFragmentSettingMainRowkcal?.id -> sortInfo = -1
                binding?.radiogroupFragmentSettingMainHighkcal?.id -> sortInfo = 1
            }
        }
        return _binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        adapter = null
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

    fun setRecyclerViewMainAdapter(adapter: RecyclerviewMainAdapter) {
        this.adapter = adapter
    }

    fun fragmentFinish() {
        activity?.supportFragmentManager?.
        beginTransaction()?.
        remove(this)?.
        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)?.
        commit()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentSettingMain.
         */

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentSettingMain().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}