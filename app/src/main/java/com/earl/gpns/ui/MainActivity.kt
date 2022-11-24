package com.earl.gpns.ui

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.earl.gpns.R
import com.earl.gpns.ui.auth.LoginFragment
import com.earl.gpns.ui.auth.SignUpFragment
import com.earl.gpns.ui.auth.StartFragment
import com.earl.gpns.ui.usersFragment.FragmentUsers
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), NavigationContract {

    var progressBar: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        start()
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

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun log(text: String) {
        Log.d("tag", "log: $text")
    }
}