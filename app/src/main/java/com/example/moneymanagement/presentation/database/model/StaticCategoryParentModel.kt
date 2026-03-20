package com.example.moneymanagement.presentation.database.model

data class StaticCategoryParentModel (
    val date : String,
    val list: List<StaticCategoryChildModel>
)