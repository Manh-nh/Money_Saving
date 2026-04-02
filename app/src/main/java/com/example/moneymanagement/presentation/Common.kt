package com.example.moneymanagement.presentation

import android.content.Context
import com.example.moneymanagement.R
import com.example.moneymanagement.presentation.database.model.LanguageModel

object Common {



    fun listLanguage(context: Context): List<LanguageModel> {
        return listOf(
            LanguageModel(R.drawable.vietnam, context.getString(R.string.vietname), "vi"),
            LanguageModel(R.drawable.english, context.getString(R.string.english), "en"),
            LanguageModel(R.drawable.protogal, context.getString(R.string.portugal), "pt"),
            LanguageModel(R.drawable.russia, context.getString(R.string.gemarni), "de"), // Germany
            LanguageModel(R.drawable.china, context.getString(R.string.china), "zh"),
            LanguageModel(R.drawable.japan, context.getString(R.string.japan), "ja"),
            LanguageModel(R.drawable.korean, context.getString(R.string.Korean), "ko"),
            LanguageModel(R.drawable.arabic, context.getString(R.string.arabic), "ar")
        )
    }












}