package com.monthlywriting.android.beta.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.monthlywriting.android.beta.R
import com.monthlywriting.android.beta.activity.MainActivity
import com.monthlywriting.android.beta.di.App
import com.monthlywriting.android.beta.walkthrough.WalkThroughActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            if (App.prefs.walkthroughPref) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, WalkThroughActivity::class.java)
                startActivity(intent)
            }
            finish()
        }, 500)
    }
}