package com.earl.gpns.ui

import android.content.Context
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.earl.gpns.R
import com.earl.gpns.core.AuthResultListener
import com.earl.gpns.core.BaseFragment
import com.earl.gpns.core.Keys
import com.earl.gpns.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException
import java.util.*

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>(), AuthResultListener {

    private lateinit var viewModel: MainFragmentViewModel

    override fun viewBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentMainBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[MainFragmentViewModel::class.java]
        viewPager(requireContext())
        authenticate()
        fetchUserInfo()
    }

    private fun viewPager(context: Context) {
        binding.pager.adapter = MainFragmentPagerAdapter(requireActivity())
        val tabs = binding.tabs
        tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val tabIconColor = ContextCompat.getColor(context, R.color.white)
                Objects.requireNonNull(tab.icon)?.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {
                val tabIconColor = ContextCompat.getColor(context, R.color.grey_tab_unselected)
                Objects.requireNonNull(tab.icon)?.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN)
            }
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        TabLayoutMediator(
            binding.tabs, binding.pager
        ) { tab: TabLayout.Tab, position: Int ->
            if (position == 0) {
                tab.text = getString(R.string.search)
                tab.setIcon(R.drawable.search)
            } else if (position == 1) {
                tab.text = getString(R.string.chat)
                tab.setIcon(R.drawable.chat)
            } else if (position == 2) {
                tab.text = getString(R.string.profile)
                tab.setIcon(R.drawable.profile)
            }}.attach()
    }

    private fun fetchUserInfo() {
        viewModel.fetchUserInfo(preferenceManager.getString(Keys.KEY_JWT) ?: "")
        viewModel.observeUserInfoLiveData(this) {
            preferenceManager.putString(Keys.KEY_NAME, it.provideName())
            preferenceManager.putString(Keys.KEY_IMAGE, it.provideImage())
            preferenceManager.putString(Keys.KEY_USER_ID, it.provideId())
        }
    }

    private fun authenticate() {
        navigator.showProgressBar()
        viewModel.authenticate(preferenceManager.getString(Keys.KEY_JWT) ?: "", this)
    }

    override fun <T> authorized(value: T) {
        navigator.hideProgressBar()
    }

    override fun unauthorized(e: HttpException) {
        navigator.hideProgressBar()
    }

    override fun unknownError(e: Exception) {
        navigator.hideProgressBar()
    }

    companion object {

        fun newInstance() = MainFragment()
    }
}