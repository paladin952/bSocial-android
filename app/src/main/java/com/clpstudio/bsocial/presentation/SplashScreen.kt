package com.clpstudio.bsocial.presentation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import com.clpstudio.bsocial.R
import com.clpstudio.bsocial.presentation.login.LoginActivity

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        LoginActivity.startActivity(this)
        finish()
    }
}