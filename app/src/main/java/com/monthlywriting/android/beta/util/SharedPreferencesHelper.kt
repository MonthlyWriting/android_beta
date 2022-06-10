package com.monthlywriting.android.beta.util

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesHelper(context: Context) {

    private val pref: SharedPreferences = context.getSharedPreferences(
        SHARED_PREFERENCE, Context.MODE_PRIVATE)

    var notificationPref: Boolean
        get() = pref.getBoolean(NOTIFICATION_PREFERENCE, true)
        set(value) = pref.edit().putBoolean(NOTIFICATION_PREFERENCE, value).apply()

    var namePref: String?
        get() = pref.getString(NAME_PREFERENCE, "")
        set(value) = pref.edit().putString(NAME_PREFERENCE, value).apply()

    var walkthroughPref: Boolean
        get() = pref.getBoolean(WALKTHROUGH_PREFERENCE, false)
        set(value) = pref.edit().putBoolean(WALKTHROUGH_PREFERENCE, value).apply()

    var guidePref: Boolean
        get() = pref.getBoolean(GUIDE_PREFERENCE, false)
        set(value) = pref.edit().putBoolean(GUIDE_PREFERENCE, value).apply()

    companion object {
        const val SHARED_PREFERENCE = "shared prefs"

        const val NOTIFICATION_PREFERENCE = "notification preference"
        const val NAME_PREFERENCE = "name preference"
        const val WALKTHROUGH_PREFERENCE = "walkthrough preference"
        const val GUIDE_PREFERENCE = "guide preference"
    }
}