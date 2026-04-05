package com.example.moneymanagement.presentation.utils

import android.util.Log
import com.example.moneymanagement.R
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings

object RemoteConfigManager {
    private const val TAG = "RemoteConfigManager"
    private const val GEMINI_KEY = "Gemini_key"

    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig

    fun init() {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 0 // 1 hour for production
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        fetchAndActivate()
    }

    private fun fetchAndActivate() {
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.d(TAG, "Config params updated: $updated")
                } else {
                    Log.d(TAG, "Config fetch failed")
                }
            }
    }

    fun getGeminiApiKey(): String {
        val key = remoteConfig.getString(GEMINI_KEY)
        Log.d(TAG, "Gemini API Key: ${if (key.isNotEmpty()) "Found" else "Not Found"}")
        return key
    }
}
