package com.vuongnq14798.vuongmp3

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.vuongnq14798.vuongmp3.base.BaseActivity

class MainActivity : BaseActivity() {

    override val layoutResId: Int
        get() = R.layout.activity_main

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun initComponents() {
    }

    companion object{
        fun getIntent(context: Context) = Intent(context, MainActivity::class.java)
    }
}
