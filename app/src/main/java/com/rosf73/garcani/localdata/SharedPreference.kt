package com.rosf73.garcani.localdata

import android.content.Context

class SharedPreference(context: Context) {
    private val pref = context.getSharedPreferences(PREFERENCE_FILE_KEY, Context.MODE_PRIVATE)

    fun setGreeting(value: String) {
        with(pref.edit()) {
            putString(PREF_VISITED, value)
            apply()
        }
    }

    fun getGreeting(): String {
        return pref.getString(PREF_VISITED, "") ?: ""
    }

    companion object {
        private const val PREFERENCE_FILE_KEY = "com.rosf73.garcani.PREFERENCE_FILE_KEY"

        private const val PREF_VISITED = "PREF_VISITED"
    }
}