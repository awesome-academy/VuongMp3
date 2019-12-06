package com.vuongnq14798.vuongmp3

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vuongnq14798.vuongmp3.base.BaseActivity
import com.vuongnq14798.vuongmp3.service.MediaPlayerService
import com.vuongnq14798.vuongmp3.ui.downloaded.DownloadedFragment
import com.vuongnq14798.vuongmp3.ui.home.HomeFragment
import com.vuongnq14798.vuongmp3.ui.miniplay.MiniPlayFragment
import com.vuongnq14798.vuongmp3.ui.mymusic.MyMusicFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var mediaPlayerService: MediaPlayerService
    private val connection = object : ServiceConnection {

        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as MediaPlayerService.MediaPlayerBinder
            mediaPlayerService = binder.getService()
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
        }
    }

    override val layoutResId: Int = R.layout.activity_main

    override fun initData(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            addFragment(R.id.frameContainer ,HomeFragment())
        }
    }

    override fun initComponents() {
        bottomNavigation.setOnNavigationItemSelectedListener(this)
    }

    override fun onStart() {
        super.onStart()
        bindService(MediaPlayerService.getIntent(this), connection, Context.BIND_AUTO_CREATE)
        if (::mediaPlayerService.isInitialized && mediaPlayerService.getTracks().isNotEmpty())
            replaceFragment(R.id.frameMiniPlay, MiniPlayFragment())
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        val fragment = when (item.itemId) {
            R.id.home -> HomeFragment()
            R.id.my_music -> MyMusicFragment()
            R.id.downloaded_songs -> DownloadedFragment()
            else -> return false
        }
        replaceFragment(R.id.frameContainer, fragment)
        return true
    }

    private fun addFragment(containerViewId: Int,fragment: Fragment) =
        supportFragmentManager
            .beginTransaction()
            .replace(
                containerViewId,
                fragment,
                fragment.javaClass.getSimpleName()
            )
            .commit()

    private fun replaceFragment(containerViewId: Int,fragment: Fragment) =
        supportFragmentManager
            .beginTransaction()
            .add(
                containerViewId,
                fragment,
                fragment.javaClass.getSimpleName()
            )
            .commit()

    companion object {
        fun getIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}
