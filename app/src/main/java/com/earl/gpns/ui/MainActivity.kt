package com.earl.gpns.ui

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.earl.gpns.R
import com.earl.gpns.core.Keys
import com.earl.gpns.core.SharedPreferenceManager
import com.earl.gpns.ui.auth.LoginFragment
import com.earl.gpns.ui.auth.SignUpFragment
import com.earl.gpns.ui.auth.StartFragment
import com.earl.gpns.ui.chat.ChatFragment
import com.earl.gpns.ui.models.ChatInfo
import com.earl.gpns.ui.search.*
import com.earl.gpns.ui.usersFragment.FragmentUsers
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationContract {

    var progressBar: Dialog? = null
    private lateinit var viewModel: MainViewModel
    private lateinit var preferenceManager: SharedPreferenceManager
    private val fragments: MutableList<Fragment> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val test = AuthorMessage(this)
//        setContentView(test)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        preferenceManager = SharedPreferenceManager(this)
        if (preferenceManager.getBoolean(Keys.KEY_IS_SIGNED_UP)) {
            mainFragment()
        } else {
            start()
        }
    }

    override fun start() {
        showFragment(StartFragment.newInstance())
    }

    override fun login() {
        showFragment(LoginFragment.newInstance())
    }

    override fun register() {
        showFragment(SignUpFragment.newInstance())
    }

    override fun back() {
        supportFragmentManager.popBackStack()
    }

    override fun mainFragment() {
        showFragment(MainFragment.newInstance())
    }

    override fun showProgressBar() {
        progressBar = Dialog(this, android.R.style.Theme_Black)
        val view = LayoutInflater.from(this).inflate(R.layout.progress_bar, null)
        progressBar!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        progressBar!!.window!!.setBackgroundDrawableResource(R.color.custom_transparent)
        progressBar!!.setContentView(view)
        progressBar!!.show()
    }

    override fun hideProgressBar() {
        progressBar?.dismiss()
    }

    override fun usersFragment() {
        showFragment(FragmentUsers.newInstance())
    }

    override fun chat(chatInfo: ChatInfo) {
        showFragment(ChatFragment.newInstance(chatInfo))
    }

    override fun newSearchForm() {
        showFragment(FragmentStartNewSearchingForm.newInstance())
    }

    override fun startFirstDriverForm() {
        showFragment(FirstDriverFormFragment.newInstance())
    }

    override fun startSecondDriverForm() {
        showFragment(SecondDriverFormFragment.newInstance())
    }

    override fun startCompanionForm() {
        showFragment(CompanionFormFragment.newInstance())
    }

    override fun driverFormDetails() {
        showFragment(DriverFormDetailsFragment.newInstance())
    }

    override fun companionFormDetails() {
        showFragment(CompanionFormDetailsFragment.newInstance())
    }

    private fun showFragment(fragment: Fragment) {
        fragments.add(fragment)
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun exit() {
        viewModel.closeSocketSession()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.closeSocketSession()
    }

    override fun log(text: String) {
        Log.d("tag", "log: $text")
    }
}