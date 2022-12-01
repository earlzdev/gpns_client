package com.earl.gpns.ui

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.earl.gpns.ui.rooms.RoomsFragment
import com.earl.gpns.ui.profile.ProfileFragment
import com.earl.gpns.ui.search.SearchFragment

class MainFragmentPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount() = FRAGMENTS_COUNT

    override fun createFragment(position: Int) = when(position) {
        0 -> SearchFragment.newInstance()
        1 -> RoomsFragment.newInstance()
        else -> ProfileFragment.newInstance()
    }

    companion object {

        private const val FRAGMENTS_COUNT = 3
    }
}