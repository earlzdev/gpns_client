package com.earl.gpns.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.earl.gpns.R
import com.earl.gpns.ui.auth.LoginFragment
import com.earl.gpns.ui.auth.SignUpFragment
import com.earl.gpns.ui.auth.StartFragment

class MainActivity : AppCompatActivity(), NavigationContract {
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

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}