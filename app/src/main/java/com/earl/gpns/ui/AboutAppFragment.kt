package com.earl.gpns.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.earl.gpns.databinding.FragmentDialogAboutBinding

class AboutAppFragment : DialogFragment() {

    private var _binding: FragmentDialogAboutBinding? = null
    private val binding: FragmentDialogAboutBinding get() = _binding!!

    override fun onStart() {
        super.onStart()
        isCancelable = true
        val width = resources.displayMetrics.widthPixels * DEFAULT_COEFF
        dialog?.window?.setLayout(width.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDialogAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {

        const val TAG = "AboutDialogFragment"
        const val DEFAULT_COEFF = 0.95

        fun newInstance() = AboutAppFragment()

//        fun newInstance(result: PriceResultUi) = ResultDialogFragment().apply {
//            arguments = bundleOf(RESULT_DETAILS to result)
//        }
    }
}