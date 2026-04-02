package com.example.moneymanagement.presentation.view.activity.language

import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.moneymanagement.databinding.ActivityLanguageBinding
import com.example.moneymanagement.presentation.Common
import com.example.moneymanagement.presentation.view.activity.splash.SplashActivity
import com.example.moneymanagement.presentation.view.adapter.LanguageAdapter
import com.example.moneymanagement.presentation.view.base.BaseActivity

import com.example.moneymanagement.presentation.database.model.LanguageModel

class LanguageActivity : BaseActivity<ActivityLanguageBinding>(ActivityLanguageBinding::inflate) {

    private lateinit var adapter: LanguageAdapter
    private var selectedLanguage: LanguageModel? = null

    override fun initializeComponent() {
        super.initializeComponent()

        val languages = Common.listLanguage(this)
        val currentLocales = AppCompatDelegate.getApplicationLocales()
        val currentLangCode = if (!currentLocales.isEmpty) currentLocales.get(0)?.language else "en"

        // Mark the current language as selected
        languages.forEach { it.isSelected = it.code == currentLangCode }
        selectedLanguage = languages.find { it.isSelected }

        adapter = LanguageAdapter(languages) { languageModel ->
            selectedLanguage = languageModel
        }
        binding.lstLanguage.adapter = adapter

    }


    override fun initializeEvents() {
        super.initializeEvents()

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.btnDone.setOnClickListener {
            selectedLanguage?.let { languageModel ->
                // Use AppCompatDelegate to set the application locale
                val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageModel.code)
                AppCompatDelegate.setApplicationLocales(appLocale)

                // Restart SplashActivity to refresh the entire app state
                val intent = Intent(this, SplashActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            } ?: run {
                finish()
            }
        }
    }

    override fun initializeData() {
        super.initializeData()
    }

    override fun bindView() {
        super.bindView()
    }


}