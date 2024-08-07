package com.rosf73.garcani.localdata

import android.content.Context
import android.util.Log

class SharedPreference(context: Context) {
    private val pref = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE)

    fun setGreeting(value: String) {
        Log.d(SharedPreference::class.java.simpleName, "setGreeting = $value")
        with(pref.edit()) {
            putString(PREF_VISITED, value)
            apply()
        }
    }

    fun setSound(on: Boolean) {
        with(pref.edit()) {
            putBoolean(PREF_SOUND, on)
            apply()
        }
    }

    fun getGreeting() = (pref.getString(PREF_VISITED, "") ?: "").also {
        Log.d(SharedPreference::class.java.simpleName, "getGreeting = $it")
    }
    fun getSound() = pref.getBoolean(PREF_SOUND, false)

    companion object {
        private const val PREFERENCE_FILE_KEY = "com.rosf73.garcani.PREFERENCE_FILE_KEY"

        private const val PREF_VISITED = "PREF_VISITED"
        private const val PREF_SOUND = "PREF_SOUND"
    }
}