package com.vuongnq14798.vuongmp3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vuongnq14798.vuongmp3.base.BaseActivity
import com.vuongnq14798.vuongmp3.ui.downloaded.DownloadedFragment
import com.vuongnq14798.vuongmp3.ui.home.HomeFragment
import com.vuongnq14798.vuongmp3.ui.mymusic.MyMusicFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    override val layoutResId: Int = R.layout.activity_main

    override fun initData(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            addFragment(HomeFragment())
        }
    }

    override fun initComponents() {
        bottomNavigation.setOnNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val fragment = when (item.itemId) {
            R.id.home -> HomeFragment()
            R.id.my_music -> MyMusicFragment()
            R.id.downloaded_songs -> DownloadedFragment()
            else -> return false
        }
        replaceFragment(fragment)
        return true
    }

    fun addFragment(fragment: Fragment) =
        supportFragmentManager
            .beginTransaction()
            .replace(
                R.id.frameContainer,
                fragment,
                fragment.javaClass.getSimpleName()
            )
            .commit()

    fun replaceFragment(fragment: Fragment) =
        supportFragmentManager
            .beginTransaction()
            .add(
                R.id.frameContainer,
                fragment,
                fragment.javaClass.getSimpleName()
            )
            .commit()

    companion object {
        fun getIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}
