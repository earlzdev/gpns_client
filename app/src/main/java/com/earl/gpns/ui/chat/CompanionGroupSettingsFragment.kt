package com.earl.gpns.ui.chat

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.earl.gpns.R
import com.earl.gpns.ui.core.BaseFragment
import com.earl.gpns.ui.core.Keys
import com.earl.gpns.databinding.FragmentCompanionGroupSettingsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class CompanionGroupSettingsFragment(
    private val groupId: String
) : BaseFragment<FragmentCompanionGroupSettingsBinding>(), OnCompanionInGroupSettingsClickListener {

    private lateinit var viewModel: CompanionGroupSettingsViewModel

    override fun viewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentCompanionGroupSettingsBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[CompanionGroupSettingsViewModel::class.java]
        viewModel.fetchAllCompanionsInGroup(
            preferenceManager.getString(Keys.KEY_JWT) ?: "",
            groupId
        )
        recycler()
        binding.backBtn.setOnClickListener {
            navigator.back()
        }
        binding.leaveGroup.setOnClickListener {
            leaveCompanionGroup()
        }
    }

    private fun recycler() {
        val adapter = CompanionGroupUsersRecyclerAdapter(this, preferenceManager.getString(Keys.KEY_NAME) ?: "")
        binding.recycler.adapter = adapter
        lifecycleScope.launchWhenStarted {
            viewModel._companionsList
                .onEach { list ->
                    adapter.submitList(list)
                }
                .collect()
        }
    }

    override fun removeCompanionFromGroup(username: String) {
        if (preferenceManager.getBoolean(Keys.IS_DRIVER) && groupId == preferenceManager.getString(
                Keys.KEY_USER_ID)) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Подтвердите действие")
            builder.setMessage("Вы действительно хотите убрать этого пользователя из группы попутчиков?")
            builder.setPositiveButton(R.string.yes) { dialog, which ->
                viewModel.removeCompanionFromGroup(
                    preferenceManager.getString(Keys.KEY_JWT) ?: "",
                    groupId,
                    username
                )
                Toast.makeText(requireContext(), "Пользователь удален из группы", Toast.LENGTH_SHORT).show()
            }
            builder.setNegativeButton(R.string.no) { dialog, which ->
            }
            builder.show()
        } else {
            Toast.makeText(requireContext(), "Вы не можете удалить этого попутчика, так как вы не водитель", Toast.LENGTH_SHORT).show()
        }
    }

    private fun leaveCompanionGroup() {
        if (!preferenceManager.getBoolean(Keys.IS_DRIVER) && groupId != preferenceManager.getString(
                Keys.KEY_USER_ID)) {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Подтвердите действие")
            builder.setMessage("Вы действительно хотите покинуть эту группу попутчиков?")
            builder.setPositiveButton(R.string.yes) { dialog, which ->
                viewModel.leaveFromCompanionGroup(
                    preferenceManager.getString(Keys.KEY_JWT) ?: "",
                    groupId
                )
                onDestroy()
                preferenceManager.putBoolean(Keys.IS_STILL_IN_COMP_GROUP, false)
                navigator.popBackStackToFragment(mainFragment)
            }
            builder.setNegativeButton(R.string.no) { dialog, which ->
            }
            builder.show()
        } else {
            Toast.makeText(requireContext(), "Вы не можете покинуть свою группу попутчиков!", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        fun newInstance(groupId: String) = CompanionGroupSettingsFragment(groupId)
        private const val mainFragment = "mainFragment"
    }
}