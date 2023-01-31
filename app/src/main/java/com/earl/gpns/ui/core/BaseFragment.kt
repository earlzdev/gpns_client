package com.earl.gpns.ui.core

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.earl.gpns.ui.NavigationContract

abstract class BaseFragment<VB: ViewBinding> : Fragment() {

    abstract fun viewBinding(inflater: LayoutInflater, container: ViewGroup?) : VB

    private var _binding : VB? = null
    protected val binding: VB get() = _binding!!
    protected lateinit var preferenceManager: SharedPreferenceManager
    protected lateinit var navigator: NavigationContract

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = viewBinding(inflater, container)
        preferenceManager = SharedPreferenceManager(requireContext())
        navigator = requireActivity() as NavigationContract
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}