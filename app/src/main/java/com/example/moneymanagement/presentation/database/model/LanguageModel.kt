package com.example.moneymanagement.presentation.database.model

data class LanguageModel(

    val imgFlag : Int,
    val nameCountry : String,
    val code : String,
    var isSelected: Boolean = false


)