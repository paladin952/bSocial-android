package com.clpstudio.bsocial.presentation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.clpstudio.bsocial.R
import com.clpstudio.bsocial.presentation.register.RegisterActivity

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        RegisterActivity.startActivity(this)
        finish()
    }
}
