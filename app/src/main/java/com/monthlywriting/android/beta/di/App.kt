package com.monthlywriting.android.beta.di

import android.app.Application
import com.monthlywriting.android.beta.util.SharedPreferencesHelper
import com.monthlywriting.android.beta.util.setNotification
import com.vanniktech.emoji.EmojiManager
import com.vanniktech.emoji.google.GoogleEmojiProvider
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    companion object {
        lateinit var prefs: SharedPreferencesHelper
    }

    override fun onCreate() {
        prefs = SharedPreferencesHelper(applicationContext)
        super.onCreate()

        setupEmoji()
        setupNotification()
    }

    private fun setupEmoji() {
        EmojiManager.install(GoogleEmojiProvider())
    }

    private fun setupNotification() {
        if (prefs.notificationPref) {
            setNotification(this, true)
        } else {
            setNotification(this, false)
        }
    }

}